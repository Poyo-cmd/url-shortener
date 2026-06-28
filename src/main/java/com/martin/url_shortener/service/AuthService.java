package com.martin.url_shortener.service;

import com.martin.url_shortener.dto.AuthResponse;
import com.martin.url_shortener.dto.RegisterRequest;
import com.martin.url_shortener.model.Usuario;
import com.martin.url_shortener.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthResponse registrar(RegisterRequest request) {
        if (usuarioRepository.existsByEmail(request.email())) {
            throw new RuntimeException("El email ya está registrado");
        }

        Usuario usuario = Usuario.builder()
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .createdAt(LocalDateTime.now())
                .build();

        usuarioRepository.save(usuario);
        String token = jwtService.generarToken(usuario.getEmail());
        return new AuthResponse(token, usuario.getEmail());
    }

    public AuthResponse login(RegisterRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password())
        );

        Usuario usuario = usuarioRepository.findByEmail(request.email())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        String token = jwtService.generarToken(usuario.getEmail());
        return new AuthResponse(token, usuario.getEmail());
    }
}