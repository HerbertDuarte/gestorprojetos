package com.herbertduarte.gestorprojetos.exceptions;

import com.herbertduarte.gestorprojetos.exceptions.globals.ApplicationException;
import org.springframework.http.HttpStatus;

public class AtribuicaoInvalidaParaIngressarEmProjetoException extends ApplicationException {
    public AtribuicaoInvalidaParaIngressarEmProjetoException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }

    public AtribuicaoInvalidaParaIngressarEmProjetoException(){
      this("Apenas membros com atribuição 'funcionário' podem ser associados a projetos");
    }
}
