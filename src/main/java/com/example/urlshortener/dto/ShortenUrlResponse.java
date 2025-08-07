package com.example.urlshortener.dto;

public class ShortenUrlResponse {
    private String originalUrl;
    private String shortUrl;
    private String shortCode;
    
    // Constructors, getters, setters
    public ShortenUrlResponse(String originalUrl, String shortUrl, String shortCode) {
        this.originalUrl = originalUrl;
        this.shortUrl = shortUrl;
        this.shortCode = shortCode;
    }
    
    // Getters and setters...
}