package com.example.minilinkify.service;

import com.example.minilinkify.model.ShortUrl;
import com.example.minilinkify.repository.UrlRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
public class UrlServiceImpl implements UrlService {
    private final UrlRepository urlRepository;

    @Autowired
    public UrlServiceImpl(UrlRepository urlRepository) {
        this.urlRepository = urlRepository;
    }

    @Override
    public String shortenUrl(String url) {
        String shortCode;
        do {
            shortCode = generateShortCode();
        } while (urlRepository.getShortUrlByShortCode(shortCode).isPresent());

        urlRepository.save(new ShortUrl(url, shortCode, LocalDateTime.now()));

        return shortCode;
    }

    private String generateShortCode() {
        final String ALPHABET = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 6) { // length of the random string.
            int index = rnd.nextInt(ALPHABET.length());
            salt.append(ALPHABET.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;
    }

    @Override
    public String getOriginalUrl(String shortCode) throws RuntimeException {
        ShortUrl shortUrl = urlRepository.getShortUrlByShortCode(shortCode).orElse(null);
        if (shortUrl != null) {
            return shortUrl.getOriginalUrl();
        } else {
            throw new RuntimeException("Short URL not found");
        }
    }
}