package com.herbertduarte.gestorprojetos.exceptions;

public class ProjetoNaoEncontradoException extends ApplicationException{
    public ProjetoNaoEncontradoException(String message) {
        super(message);
    }

    public ProjetoNaoEncontradoException() {
        super("Não foi possível encontrar o projeto");
    }
}
