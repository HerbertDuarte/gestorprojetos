package com.herbertduarte.gestorprojetos.exceptions;

import com.herbertduarte.gestorprojetos.config.exceptions.ApplicationException;
import org.springframework.http.HttpStatus;

public class LimiteProjetosPorMembroException extends ApplicationException {
    public LimiteProjetosPorMembroException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }

    public LimiteProjetosPorMembroException() {
        this("Membro não pode estar alocado em mais de 3 projetos simultaneamente com status diferente de encerrado ou cancelado");
    }
}
