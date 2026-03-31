package com.herbertduarte.gestorprojetos.dtos.projeto;

import com.herbertduarte.gestorprojetos.enums.Risco;
import com.herbertduarte.gestorprojetos.models.Projeto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Getter
@Setter
@NoArgsConstructor
public class ProjetoDto extends Projeto {
    private Risco risco;

    public static ProjetoDto from(Projeto projeto){
        ProjetoDto dto = new ProjetoDto();
        BeanUtils.copyProperties(projeto, dto);
        dto.setRisco(Risco.from(projeto));
        return dto;
    }

}
