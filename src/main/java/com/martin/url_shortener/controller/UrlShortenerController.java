package com.martin.url_shortener.controller;

import com.martin.url_shortener.dto.ShortenRequest;
import com.martin.url_shortener.dto.ShortenResponse;
import com.martin.url_shortener.service.UrlShortenerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequiredArgsConstructor
public class UrlShortenerController {

    private final UrlShortenerService urlShortenerService;

    @PostMapping("/api/shorten")
    public ResponseEntity<ShortenResponse> acortar(@Valid @RequestBody ShortenRequest request) {
        ShortenResponse response = urlShortenerService.createShortUrl(request, 30);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{code}")
    public ResponseEntity<Void> redirigir(@PathVariable String code) {
        return urlShortenerService.findByCode(code)
                .map(shortUrl -> ResponseEntity.status(HttpStatus.FOUND)
                        .location(URI.create(shortUrl.getOriginalUrl()))
                        .<Void>build())
                .orElse(ResponseEntity.notFound().build());
    }
}