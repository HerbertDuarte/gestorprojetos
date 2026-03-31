package com.herbertduarte.gestorprojetos.dtos.projetomembro;

import jakarta.validation.constraints.NotNull;

public record CreateProjetoMembroDto(
        @NotNull(message = "ID do projeto é obrigatório")
        Integer projetoId,
        
        @NotNull(message = "ID do membro é obrigatório")
        Integer membroId
) {
}
