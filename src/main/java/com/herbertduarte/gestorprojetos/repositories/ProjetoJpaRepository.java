package com.herbertduarte.gestorprojetos.repositories;

import com.herbertduarte.gestorprojetos.models.Projeto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjetoJpaRepository extends JpaRepository<Projeto, Integer> {
}
