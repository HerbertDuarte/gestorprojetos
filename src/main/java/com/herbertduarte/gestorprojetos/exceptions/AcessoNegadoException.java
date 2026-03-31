package com.herbertduarte.gestorprojetos.exceptions;

import com.herbertduarte.gestorprojetos.config.exceptions.ApplicationException;
import org.springframework.http.HttpStatus;

public class AcessoNegadoException extends ApplicationException {
    public AcessoNegadoException(String message) {
        super(message, HttpStatus.FORBIDDEN);
    }

    public AcessoNegadoException() {
        this("Acesso negado");
    }

    public static final String example = "{\"message\": \"Acesso negado\", \"statusCode\": 403, \"error\": \"AcessoNegadoException\"}";
}
