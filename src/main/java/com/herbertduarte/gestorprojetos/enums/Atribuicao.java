package com.herbertduarte.gestorprojetos.enums;

public enum Atribuicao {
    FUNCIONARIO(1),
    OUTRO(2);

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


