package com.herbertduarte.gestorprojetos.models;

import com.herbertduarte.gestorprojetos.enums.Status;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Projeto {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;
    @Column(nullable = false)
    private String nome;
    @Column(nullable = false)
    private LocalDateTime dataInicio;
    @Column(nullable = false)
    private LocalDateTime previsaoTermino;
    private LocalDateTime dataTermino;
    @Column(columnDefinition = "DECIMAL(19,4) NOT NULL")
    @NotNull
    private BigDecimal orcamentoTotal;
    private String descricao;
    @ManyToOne
    @JoinColumn(name = "gerente_id")
    private Membro gerente;
    @OneToMany(mappedBy = "projeto")
    private List<ProjetoMembro> projetoMembros;
    @Column(nullable = false)
    private Status status;
}
