package com.Back_ev3_Fullstack.security;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class JwtFilterTest {
    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private CustomUserDetailsService userDetailsService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @InjectMocks
    private JwtFilter jwtFilter;

    @AfterEach
    void clearSecurityContext() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void doFilterInternal_rutaPublica_noProcesaToken() throws Exception {
        when(request.getServletPath()).thenReturn("/api/auth/login");

        jwtFilter.doFilterInternal(request, response, filterChain);

        // Verifica que el filterChain continue
        verify(filterChain).doFilter(request, response);
        verifyNoInteractions(jwtUtil, userDetailsService);
    }

    @Test
    void doFilterInternal_tokenValido_seteaAuthentication() throws Exception {
        when(request.getServletPath()).thenReturn("/api/ventas");
        when(request.getHeader("Authorization")).thenReturn("Bearer TOKEN123");

        Claims claims = mock(Claims.class);
        when(claims.getSubject()).thenReturn("user@mail.com");
        when(claims.get("roles", List.class)).thenReturn(List.of("ROLE_USER"));

        when(jwtUtil.extractAllClaims("TOKEN123")).thenReturn(claims);

        UserDetails userDetails = User.builder()
                .username("user@mail.com")
                .password("1234")
                .roles("USER")
                .build();
        when(userDetailsService.loadUserByUsername("user@mail.com")).thenReturn(userDetails);

        jwtFilter.doFilterInternal(request, response, filterChain);

        // Verifica que se haya seteado la autenticacion
        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
        assertEquals(userDetails, SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void doFilterInternal_tokenInvalido_noSeteaAuthentication() throws Exception {
        when(request.getServletPath()).thenReturn("/api/ventas");
        when(request.getHeader("Authorization")).thenReturn("Bearer TOKEN123");
        when(jwtUtil.extractAllClaims("TOKEN123")).thenThrow(new RuntimeException("Token inválido"));

        jwtFilter.doFilterInternal(request, response, filterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(filterChain).doFilter(request, response);
    }
}