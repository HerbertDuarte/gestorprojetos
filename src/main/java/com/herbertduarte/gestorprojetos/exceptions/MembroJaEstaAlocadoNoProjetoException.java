package com.herbertduarte.gestorprojetos.exceptions;

import com.herbertduarte.gestorprojetos.config.exceptions.ApplicationException;
import org.springframework.http.HttpStatus;

public class MembroJaEstaAlocadoNoProjetoException extends ApplicationException {
    public MembroJaEstaAlocadoNoProjetoException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }

    public MembroJaEstaAlocadoNoProjetoException(){
        this("Membro já está alocado neste projeto");
    }
}
