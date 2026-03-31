package com.herbertduarte.gestorprojetos.dtos.relatorio;

import com.herbertduarte.gestorprojetos.enums.Status;
import lombok.*;

import java.math.BigDecimal;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RelatorioDto {
    private Map<Status, Long> quantidadeProjetosPorStatus;
    private Map<Status, BigDecimal> totalOrcadoPorStatus;
    private Double mediaDuracaoProjetosEncerrados;
    private Long totalMembrosUnicosAlocados;
}
