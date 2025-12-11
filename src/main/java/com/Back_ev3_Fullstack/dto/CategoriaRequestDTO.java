package com.Back_ev3_Fullstack.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CategoriaRequestDTO{
    private String nombre;

    public CategoriaRequestDTO(String nombre) {
        this.nombre = nombre;
    }


}
