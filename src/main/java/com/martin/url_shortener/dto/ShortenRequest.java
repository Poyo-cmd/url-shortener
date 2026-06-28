package com.martin.url_shortener.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record ShortenRequest(
        @NotBlank(message = "La URL no puede estar vacía")
        @Pattern(
                regexp = "^(https?://).+",
                message = "La URL debe comenzar con http:// o https://"
        )
        String url
) {}