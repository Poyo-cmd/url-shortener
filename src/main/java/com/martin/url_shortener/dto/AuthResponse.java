package com.martin.url_shortener.dto;

public record AuthResponse(
        String token,
        String email
) {}