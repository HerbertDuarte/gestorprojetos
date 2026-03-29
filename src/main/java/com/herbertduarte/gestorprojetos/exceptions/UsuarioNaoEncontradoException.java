package com.herbertduarte.gestorprojetos.exceptions;

import org.springframework.http.HttpStatus;

public class UsuarioNaoEncontradoException extends ApplicationException {
    public UsuarioNaoEncontradoException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }

    public UsuarioNaoEncontradoException(){
        this("Usuário não encontrado");
    }
}
