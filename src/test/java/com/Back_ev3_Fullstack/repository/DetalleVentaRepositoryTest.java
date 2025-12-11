package com.Back_ev3_Fullstack.repository;

import com.Back_ev3_Fullstack.entity.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
public class DetalleVentaRepositoryTest {

    @Autowired
    private DetalleVentaRepository detalleVentaRepository;

    @Autowired
    private VentaRepository ventaRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Test
    public void testGuardarYRecuperarDetalleVenta() {
        // Crear y guardar una categoría
        Categoria categoria = Categoria.builder()
                .nombre("Categoría Test")
                .build();
        categoria = categoriaRepository.save(categoria);

        // Crear y guardar un producto
        Producto producto = Producto.builder()
                .nombre("Producto Test")
                .precio(1000)
                .stock(10)
                .categoria(categoria)
                .build();
        producto = productoRepository.save(producto);

        // Crear y guardar un usuario
        Usuario usuario = Usuario.builder()
                .correo("test@example.com")
                .contrasenia("1234")
                .build();
        usuario = usuarioRepository.save(usuario);

        // Crear y guardar una venta
        Venta venta = Venta.builder()
                .usuario(usuario)
                .total(2000)
                .build();
        venta = ventaRepository.save(venta);

        // Crear y guardar detalle de venta
        DetalleVenta detalle = DetalleVenta.builder()
                .venta(venta)
                .producto(producto)
                .cantidad(2)
                .precioUnitario(1000)
                .subtotal(2000)
                .build();
        detalle = detalleVentaRepository.save(detalle);

        // Recuperar y verificar
        DetalleVenta detalleRecuperado = detalleVentaRepository.findById(detalle.getId()).orElse(null);
        assertNotNull(detalleRecuperado);
        assertEquals(2000, detalleRecuperado.getSubtotal());
        assertEquals("Producto Test", detalleRecuperado.getProducto().getNombre());
        assertEquals("test@example.com", detalleRecuperado.getVenta().getUsuario().getCorreo());
    }
}