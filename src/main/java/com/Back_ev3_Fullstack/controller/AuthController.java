package com.Back_ev3_Fullstack.controller;

import com.Back_ev3_Fullstack.dto.JwtResponse;
import com.Back_ev3_Fullstack.dto.LoginRequest;
import com.Back_ev3_Fullstack.dto.LoginResponse;
import com.Back_ev3_Fullstack.dto.RegistroRequest;
import com.Back_ev3_Fullstack.entity.Role;
import com.Back_ev3_Fullstack.entity.Usuario;
import com.Back_ev3_Fullstack.repository.UsuarioRepository;
import com.Back_ev3_Fullstack.security.CustomUserDetailsService;
import com.Back_ev3_Fullstack.security.JwtUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    // LOGIN
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest req) {
        try {
            // Validar credenciales usando AuthenticationManager
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(req.getCorreo(), req.getContrasenia())
            );
        } catch (BadCredentialsException e) {
            // Credenciales inválidas
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Credenciales inválidas");
        }

        // Obtener información del usuario
        UserDetails userDetails = userDetailsService.loadUserByUsername(req.getCorreo());

        // Generar token JWT
        String token = jwtUtil.generateToken(userDetails);

        // Retornar token en un Map
        return ResponseEntity.ok(Map.of("token", token));
    }


    @PostMapping("/register")
    public ResponseEntity<String> registro(@RequestBody RegistroRequest request) {

        if (usuarioRepository.findByCorreo(request.getCorreo()) != null) {
            return ResponseEntity.badRequest().body("El correo ya está registrado");
        }

        Usuario usuario = new Usuario();
        usuario.setCorreo(request.getCorreo());
        usuario.setContrasenia(passwordEncoder.encode(request.getContrasenia()));
        usuario.setRoles(new HashSet<>());
        usuario.getRoles().add(Role.USER);

        usuarioRepository.save(usuario);

        return ResponseEntity.ok("Usuario registrado correctamente");
    }

}
