package com.Back_ev3_Fullstack.repository;

import com.Back_ev3_Fullstack.entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductoRepository extends JpaRepository<Producto, Long> {
    boolean existsByNombre(String nombre);
}
