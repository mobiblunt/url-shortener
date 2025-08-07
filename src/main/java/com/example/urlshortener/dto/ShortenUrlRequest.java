package com.example.urlshortener.dto;

import jakarta.validation.constraints.NotBlank;

public class ShortenUrlRequest {
    @NotBlank(message = "URL is required")
    private String url;
    
    // Constructor, getter, setter
    public ShortenUrlRequest() {}
    
    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }

    
}
