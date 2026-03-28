package com.herbertduarte.gestorprojetos.services;

import com.herbertduarte.gestorprojetos.dtos.projeto.CreateProjetoDto;
import com.herbertduarte.gestorprojetos.dtos.projeto.ProjetoDto;
import com.herbertduarte.gestorprojetos.dtos.projeto.UpdateProjetoDto;
import com.herbertduarte.gestorprojetos.enums.Status;
import com.herbertduarte.gestorprojetos.exceptions.MembroNaoEncontradoException;
import com.herbertduarte.gestorprojetos.exceptions.ProjetoNaoEncontradoException;
import com.herbertduarte.gestorprojetos.exceptions.StatusNaoExcluivelException;
import com.herbertduarte.gestorprojetos.models.Membro;
import com.herbertduarte.gestorprojetos.models.Projeto;
import com.herbertduarte.gestorprojetos.repositories.MembroJpaRepository;
import com.herbertduarte.gestorprojetos.repositories.ProjetoJpaRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ProjetoService {

    private final ProjetoJpaRepository projetoRepository;
    private final MembroJpaRepository membroRepository;

    public ProjetoService(ProjetoJpaRepository projetoRepository,
                          MembroJpaRepository membroRepository) {
        this.projetoRepository = projetoRepository;
        this.membroRepository = membroRepository;
    }

    public Page<ProjetoDto> getAllProjetos(Pageable pageable){
        return projetoRepository.findAll(pageable).map(ProjetoDto::from);
    }

    public void createProjeto(CreateProjetoDto payload){

        Membro gerente = membroRepository.findById(payload.gerenteId())
                .orElseThrow(MembroNaoEncontradoException::new);

        Projeto novoProjeto = Projeto.builder()
                .nome(payload.nome())
                .descricao(payload.descricao())
                .dataInicio(payload.dataInicio())
                .dataTermino(payload.dataTermino())
                .gerente(gerente)
                .status(Status.EM_ANALISE)
                .orcamentoTotal(payload.orcamentoTotal())
                .build();

        projetoRepository.save(novoProjeto);
    }

    public ProjetoDto getProjetoById(Integer id){
        return ProjetoDto.from(projetoRepository.findById(id).orElseThrow(ProjetoNaoEncontradoException::new));
    }

    public void atualizarProjeto(Integer projetoId, UpdateProjetoDto payload){
        Projeto projeto = projetoRepository.findById(projetoId).orElseThrow(ProjetoNaoEncontradoException::new);

        BeanUtils.copyProperties(payload, projeto);

        if(!projeto.getGerente().getId().equals(payload.gerenteId())){
            Membro novoGerente = membroRepository.findById(payload.gerenteId())
                    .orElseThrow(MembroNaoEncontradoException::new);
            projeto.setGerente(novoGerente);
        }

        projetoRepository.save(projeto);
    }

    public void excluirProjeto(Integer id){
        Projeto projeto = projetoRepository.findById(id).orElseThrow(ProjetoNaoEncontradoException::new);
        if(!projeto.getStatus().excluivel()){
            throw new StatusNaoExcluivelException();
        }
        projetoRepository.delete(projeto);
    }

    public ProjetoDto avancaStatusProjeto(Integer projetoId){
        Projeto projeto = projetoRepository.findById(projetoId).orElseThrow(ProjetoNaoEncontradoException::new);
        projeto.setStatus(projeto.getStatus().proximo());
        projetoRepository.save(projeto);
        return ProjetoDto.from(projeto);
    }

    public ProjetoDto cancelarProjeto(Integer projetoId){
        Projeto projeto = projetoRepository.findById(projetoId).orElseThrow(ProjetoNaoEncontradoException::new);
        projeto.setStatus(Status.CANCELADO);
        projetoRepository.save(projeto);
        return ProjetoDto.from(projeto);
    }
}
