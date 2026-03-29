package com.herbertduarte.gestorprojetos.dtos.common;

import java.time.LocalDateTime;

public record ErrorResponseDto (
        int statusCode,
        String message,
        LocalDateTime timestamp
){
}
