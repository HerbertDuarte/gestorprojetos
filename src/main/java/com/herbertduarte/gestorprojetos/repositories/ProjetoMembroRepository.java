package com.herbertduarte.gestorprojetos.repositories;

import com.herbertduarte.gestorprojetos.models.ProjetoMembro;
import com.herbertduarte.gestorprojetos.models.ProjetoMembroId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjetoMembroRepository extends JpaRepository<ProjetoMembro, ProjetoMembroId> {
    List<ProjetoMembro> findByProjetoId(Integer projetoId);
    List<ProjetoMembro> findByMembroId(Integer membroId);
}
