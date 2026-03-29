package com.herbertduarte.gestorprojetos.repositories;

import com.herbertduarte.gestorprojetos.enums.Atribuicao;
import com.herbertduarte.gestorprojetos.models.Membro;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MembroJpaRepository extends JpaRepository<Membro, Integer> {

    @Query("SELECT m FROM Membro m WHERE " +
           "(:nome IS NULL OR LOWER(m.nome) LIKE LOWER(CONCAT('%', :nome, '%'))) AND " +
           "(:atribuicao IS NULL OR m.atribuicao = :atribuicao)")
    Page<Membro> findAll(@Param("nome") String nome,
                         @Param("atribuicao") Atribuicao atribuicao,
                         Pageable pageable);
}
