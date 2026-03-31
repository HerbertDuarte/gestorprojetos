package com.herbertduarte.gestorprojetos.exceptions;

import com.herbertduarte.gestorprojetos.config.exceptions.ApplicationException;
import org.springframework.http.HttpStatus;

public class StatusNaoExcluivelException extends ApplicationException {
    public StatusNaoExcluivelException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }

    public StatusNaoExcluivelException() {
        this("Não é possível excluir um projeto nessa fase");
    }
}
