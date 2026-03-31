package com.herbertduarte.gestorprojetos.models.dtos.projeto;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

// Mesmo dto de criação para poder controlar o que pode ou não ser atualizado
public record UpdateProjetoDto(
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
