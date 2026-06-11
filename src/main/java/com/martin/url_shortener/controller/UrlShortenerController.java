package com.martin.url_shortener.controller;

import com.martin.url_shortener.model.ShortUrl;
import com.martin.url_shortener.service.UrlShortenerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class UrlShortenerController {

    private final UrlShortenerService urlShortenerService;

    @PostMapping("/api/shorten")
    public ResponseEntity<Map<String, String>> shorten(@RequestBody Map<String, String> body) {
        String originalUrl = body.get("url");
        ShortUrl shortUrl = urlShortenerService.createShortUrl(originalUrl);

        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                "code", shortUrl.getCode(),
                "shortUrl", "http://localhost:8080/" + shortUrl.getCode(),
                "originalUrl", shortUrl.getOriginalUrl()
        ));
    }

    @GetMapping("/{code}")
    public ResponseEntity<Void> redirect(@PathVariable String code) {
        return urlShortenerService.findByCode(code)
                .filter(ShortUrl::isActive)
                .map(shortUrl -> ResponseEntity.status(HttpStatus.FOUND)
                        .location(URI.create(shortUrl.getOriginalUrl()))
                        .<Void>build())
                .orElse(ResponseEntity.notFound().build());
    }
}