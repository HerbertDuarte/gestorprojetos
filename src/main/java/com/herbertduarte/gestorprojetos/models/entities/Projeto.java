package com.herbertduarte.gestorprojetos.models.entities;

import com.herbertduarte.gestorprojetos.models.enums.Status;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Projeto {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "projeto_seq")
    @SequenceGenerator(name = "projeto_seq", sequenceName = "projeto_seq", allocationSize = 1)
    private Integer id;
    @Column(nullable = false)
    private String nome;
    @Column(nullable = false)
    private LocalDateTime dataInicio;
    @Column(nullable = false)
    private LocalDateTime previsaoTermino;
    private LocalDateTime dataTermino;
    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal orcamentoTotal;
    private String descricao;
    @ManyToOne
    @JoinColumn(name = "gerente_id")
    private Membro gerente;
    @Column(nullable = false)
    private Status status;
}
