package com.herbertduarte.gestorprojetos.validators;

import com.herbertduarte.gestorprojetos.models.enums.Atribuicao;
import com.herbertduarte.gestorprojetos.models.enums.Status;
import com.herbertduarte.gestorprojetos.exceptions.*;
import com.herbertduarte.gestorprojetos.models.entities.Membro;
import com.herbertduarte.gestorprojetos.models.entities.Projeto;
import com.herbertduarte.gestorprojetos.repositories.ProjetoMembroRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProjetoMembroValidator {

    private final ProjetoMembroRepository projetoMembroRepository;

    public ProjetoMembroValidator(ProjetoMembroRepository projetoMembroRepository) {
        this.projetoMembroRepository = projetoMembroRepository;
    }


    public void validarAtribuicaoMembro(Membro membro) {
        if (!Atribuicao.FUNCIONARIO.equals(membro.getAtribuicao())) {
            throw new AtribuicaoInvalidaParaIngressarEmProjetoException();
        }
    }


    public void validarAlocacaoSimultanea(Membro membro) {
        List<Status> statusExcluidos = List.of(Status.ENCERRADO, Status.CANCELADO);
        Long qtdProjetosAtivos = projetoMembroRepository.countByMembroIdAndProjetoStatusNotIn(membro.getId(), statusExcluidos);

        if (qtdProjetosAtivos >= 3) {
            throw new LimiteProjetosPorMembroException();
        }
    }


    public void validarCapacidadeProjeto(Projeto projeto) {
        Long qtdMembrosProjeto = projetoMembroRepository
                .countByProjetoId(projeto.getId());

        if (qtdMembrosProjeto >= 10) {
            throw new LimiteMembrosPorProjetoException();
        }
    }


    public void validarAlocacaoDuplicada(Integer projetoId, Integer membroId) {
        boolean membroJaAlocado = projetoMembroRepository
                .findByProjetoIdAndMembroId(projetoId, membroId)
                .isPresent();

        if (membroJaAlocado) {
            throw new MembroJaEstaAlocadoNoProjetoException();
        }
    }
}
