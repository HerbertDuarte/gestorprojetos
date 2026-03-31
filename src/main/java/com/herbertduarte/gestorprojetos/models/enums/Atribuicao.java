package com.herbertduarte.gestorprojetos.models.enums;

public enum Atribuicao {
    FUNCIONARIO(0),
    OUTRO(1);

    private final Integer value;
    Atribuicao(Integer value) {
        this.value = value;
    }

    public Integer value() {
        return value;
    }

    public static Atribuicao toEnum(Integer value) {
        return Atribuicao.values()[value];
    }
}


