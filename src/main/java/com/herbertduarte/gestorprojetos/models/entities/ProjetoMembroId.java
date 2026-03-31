package com.herbertduarte.gestorprojetos.models;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Embeddable
public class ProjetoMembroId implements Serializable {
    private Integer projetoId;
    private Integer membroId;
}
