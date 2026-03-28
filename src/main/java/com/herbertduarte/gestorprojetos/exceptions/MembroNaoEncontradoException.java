package com.herbertduarte.gestorprojetos.exceptions;

public class MembroNaoEncontradoException extends ApplicationException{
    public MembroNaoEncontradoException(String message) {
        super(message);
    }

    public MembroNaoEncontradoException() {
        super("Não foi possível encontrar o membro");
    }
}
