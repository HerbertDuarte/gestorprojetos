package com.herbertduarte.gestorprojetos.exceptions;

public class StatusNaoExcluivelException extends ApplicationException{
    public StatusNaoExcluivelException(String message) {
        super(message);
    }

    public StatusNaoExcluivelException() {
        super("Não é possível excluir um projeto nessa fase");
    }
}
