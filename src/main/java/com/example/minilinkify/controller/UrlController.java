package com.example.minilinkify.controller;

import com.example.minilinkify.dto.UrlRequest;
import com.example.minilinkify.service.UrlService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "api/")
public class UrlController {
    private final UrlService urlService;

    public UrlController(UrlService urlService) {
        this.urlService = urlService;
    }

    @PostMapping("/shorten")
    public ResponseEntity<String> shortenUrl(@RequestBody UrlRequest request) {
        return ResponseEntity.ok(urlService.shortenUrl(request.getUrl()));
    }

    @GetMapping("/{shortCode}")
    public ResponseEntity<String> getOriginalUrl(@PathVariable String shortCode) {
        return ResponseEntity.ok(urlService.getOriginalUrl(shortCode));
    }
}
