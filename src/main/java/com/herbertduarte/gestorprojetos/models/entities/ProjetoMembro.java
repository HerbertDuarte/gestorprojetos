package com.herbertduarte.gestorprojetos.models.entities;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "projeto_membro")
public class ProjetoMembro {
    @EmbeddedId
    private ProjetoMembroId id;

    @ManyToOne
    @MapsId("projetoId")
    @JoinColumn(name = "projeto_id")
    private Projeto projeto;

    @ManyToOne
    @MapsId("membroId")
    @JoinColumn(name = "membro_id")
    private Membro membro;
}
