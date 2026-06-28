package com.martin.url_shortener.service;

import com.martin.url_shortener.dto.ShortenRequest;
import com.martin.url_shortener.dto.ShortenResponse;
import com.martin.url_shortener.model.ShortUrl;
import com.martin.url_shortener.repository.ShortUrlRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UrlShortenerService {

    private final ShortUrlRepository shortUrlRepository;

    public ShortenResponse createShortUrl(ShortenRequest request, int diasExpiracion) {
        String code = generarCodigoUnico();

        ShortUrl shortUrl = ShortUrl.builder()
                .code(code)
                .originalUrl(request.url())
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusDays(diasExpiracion))
                .active(true)
                .build();

        shortUrlRepository.save(shortUrl);
        return toResponse(shortUrl);
    }

    @Cacheable(value = "urls", key = "#code")
    public Optional<ShortUrl> findByCode(String code) {
        return shortUrlRepository.findByCode(code)
                .filter(ShortUrl::isActive)
                .filter(u -> u.getExpiresAt() == null || u.getExpiresAt().isAfter(LocalDateTime.now()));
    }

    @CacheEvict(value = "urls", key = "#code")
    public void invalidarCache(String code) {
        // elimina la entrada del caché cuando sea necesario
    }

    private ShortenResponse toResponse(ShortUrl shortUrl) {
        return new ShortenResponse(
                shortUrl.getCode(),
                "http://localhost:8080/" + shortUrl.getCode(),
                shortUrl.getOriginalUrl(),
                shortUrl.getCreatedAt(),
                shortUrl.getExpiresAt()
        );
    }

    private String generarCodigoUnico() {
        String caracteres = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        String code;

        do {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < 6; i++) {
                int index = (int) (Math.random() * caracteres.length());
                sb.append(caracteres.charAt(index));
            }
            code = sb.toString();
        } while (shortUrlRepository.existsByCode(code));

        return code;
    }
}