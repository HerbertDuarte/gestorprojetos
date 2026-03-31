package com.herbertduarte.gestorprojetos.enums;

//em análise → análise realizada → análise aprovada → iniciado → planejado → em andamento → encerrado
public enum Status {
    ENCERRADO(0,null, false),
    EM_ANDAMENTO(1,ENCERRADO, false),
    PLANEJADO(2,EM_ANDAMENTO, true),
    INICIADO(3,PLANEJADO, false),
    ANALISE_APROVADA(4,INICIADO, true),
    ANALISE_REALIZADA(5,ANALISE_APROVADA, true),
    EM_ANALISE(6,ANALISE_REALIZADA, true),
    CANCELADO(7,null, true);

    private final Integer value;
    private final Status proximo;
    private final boolean excluivel;

    Status(Integer value,Status proximo,boolean excluivel) {
        this.value = value;
        this.proximo = proximo;
        this.excluivel = excluivel;
    }

    public Status proximo() {
        return proximo;
    }
    public Integer value() {
        return value;
    }
    public boolean excluivel() {
        return excluivel;
    }
}
