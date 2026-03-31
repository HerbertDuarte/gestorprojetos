package com.herbertduarte.gestorprojetos.exceptions;

import com.herbertduarte.gestorprojetos.exceptions.globals.ApplicationException;
import org.springframework.http.HttpStatus;

public class LimiteMembrosPorProjetoException extends ApplicationException {
    public LimiteMembrosPorProjetoException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }

    public LimiteMembrosPorProjetoException() {
        this("Limite de membros por projeto atingido");
    }
}
