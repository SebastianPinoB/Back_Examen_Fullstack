package com.Back_ev3_Fullstack.controller;

import com.Back_ev3_Fullstack.dto.CategoriaRequestDTO;
import com.Back_ev3_Fullstack.dto.CategoriaResponseDTO;
import com.Back_ev3_Fullstack.service.CategoriaService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/categorias")
@RequiredArgsConstructor
public class CategoriaController {

    private final CategoriaService categoriaService;

    @PostMapping
    public CategoriaResponseDTO crear(@RequestBody CategoriaRequestDTO dto) {
        return categoriaService.crear(dto);
    }

    @GetMapping
    public List<CategoriaResponseDTO> listar() {
        return categoriaService.listar();
    }
}
