package com.herbertduarte.gestorprojetos.exceptions;

import com.herbertduarte.gestorprojetos.config.exceptions.ApplicationException;
import org.springframework.http.HttpStatus;

public class AlocacaoProjetoMembroNaoEncontrada extends ApplicationException {
    public AlocacaoProjetoMembroNaoEncontrada(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }

    public AlocacaoProjetoMembroNaoEncontrada(){
        this("Alocação de membro em projeto não encontrada");
    }
}
