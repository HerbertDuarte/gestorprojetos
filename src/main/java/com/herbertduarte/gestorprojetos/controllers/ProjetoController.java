package com.herbertduarte.gestorprojetos.controllers;

import com.herbertduarte.gestorprojetos.dtos.projeto.CreateProjetoDto;
import com.herbertduarte.gestorprojetos.dtos.projeto.ProjetoDto;
import com.herbertduarte.gestorprojetos.dtos.projeto.UpdateProjetoDto;
import com.herbertduarte.gestorprojetos.services.ProjetoService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/projetos")
public class ProjetoController {

    private final ProjetoService projetoService;

    public ProjetoController(ProjetoService projetoService) {
        this.projetoService = projetoService;
    }

    @GetMapping()
    public ResponseEntity<Page<ProjetoDto>> getAllProjetos(
            @PageableDefault(sort = "nome") // default size = 10, page = 0
            Pageable pageable){
        Page<ProjetoDto> projetos = projetoService.getAllProjetos(pageable);
        return ResponseEntity.ok(projetos);
    }

    @GetMapping("/{projetoId}")
    public ResponseEntity<ProjetoDto> getProjetoById(@PathVariable Integer projetoId){
        ProjetoDto projeto = projetoService.getProjetoById(projetoId);
        return ResponseEntity.ok(projeto);
    }

    @PostMapping()
    public ResponseEntity<Void> createProjeto(CreateProjetoDto payload){
        projetoService.createProjeto(payload);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{projetoId}")
    public ResponseEntity<Void> updateProjeto(@PathVariable Integer projetoId, UpdateProjetoDto payload){
        projetoService.atualizarProjeto(projetoId, payload);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{projetoId}")
    public ResponseEntity<Void> deleteProjeto(@PathVariable Integer projetoId){
        projetoService.excluirProjeto(projetoId);
        return ResponseEntity.ok().build();
    }
}
