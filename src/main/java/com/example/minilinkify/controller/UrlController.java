package com.example.minilinkify.controller;

import com.example.minilinkify.dto.UrlRequest;
import com.example.minilinkify.dto.UrlResponse;
import com.example.minilinkify.dto.UrlStatsResponse;
import com.example.minilinkify.model.ShortUrl;
import com.example.minilinkify.service.UrlService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@CrossOrigin(origins = { "http://localhost:63342", "https://minilinkify.tech", "https://www.minilinkify.tech"})
@RestController
@RequestMapping("")
public class UrlController {
    private final UrlService urlService;

    public UrlController(UrlService urlService) {
        this.urlService = urlService;
    }

    @PostMapping("/shorten")
    public ResponseEntity<UrlResponse> shortenUrl(@RequestBody @Valid UrlRequest request, HttpServletRequest httpRequest) {
        String baseUrl = ServletUriComponentsBuilder.fromRequestUri(httpRequest)
                .replacePath(null)
                .build()
                .toUriString();
        String shortCode = urlService.shortenUrl(request.getUrl());
        String shortUrl = baseUrl + "/" + shortCode;

        return ResponseEntity.status(HttpStatus.CREATED).body(new UrlResponse(request.getUrl(), shortUrl));
//      String shortUrl = "http://localhost:8080/shorten/" +  urlService.shortenUrl(request.getUrl());
//      return ResponseEntity.status(HttpStatus.CREATED).body(new UrlResponse(request.getUrl(), shortUrl));
    }

    @GetMapping("/{shortCode:[a-zA-Z0-9]+}")
    public ResponseEntity<Void> getOriginalUrl(@PathVariable String shortCode) {
        return ResponseEntity.status(HttpStatus.FOUND)
                .header("Location", urlService.getOriginalUrl(shortCode))
                .build();
    }

    @GetMapping("/{shortcode}/stats")
    public ResponseEntity<UrlStatsResponse> getStats(@PathVariable String shortcode) {
        ShortUrl shortUrl= urlService.getShortUrlOrThrow(shortcode);

        UrlStatsResponse stats = new UrlStatsResponse(
                shortUrl.getShortCode(),
                shortUrl.getOriginalUrl(),
                shortUrl.getAccessCount(),
                shortUrl.getCreatedAt()
        );

        return ResponseEntity.ok(stats);
    }

    @DeleteMapping("/{shortcode}/delete")
    public ResponseEntity<Void> deleteShortUrl(@PathVariable String shortcode) {
        urlService.deleteShortUrl(shortcode);
        return ResponseEntity.noContent().build();
    }
}
