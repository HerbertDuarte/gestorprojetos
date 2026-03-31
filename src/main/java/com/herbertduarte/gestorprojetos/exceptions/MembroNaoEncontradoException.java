package com.herbertduarte.gestorprojetos.exceptions;

import com.herbertduarte.gestorprojetos.config.exceptions.ApplicationException;
import org.springframework.http.HttpStatus;

public class MembroNaoEncontradoException extends ApplicationException {
    public MembroNaoEncontradoException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }

    public MembroNaoEncontradoException() {
        this("Não foi possível encontrar o membro");
    }
}
