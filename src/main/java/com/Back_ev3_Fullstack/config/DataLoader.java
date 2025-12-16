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
            admin.setNombreCompleto("Administrador");

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
                System.out.println("✔ Ya existen " + categoriaRepo.count() + " productos");
                return;
            }

            System.out.println("Creando categorias");

            List<Categoria> categorias = List.of(
                    Categoria.builder().nombre("Juegos de Mesa").build(),
                    Categoria.builder().nombre("Accesorios").build(),
                    Categoria.builder().nombre("Consolas").build(),
                    Categoria.builder().nombre("Computadores Gamers").build(),
                    Categoria.builder().nombre("Sillas Gamers").build(),
                    Categoria.builder().nombre("Mouse").build(),
                    Categoria.builder().nombre("Mousepad").build(),
                    Categoria.builder().nombre("Poleras Personalizadas").build(),
                    Categoria.builder().nombre("Polerones Gamers Personalizados").build()
            );

            try {
                categoriaRepo.saveAll(categorias);
                System.out.println(categorias.size() + " categorías creadas correctamente");
            } catch (Exception e) {
                System.err.println("Error al crear categorías: " + e.getMessage());
                e.printStackTrace();
            }
        };
    }

    @Bean
    @Order(3)
    public CommandLineRunner initProductos(ProductoRepository productoRepo,
                                           CategoriaRepository categoriaRepo) {
        return args -> {
            if (productoRepo.count() > 0) {
                System.out.println("Ya existen " + productoRepo.count() + " productos en la base de datos");
                return;
            }

            System.out.println("Creando productos");

            if (categoriaRepo.count() == 0) {
                System.err.println("No se pueden crear productos porque no existen categorías.");
                return;
            }

            // obtener categorías
            Categoria juegosMesa = categoriaRepo.findByNombre("Juegos de Mesa");
            Categoria accesorios = categoriaRepo.findByNombre("Accesorios");
            Categoria consolas = categoriaRepo.findByNombre("Consolas");
            Categoria computadores = categoriaRepo.findByNombre("Computadores Gamers");
            Categoria sillas = categoriaRepo.findByNombre("Sillas Gamers");
            Categoria mouse = categoriaRepo.findByNombre("Mouse");
            Categoria mousepad = categoriaRepo.findByNombre("Mousepad");
            Categoria poleras = categoriaRepo.findByNombre("Poleras Personalizadas");
            Categoria polerones = categoriaRepo.findByNombre("Polerones Gamers Personalizados");

            List<Producto> productos = List.of(
                    // JUEGOS DE MESA
                    Producto.builder()
                            .nombre("Catan")
                            .precio(29990)
                            .stock(15)
                            .imagen("https://images.unsplash.com/photo-1610890716171-6b1bb98ffd09?w=400")
                            .descripcion("Un clásico juego de estrategia donde los jugadores compiten por colonizar y expandirse en la isla de Catan. Ideal para 3-4 jugadores y perfecto para noches de juego en familia o con amigos.")
                            .rating(4.8)
                            .numResenas(234)
                            .activo(true)
                            .categoria(juegosMesa)
                            .build(),

                    Producto.builder()
                            .nombre("Carcassonne")
                            .precio(24990)
                            .stock(12)
                            .imagen("https://images.unsplash.com/photo-1566694271355-9ead56467fd0?w=400")
                            .descripcion("Un juego de colocación de fichas donde los jugadores construyen el paisaje alrededor de la fortaleza medieval de Carcassonne. Ideal para 2-5 jugadores y fácil de aprender.")
                            .rating(4.5)
                            .numResenas(156)
                            .activo(true)
                            .categoria(juegosMesa)
                            .build(),

                    // ACCESORIOS
                    Producto.builder()
                            .nombre("Controlador Inalámbrico Xbox Series X")
                            .precio(59990)
                            .stock(25)
                            .imagen("https://images.unsplash.com/photo-1621259182978-fbf93132d53d?w=400")
                            .descripcion("Ofrece una experiencia de juego cómoda con botones mapeables y una respuesta táctil mejorada. Compatible con consolas Xbox y PC.")
                            .rating(4.7)
                            .numResenas(892)
                            .activo(true)
                            .categoria(accesorios)
                            .build(),

                    Producto.builder()
                            .nombre("Auriculares Gamer HyperX Cloud II")
                            .precio(79990)
                            .stock(30)
                            .imagen("https://images.unsplash.com/photo-1618366712010-f4ae9c647dcf?w=400")
                            .descripcion("Proporcionan un sonido envolvente de calidad con un micrófono desmontable y almohadillas de espuma viscoelástica para mayor comodidad durante largas sesiones de juego.")
                            .rating(4.9)
                            .numResenas(1024)
                            .activo(true)
                            .categoria(accesorios)
                            .build(),

                    // CONSOLAS
                    Producto.builder()
                            .nombre("PlayStation 5")
                            .precio(549990)
                            .stock(8)
                            .imagen("https://images.unsplash.com/photo-1606144042614-b2417e99c4e3?w=400")
                            .descripcion("La consola de última generación de Sony, que ofrece gráficos impresionantes y tiempos de carga ultrarrápidos para una experiencia de juego inmersiva.")
                            .rating(5.0)
                            .numResenas(2341)
                            .activo(true)
                            .categoria(consolas)
                            .build(),

                    // COMPUTADORES GAMERS
                    Producto.builder()
                            .nombre("PC Gamer ASUS ROG Strix")
                            .precio(1299990)
                            .stock(5)
                            .imagen("https://images.unsplash.com/photo-1587202372634-32705e3bf49c?w=400")
                            .descripcion("Un potente equipo diseñado para los gamers más exigentes, equipado con los últimos componentes para ofrecer un rendimiento excepcional en cualquier juego.")
                            .rating(4.9)
                            .numResenas(234)
                            .activo(true)
                            .categoria(computadores)
                            .build(),

                    // SILLAS GAMERS
                    Producto.builder()
                            .nombre("Silla Gamer Secretlab Titan")
                            .precio(349990)
                            .stock(10)
                            .imagen("https://images.unsplash.com/photo-1598550476439-6847785fcea6?w=400")
                            .descripcion("Diseñada para el máximo confort, esta silla ofrece un soporte ergonómico y personalización ajustable para sesiones de juego prolongadas.")
                            .rating(4.8)
                            .numResenas(567)
                            .activo(true)
                            .categoria(sillas)
                            .build(),

                    // MOUSE
                    Producto.builder()
                            .nombre("Mouse Gamer Logitech G502 HERO")
                            .precio(49990)
                            .stock(40)
                            .imagen("https://images.unsplash.com/photo-1527814050087-3793815479db?w=400")
                            .descripcion("Con sensor de alta precisión y botones personalizables, este mouse es ideal para gamers que buscan un control preciso y personalización.")
                            .rating(4.8)
                            .numResenas(2134)
                            .activo(true)
                            .categoria(mouse)
                            .build(),

                    // MOUSEPAD
                    Producto.builder()
                            .nombre("Mousepad Razer Goliathus Extended Chroma")
                            .precio(29990)
                            .stock(35)
                            .imagen("https://images.unsplash.com/photo-1616588589676-62b3bd4ff6d2?w=400")
                            .descripcion("Ofrece un área de juego amplia con iluminación RGB personalizable, asegurando una superficie suave y uniforme para el movimiento del mouse.")
                            .rating(4.6)
                            .numResenas(567)
                            .activo(true)
                            .categoria(mousepad)
                            .build(),

                    // POLERAS PERSONALIZADAS
                    Producto.builder()
                            .nombre("Polera Gamer Personalizada 'Level-Up'")
                            .precio(14990)
                            .stock(50)
                            .imagen("https://images.unsplash.com/photo-1576566588028-4147f3842f27?w=400")
                            .descripcion("Una camiseta cómoda y estilizada, con la posibilidad de personalizarla con tu gamer tag o diseño favorito.")
                            .rating(4.4)
                            .numResenas(234)
                            .activo(true)
                            .categoria(poleras)
                            .build()
            );

            try {
                productoRepo.saveAll(productos);
                System.out.println(productos.size() + " productos creados correctamente");
            } catch (Exception e) {
                System.err.println("Error al crear productos: " + e.getMessage());
                e.printStackTrace();
            }
        };
    }
}
