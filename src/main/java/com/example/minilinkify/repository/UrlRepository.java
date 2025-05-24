package com.example.minilinkify.repository;

import com.example.minilinkify.model.ShortUrl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UrlRepository extends JpaRepository<ShortUrl, Long> {
    @Query("SELECT u FROM ShortUrl u WHERE u.shortCode = :shortCode")
    Optional<ShortUrl> getShortUrlByShortCode(@Param("shortCode") String shortCode);
}
