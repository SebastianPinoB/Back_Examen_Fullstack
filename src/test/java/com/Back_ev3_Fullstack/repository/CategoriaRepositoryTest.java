package com.Back_ev3_Fullstack.repository;

import com.Back_ev3_Fullstack.entity.Categoria;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@DataJpaTest
public class CategoriaRepositoryTest {

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Test
    public void testGuardarYRecuperarCategoria() {
        Categoria cat = Categoria.builder()
                .nombre("Electronica")
                .build();

        categoriaRepository.save(cat);

        Categoria encontrada = categoriaRepository.findById(cat.getId()).orElse(null);
        assertThat(encontrada).isNotNull();
        assertThat(encontrada.getNombre()).isEqualTo("Electronica");
    }
}