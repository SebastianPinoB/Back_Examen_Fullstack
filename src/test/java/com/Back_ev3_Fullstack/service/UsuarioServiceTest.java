package com.Back_ev3_Fullstack.service;

import com.Back_ev3_Fullstack.entity.Usuario;
import com.Back_ev3_Fullstack.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UsuarioService usuarioService;

    private Usuario usuario;

    @BeforeEach
    void setUp() {
        usuario = Usuario.builder()
                .id(1L)
                .correo("test@mail.com")
                .contrasenia("1234")
                .build();
    }

    // Test registro
    @Test
    void registrar_debeRegistrarUsuario() {

        when(usuarioRepository.existsByCorreo("test@mail.com")).thenReturn(false);
        when(passwordEncoder.encode("1234")).thenReturn("encodedPass");
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        Usuario result = usuarioService.registrar(usuario);

        assertNotNull(result);
        assertEquals("test@mail.com", result.getCorreo());
        assertEquals("encodedPass", result.getContrasenia()); // <-- corregido

        verify(usuarioRepository, times(1)).save(any(Usuario.class));
        verify(passwordEncoder, times(1)).encode("1234");
    }

    // Falla por correo duplicado
    @Test
    void registrar_correoDuplicado_debeLanzarExcepcion() {

        when(usuarioRepository.existsByCorreo("test@mail.com")).thenReturn(true);

        assertThrows(RuntimeException.class,
                () -> usuarioService.registrar(usuario));

        verify(usuarioRepository, never()).save(any());
    }

    // Buscar por correo
    @Test
    void findByCorreo_debeRetornarUsuario() {

        when(usuarioRepository.findByCorreo("test@mail.com"))
                .thenReturn(Optional.of(usuario));

        Usuario result = usuarioService.findByCorreo("test@mail.com");

        assertNotNull(result);
        assertEquals("test@mail.com", result.getCorreo());
    }

    // Buscar por correo no existe
    @Test
    void findByCorreo_noExiste_debeLanzarExcepcion() {

        when(usuarioRepository.findByCorreo("test@mail.com"))
                .thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> usuarioService.findByCorreo("test@mail.com"));
    }
}