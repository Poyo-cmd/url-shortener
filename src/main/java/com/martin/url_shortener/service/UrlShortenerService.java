package com.martin.url_shortener.service;

import com.martin.url_shortener.model.ShortUrl;
import com.martin.url_shortener.repository.ShortUrlRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UrlShortenerService {

    private final ShortUrlRepository shortUrlRepository;

    public ShortUrl createShortUrl(String originalUrl) {
        String code = generateUniqueCode();

        ShortUrl shortUrl = ShortUrl.builder()
                .code(code)
                .originalUrl(originalUrl)
                .createdAt(LocalDateTime.now())
                .active(true)
                .build();

        return shortUrlRepository.save(shortUrl);
    }

    public Optional<ShortUrl> findByCode(String code) {
        return shortUrlRepository.findByCode(code);
    }

    private String generateUniqueCode() {
        String characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        String code;

        do {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < 6; i++) {
                int index = (int) (Math.random() * characters.length());
                sb.append(characters.charAt(index));
            }
            code = sb.toString();
        } while (shortUrlRepository.existsByCode(code));

        return code;
    }
}