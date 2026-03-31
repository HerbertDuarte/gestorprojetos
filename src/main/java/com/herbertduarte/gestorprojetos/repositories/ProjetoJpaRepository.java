package com.herbertduarte.gestorprojetos.repositories;

import com.herbertduarte.gestorprojetos.enums.Status;
import com.herbertduarte.gestorprojetos.models.Projeto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ProjetoJpaRepository extends JpaRepository<Projeto, Integer> {

    @Query("SELECT p.status, COUNT(p) FROM Projeto p GROUP BY p.status")
    List<Object[]> countProjetosPorStatus();

    @Query("SELECT p.status, SUM(p.orcamentoTotal) FROM Projeto p GROUP BY p.status")
    List<Object[]> sumOrcamentoPorStatus();

    @Query(value = "SELECT AVG(data_termino - data_inicio) " +
            "FROM projeto p WHERE p.status = :status AND p.data_termino IS NOT NULL",
            nativeQuery = true)
    Double calcularMediaDuracaoProjetosEncerrados(Status status);

    @Query("SELECT COUNT(DISTINCT pm.membro.id) FROM ProjetoMembro pm")
    Long countMembrosUnicosAlocados();
}
