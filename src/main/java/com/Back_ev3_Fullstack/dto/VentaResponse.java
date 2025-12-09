package com.Back_ev3_Fullstack.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VentaResponse {

    private Long id;
    private LocalDateTime fechaVenta;
    private Integer total;
    private List<DetalleResponse> detalles;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class DetalleResponse {
        private Long id;
        private ProductoInfo producto;
        private Integer cantidad;
        private Integer precioUnitario;
        private Integer subtotal;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ProductoInfo {
        private Long id;
        private String nombre;
        private Long categoriaId;
        private String categoriaNombre;
    }
}
