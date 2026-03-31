package com.herbertduarte.gestorprojetos.models.enums;

import com.herbertduarte.gestorprojetos.models.entities.Projeto;

import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;


public enum Risco {
    BAIXO(0),
    MEDIO(1),
    ALTO(2),
    INDEFINIDO(999);

    private final Integer value;

    Risco(Integer value) {
        this.value = value;
    }

    public Integer value() {
        return value;
    }

    public static Risco toEnum(Integer value) {
        return Risco.values()[value];
    }

    //• Baixo risco: orçamento até R$ 100.000 e prazo ≤ 3 meses
    //• Médio risco: orçamento entre R$ 100.001 e R$ 500.000 ou prazo entre 3 a 6 meses
    //• Alto risco: orçamento acima de R$ 500.000 ou prazo superior a 6 meses
    public static Risco from(Projeto projeto){
        BigDecimal orcamentoTotal = projeto.getOrcamentoTotal();
        long prazoEmMeses = ChronoUnit.MONTHS.between(projeto.getDataInicio(), projeto.getPrevisaoTermino());
        if(orcamentoTotal.intValue() > 500000 || prazoEmMeses > 6){
            return ALTO;
        }
        else if(orcamentoTotal.intValue() > 100000 && orcamentoTotal.intValue() < 500000
                || prazoEmMeses > 3 && prazoEmMeses < 6){
            return MEDIO;
        }
        else if(orcamentoTotal.intValue() <= 100000  && prazoEmMeses <= 3){
            return BAIXO;
        }
        else{
            return INDEFINIDO; // CABE UMA ESTRATÉGIA PARA QUANDO NENHUMA CONDIÇÃO FOR ATENDIDA. SUGESTÃO: COMPARAR APENAS PELAS DATAS.
        }
    };
}
