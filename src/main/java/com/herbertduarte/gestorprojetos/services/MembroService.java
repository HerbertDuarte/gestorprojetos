package com.herbertduarte.gestorprojetos.services;

import com.herbertduarte.gestorprojetos.dtos.membro.CreateMembroDto;
import com.herbertduarte.gestorprojetos.models.Membro;
import com.herbertduarte.gestorprojetos.enums.Atribuicao;
import com.herbertduarte.gestorprojetos.repositories.MembroJpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MembroService {

    private final MembroJpaRepository repository;

    public MembroService(MembroJpaRepository repository) {
        this.repository = repository;
    }

    public void createMembro(CreateMembroDto dto){
        Membro novoMembro = Membro.builder()
                .nome(dto.nome())
                .atribuicao(Atribuicao.toEnum(dto.atribuicao()))
                .build();

        repository.save(novoMembro);
    }

    public List<Membro> getAllMembros(){
        return repository.findAll();
    }
}
