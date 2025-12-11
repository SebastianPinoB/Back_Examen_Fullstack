package com.Back_ev3_Fullstack.service;

import com.Back_ev3_Fullstack.dto.ProductoRequestDTO;
import com.Back_ev3_Fullstack.dto.ProductoResponseDTO;
import com.Back_ev3_Fullstack.entity.Categoria;
import com.Back_ev3_Fullstack.entity.Producto;
import com.Back_ev3_Fullstack.repository.CategoriaRepository;
import com.Back_ev3_Fullstack.repository.ProductoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductoServiceTest {
    @Mock
    private ProductoRepository repo;

    @Mock
    private CategoriaRepository categoriaRepo;

    @InjectMocks
    private ProductoService service;

    private Categoria categoria;
    private Producto producto;

    @BeforeEach
    void setUp() {
        categoria = Categoria.builder()
                .id(1L)
                .nombre("Tecnología")
                .build();

        producto = Producto.builder()
                .id(10L)
                .nombre("Laptop")
                .precio(500000)
                .stock(5)
                .imagen("laptop.png")
                .categoria(categoria)
                .build();
    }

    @Test
    void crear_debeCrearProducto() {
        ProductoRequestDTO dto = new ProductoRequestDTO(
                "Laptop", 500000, 5, "laptop.png", 1L
        );

        when(categoriaRepo.findById(1L)).thenReturn(Optional.of(categoria));
        when(repo.save(any(Producto.class))).thenReturn(producto);

        ProductoResponseDTO response = service.crear(dto);

        assertEquals(10L, response.getId());
        assertEquals("Laptop", response.getNombre());
        assertEquals(1L, response.getCategoriaId());
    }

    @Test
    void crear_categoriaNoExiste_debeLanzarExcepcion() {
        ProductoRequestDTO dto = new ProductoRequestDTO(
                "Laptop", 500000, 5, "laptop.png", 1L
        );

        when(categoriaRepo.findById(1L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class,
                () -> service.crear(dto));
    }

    @Test
    void listar_debeRetornarLista() {
        when(repo.findAll()).thenReturn(List.of(producto));

        List<ProductoResponseDTO> lista = service.listar();

        assertEquals(1, lista.size());
        assertEquals("Laptop", lista.get(0).getNombre());
    }

    @Test
    void obtener_debeRetornarProducto() {
        when(repo.findById(10L)).thenReturn(Optional.of(producto));

        ProductoResponseDTO r = service.obtener(10L);

        assertEquals("Laptop", r.getNombre());
    }

    @Test
    void obtener_productoNoExiste_debeLanzarExcepcion() {
        when(repo.findById(10L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> service.obtener(10L));
    }

    @Test
    void actualizar_debeActualizarProducto() {

        ProductoRequestDTO dto = new ProductoRequestDTO(
                "Laptop PRO", 700000, 3, "new.png", 1L
        );

        when(repo.findById(10L)).thenReturn(Optional.of(producto));
        when(categoriaRepo.findById(1L)).thenReturn(Optional.of(categoria));
        when(repo.save(any(Producto.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ProductoResponseDTO r = service.actualizar(10L, dto);

        assertEquals("Laptop PRO", r.getNombre());
        assertEquals(700000, r.getPrecio());
    }

    @Test
    void actualizar_categoriaNoExiste_debeLanzarExcepcion() {
        ProductoRequestDTO dto = new ProductoRequestDTO(
                "Laptop", 500000, 5, "laptop.png", 99L
        );

        when(repo.findById(10L)).thenReturn(Optional.of(producto));
        when(categoriaRepo.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> service.actualizar(10L, dto));
    }

    @Test
    void eliminar_debeEliminarProducto() {
        when(repo.existsById(10L)).thenReturn(true);

        service.eliminar(10L);

        verify(repo, times(1)).deleteById(10L);
    }

    @Test
    void eliminar_productoNoExiste_debeLanzarExcepcion() {
        when(repo.existsById(10L)).thenReturn(false);

        assertThrows(RuntimeException.class,
                () -> service.eliminar(10L));
    }
}
