package com.herbertduarte.gestorprojetos.exceptions;

import com.herbertduarte.gestorprojetos.config.exceptions.ApplicationException;
import org.springframework.http.HttpStatus;

public class StatusNaoAvancavelException extends ApplicationException {
    public StatusNaoAvancavelException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }

    public StatusNaoAvancavelException() {
        this("Não é possível avançar o status desse projeto");
    }
}
