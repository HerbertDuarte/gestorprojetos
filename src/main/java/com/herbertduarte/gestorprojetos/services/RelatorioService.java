package com.herbertduarte.gestorprojetos.services;

import com.herbertduarte.gestorprojetos.dtos.relatorio.RelatorioDto;
import com.herbertduarte.gestorprojetos.enums.Status;
import com.herbertduarte.gestorprojetos.helpers.ObjectUtils;
import com.herbertduarte.gestorprojetos.repositories.ProjetoJpaRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class RelatorioService {

    private final ProjetoJpaRepository projetoRepository;

    public RelatorioService(ProjetoJpaRepository projetoRepository) {
        this.projetoRepository = projetoRepository;
    }

    public RelatorioDto gerarRelatorioPortfolio() {
        return RelatorioDto.builder()
                .quantidadeProjetosPorStatus(calcularQuantidadeProjetosPorStatus())
                .totalOrcadoPorStatus(calcularTotalOrcadoPorStatus())
                .mediaDuracaoProjetosEncerrados(calcularMediaDuracaoProjetosEncerrados())
                .totalMembrosUnicosAlocados(calcularTotalMembrosUnicosAlocados())
                .build();
    }

    private Map<Status, Long> calcularQuantidadeProjetosPorStatus() {
        List<Object[]> resultados = projetoRepository.countProjetosPorStatus();
        return ObjectUtils.mapearResultados(resultados,
                row -> (Status) row[0],
                row -> (Long) row[1]);
    }

    private Map<Status, BigDecimal> calcularTotalOrcadoPorStatus() {
        List<Object[]> resultados = projetoRepository.sumOrcamentoPorStatus();
        return ObjectUtils.mapearResultados(resultados,
                row -> (Status) row[0],
                row -> Optional.ofNullable((BigDecimal) row[1]).orElse(BigDecimal.ZERO));
    }

    private Double calcularMediaDuracaoProjetosEncerrados() {
        return Optional.ofNullable(
                projetoRepository.calcularMediaDuracaoProjetosEncerrados(Status.ENCERRADO)
        ).orElse(0.0);
    }

    private Long calcularTotalMembrosUnicosAlocados() {
        return Optional.ofNullable(
                projetoRepository.countMembrosUnicosAlocados()
        ).orElse(0L);
    }

}
