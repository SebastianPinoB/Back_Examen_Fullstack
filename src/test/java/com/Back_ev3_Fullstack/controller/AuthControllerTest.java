package com.Back_ev3_Fullstack.controller;

import com.Back_ev3_Fullstack.dto.LoginRequest;
import com.Back_ev3_Fullstack.dto.RegistroRequest;
import com.Back_ev3_Fullstack.entity.Role;
import com.Back_ev3_Fullstack.entity.Usuario;
import com.Back_ev3_Fullstack.repository.UsuarioRepository;
import com.Back_ev3_Fullstack.security.CustomUserDetailsService;
import com.Back_ev3_Fullstack.security.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @InjectMocks
    private AuthController authController;

    @Mock
    private CustomUserDetailsService userDetailsService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @BeforeEach
    void setUp() {}

    @Test
    void testLoginSuccess() {
        String correo = "test@example.com";
        String contrasenia = "1234";

        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(correo, contrasenia);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authToken);

        Usuario usuario = new Usuario();
        usuario.setCorreo(correo);
        usuario.setNombreCompleto("Test User");
        usuario.setRoles(new HashSet<>());
        usuario.getRoles().add(Role.USER);

        when(usuarioRepository.findByCorreo(correo)).thenReturn(Optional.of(usuario));
        when(jwtUtil.generateToken(eq(correo), eq("Test User"), anyList())).thenReturn("mocked-jwt-token");
        LoginRequest req = new LoginRequest(correo, contrasenia);

        ResponseEntity<?> response = authController.login(req);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(((Map<String, String>) response.getBody()).containsKey("token"));
        assertEquals("mocked-jwt-token", ((Map<String, String>) response.getBody()).get("token"));
    }

    @Test
    void testLoginBadCredentials() {
        String correo = "wrong@example.com";
        String contrasenia = "bad";

        doThrow(new BadCredentialsException("Bad creds"))
                .when(authenticationManager)
                .authenticate(any(UsernamePasswordAuthenticationToken.class));

        LoginRequest req = new LoginRequest(correo, contrasenia);

        ResponseEntity<?> response = authController.login(req);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Credenciales inválidas", response.getBody());
    }

    @Test
    void testRegistroSuccess() {
        String correo = "new@example.com";
        String contrasenia = "pass";

        when(usuarioRepository.findByCorreo(correo)).thenReturn(Optional.empty());
        when(passwordEncoder.encode(contrasenia)).thenReturn("encoded-pass");

        RegistroRequest req = new RegistroRequest();
        req.setCorreo(correo);
        req.setContrasenia(contrasenia);

        ResponseEntity<String> response = authController.registro(req);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Usuario registrado correctamente", response.getBody());
        verify(usuarioRepository, times(1)).save(any(Usuario.class));
    }

    @Test
    void testRegistroCorreoExistente() {
        String correo = "existing@example.com";
        String contrasenia = "pass";

        Usuario usuario = new Usuario();
        usuario.setCorreo(correo);

        when(usuarioRepository.findByCorreo(correo)).thenReturn(Optional.of(usuario));

        RegistroRequest req = new RegistroRequest();
        req.setCorreo(correo);
        req.setContrasenia(contrasenia);

        ResponseEntity<String> response = authController.registro(req);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("El correo ya está registrado", response.getBody());
    }
}