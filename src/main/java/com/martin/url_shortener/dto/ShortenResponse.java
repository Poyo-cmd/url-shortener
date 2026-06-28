package com.martin.url_shortener.dto;

import java.time.LocalDateTime;

public record ShortenResponse(
        String code,
        String shortUrl,
        String originalUrl,
        LocalDateTime createdAt,
        LocalDateTime expiresAt
) {}