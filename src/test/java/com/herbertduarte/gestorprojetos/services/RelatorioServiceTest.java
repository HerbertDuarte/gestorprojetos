package com.herbertduarte.gestorprojetos.services;

import com.herbertduarte.gestorprojetos.models.dtos.relatorio.RelatorioDto;
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

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class RelatorioServiceTest {

    @Autowired
    private RelatorioService relatorioService;

    @Autowired
    private ProjetoJpaRepository projetoRepository;

    @Autowired
    private MembroJpaRepository membroRepository;

    @Autowired
    private ProjetoMembroRepository projetoMembroRepository;

    private Membro gerente;
    private Membro membro1;
    private Membro membro2;

    @BeforeEach
    void setUp() {
        gerente = Membro.builder()
                .nome("Gerente do Relatorio")
                .atribuicao(Atribuicao.FUNCIONARIO)
                .build();
        gerente = membroRepository.save(gerente);

        membro1 = Membro.builder()
                .nome("Membro 1")
                .atribuicao(Atribuicao.FUNCIONARIO)
                .build();
        membro1 = membroRepository.save(membro1);

        membro2 = Membro.builder()
                .nome("Membro 2")
                .atribuicao(Atribuicao.FUNCIONARIO)
                .build();
        membro2 = membroRepository.save(membro2);
    }

    @AfterEach
    void tearDown() {
        projetoMembroRepository.deleteAll();
        projetoRepository.deleteAll();
        membroRepository.deleteAll();
    }

    
    @Test
    @DisplayName("Deve gerar relatorio de portfolio vazio quando não houver dados")
    void gerarRelatorioPortfolioShouldReturnEmptyMetricsWhenNoData() {
        RelatorioDto relatorio = relatorioService.gerarRelatorioPortfolio();

        assertNotNull(relatorio);
        assertTrue(relatorio.getQuantidadeProjetosPorStatus().isEmpty());
        assertTrue(relatorio.getTotalOrcadoPorStatus().isEmpty());
        assertEquals(0.0, relatorio.getMediaDuracaoProjetosEncerrados());
        assertEquals(0L, relatorio.getTotalMembrosUnicosAlocados());
    }

}
