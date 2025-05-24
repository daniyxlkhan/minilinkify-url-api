package com.example.minilinkify.service;

import com.example.minilinkify.repository.UrlRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UrlServiceImpl implements UrlService {
    private final UrlRepository urlRepository;

    @Autowired
    public UrlServiceImpl(UrlRepository urlRepository) {
        this.urlRepository = urlRepository;
    }

    @Override
    public String shortenUrl(String url) {
        return null;
    }

    @Override
    public String getOriginalUrl(String shortCode) {
        return null;
    }
}
