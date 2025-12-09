package com.Back_ev3_Fullstack.service;

import com.Back_ev3_Fullstack.dto.VentaRequest;
import com.Back_ev3_Fullstack.dto.VentaResponse;
import com.Back_ev3_Fullstack.entity.DetalleVenta;
import com.Back_ev3_Fullstack.entity.Producto;
import com.Back_ev3_Fullstack.entity.Usuario;
import com.Back_ev3_Fullstack.entity.Venta;
import com.Back_ev3_Fullstack.repository.ProductoRepository;
import com.Back_ev3_Fullstack.repository.UsuarioRepository;
import com.Back_ev3_Fullstack.repository.VentaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VentaServiceImpl implements VentaService {

    private final VentaRepository ventaRepository;
    private final ProductoRepository productoRepository;
    private final UsuarioRepository usuarioRepository;

    @Override
    public VentaResponse crearVenta(VentaRequest request, String correoUsuario) {

        Usuario usuario = usuarioRepository.findByCorreo(correoUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Venta venta = new Venta();
        venta.setFechaVenta(LocalDateTime.now());
        venta.setTotal(request.getTotal());
        venta.setUsuario(usuario);

        List<DetalleVenta> detalles = request.getItems().stream().map(item -> {

            Producto producto = productoRepository.findById(item.getProductoId())
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

            DetalleVenta d = new DetalleVenta();
            d.setProducto(producto);
            d.setCantidad(item.getCantidad());
            d.setPrecioUnitario(item.getPrecio());
            d.setSubtotal(item.getCantidad() * item.getPrecio());
            d.setVenta(venta);

            return d;
        }).collect(Collectors.toList());

        venta.setDetalles(detalles);

        ventaRepository.save(venta);
        return VentaMapper.toResponse(venta);
    }

    @Override
    public List<VentaResponse> listarVentasDeUsuario(String correoUsuario) {

        return ventaRepository.findAllByUsuarioCorreo(correoUsuario)
                .stream()
                .map(VentaMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<VentaResponse> listarVentas() {

        return ventaRepository.findAll()
                .stream()
                .map(VentaMapper::toResponse)
                .collect(Collectors.toList());
    }
}