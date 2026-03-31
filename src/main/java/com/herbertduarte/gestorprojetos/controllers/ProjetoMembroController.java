package com.herbertduarte.gestorprojetos.controllers;

import com.herbertduarte.gestorprojetos.dtos.projeto.ProjetoDto;
import com.herbertduarte.gestorprojetos.dtos.projetomembro.CreateProjetoMembroDto;
import com.herbertduarte.gestorprojetos.dtos.projetomembro.ProjetoMembroDto;
import com.herbertduarte.gestorprojetos.models.Membro;
import com.herbertduarte.gestorprojetos.services.ProjetoMembroService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("projeto-membro")
public class ProjetoMembroController {

    private final ProjetoMembroService projetoMembroService;

    public ProjetoMembroController(ProjetoMembroService projetoMembroService) {
        this.projetoMembroService = projetoMembroService;
    }


    @PostMapping
    public ResponseEntity<ProjetoMembroDto> alocarMembroProjeto(
            @Valid @RequestBody CreateProjetoMembroDto dto
    ) {
        ProjetoMembroDto resultado = projetoMembroService.alocarMembroEmProjeto(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(resultado);
    }


    @DeleteMapping("/{projetoId}/{membroId}")
    public ResponseEntity<Void> removerMembroDoProjeto(
            @PathVariable Integer projetoId,
            @PathVariable Integer membroId
    ) {
        projetoMembroService.removerMembroDoProjeto(projetoId, membroId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/projeto/{projetoId}/membros")
    public ResponseEntity<List<Membro>> listarMembrosDoProjetoPor(
            @PathVariable Integer projetoId
    ) {
        List<Membro> membros = projetoMembroService.listarMembrosDoProjetoPor(projetoId);
        return ResponseEntity.ok(membros);
    }

    @GetMapping("/membro/{membroId}/projetos")
    public ResponseEntity<List<ProjetoDto>> listarProjetosDoMembro(
            @PathVariable Integer membroId
    ) {
        List<ProjetoDto> projetos = projetoMembroService.listarProjetosDoMembro(membroId);
        return ResponseEntity.ok(projetos);
    }
}
