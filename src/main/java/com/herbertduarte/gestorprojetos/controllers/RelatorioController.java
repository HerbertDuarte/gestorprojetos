package com.herbertduarte.gestorprojetos.controllers;

import com.herbertduarte.gestorprojetos.models.dtos.relatorio.RelatorioDto;
import com.herbertduarte.gestorprojetos.config.exceptions.ErrorResponseDto;
import com.herbertduarte.gestorprojetos.services.RelatorioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/relatorios")
@Tag(name = "Relatórios", description = "Endpoints para geração de relatórios do portfólio")
public class RelatorioController {

    private final RelatorioService relatorioService;

    public RelatorioController(RelatorioService relatorioService) {
        this.relatorioService = relatorioService;
    }

    @GetMapping("/portfolio")
    @Operation(summary = "Gerar relatório do portfólio", description = "Retorna um relatório resumido do portfólio contendo quantidade de projetos por status, total orçado por status, média de duração dos projetos encerrados e total de membros únicos alocados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Relatório gerado com sucesso"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            )
    })
    public ResponseEntity<RelatorioDto> gerarRelatorioPortfolio() {
        RelatorioDto relatorio = relatorioService.gerarRelatorioPortfolio();
        return ResponseEntity.ok(relatorio);
    }
}
