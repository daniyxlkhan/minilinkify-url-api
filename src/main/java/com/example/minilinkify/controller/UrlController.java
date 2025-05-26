package com.example.minilinkify.controller;

import com.example.minilinkify.dto.UrlRequest;
import com.example.minilinkify.dto.UrlResponse;
import com.example.minilinkify.service.UrlService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<UrlResponse> shortenUrl(@RequestBody @Valid UrlRequest request) {
        String shortUrl = "http://localhost:8080/api/" +  urlService.shortenUrl(request.getUrl());
        return ResponseEntity.ok(new UrlResponse(request.getUrl(), shortUrl));
    }

    @GetMapping("/{shortCode}")
    public ResponseEntity<Void> getOriginalUrl(@PathVariable String shortCode) {
        return ResponseEntity.status(HttpStatus.FOUND)
                .header("Location", urlService.getOriginalUrl(shortCode))
                .build();
    }


}
