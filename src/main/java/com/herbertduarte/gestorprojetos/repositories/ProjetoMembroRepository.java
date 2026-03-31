package com.herbertduarte.gestorprojetos.repositories;

import com.herbertduarte.gestorprojetos.models.enums.Status;
import com.herbertduarte.gestorprojetos.models.entities.Membro;
import com.herbertduarte.gestorprojetos.models.entities.Projeto;
import com.herbertduarte.gestorprojetos.models.entities.ProjetoMembro;
import com.herbertduarte.gestorprojetos.models.entities.ProjetoMembroId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjetoMembroRepository extends JpaRepository<ProjetoMembro, ProjetoMembroId> {
    @Query("SELECT pm.membro FROM ProjetoMembro pm WHERE pm.projeto.id = :projetoId")
    List<Membro> findMembrosByProjetoId(Integer projetoId);

    @Query("SELECT pm.projeto FROM ProjetoMembro pm WHERE pm.membro.id = :membroId")
    List<Projeto> findProjetosByMembroId(Integer membroId);

    Optional<ProjetoMembro> findByProjetoIdAndMembroId(Integer projetoId, Integer membroId);
    Long countByProjetoId(Integer projetoId);

    @Query("SELECT COUNT(pm) FROM ProjetoMembro pm WHERE pm.membro.id = :membroId AND pm.projeto.status NOT IN :statusExcluidos")
    Long countByMembroIdAndProjetoStatusNotIn(@Param("membroId") Integer membroId, @Param("statusExcluidos") List<Status> statusExcluidos);
}
