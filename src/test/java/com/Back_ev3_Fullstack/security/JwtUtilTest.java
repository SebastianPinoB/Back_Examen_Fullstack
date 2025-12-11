package com.Back_ev3_Fullstack.security;

import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class JwtUtilTest {
    private JwtUtil jwtUtil;

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil();
    }

    @Test
    void generateToken_debeGenerarTokenValido() {
        String correo = "user@mail.com";
        List<String> roles = List.of("ROLE_USER");

        String token = jwtUtil.generateToken(correo, roles);

        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    void extractAllClaims_debeRetornarClaimsCorrectos() {
        String correo = "user@mail.com";
        List<String> roles = List.of("ROLE_USER");

        String token = jwtUtil.generateToken(correo, roles);
        Claims claims = jwtUtil.extractAllClaims(token);

        assertEquals(correo, claims.getSubject());
        assertEquals(roles, claims.get("roles", List.class));
        assertNotNull(claims.getIssuedAt());
        assertNotNull(claims.getExpiration());
    }

    @Test
    void extractCorreo_debeRetornarCorreoCorrecto() {
        String correo = "user@mail.com";
        List<String> roles = List.of("ROLE_USER");

        String token = jwtUtil.generateToken(correo, roles);

        String extractedCorreo = jwtUtil.extractCorreo(token);
        assertEquals(correo, extractedCorreo);
    }

    @Test
    void extractRoles_debeRetornarRolesCorrectos() {
        String correo = "user@mail.com";
        List<String> roles = List.of("ROLE_USER", "ROLE_ADMIN");

        String token = jwtUtil.generateToken(correo, roles);

        List<String> extractedRoles = jwtUtil.extractRoles(token);
        assertEquals(roles, extractedRoles);
    }
}
