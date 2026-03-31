package com.herbertduarte.gestorprojetos.config.exceptions;

import java.time.LocalDateTime;

public record ErrorResponseDto (
        int statusCode,
        String message,
        LocalDateTime timestamp
){
}
