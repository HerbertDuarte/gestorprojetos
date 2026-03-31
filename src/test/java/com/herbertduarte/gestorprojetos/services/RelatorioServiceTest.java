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
    @DisplayName("Deve gerar relatorio de portfolio corretamente")
    void gerarRelatorioPortfolioShouldReturnValidMetrics() {
        Projeto projetoEmAnalise = Projeto.builder()
                .nome("Projeto 1")
                .dataInicio(LocalDateTime.now().minusDays(10))
                .previsaoTermino(LocalDateTime.now().plusMonths(2))
                .orcamentoTotal(new BigDecimal("10000.00"))
                .gerente(gerente)
                .status(Status.EM_ANALISE)
                .build();
        projetoEmAnalise = projetoRepository.save(projetoEmAnalise);

        Projeto projetoEmAndamento = Projeto.builder()
                .nome("Projeto 2")
                .dataInicio(LocalDateTime.now().minusDays(5))
                .previsaoTermino(LocalDateTime.now().plusMonths(3))
                .orcamentoTotal(new BigDecimal("20000.00"))
                .gerente(gerente)
                .status(Status.EM_ANDAMENTO)
                .build();
        projetoEmAndamento = projetoRepository.save(projetoEmAndamento);

        Projeto projetoEncerrado = Projeto.builder()
                .nome("Projeto 3")
                .dataInicio(LocalDateTime.now().minusDays(20))
                .previsaoTermino(LocalDateTime.now().plusDays(10))
                .dataTermino(LocalDateTime.now().minusDays(10)) 
                .orcamentoTotal(new BigDecimal("30000.00"))
                .gerente(gerente)
                .status(Status.ENCERRADO)
                .build();
        projetoEncerrado = projetoRepository.save(projetoEncerrado);

    
        ProjetoMembro alocacao1 = ProjetoMembro.builder()
                .id(new ProjetoMembroId(projetoEmAnalise.getId(), membro1.getId()))
                .projeto(projetoEmAnalise)
                .membro(membro1)
                .build();
        projetoMembroRepository.save(alocacao1);

        ProjetoMembro alocacao2 = ProjetoMembro.builder()
                .id(newProjetoMembroId(projetoEmAnalise.getId(), membro2.getId()))
                .projeto(projetoEmAnalise)
                .membro(membro2)
                .build();
        projetoMembroRepository.save(alocacao2);

        ProjetoMembro alocacao3 = ProjetoMembro.builder()
                .id(new ProjetoMembroId(projetoEmAndamento.getId(), membro1.getId()))
                .projeto(projetoEmAndamento)
                .membro(membro1)
                .build();
        projetoMembroRepository.save(alocacao3);

        RelatorioDto relatorio = relatorioService.gerarRelatorioPortfolio();

        assertNotNull(relatorio);
        
        assertEquals(1, relatorio.getQuantidadeProjetosPorStatus().get(Status.EM_ANALISE));
        assertEquals(1, relatorio.getQuantidadeProjetosPorStatus().get(Status.EM_ANDAMENTO));
        assertEquals(1, relatorio.getQuantidadeProjetosPorStatus().get(Status.ENCERRADO));
        assertNull(relatorio.getQuantidadeProjetosPorStatus().get(Status.CANCELADO));

        assertEquals(0, new BigDecimal("10000.00").compareTo(relatorio.getTotalOrcadoPorStatus().get(Status.EM_ANALISE)));
        assertEquals(0, new BigDecimal("20000.00").compareTo(relatorio.getTotalOrcadoPorStatus().get(Status.EM_ANDAMENTO)));
        assertEquals(0, new BigDecimal("30000.00").compareTo(relatorio.getTotalOrcadoPorStatus().get(Status.ENCERRADO)));

 
        assertEquals(2L, relatorio.getTotalMembrosUnicosAlocados());
        
     
        assertNotNull(relatorio.getMediaDuracaoProjetosEncerrados());
        assertTrue(relatorio.getMediaDuracaoProjetosEncerrados() >= 0.0);
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
    
    private ProjetoMembroId newProjetoMembroId(Integer projetoId, Integer membroId) {
        return new ProjetoMembroId(projetoId, membroId);
    }

}
