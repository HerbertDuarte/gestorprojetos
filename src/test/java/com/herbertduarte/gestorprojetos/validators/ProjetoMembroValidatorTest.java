package com.herbertduarte.gestorprojetos.validators;

import com.herbertduarte.gestorprojetos.exceptions.*;
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
class ProjetoMembroValidatorTest {

    @Autowired
    private ProjetoMembroValidator validator;

    @Autowired
    private ProjetoMembroRepository projetoMembroRepository;

    @Autowired
    private MembroJpaRepository membroRepository;

    @Autowired
    private ProjetoJpaRepository projetoRepository;

    private Membro gerente;
    private Projeto projeto;

    @BeforeEach
    void setUp() {
        gerente = Membro.builder()
                .nome("Gerente Teste")
                .atribuicao(Atribuicao.FUNCIONARIO)
                .build();
        gerente = membroRepository.save(gerente);

        projeto = Projeto.builder()
                .nome("Projeto Teste")
                .dataInicio(LocalDateTime.now())
                .previsaoTermino(LocalDateTime.now().plusMonths(6))
                .orcamentoTotal(new BigDecimal("100000.00"))
                .gerente(gerente)
                .status(Status.EM_ANDAMENTO)
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
    @DisplayName("Deve validar atribuição FUNCIONARIO com sucesso")
    void validarAtribuicaoMembroShouldPassForFuncionario() {
        Membro funcionario = Membro.builder()
                .nome("Funcionario Teste")
                .atribuicao(Atribuicao.FUNCIONARIO)
                .build();
        Membro funcionarioSalvo = membroRepository.save(funcionario);

        assertDoesNotThrow(() -> validator.validarAtribuicaoMembro(funcionarioSalvo));
    }

    @Test
    @DisplayName("Deve lançar exceção quando atribuição não é FUNCIONARIO")
    void validarAtribuicaoMembroShouldThrowExceptionForNonFuncionario() {
        Membro gerente = Membro.builder()
                .nome("Gerente")
                .atribuicao(Atribuicao.OUTRO)
                .build();
        Membro gerenteSalvo = membroRepository.save(gerente);

        assertThrows(AtribuicaoInvalidaParaIngressarEmProjetoException.class,
                () -> validator.validarAtribuicaoMembro(gerenteSalvo));
    }

    @Test
    @DisplayName("Deve lançar exceção quando membro tem atribuição OUTRO")
    void validarAtribuicaoMembroShouldThrowExceptionForOutro() {
        Membro outro = Membro.builder()
                .nome("Outro Membro")
                .atribuicao(Atribuicao.OUTRO)
                .build();
        Membro outroMembroSalvo = membroRepository.save(outro);

        assertThrows(AtribuicaoInvalidaParaIngressarEmProjetoException.class,
                () -> validator.validarAtribuicaoMembro(outroMembroSalvo));
    }

    @Test
    @DisplayName("Deve permitir alocação quando membro tem menos de 3 projetos ativos")
    void validarAlocacaoSimultaneaShouldPassWhenLessThan3Projects() {
        Membro funcionario = Membro.builder()
                .nome("Funcionario")
                .atribuicao(Atribuicao.FUNCIONARIO)
                .build();
        Membro funcionarioSalvo = membroRepository.save(funcionario);

        for (int i = 0; i < 2; i++) {
            Projeto p = Projeto.builder()
                    .nome("Projeto " + i)
                    .dataInicio(LocalDateTime.now())
                    .previsaoTermino(LocalDateTime.now().plusMonths(3))
                    .orcamentoTotal(new BigDecimal("50000.00"))
                    .gerente(gerente)
                    .status(Status.EM_ANDAMENTO)
                    .build();
            p = projetoRepository.save(p);

            ProjetoMembro pm = ProjetoMembro.builder()
                    .id(new ProjetoMembroId(p.getId(), funcionarioSalvo.getId()))
                    .projeto(p)
                    .membro(funcionarioSalvo)
                    .build();
            projetoMembroRepository.save(pm);
        }

        assertDoesNotThrow(() -> validator.validarAlocacaoSimultanea(funcionarioSalvo));
    }

    @Test
    @DisplayName("Deve lançar exceção quando membro já está em 3 projetos ativos")
    void validarAlocacaoSimultaneaShouldThrowExceptionWhen3ActiveProjects() {
        Membro funcionario = Membro.builder()
                .nome("Funcionario")
                .atribuicao(Atribuicao.FUNCIONARIO)
                .build();
        Membro funcionarioSalvo = membroRepository.save(funcionario);

        // Aloca em 3 projetos ativos
        for (int i = 0; i < 3; i++) {
            Projeto p = Projeto.builder()
                    .nome("Projeto " + i)
                    .dataInicio(LocalDateTime.now())
                    .previsaoTermino(LocalDateTime.now().plusMonths(3))
                    .orcamentoTotal(new BigDecimal("50000.00"))
                    .gerente(gerente)
                    .status(Status.EM_ANDAMENTO)
                    .build();
            p = projetoRepository.save(p);

            ProjetoMembro pm = ProjetoMembro.builder()
                    .id(new ProjetoMembroId(p.getId(), funcionarioSalvo.getId()))
                    .projeto(p)
                    .membro(funcionarioSalvo)
                    .build();
            projetoMembroRepository.save(pm);
        }

        assertThrows(LimiteProjetosPorMembroException.class,
                () -> validator.validarAlocacaoSimultanea(funcionarioSalvo));
    }

    @Test
    @DisplayName("Deve ignorar projetos ENCERRADO e CANCELADO na contagem de alocação")
    void validarAlocacaoSimultaneaShouldIgnoreClosedAndCanceledProjects() {
        Membro funcionario = Membro.builder()
                .nome("Funcionario")
                .atribuicao(Atribuicao.FUNCIONARIO)
                .build();
        Membro funcionarioSalvo = membroRepository.save(funcionario);

        // Aloca em 2 projetos encerrados
        for (int i = 0; i < 2; i++) {
            Projeto p = Projeto.builder()
                    .nome("Projeto Encerrado " + i)
                    .dataInicio(LocalDateTime.now())
                    .previsaoTermino(LocalDateTime.now().plusMonths(3))
                    .orcamentoTotal(new BigDecimal("50000.00"))
                    .gerente(gerente)
                    .status(Status.ENCERRADO)
                    .build();
            p = projetoRepository.save(p);

            ProjetoMembro pm = ProjetoMembro.builder()
                    .id(new ProjetoMembroId(p.getId(), funcionarioSalvo.getId()))
                    .projeto(p)
                    .membro(funcionario)
                    .build();
            projetoMembroRepository.save(pm);
        }

        // Aloca em 1 projeto cancelado
        Projeto pCancelado = Projeto.builder()
                .nome("Projeto Cancelado")
                .dataInicio(LocalDateTime.now())
                .previsaoTermino(LocalDateTime.now().plusMonths(3))
                .orcamentoTotal(new BigDecimal("50000.00"))
                .gerente(gerente)
                .status(Status.CANCELADO)
                .build();
        pCancelado = projetoRepository.save(pCancelado);

        ProjetoMembro pmCancelado = ProjetoMembro.builder()
                .id(new ProjetoMembroId(pCancelado.getId(), funcionarioSalvo.getId()))
                .projeto(pCancelado)
                .membro(funcionarioSalvo)
                .build();
        projetoMembroRepository.save(pmCancelado);

        assertDoesNotThrow(() -> validator.validarAlocacaoSimultanea(funcionarioSalvo));
    }

    @Test
    @DisplayName("Deve permitir adicionar membro quando projeto tem menos de 10 membros")
    void validarCapacidadeProjetoShouldPassWhenLessThan10Members() {
        // Adiciona 9 membros ao projeto
        for (int i = 0; i < 9; i++) {
            Membro m = Membro.builder()
                    .nome("Membro " + i)
                    .atribuicao(Atribuicao.FUNCIONARIO)
                    .build();
            m = membroRepository.save(m);

            ProjetoMembro pm = ProjetoMembro.builder()
                    .id(new ProjetoMembroId(projeto.getId(), m.getId()))
                    .projeto(projeto)
                    .membro(m)
                    .build();
            projetoMembroRepository.save(pm);
        }

        assertDoesNotThrow(() -> validator.validarCapacidadeProjeto(projeto));
    }

    @Test
    @DisplayName("Deve lançar exceção quando projeto já tem 10 membros")
    void validarCapacidadeProjetoShouldThrowExceptionWhen10Members() {
        // Adiciona 10 membros ao projeto
        for (int i = 0; i < 10; i++) {
            Membro m = Membro.builder()
                    .nome("Membro " + i)
                    .atribuicao(Atribuicao.FUNCIONARIO)
                    .build();
            m = membroRepository.save(m);

            ProjetoMembro pm = ProjetoMembro.builder()
                    .id(new ProjetoMembroId(projeto.getId(), m.getId()))
                    .projeto(projeto)
                    .membro(m)
                    .build();
            projetoMembroRepository.save(pm);
        }

        assertThrows(LimiteMembrosPorProjetoException.class,
                () -> validator.validarCapacidadeProjeto(projeto));
    }

    @Test
    @DisplayName("Deve permitir alocação quando membro não está no projeto")
    void validarAlocacaoDuplicadaShouldPassWhenMemberNotInProject() {
        Membro funcionario = Membro.builder()
                .nome("Funcionario")
                .atribuicao(Atribuicao.FUNCIONARIO)
                .build();
        Membro funcionarioSalvo = membroRepository.save(funcionario);

        assertDoesNotThrow(() -> validator.validarAlocacaoDuplicada(projeto.getId(), funcionarioSalvo.getId()));
    }

    @Test
    @DisplayName("Deve lançar exceção quando membro já está alocado no projeto")
    void validarAlocacaoDuplicadaShouldThrowExceptionWhenMemberAlreadyAllocated() {
        Membro funcionario = Membro.builder()
                .nome("Funcionario")
                .atribuicao(Atribuicao.FUNCIONARIO)
                .build();
        Membro funcionarioSalvo = membroRepository.save(funcionario);

        // Aloca membro no projeto
        ProjetoMembro pm = ProjetoMembro.builder()
                .id(new ProjetoMembroId(projeto.getId(), funcionarioSalvo.getId()))
                .projeto(projeto)
                .membro(funcionarioSalvo)
                .build();
        projetoMembroRepository.save(pm);

        assertThrows(MembroJaEstaAlocadoNoProjetoException.class,
                () -> validator.validarAlocacaoDuplicada(projeto.getId(), funcionarioSalvo.getId()));
    }
}
