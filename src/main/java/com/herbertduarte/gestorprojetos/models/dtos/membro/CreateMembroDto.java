package com.herbertduarte.gestorprojetos.dtos.membro;

import jakarta.validation.constraints.NotNull;

public record CreateMembroDto(
        @NotNull
        String nome,
        @NotNull
        Integer atribuicao
) {
}
