package com.herbertduarte.gestorprojetos.exceptions;

import java.time.LocalDateTime;

public record ErrorResponseDto (
        int statusCode,
        String message,
        LocalDateTime timestamp
){
}
