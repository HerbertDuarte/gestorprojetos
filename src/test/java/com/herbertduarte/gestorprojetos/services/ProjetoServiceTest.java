package com.herbertduarte.gestorprojetos.services;

import com.herbertduarte.gestorprojetos.exceptions.MembroNaoEncontradoException;
import com.herbertduarte.gestorprojetos.exceptions.ProjetoNaoEncontradoException;
import com.herbertduarte.gestorprojetos.exceptions.StatusNaoAvancavelException;
import com.herbertduarte.gestorprojetos.exceptions.StatusNaoExcluivelException;
import com.herbertduarte.gestorprojetos.models.dtos.projeto.CreateProjetoDto;
import com.herbertduarte.gestorprojetos.models.dtos.projeto.ProjetoDto;
import com.herbertduarte.gestorprojetos.models.dtos.projeto.UpdateProjetoDto;
import com.herbertduarte.gestorprojetos.models.entities.Membro;
import com.herbertduarte.gestorprojetos.models.entities.Projeto;
import com.herbertduarte.gestorprojetos.models.enums.Atribuicao;
import com.herbertduarte.gestorprojetos.models.enums.Status;
import com.herbertduarte.gestorprojetos.repositories.MembroJpaRepository;
import com.herbertduarte.gestorprojetos.repositories.ProjetoJpaRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class ProjetoServiceTest {

    @Autowired
    private ProjetoService service;

    @Autowired
    private ProjetoJpaRepository projetoRepository;

    @Autowired
    private MembroJpaRepository membroRepository;

    private Membro gerente;

    @BeforeEach
    void setUp() {
        gerente = Membro.builder()
                .nome("Gerente Teste")
                .atribuicao(Atribuicao.FUNCIONARIO)
                .build();
        gerente = membroRepository.save(gerente);
    }

    @AfterEach
    void tearDown() {
        projetoRepository.deleteAll();
        membroRepository.deleteAll();
    }

    @Test
    @DisplayName("Deve criar projeto com status EM_ANALISE")
    void createProjetoShouldPersistWithAnalysisStatus() {
        CreateProjetoDto dto = new CreateProjetoDto(
                "Projeto Teste",
                LocalDateTime.now(),
                LocalDateTime.now().plusMonths(6),
                new BigDecimal("100000.00"),
                "Descrição do projeto",
                gerente.getId()
        );

        service.createProjeto(dto);

        Page<Projeto> projetos = projetoRepository.findAll(PageRequest.of(0, 10));
        assertEquals(1, projetos.getTotalElements());

        Projeto savedProjeto = projetos.getContent().get(0);
        assertEquals("Projeto Teste", savedProjeto.getNome());
        assertEquals(Status.EM_ANALISE, savedProjeto.getStatus());
        assertEquals(gerente.getId(), savedProjeto.getGerente().getId());
        assertEquals(0, new BigDecimal("100000.00").compareTo(savedProjeto.getOrcamentoTotal()));
    }

    @Test
    @DisplayName("Deve lançar exceção ao criar projeto com gerente inexistente")
    void createProjetoShouldThrowExceptionWhenManagerNotFound() {
        CreateProjetoDto dto = new CreateProjetoDto(
                "Projeto Teste",
                LocalDateTime.now(),
                LocalDateTime.now().plusMonths(6),
                new BigDecimal("100000.00"),
                "Descrição do projeto",
                99999
        );

        assertThrows(MembroNaoEncontradoException.class, () -> service.createProjeto(dto));
    }

    @Test
    @DisplayName("Deve retornar todos os projetos com paginação")
    void getAllProjetosShouldReturnPagedResults() {
        Projeto projeto1 = Projeto.builder()
                .nome("Projeto 1")
                .dataInicio(LocalDateTime.now())
                .previsaoTermino(LocalDateTime.now().plusMonths(3))
                .orcamentoTotal(new BigDecimal("50000.00"))
                .gerente(gerente)
                .status(Status.EM_ANALISE)
                .build();

        Projeto projeto2 = Projeto.builder()
                .nome("Projeto 2")
                .dataInicio(LocalDateTime.now())
                .previsaoTermino(LocalDateTime.now().plusMonths(6))
                .orcamentoTotal(new BigDecimal("75000.00"))
                .gerente(gerente)
                .status(Status.EM_ANDAMENTO)
                .build();

        projetoRepository.save(projeto1);
        projetoRepository.save(projeto2);

        Pageable pageable = PageRequest.of(0, 10);
        Page<ProjetoDto> result = service.getAllProjetos(pageable);

        assertEquals(2, result.getTotalElements());
        assertFalse(result.isEmpty());
    }

    @Test
    @DisplayName("Deve retornar projeto por ID")
    void getProjetoByIdShouldReturnProject() {
        Projeto projeto = Projeto.builder()
                .nome("Projeto Busca")
                .dataInicio(LocalDateTime.now())
                .previsaoTermino(LocalDateTime.now().plusMonths(4))
                .orcamentoTotal(new BigDecimal("60000.00"))
                .gerente(gerente)
                .status(Status.EM_ANALISE)
                .build();

        Projeto saved = projetoRepository.save(projeto);

        ProjetoDto result = service.getProjetoById(saved.getId());

        assertNotNull(result);
        assertEquals("Projeto Busca", result.getNome());
    }

    @Test
    @DisplayName("Deve lançar exceção ao buscar projeto inexistente")
    void getProjetoByIdShouldThrowExceptionWhenNotFound() {
        assertThrows(ProjetoNaoEncontradoException.class, () -> service.getProjetoById(99999));
    }

    @Test
    @DisplayName("Deve atualizar projeto existente")
    void atualizarProjetoShouldUpdateFields() {
        Projeto projeto = Projeto.builder()
                .nome("Projeto Original")
                .dataInicio(LocalDateTime.now())
                .previsaoTermino(LocalDateTime.now().plusMonths(3))
                .orcamentoTotal(new BigDecimal("40000.00"))
                .gerente(gerente)
                .status(Status.EM_ANALISE)
                .build();

        Projeto saved = projetoRepository.save(projeto);

        UpdateProjetoDto updateDto = new UpdateProjetoDto(
                "Projeto Atualizado",
                LocalDateTime.now(),
                LocalDateTime.now().plusMonths(8),
                new BigDecimal("80000.00"),
                "Nova descrição",
                gerente.getId()
        );

        service.atualizarProjeto(saved.getId(), updateDto);

        Projeto updated = projetoRepository.findById(saved.getId()).orElseThrow();
        assertEquals("Projeto Atualizado", updated.getNome());
        assertEquals(0, new BigDecimal("80000.00").compareTo(updated.getOrcamentoTotal()));
    }

    @Test
    @DisplayName("Deve trocar gerente do projeto na atualização")
    void atualizarProjetoShouldChangeManager() {
        Membro novoGerente = Membro.builder()
                .nome("Novo Gerente")
                .atribuicao(Atribuicao.FUNCIONARIO)
                .build();
        novoGerente = membroRepository.save(novoGerente);

        Projeto projeto = Projeto.builder()
                .nome("Projeto")
                .dataInicio(LocalDateTime.now())
                .previsaoTermino(LocalDateTime.now().plusMonths(3))
                .orcamentoTotal(new BigDecimal("40000.00"))
                .gerente(gerente)
                .status(Status.EM_ANALISE)
                .build();

        Projeto saved = projetoRepository.save(projeto);

        UpdateProjetoDto updateDto = new UpdateProjetoDto(
                "Projeto",
                LocalDateTime.now(),
                LocalDateTime.now().plusMonths(3),
                new BigDecimal("40000.00"),
                null,
                novoGerente.getId()
        );

        service.atualizarProjeto(saved.getId(), updateDto);

        Projeto updated = projetoRepository.findById(saved.getId()).orElseThrow();
        assertEquals(novoGerente.getId(), updated.getGerente().getId());
    }

    @Test
    @DisplayName("Deve excluir projeto quando status é excluível")
    void excluirProjetoShouldDeleteWhenStatusAllows() {
        Projeto projeto = Projeto.builder()
                .nome("Projeto Para Excluir")
                .dataInicio(LocalDateTime.now())
                .previsaoTermino(LocalDateTime.now().plusMonths(2))
                .orcamentoTotal(new BigDecimal("30000.00"))
                .gerente(gerente)
                .status(Status.EM_ANALISE)
                .build();

        Projeto saved = projetoRepository.save(projeto);

        service.excluirProjeto(saved.getId());

        assertFalse(projetoRepository.findById(saved.getId()).isPresent());
    }

    @Test
    @DisplayName("Deve lançar exceção ao excluir projeto com status não excluível")
    void excluirProjetoShouldThrowExceptionWhenStatusNotDeletable() {
        Projeto projeto = Projeto.builder()
                .nome("Projeto Em Andamento")
                .dataInicio(LocalDateTime.now())
                .previsaoTermino(LocalDateTime.now().plusMonths(2))
                .orcamentoTotal(new BigDecimal("30000.00"))
                .gerente(gerente)
                .status(Status.EM_ANDAMENTO)
                .build();

        Projeto saved = projetoRepository.save(projeto);

        assertThrows(StatusNaoExcluivelException.class, () -> service.excluirProjeto(saved.getId()));
    }

    @Test
    @DisplayName("Deve avançar status do projeto")
    void avancaStatusProjetoShouldProgressToNextStatus() {
        Projeto projeto = Projeto.builder()
                .nome("Projeto Avanço")
                .dataInicio(LocalDateTime.now())
                .previsaoTermino(LocalDateTime.now().plusMonths(4))
                .orcamentoTotal(new BigDecimal("50000.00"))
                .gerente(gerente)
                .status(Status.EM_ANALISE)
                .build();

        Projeto saved = projetoRepository.save(projeto);

        ProjetoDto result = service.avancaStatusProjeto(saved.getId());

        assertEquals(Status.ANALISE_REALIZADA, result.getStatus());
    }

    @Test
    @DisplayName("Deve definir data de término ao avançar para ENCERRADO")
    void avancaStatusProjetoShouldSetEndDateWhenFinalized() {
        Projeto projeto = Projeto.builder()
                .nome("Projeto Finalização")
                .dataInicio(LocalDateTime.now())
                .previsaoTermino(LocalDateTime.now().plusMonths(1))
                .orcamentoTotal(new BigDecimal("25000.00"))
                .gerente(gerente)
                .status(Status.EM_ANDAMENTO)
                .build();

        Projeto saved = projetoRepository.save(projeto);

        ProjetoDto result = service.avancaStatusProjeto(saved.getId());

        assertEquals(Status.ENCERRADO, result.getStatus());
        assertNotNull(result.getDataTermino());
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar avançar status não avançável")
    void avancaStatusProjetoShouldThrowExceptionWhenNotAdvanceable() {
        Projeto projeto = Projeto.builder()
                .nome("Projeto Encerrado")
                .dataInicio(LocalDateTime.now())
                .previsaoTermino(LocalDateTime.now().plusMonths(1))
                .orcamentoTotal(new BigDecimal("25000.00"))
                .gerente(gerente)
                .status(Status.ENCERRADO)
                .build();

        Projeto saved = projetoRepository.save(projeto);

        assertThrows(StatusNaoAvancavelException.class, () -> service.avancaStatusProjeto(saved.getId()));
    }

    @Test
    @DisplayName("Deve cancelar projeto")
    void cancelarProjetoShouldSetStatusToCanceled() {
        Projeto projeto = Projeto.builder()
                .nome("Projeto Cancelamento")
                .dataInicio(LocalDateTime.now())
                .previsaoTermino(LocalDateTime.now().plusMonths(3))
                .orcamentoTotal(new BigDecimal("45000.00"))
                .gerente(gerente)
                .status(Status.EM_ANDAMENTO)
                .build();

        Projeto saved = projetoRepository.save(projeto);

        ProjetoDto result = service.cancelarProjeto(saved.getId());

        assertEquals(Status.CANCELADO, result.getStatus());
    }

    @Test
    @DisplayName("Deve lançar exceção ao cancelar projeto inexistente")
    void cancelarProjetoShouldThrowExceptionWhenNotFound() {
        assertThrows(ProjetoNaoEncontradoException.class, () -> service.cancelarProjeto(99999));
    }
}
