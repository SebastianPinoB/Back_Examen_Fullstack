package com.Back_ev3_Fullstack.service;

import com.Back_ev3_Fullstack.entity.Role;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

class JwtServiceTest {

    @Test
    void generarToken_debeGenerarTokenValido() {
        String correo = "user@mail.com";
        Set<Role> roles = Set.of(Role.USER);

        try (MockedStatic<Jwts> jwtsMock = Mockito.mockStatic(Jwts.class)) {

            JwtBuilder builder = Mockito.mock(JwtBuilder.class);

            jwtsMock.when(Jwts::builder).thenReturn(builder);

            when(builder.setSubject(any())).thenReturn(builder);
            when(builder.claim(anyString(), any())).thenReturn(builder);
            when(builder.setIssuedAt(any())).thenReturn(builder);
            when(builder.setExpiration(any())).thenReturn(builder);
            when(builder.signWith(any(), anyString())).thenReturn(builder);
            when(builder.compact()).thenReturn("FAKE_TOKEN");

            JwtService service = new JwtService();

            String token = service.generarToken(correo, roles);

            assertEquals("FAKE_TOKEN", token);
        }
    }
}