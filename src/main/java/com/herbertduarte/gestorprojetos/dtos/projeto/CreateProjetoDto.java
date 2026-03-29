package com.herbertduarte.gestorprojetos.dtos.projeto;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;


public record CreateProjetoDto(
        @NotNull
        String nome,
        @NotNull
        LocalDateTime dataInicio,
        @NotNull
        LocalDateTime dataTermino,
        @NotNull
        BigDecimal orcamentoTotal,
        String descricao,
        @NotNull
        Integer gerenteId
) {
}
