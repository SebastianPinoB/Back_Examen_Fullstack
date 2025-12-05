package com.Back_ev3_Fullstack.config;

import com.Back_ev3_Fullstack.entity.Categoria;
import com.Back_ev3_Fullstack.entity.Producto;
import com.Back_ev3_Fullstack.entity.Usuario;
import com.Back_ev3_Fullstack.entity.Role;
import com.Back_ev3_Fullstack.repository.CategoriaRepository;
import com.Back_ev3_Fullstack.repository.ProductoRepository;
import com.Back_ev3_Fullstack.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@Configuration
public class DataLoader {

    @Bean
    @Order(1)
    public CommandLineRunner initAdmin(
            UsuarioRepository repo,
            PasswordEncoder encoder
    ) {
        return args -> {

            // ADMIN POR DEFECTO
            String adminCorreo = "admin@local";
            String adminPass = "admin123";

            // Si ya existe un admin, no hacemos nada
            if (repo.existsByCorreo(adminCorreo)) {
                System.out.println("✔ Admin ya existe: " + adminCorreo);
                return;
            }

            Usuario admin = new Usuario();
            admin.setCorreo(adminCorreo);
            admin.setContrasenia(encoder.encode(adminPass));

            // asignar roles
            admin.getRoles().add(Role.ADMIN);
            admin.getRoles().add(Role.USER);

            repo.save(admin);

            System.out.println("✔ Admin inicial creado: " + adminCorreo);
        };
    }

    @Bean
    @Order(2)
    public CommandLineRunner initCategoria(CategoriaRepository categoriaRepo){
        return args -> {
            // solo crear si no hay libros
            if (categoriaRepo.count() > 0) {
                System.out.println("✔ Ya existen " + categoriaRepo.count() + " libros en la base de datos");
                return;
            }

            System.out.println("Creando ategorias iniciales");

            Categoria categoria1 = Categoria.builder()
                    .nombre("Accesorios")
                    .build();

            Categoria categoria2 = Categoria.builder()
                    .nombre("Componentes")
                    .build();

            try {
                categoriaRepo.saveAll(List.of(categoria1, categoria2));
                System.out.println("✔ Categorías iniciales creadas correctamente");
            } catch (Exception e) {
                System.err.println("❌ Error al crear categorías: " + e.getMessage());
                e.printStackTrace();
            }
        };
    }

    @Bean
    @Order(3)
    public CommandLineRunner initProductos(ProductoRepository productoRepo,
                                           CategoriaRepository categoriaRepo) {
        return args -> {

            // Evitar duplicados
            if (productoRepo.count() > 0) {
                System.out.println("✔ Ya existen " + productoRepo.count() + " productos en la base de datos");
                return;
            }

            System.out.println("Creando productos iniciales...");

            // Verificar categorías
            if (categoriaRepo.count() == 0) {
                System.err.println("❌ No se pueden crear productos porque no existen categorías.");
                return;
            }

            // Obtener categorías (por nombre o por ID)
            Categoria accesorios = categoriaRepo.findByNombre("Accesorios");
            Categoria componentes = categoriaRepo.findByNombre("Componentes");

            if (accesorios == null || componentes == null) {
                System.err.println("❌ Falta una de las categorías iniciales. No se crearán productos.");
                return;
            }

            // Productos iniciales
            Producto p1 = Producto.builder()
                    .nombre("Mouse Gaming RGB")
                    .precio(15000)
                    .stock(30)
                    .imagen("https://example.com/mouse.jpg")
                    .categoria(accesorios)
                    .build();

            Producto p2 = Producto.builder()
                    .nombre("Teclado Mecánico")
                    .precio(30000)
                    .stock(20)
                    .imagen("https://example.com/teclado.jpg")
                    .categoria(accesorios)
                    .build();

            Producto p3 = Producto.builder()
                    .nombre("Procesador AMD Ryzen 5")
                    .precio(120000)
                    .stock(15)
                    .imagen("https://example.com/ryzen.jpg")
                    .categoria(componentes)
                    .build();

            try {
                productoRepo.saveAll(List.of(p1, p2, p3));
                System.out.println("✔ Productos iniciales creados correctamente");
            } catch (Exception e) {
                System.err.println("❌ Error al crear productos: " + e.getMessage());
                e.printStackTrace();
            }
        };
    }
}
