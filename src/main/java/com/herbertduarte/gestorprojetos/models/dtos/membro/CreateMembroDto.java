package com.herbertduarte.gestorprojetos.models.dtos.membro;

import jakarta.validation.constraints.NotNull;

public record CreateMembroDto(
        @NotNull
        String nome,
        @NotNull
        Integer atribuicao
) {
}
