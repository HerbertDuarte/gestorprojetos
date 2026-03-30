package com.herbertduarte.gestorprojetos.security;

import jakarta.validation.constraints.NotNull;

public record LoginDto(
        @NotNull
        String username,
        @NotNull
        String password
) {
}
