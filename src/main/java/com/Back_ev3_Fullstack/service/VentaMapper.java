package com.Back_ev3_Fullstack.service;

import com.Back_ev3_Fullstack.dto.VentaResponse;
import com.Back_ev3_Fullstack.entity.DetalleVenta;
import com.Back_ev3_Fullstack.entity.Venta;

public class VentaMapper {

    public static VentaResponse toResponse(Venta venta) {

        return VentaResponse.builder()
                .id(venta.getId())
                .fechaVenta(venta.getFechaVenta())
                .total(venta.getTotal())
                .detalles(
                        venta.getDetalles().stream()
                                .map(VentaMapper::detalleToResponse)
                                .toList()
                )
                .build();
    }

    private static VentaResponse.DetalleResponse detalleToResponse(DetalleVenta d) {

        return VentaResponse.DetalleResponse.builder()
                .id(d.getId())
                .cantidad(d.getCantidad())
                .precioUnitario(d.getPrecioUnitario())
                .subtotal(d.getSubtotal())
                .producto(
                        VentaResponse.ProductoInfo.builder()
                                .id(d.getProducto().getId())
                                .nombre(d.getProducto().getNombre())
                                .categoriaId(d.getProducto().getCategoria().getId())
                                .categoriaNombre(d.getProducto().getCategoria().getNombre())
                                .build()
                )
                .build();
    }
}