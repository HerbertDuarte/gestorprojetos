package com.herbertduarte.gestorprojetos.services;

import com.herbertduarte.gestorprojetos.models.dtos.membro.CreateMembroDto;
import com.herbertduarte.gestorprojetos.models.entities.Membro;
import com.herbertduarte.gestorprojetos.models.enums.Atribuicao;
import com.herbertduarte.gestorprojetos.repositories.MembroJpaRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


@SpringBootTest
@ActiveProfiles("test")
@Transactional
class MembroServiceTest {

    @Autowired
    private MembroJpaRepository repository;

    @Autowired
    private MembroService service;

    @AfterEach
    void tearDown() {
        repository.deleteAll();
    }

    @Test
    @DisplayName("Deve persistir membro no banco com campos mapeados")
    void createMembroShouldPersistEntityWithMappedFields() {
        CreateMembroDto dto = new CreateMembroDto("Joao", 1);

        service.createMembro(dto);

        Page<Membro> page = repository.findAll(null, null, PageRequest.of(0, 10));

        assertEquals(1, page.getTotalElements());
        Membro savedMembro = page.getContent().get(0);
        assertEquals("Joao", savedMembro.getNome());
        assertEquals(Atribuicao.OUTRO, savedMembro.getAtribuicao());
    }

    @Test
    @DisplayName("Deve aplicar filtros de nome e atribuicao com paginacao usando H2")
    void getAllMembrosShouldApplyFiltersAndPagination() {
        repository.saveAll(List.of(
                Membro.builder().nome("Ana Paula").atribuicao(Atribuicao.FUNCIONARIO).build(),
                Membro.builder().nome("Ana Clara").atribuicao(Atribuicao.OUTRO).build(),
                Membro.builder().nome("Carlos").atribuicao(Atribuicao.FUNCIONARIO).build()
        ));

        Pageable pageable = PageRequest.of(0, 10);
        String nome = "ana";
        Atribuicao atribuicao = Atribuicao.FUNCIONARIO;

        Page<Membro> result = service.getAllMembros(pageable, nome, atribuicao);

        assertEquals(1, result.getTotalElements());
        assertFalse(result.isEmpty());
        assertEquals("Ana Paula", result.getContent().get(0).getNome());
        assertTrue(result.getContent().stream().allMatch(m -> m.getAtribuicao() == Atribuicao.FUNCIONARIO));
    }
}
