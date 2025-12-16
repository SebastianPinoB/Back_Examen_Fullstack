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
                .descripcion(dto.getDescripcion())
                .categoria(categoria)
                .build();
        producto.setActivo(true);
        producto.setRating(0.0);
        producto.setNumResenas(0);

        Producto guardado = repo.save(producto);

        return new ProductoResponseDTO(
                guardado.getId(),
                guardado.getNombre(),
                guardado.getPrecio(),
                guardado.getStock(),
                guardado.getImagen(),
                guardado.getDescripcion(),
                guardado.getCategoria().getId(),
                guardado.getCategoria().getNombre(),
                guardado.getRating(),
                guardado.getNumResenas()
        );
    }

    // Listar productos activos
    public List<ProductoResponseDTO> listar() {
        return repo.findAll()
                .stream()
                .filter(p -> p.getActivo() != null && p.getActivo()) // Solo activos
                .map(p -> new ProductoResponseDTO(
                        p.getId(),
                        p.getNombre(),
                        p.getPrecio(),
                        p.getStock(),
                        p.getImagen(),
                        p.getDescripcion(),
                        p.getCategoria().getId(),
                        p.getCategoria().getNombre(),
                        p.getRating(),
                        p.getNumResenas()
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
                p.getDescripcion(),
                p.getCategoria().getId(),
                p.getCategoria().getNombre(),
                p.getRating(),
                p.getNumResenas()
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
        existente.setDescripcion(dto.getDescripcion());
        existente.setCategoria(categoria);

        Producto guardado = repo.save(existente);

        return new ProductoResponseDTO(
                guardado.getId(),
                guardado.getNombre(),
                guardado.getPrecio(),
                guardado.getStock(),
                guardado.getImagen(),
                guardado.getDescripcion(),
                guardado.getCategoria().getId(),
                guardado.getCategoria().getNombre(),
                guardado.getRating(),
                guardado.getNumResenas()
        );
    }

    // Eliminar
    public void eliminar(Long id) {
        Producto producto = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        producto.setActivo(false);
        repo.save(producto);
    }

}
