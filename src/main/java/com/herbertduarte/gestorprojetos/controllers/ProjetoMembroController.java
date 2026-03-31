package com.herbertduarte.gestorprojetos.controllers;

import com.herbertduarte.gestorprojetos.models.dtos.projeto.ProjetoDto;
import com.herbertduarte.gestorprojetos.models.dtos.projetomembro.CreateProjetoMembroDto;
import com.herbertduarte.gestorprojetos.models.dtos.projetomembro.ProjetoMembroDto;
import com.herbertduarte.gestorprojetos.config.exceptions.ErrorResponseDto;
import com.herbertduarte.gestorprojetos.models.entities.Membro;
import com.herbertduarte.gestorprojetos.services.ProjetoMembroService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("projeto-membro")
@Tag(name = "Projeto-Membro", description = "Endpoints para gerenciamento de alocação de membros em projetos")
public class ProjetoMembroController {

    private final ProjetoMembroService projetoMembroService;

    public ProjetoMembroController(ProjetoMembroService projetoMembroService) {
        this.projetoMembroService = projetoMembroService;
    }


    @PostMapping
    @Operation(summary = "Alocar membro em projeto", description = "Aloca um membro em um projeto com função específica")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Membro alocado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            ),
            @ApiResponse(responseCode = "404", description = "Projeto ou membro não encontrado",
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
    public ResponseEntity<ProjetoMembroDto> alocarMembroProjeto(
            @Valid @RequestBody CreateProjetoMembroDto dto
    ) {
        ProjetoMembroDto resultado = projetoMembroService.alocarMembroEmProjeto(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(resultado);
    }


    @DeleteMapping("/{projetoId}/{membroId}")
    @Operation(summary = "Remover membro do projeto", description = "Remove a alocação de um membro de um projeto")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Membro removido com sucesso"),
            @ApiResponse(responseCode = "404", description = "Projeto, membro ou alocação não encontrada",
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
    public ResponseEntity<Void> removerMembroDoProjeto(
            @PathVariable Integer projetoId,
            @PathVariable Integer membroId
    ) {
        projetoMembroService.removerMembroDoProjeto(projetoId, membroId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/projeto/{projetoId}/membros")
    @Operation(summary = "Listar membros do projeto", description = "Retorna a lista de membros alocados em um projeto")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de membros retornada com sucesso"),
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
    public ResponseEntity<List<Membro>> listarMembrosDoProjetoPor(
            @PathVariable Integer projetoId
    ) {
        List<Membro> membros = projetoMembroService.listarMembrosDoProjetoPor(projetoId);
        return ResponseEntity.ok(membros);
    }

    @GetMapping("/membro/{membroId}/projetos")
    @Operation(summary = "Listar projetos do membro", description = "Retorna a lista de projetos em que um membro está alocado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de projetos retornada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Membro não encontrado",
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
    public ResponseEntity<List<ProjetoDto>> listarProjetosDoMembro(
            @PathVariable Integer membroId
    ) {
        List<ProjetoDto> projetos = projetoMembroService.listarProjetosDoMembro(membroId);
        return ResponseEntity.ok(projetos);
    }
}
