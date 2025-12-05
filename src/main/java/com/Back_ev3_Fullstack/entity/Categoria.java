package com.Back_ev3_Fullstack.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Categoria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)    // Evita nombres repetidos
    private String nombre;

    //@OneToMany(mappedBy = "categoria")
    //private List<Producto> productos;

}
