package com.herbertduarte.gestorprojetos.services;

import com.herbertduarte.gestorprojetos.dtos.projeto.ProjetoDto;
import com.herbertduarte.gestorprojetos.dtos.projetomembro.CreateProjetoMembroDto;
import com.herbertduarte.gestorprojetos.dtos.projetomembro.ProjetoMembroDto;
import com.herbertduarte.gestorprojetos.exceptions.AlocacaoProjetoMembroNaoEncontrada;
import com.herbertduarte.gestorprojetos.exceptions.MembroNaoEncontradoException;
import com.herbertduarte.gestorprojetos.exceptions.ProjetoNaoEncontradoException;
import com.herbertduarte.gestorprojetos.models.Membro;
import com.herbertduarte.gestorprojetos.models.Projeto;
import com.herbertduarte.gestorprojetos.models.ProjetoMembro;
import com.herbertduarte.gestorprojetos.models.ProjetoMembroId;
import com.herbertduarte.gestorprojetos.repositories.MembroJpaRepository;
import com.herbertduarte.gestorprojetos.repositories.ProjetoJpaRepository;
import com.herbertduarte.gestorprojetos.repositories.ProjetoMembroRepository;
import com.herbertduarte.gestorprojetos.validators.ProjetoMembroValidator;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjetoMembroService {

    private final ProjetoMembroRepository projetoMembroRepository;
    private final ProjetoJpaRepository projetoRepository;
    private final MembroJpaRepository membroRepository;
    private final ProjetoMembroValidator validator;

    public ProjetoMembroService(
            ProjetoMembroRepository projetoMembroRepository,
            ProjetoJpaRepository projetoRepository,
            MembroJpaRepository membroRepository,
            ProjetoMembroValidator validator
    ) {
        this.projetoMembroRepository = projetoMembroRepository;
        this.projetoRepository = projetoRepository;
        this.membroRepository = membroRepository;
        this.validator = validator;
    }


    public ProjetoMembroDto alocarMembroEmProjeto(CreateProjetoMembroDto dto) {
        Projeto projeto = projetoRepository.findById(dto.projetoId())
                .orElseThrow(ProjetoNaoEncontradoException::new);

        Membro membro = membroRepository.findById(dto.membroId())
                .orElseThrow(MembroNaoEncontradoException::new);

        validator.validarAtribuicaoMembro(membro);
        validator.validarAlocacaoSimultanea(membro);
        validator.validarCapacidadeProjeto(projeto);
        validator.validarAlocacaoDuplicada(dto.projetoId(), dto.membroId());

        ProjetoMembroId id = new ProjetoMembroId(dto.projetoId(), dto.membroId());
        ProjetoMembro projetoMembro = ProjetoMembro.builder()
                .id(id)
                .projeto(projeto)
                .membro(membro)
                .build();

        ProjetoMembro projetoMembroSalvo = projetoMembroRepository.save(projetoMembro);
        return ProjetoMembroDto.from(projetoMembroSalvo);
    }


    public void removerMembroDoProjeto(Integer projetoId, Integer membroId) {
        ProjetoMembroId id = new ProjetoMembroId(projetoId, membroId);

        if (!projetoMembroRepository.existsById(id))
            throw new AlocacaoProjetoMembroNaoEncontrada();

        projetoMembroRepository.deleteById(id);
    }


    public List<Membro> listarMembrosDoProjetoPor(Integer projetoId) {
        return projetoMembroRepository.findMembrosByProjetoId(projetoId);
    }


    public List<ProjetoDto> listarProjetosDoMembro(Integer membroId) {
        return projetoMembroRepository.findProjetosByMembroId(membroId)
                .stream()
                .map(ProjetoDto::from)
                .toList();
    }


}
