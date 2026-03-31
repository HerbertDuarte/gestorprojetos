package com.herbertduarte.gestorprojetos.models.dtos.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Credenciais de login do usuário")
public record LoginDto(
        @NotNull
        @Schema(description = "Nome de usuário", example = "admin")
        String username,
        @NotNull
        @Schema(description = "Senha do usuário", example = "admin")
        String password
) {
}
