package com.herbertduarte.gestorprojetos.models.dtos.auth;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Resposta de login com token JWT")
public record LoginResponseDto(
        @Schema(description = "Token JWT para autenticação", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
        String accessToken
) {
}
