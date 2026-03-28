package com.herbertduarte.gestorprojetos.enums;

//em análise → análise realizada → análise aprovada → iniciado → planejado → em andamento → encerrado
public enum Status {
    ENCERRADO(0,null),
    EM_ANDAMENTO(1,ENCERRADO),
    PLANEJADO(2,EM_ANDAMENTO),
    INICIADO(3,PLANEJADO),
    ANALISE_APROVADA(4,INICIADO),
    ANALISE_REALIZADA(5,ANALISE_APROVADA),
    EM_ANALISE(6,ANALISE_REALIZADA),
    CANCELADO(7,null);

    private final Status proximo;
    private final Integer value;

    Status(Integer value,Status proximo) {
        this.value = value;
        this.proximo = proximo;
    }

    public Status proximo() {
        return proximo;
    }
    public Integer value() {
        return value;
    }
}
