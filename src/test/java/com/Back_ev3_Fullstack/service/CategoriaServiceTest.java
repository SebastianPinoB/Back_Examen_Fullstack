package com.Back_ev3_Fullstack.service;

import com.Back_ev3_Fullstack.dto.CategoriaRequestDTO;
import com.Back_ev3_Fullstack.dto.CategoriaResponseDTO;
import com.Back_ev3_Fullstack.entity.Categoria;
import com.Back_ev3_Fullstack.repository.CategoriaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class CategoriaServiceTest {

    @Mock
    private CategoriaRepository repo;

    @InjectMocks
    private CategoriaService service;

    private Categoria categoria;

    @BeforeEach
    void setUp() {
        categoria = Categoria.builder()
                .id(1L)
                .nombre("Tecnologia")
                .build();
    }

    @Test
    void crear_debeCrearCategoria() {
        CategoriaRequestDTO req = new CategoriaRequestDTO("Tecnologia");

        when(repo.existsByNombre("Tecnologia")).thenReturn(false);
        when(repo.save(any(Categoria.class))).thenReturn(categoria);

        CategoriaResponseDTO res = service.crear(req);

        assertEquals(1L, res.getId());
        assertEquals("Tecnologia", res.getNombre());
    }

    @Test
    void crear_categoriaDuplicada() {
        CategoriaRequestDTO req = new CategoriaRequestDTO("Tecnologia");

        when(repo.existsByNombre("Tecnologia")).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> service.crear(req));
    }

    @Test
    void listar_debeRetornarLista() {
        when(repo.findAll()).thenReturn(List.of(categoria));

        List<CategoriaResponseDTO> lista = service.listar();

        assertEquals(1, lista.size());
        assertEquals("Tecnologia", lista.get(0).getNombre());
    }
}