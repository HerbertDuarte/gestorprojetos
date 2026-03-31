package com.herbertduarte.gestorprojetos.dtos.projetomembro;

import com.herbertduarte.gestorprojetos.models.ProjetoMembro;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProjetoMembroDto {
    private Integer projetoId;
    private String projetoNome;
    private Integer membroId;
    private String membroNome;


    public static ProjetoMembroDto from(ProjetoMembro projetoMembro) {
        return new ProjetoMembroDto(
                projetoMembro.getProjeto().getId(),
                projetoMembro.getProjeto().getNome(),
                projetoMembro.getMembro().getId(),
                projetoMembro.getMembro().getNome()
        );
    }
}
