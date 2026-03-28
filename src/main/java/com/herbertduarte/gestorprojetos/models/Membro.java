package com.herbertduarte.gestorprojetos.models;

import com.herbertduarte.gestorprojetos.enums.Atribuicao;
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
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;
    @Column(nullable = false)
    private Atribuicao atribuicao;
    @Column(nullable = false)
    private String nome;
    @OneToMany(mappedBy = "membro")
    private List<ProjetoMembro> projetoMembros;
}
