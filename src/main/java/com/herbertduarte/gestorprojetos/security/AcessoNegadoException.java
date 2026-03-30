package com.herbertduarte.gestorprojetos.security;

import com.herbertduarte.gestorprojetos.exceptions.ApplicationException;
import org.springframework.http.HttpStatus;

public class AcessoNegadoException extends ApplicationException {
    public AcessoNegadoException(String message) {
        super(message, HttpStatus.UNAUTHORIZED);
    }

    public AcessoNegadoException() {
        this("Acesso negado");
    }
}
