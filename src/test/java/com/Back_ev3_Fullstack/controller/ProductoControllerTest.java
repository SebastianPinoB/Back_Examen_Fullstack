package com.Back_ev3_Fullstack.controller;

import com.Back_ev3_Fullstack.dto.ProductoRequestDTO;
import com.Back_ev3_Fullstack.dto.ProductoResponseDTO;
import com.Back_ev3_Fullstack.service.ProductoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
class ProductoControllerTest {

    @InjectMocks
    private ProductoController productoController;

    @Mock
    private ProductoService productoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testListarProductos() {
        ProductoResponseDTO p1 = new ProductoResponseDTO(1L, "Producto1", 100, 10, "img1", "desc", 1L, "Categoria Test", 0.0, 0);
        ProductoResponseDTO p2 = new ProductoResponseDTO(2L, "Producto2", 100, 10, "img1", "desc", 1L, "Categoria Test", 0.0, 0);

        when(productoService.listar()).thenReturn(List.of(p1, p2));

        ResponseEntity<List<ProductoResponseDTO>> response = productoController.listar();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
        assertEquals("Producto1", response.getBody().get(0).getNombre());

        verify(productoService, times(1)).listar();
    }

    @Test
    void testCrearProducto() {
        ProductoRequestDTO request = new ProductoRequestDTO("Producto1", 100, 10, "img1", "desc", 1L, 0.0, 0);
        ProductoResponseDTO responseDTO = new ProductoResponseDTO(2L, "Producto1", 100, 10, "img1", "desc", 1L, "Categoria Test", 0.0, 0);

        when(productoService.crear(request)).thenReturn(responseDTO);

        ResponseEntity<ProductoResponseDTO> response = productoController.crear(request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Producto1", response.getBody().getNombre());
        assertEquals(100, response.getBody().getPrecio());
        verify(productoService, times(1)).crear(request);
    }

    @Test
    void testObtenerProducto() {
        ProductoResponseDTO responseDTO = new ProductoResponseDTO(1L, "Producto1", 100, 10, "img1", "desc", 1L, "Categoria Test", 0.0, 0);

        when(productoService.obtener(1L)).thenReturn(responseDTO);

        ResponseEntity<ProductoResponseDTO> response = productoController.obtener(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Producto1", response.getBody().getNombre());
        assertEquals(1L, response.getBody().getId());
        verify(productoService, times(1)).obtener(1L);
    }

    @Test
    void testActualizarProducto() {
        ProductoRequestDTO request = new ProductoRequestDTO("producto1", 100, 10, "img1", "desc", 1L, 0.0, 0);
        ProductoResponseDTO responseDTO = new ProductoResponseDTO(1L, "producto1", 150, 10, "img1", "desc", 1L, "Categoria Test", 0.0, 0);

        when(productoService.actualizar(1L, request)).thenReturn(responseDTO);

        ResponseEntity<ProductoResponseDTO> response = productoController.actualizar(1L, request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(150, response.getBody().getPrecio());
        verify(productoService, times(1)).actualizar(1L, request);
    }

    @Test
    void testEliminarProducto() {
        doNothing().when(productoService).eliminar(1L);

        ResponseEntity<Void> response = productoController.eliminar(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(productoService, times(1)).eliminar(1L);
    }
}