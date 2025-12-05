package com.Back_ev3_Fullstack.service;

import com.Back_ev3_Fullstack.dto.CategoriaRequestDTO;
import com.Back_ev3_Fullstack.dto.CategoriaResponseDTO;
import com.Back_ev3_Fullstack.entity.Categoria;
import com.Back_ev3_Fullstack.repository.CategoriaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoriaService {

    private final CategoriaRepository repo;

    // Crear
    public CategoriaResponseDTO crear(CategoriaRequestDTO dto) {

        // Validacion
        if (repo.existsByNombre(dto.getNombre())) {
            throw new IllegalArgumentException("La categoría '" + dto.getNombre() + "' ya existe");
        }

        Categoria categoria = Categoria.builder()
                .nombre(dto.getNombre())
                .build();

        Categoria guardada = repo.save(categoria);

        return new CategoriaResponseDTO(
                guardada.getId(),
                guardada.getNombre()
        );
    }

    // Listar
    public List<CategoriaResponseDTO> listar() {
        return repo.findAll()
                .stream()
                .map(c -> new CategoriaResponseDTO(c.getId(), c.getNombre()))
                .toList();
    }
}
