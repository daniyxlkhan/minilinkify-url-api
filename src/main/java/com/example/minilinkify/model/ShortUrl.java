package com.example.minilinkify.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;


@Entity
public class ShortUrl {

    @Id
    @SequenceGenerator(
            name= "short_url_sequence",
            sequenceName = "short_url_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "short_url_sequence"
    )
    private long id;

    private String originalUrl;

    private String shortCode;

    private LocalDateTime createdAt;

    public ShortUrl() {

    }

    public ShortUrl(String originalUrl, String shortCode, LocalDateTime createdAt) {
        this.originalUrl = originalUrl;
        this.shortCode = shortCode;
        this.createdAt = createdAt;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getOriginalUrl() {
        return originalUrl;
    }

    public void setOriginalUrl(String originalUrl) {
        this.originalUrl = originalUrl;
    }

    public String getShortCode() {
        return shortCode;
    }

    public void setShortCode(String shortCode) {
        this.shortCode = shortCode;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
