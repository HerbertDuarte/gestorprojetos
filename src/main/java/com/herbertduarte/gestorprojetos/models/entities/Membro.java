package com.herbertduarte.gestorprojetos.models.entities;

import com.herbertduarte.gestorprojetos.models.enums.Atribuicao;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Membro {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "membro_seq")
    @SequenceGenerator(name = "membro_seq", sequenceName = "membro_seq", allocationSize = 1)
    private Integer id;
    @Column(nullable = false)
    private Atribuicao atribuicao;
    @Column(nullable = false)
    private String nome;
    @OneToMany(mappedBy = "membro")
    private List<ProjetoMembro> projetoMembros;
}
