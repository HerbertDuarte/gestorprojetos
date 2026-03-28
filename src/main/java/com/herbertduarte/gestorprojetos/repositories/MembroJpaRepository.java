package com.herbertduarte.gestorprojetos.repositories;

import com.herbertduarte.gestorprojetos.models.Membro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MembroJpaRepository extends JpaRepository<Membro, Integer> {
}
