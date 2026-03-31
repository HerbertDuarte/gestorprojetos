package com.herbertduarte.gestorprojetos.models.dtos.projeto;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;


public record CreateProjetoDto(
        @NotNull
        String nome,
        @NotNull
        LocalDateTime dataInicio,
        @NotNull
        LocalDateTime previsaoTermino,
        @NotNull
        BigDecimal orcamentoTotal,
        String descricao,
        @NotNull
        Integer gerenteId
) {
}
