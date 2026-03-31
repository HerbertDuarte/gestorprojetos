package com.herbertduarte.gestorprojetos.controllers;

import com.herbertduarte.gestorprojetos.dtos.membro.CreateMembroDto;
import com.herbertduarte.gestorprojetos.enums.Atribuicao;
import com.herbertduarte.gestorprojetos.exceptions.globals.ErrorResponseDto;
import com.herbertduarte.gestorprojetos.models.Membro;
import com.herbertduarte.gestorprojetos.services.MembroService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/membros")
@Tag(name = "Membros", description = "Endpoints para gerenciamento de membros")
public class MembroController {

    private final MembroService membroService;

    public MembroController(MembroService membroService) {
        this.membroService = membroService;
    }

    @GetMapping()
    @Operation(summary = "Listar membros", description = "Retorna uma lista paginada de membros com opções de filtro por nome e atribuição")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de membros retornada com sucesso"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            )
    })
    public ResponseEntity<Page<Membro>> getMembros(
            @PageableDefault(sort = "nome")
            @Schema(example = "{\"page\": 0, \"size\": 10, \"sort\": \"nome\"}")
            Pageable pageable,
            @RequestParam(required = false, defaultValue = "") String nome,
            @RequestParam(required = false) Atribuicao atribuicao){
        Page<Membro> membros = membroService.getAllMembros(pageable, nome, atribuicao);

        return ResponseEntity.ok(membros);
    }

    @PostMapping()
    @Operation(summary = "Criar membro", description = "Cria um novo membro no sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Membro criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            ),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            )
    })
    public ResponseEntity<Void> createMembro(@RequestBody CreateMembroDto payload){
        membroService.createMembro(payload);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
