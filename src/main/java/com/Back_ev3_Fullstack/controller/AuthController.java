package com.Back_ev3_Fullstack.controller;

import com.Back_ev3_Fullstack.dto.LoginRequest;
import com.Back_ev3_Fullstack.dto.LoginResponse;
import com.Back_ev3_Fullstack.entity.Usuario;
import com.Back_ev3_Fullstack.repository.UsuarioRepository;
import com.Back_ev3_Fullstack.security.JwtUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    // ============================
    // LOGIN
    // ============================
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getCorreo(),
                            request.getContrasenia()
                    )
            );
        } catch (AuthenticationException e) {
            return ResponseEntity.badRequest().body(new LoginResponse("Credenciales incorrectas"));
        }

        // Generar token
        String token = jwtUtil.generateToken(request.getCorreo());

        return ResponseEntity.ok(new LoginResponse(token));
    }

    // ============================
    // REGISTRO
    // ============================
    @PostMapping("/registro")
    public ResponseEntity<String> registro(@RequestBody Usuario usuario) {

        if (usuarioRepository.findByCorreo(usuario.getCorreo()) != null) {
            return ResponseEntity.badRequest().body("El correo ya está registrado");
        }

        usuario.setContrasenia(passwordEncoder.encode(usuario.getContrasenia()));
        usuarioRepository.save(usuario);

        return ResponseEntity.ok("Usuario registrado correctamente");
    }
}
