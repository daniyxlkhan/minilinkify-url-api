package com.example.minilinkify.service;

import org.springframework.stereotype.Service;

@Service
public interface UrlService {
    String shortenUrl(String url);
    String getOriginalUrl(String shortCode) throws RuntimeException;
    Integer getAccessCount(String shortCode);
}
