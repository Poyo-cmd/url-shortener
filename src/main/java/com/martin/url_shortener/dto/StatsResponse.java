package com.martin.url_shortener.dto;

import java.time.LocalDateTime;

public record StatsResponse(
        String code,
        String originalUrl,
        long totalClicks,
        long clicksUltimos7Dias,
        LocalDateTime createdAt,
        LocalDateTime expiresAt
) {}