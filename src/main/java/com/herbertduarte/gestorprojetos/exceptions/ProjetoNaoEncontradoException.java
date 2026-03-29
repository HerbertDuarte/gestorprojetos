package com.herbertduarte.gestorprojetos.exceptions;

import org.springframework.http.HttpStatus;

public class ProjetoNaoEncontradoException extends ApplicationException{
    public ProjetoNaoEncontradoException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }

    public ProjetoNaoEncontradoException() {
        this("Não foi possível encontrar o projeto");
    }
}
