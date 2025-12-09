package com.Back_ev3_Fullstack.service;

import com.Back_ev3_Fullstack.dto.VentaRequest;
import com.Back_ev3_Fullstack.dto.VentaResponse;


import java.util.List;

public interface VentaService {

    VentaResponse crearVenta(VentaRequest request, String correoUsuario);

    List<VentaResponse> listarVentasDeUsuario(String correoUsuario);

    List<VentaResponse> listarVentas(); // Solo ADMIN
}