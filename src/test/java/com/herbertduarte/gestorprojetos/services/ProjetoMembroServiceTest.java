package com.herbertduarte.gestorprojetos.services;

import com.herbertduarte.gestorprojetos.exceptions.AlocacaoProjetoMembroNaoEncontrada;
import com.herbertduarte.gestorprojetos.exceptions.MembroNaoEncontradoException;
import com.herbertduarte.gestorprojetos.exceptions.ProjetoNaoEncontradoException;
import com.herbertduarte.gestorprojetos.models.dtos.projeto.ProjetoDto;
import com.herbertduarte.gestorprojetos.models.dtos.projetomembro.CreateProjetoMembroDto;
import com.herbertduarte.gestorprojetos.models.dtos.projetomembro.ProjetoMembroDto;
import com.herbertduarte.gestorprojetos.models.entities.Membro;
import com.herbertduarte.gestorprojetos.models.entities.Projeto;
import com.herbertduarte.gestorprojetos.models.entities.ProjetoMembro;
import com.herbertduarte.gestorprojetos.models.entities.ProjetoMembroId;
import com.herbertduarte.gestorprojetos.models.enums.Atribuicao;
import com.herbertduarte.gestorprojetos.models.enums.Status;
import com.herbertduarte.gestorprojetos.repositories.MembroJpaRepository;
import com.herbertduarte.gestorprojetos.repositories.ProjetoJpaRepository;
import com.herbertduarte.gestorprojetos.repositories.ProjetoMembroRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class ProjetoMembroServiceTest {

    @Autowired
    private ProjetoMembroService service;

    @Autowired
    private ProjetoMembroRepository projetoMembroRepository;

    @Autowired
    private ProjetoJpaRepository projetoRepository;

    @Autowired
    private MembroJpaRepository membroRepository;

    private Membro gerente;
    private Membro membro;
    private Projeto projeto;

    @BeforeEach
    void setUp() {
        gerente = Membro.builder()
                .nome("Gerente Teste")
                .atribuicao(Atribuicao.FUNCIONARIO)
                .build();
        gerente = membroRepository.save(gerente);

        membro = Membro.builder()
                .nome("Membro Alocado Teste")
                .atribuicao(Atribuicao.FUNCIONARIO)
                .build();
        membro = membroRepository.save(membro);

        projeto = Projeto.builder()
                .nome("Projeto Teste")
                .dataInicio(LocalDateTime.now())
                .previsaoTermino(LocalDateTime.now().plusMonths(6))
                .orcamentoTotal(new BigDecimal("100000.00"))
                .gerente(gerente)
                .status(Status.EM_ANALISE)
                .build();
        projeto = projetoRepository.save(projeto);
    }

    @AfterEach
    void tearDown() {
        projetoMembroRepository.deleteAll();
        projetoRepository.deleteAll();
        membroRepository.deleteAll();
    }

    @Test
    @DisplayName("Deve alocar membro no projeto com sucesso")
    void alocarMembroEmProjetoShouldPersistAllocation() {
        CreateProjetoMembroDto dto = new CreateProjetoMembroDto(projeto.getId(), membro.getId());

        ProjetoMembroDto result = service.alocarMembroEmProjeto(dto);

        assertNotNull(result);
        
        ProjetoMembroId id = new ProjetoMembroId(projeto.getId(), membro.getId());
        assertTrue(projetoMembroRepository.existsById(id));
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar alocar membro em projeto inexistente")
    void alocarMembroEmProjetoShouldThrowExceptionWhenProjectNotFound() {
        CreateProjetoMembroDto dto = new CreateProjetoMembroDto(99999, membro.getId());

        assertThrows(ProjetoNaoEncontradoException.class, () -> service.alocarMembroEmProjeto(dto));
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar alocar membro inexistente")
    void alocarMembroEmProjetoShouldThrowExceptionWhenMemberNotFound() {
        CreateProjetoMembroDto dto = new CreateProjetoMembroDto(projeto.getId(), 99999);

        assertThrows(MembroNaoEncontradoException.class, () -> service.alocarMembroEmProjeto(dto));
    }

    @Test
    @DisplayName("Deve remover membro do projeto com sucesso")
    void removerMembroDoProjetoShouldDeleteAllocation() {
        ProjetoMembroId id = new ProjetoMembroId(projeto.getId(), membro.getId());
        ProjetoMembro alocacao = ProjetoMembro.builder()
                .id(id)
                .projeto(projeto)
                .membro(membro)
                .build();
        projetoMembroRepository.save(alocacao);

        service.removerMembroDoProjeto(projeto.getId(), membro.getId());

        assertFalse(projetoMembroRepository.existsById(id));
    }

    @Test
    @DisplayName("Deve lançar exceção ao remover membro não alocado")
    void removerMembroDoProjetoShouldThrowExceptionWhenNotFound() {
        assertThrows(AlocacaoProjetoMembroNaoEncontrada.class, () -> 
            service.removerMembroDoProjeto(projeto.getId(), membro.getId())
        );
    }

    @Test
    @DisplayName("Deve listar todos os membros de um determinado projeto")
    void listarMembrosDoProjetoPorShouldReturnMembersList() {
        ProjetoMembroId id = new ProjetoMembroId(projeto.getId(), membro.getId());
        ProjetoMembro alocacao = ProjetoMembro.builder()
                .id(id)
                .projeto(projeto)
                .membro(membro)
                .build();
        projetoMembroRepository.save(alocacao);

        List<Membro> membros = service.listarMembrosDoProjetoPor(projeto.getId());

        assertFalse(membros.isEmpty());
        assertEquals(1, membros.size());
        assertEquals(membro.getId(), membros.get(0).getId());
    }

    @Test
    @DisplayName("Deve listar todos os projetos de um determinado membro")
    void listarProjetosDoMembroShouldReturnProjectsList() {
        ProjetoMembroId id = new ProjetoMembroId(projeto.getId(), membro.getId());
        ProjetoMembro alocacao = ProjetoMembro.builder()
                .id(id)
                .projeto(projeto)
                .membro(membro)
                .build();
        projetoMembroRepository.save(alocacao);

        List<ProjetoDto> projetos = service.listarProjetosDoMembro(membro.getId());

        assertFalse(projetos.isEmpty());
        assertEquals(1, projetos.size());
        assertEquals(projeto.getId(), projetos.get(0).getId());
    }
}
