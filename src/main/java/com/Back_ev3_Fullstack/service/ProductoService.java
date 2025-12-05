package com.Back_ev3_Fullstack.service;

import com.Back_ev3_Fullstack.dto.ProductoRequestDTO;
import com.Back_ev3_Fullstack.dto.ProductoResponseDTO;
import com.Back_ev3_Fullstack.entity.Categoria;
import com.Back_ev3_Fullstack.entity.Producto;
import com.Back_ev3_Fullstack.repository.CategoriaRepository;
import com.Back_ev3_Fullstack.repository.ProductoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductoService {
    private final ProductoRepository repo;
    private final CategoriaRepository categoriaRepo;

    // Crear producto
    public ProductoResponseDTO crear(ProductoRequestDTO dto) {

        // Validar categoría
        Categoria categoria = categoriaRepo.findById(dto.getCategoriaId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "La categoría con ID " + dto.getCategoriaId() + " no existe"));

        // Crear entidad
        Producto producto = Producto.builder()
                .nombre(dto.getNombre())
                .precio(dto.getPrecio())
                .stock(dto.getStock())
                .imagen(dto.getImagen())
                .categoria(categoria)
                .build();

        Producto guardado = repo.save(producto);

        return new ProductoResponseDTO(
                guardado.getId(),
                guardado.getNombre(),
                guardado.getPrecio(),
                guardado.getStock(),
                guardado.getImagen(),
                guardado.getCategoria().getId()
        );
    }

    // Listar productos
    public List<ProductoResponseDTO> listar() {
        return repo.findAll()
                .stream()
                .map(p -> new ProductoResponseDTO(
                        p.getId(),
                        p.getNombre(),
                        p.getPrecio(),
                        p.getStock(),
                        p.getImagen(),
                        p.getCategoria().getId()
                ))
                .toList();
    }

    // Obtener por id
    public ProductoResponseDTO obtener(Long id) {
        Producto p = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        return new ProductoResponseDTO(
                p.getId(),
                p.getNombre(),
                p.getPrecio(),
                p.getStock(),
                p.getImagen(),
                p.getCategoria().getId()
        );
    }

    // Actualizar
    public ProductoResponseDTO actualizar(Long id, ProductoRequestDTO dto) {

        Producto existente = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        Categoria categoria = categoriaRepo.findById(dto.getCategoriaId())
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));

        existente.setNombre(dto.getNombre());
        existente.setPrecio(dto.getPrecio());
        existente.setStock(dto.getStock());
        existente.setImagen(dto.getImagen());
        existente.setCategoria(categoria);

        Producto guardado = repo.save(existente);

        return new ProductoResponseDTO(
                guardado.getId(),
                guardado.getNombre(),
                guardado.getPrecio(),
                guardado.getStock(),
                guardado.getImagen(),
                guardado.getCategoria().getId()
        );
    }

    // Eliminar
    public void eliminar(Long id) {
        if (!repo.existsById(id)) {
            throw new RuntimeException("Producto no encontrado");
        }
        repo.deleteById(id);
    }

}
