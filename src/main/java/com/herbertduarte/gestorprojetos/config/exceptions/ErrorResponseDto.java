package com.herbertduarte.gestorprojetos.exceptions.globals;

import java.time.LocalDateTime;

public record ErrorResponseDto (
        int statusCode,
        String message,
        LocalDateTime timestamp
){
}
