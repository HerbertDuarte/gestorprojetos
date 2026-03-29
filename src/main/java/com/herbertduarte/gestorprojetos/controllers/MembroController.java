package com.herbertduarte.gestorprojetos.controllers;

import com.herbertduarte.gestorprojetos.dtos.membro.CreateMembroDto;
import com.herbertduarte.gestorprojetos.enums.Atribuicao;
import com.herbertduarte.gestorprojetos.models.Membro;
import com.herbertduarte.gestorprojetos.services.MembroService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/membros")
public class MembroController {

    private final MembroService membroService;

    public MembroController(MembroService membroService) {
        this.membroService = membroService;
    }

    @GetMapping()
    public ResponseEntity<Page<Membro>> getMembros(
            @PageableDefault(sort = "nome") Pageable pageable,
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) Atribuicao atribuicao){
        Page<Membro> membros = membroService.getAllMembros(pageable, nome, atribuicao);

        return ResponseEntity.ok(membros);
    }

    @PostMapping()
    public ResponseEntity<Void> createMembro(@RequestBody CreateMembroDto payload){
        membroService.createMembro(payload);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
