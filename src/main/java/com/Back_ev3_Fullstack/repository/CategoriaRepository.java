package com.Back_ev3_Fullstack.repository;

import com.Back_ev3_Fullstack.entity.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoriaRepository extends JpaRepository <Categoria, Long>{
    boolean existsByNombre(String nombre);
    Categoria findByNombre(String nombre);
}
