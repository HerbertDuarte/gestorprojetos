package com.herbertduarte.gestorprojetos.controllers;

import com.herbertduarte.gestorprojetos.models.dtos.projeto.CreateProjetoDto;
import com.herbertduarte.gestorprojetos.models.dtos.projeto.ProjetoDto;
import com.herbertduarte.gestorprojetos.models.dtos.projeto.UpdateProjetoDto;
import com.herbertduarte.gestorprojetos.config.exceptions.ErrorResponseDto;
import com.herbertduarte.gestorprojetos.services.ProjetoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/projetos")
@Tag(name = "Projetos", description = "Endpoints para gerenciamento de projetos")
public class ProjetoController {

    private final ProjetoService projetoService;

    public ProjetoController(ProjetoService projetoService) {
        this.projetoService = projetoService;
    }

    @GetMapping()
    @Operation(summary = "Listar projetos", description = "Retorna uma lista paginada de projetos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de projetos retornada com sucesso"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            )
    })
    public ResponseEntity<Page<ProjetoDto>> getAllProjetos(
            @PageableDefault(sort = "nome")
            @Schema(example = "{\"page\": 0, \"size\": 10, \"sort\": \"nome\"}")
            Pageable pageable){
        Page<ProjetoDto> projetos = projetoService.getAllProjetos(pageable);
        return ResponseEntity.ok(projetos);
    }

    @GetMapping("/{projetoId}")
    @Operation(summary = "Buscar projeto por ID", description = "Retorna os detalhes de um projeto específico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Projeto encontrado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Projeto não encontrado",
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
    public ResponseEntity<ProjetoDto> getProjetoById(@PathVariable Integer projetoId){
        ProjetoDto projeto = projetoService.getProjetoById(projetoId);
        return ResponseEntity.ok(projeto);
    }

    @PostMapping()
    @Operation(summary = "Criar projeto", description = "Cria um novo projeto no sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Projeto criado com sucesso"),
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
    public ResponseEntity<Void> createProjeto(@RequestBody @Valid CreateProjetoDto payload){
        projetoService.createProjeto(payload);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/{projetoId}")
    @Operation(summary = "Atualizar projeto", description = "Atualiza os dados de um projeto existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Projeto atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            ),
            @ApiResponse(responseCode = "404", description = "Projeto não encontrado",
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
    public ResponseEntity<Void> updateProjeto(
            @PathVariable Integer projetoId,
            @RequestBody @Valid UpdateProjetoDto payload){
        projetoService.atualizarProjeto(projetoId, payload);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{projetoId}")
    @Operation(summary = "Excluir projeto", description = "Exclui um projeto do sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Projeto excluído com sucesso"),
            @ApiResponse(responseCode = "404", description = "Projeto não encontrado",
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
    public ResponseEntity<Void> deleteProjeto(@PathVariable Integer projetoId){
        projetoService.excluirProjeto(projetoId);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{projetoId}/avancar-status")
    @Operation(summary = "Avançar status do projeto", description = "Avança o projeto para o próximo status no fluxo")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Status do projeto avançado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Operação inválida para o status atual",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            ),
            @ApiResponse(responseCode = "404", description = "Projeto não encontrado",
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
    public ResponseEntity<ProjetoDto> avancaStatusProjeto(@PathVariable Integer projetoId){
        ProjetoDto projeto = projetoService.avancaStatusProjeto(projetoId);
        return ResponseEntity.ok(projeto);
    }

    @PatchMapping("/{projetoId}/cancelar")
    @Operation(summary = "Cancelar projeto", description = "Cancela um projeto em andamento")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Projeto cancelado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Operação inválida para o status atual",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            ),
            @ApiResponse(responseCode = "404", description = "Projeto não encontrado",
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
    public ResponseEntity<ProjetoDto> cancelarProjeto(@PathVariable Integer projetoId){
        ProjetoDto projeto = projetoService.cancelarProjeto(projetoId);
        return ResponseEntity.ok(projeto);
    }
}
