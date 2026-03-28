package com.herbertduarte.gestorprojetos.services;

import com.herbertduarte.gestorprojetos.dtos.projeto.ProjetoDto;
import com.herbertduarte.gestorprojetos.repositories.ProjetoJpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjetoService {

    private final ProjetoJpaRepository repository;

    public ProjetoService(ProjetoJpaRepository repository) {
        this.repository = repository;
    }

    public List<ProjetoDto> getAllProjetos(){
        return repository.findAll()
                .stream()
                .map(ProjetoDto::from)
                .toList();
    }
}
