package com.Back_ev3_Fullstack.service;

import com.Back_ev3_Fullstack.dto.VentaRequest;
import com.Back_ev3_Fullstack.dto.VentaResponse;
import com.Back_ev3_Fullstack.entity.Categoria;
import com.Back_ev3_Fullstack.entity.Producto;
import com.Back_ev3_Fullstack.entity.Usuario;
import com.Back_ev3_Fullstack.entity.Venta;
import com.Back_ev3_Fullstack.repository.ProductoRepository;
import com.Back_ev3_Fullstack.repository.UsuarioRepository;
import com.Back_ev3_Fullstack.repository.VentaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class VentaServiceImplTest {

    @Mock
    private VentaRepository ventaRepository;

    @Mock
    private ProductoRepository productoRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private VentaServiceImpl ventaService;

    private Usuario usuario;
    private Producto producto;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setCorreo("user@mail.com");

        producto = new Producto();
        producto.setId(10L);
        producto.setNombre("Laptop Gamer");
        producto.setPrecio(500_000);

        Categoria categoria = new Categoria();
        categoria.setId(1L);
        categoria.setNombre("Tecnología");

        producto.setCategoria(categoria);
    }

    @Test
    void crearVenta_debeCrearVentaConPreciosReales() {
        // ARRANGE
        VentaRequest.ItemVenta item = new VentaRequest.ItemVenta();
        item.setProductoId(10L);
        item.setCantidad(2);

        // Se usa el constructor correcto
        VentaRequest request = new VentaRequest(List.of(item), null);

        when(usuarioRepository.findByCorreo("user@mail.com"))
                .thenReturn(Optional.of(usuario));

        when(productoRepository.findById(10L))
                .thenReturn(Optional.of(producto));

        when(ventaRepository.save(any(Venta.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // ACT
        VentaResponse response = ventaService.crearVenta(request, "user@mail.com");

        // ASSERT
        assertNotNull(response);
        assertEquals(1, response.getDetalles().size());

        var d = response.getDetalles().get(0);

        assertEquals("Laptop Gamer", d.getProducto().getNombre());
        assertEquals(500_000, d.getPrecioUnitario());
        assertEquals(2, d.getCantidad());
        assertEquals(1_000_000, d.getSubtotal());
        assertEquals(1_000_000, response.getTotal());

        verify(ventaRepository, times(1)).save(any(Venta.class));
    }

    @Test
    void crearVenta_usuarioNoExiste_debeLanzarExcepcion() {

        when(usuarioRepository.findByCorreo("noExiste@mail.com"))
                .thenReturn(Optional.empty());

        // Constructor corregido
        VentaRequest req = new VentaRequest(new ArrayList<>(), null);

        assertThrows(RuntimeException.class,
                () -> ventaService.crearVenta(req, "noExiste@mail.com"));
    }

    @Test
    void crearVenta_productoNoExiste_debeLanzarExcepcion() {

        VentaRequest.ItemVenta item = new VentaRequest.ItemVenta();
        item.setProductoId(99L);
        item.setCantidad(1);

        // Constructor corregido
        VentaRequest request = new VentaRequest(List.of(item), null);

        when(usuarioRepository.findByCorreo("user@mail.com"))
                .thenReturn(Optional.of(usuario));

        when(productoRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> ventaService.crearVenta(request, "user@mail.com"));
    }
}
