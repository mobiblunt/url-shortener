package com.example.urlshortener.controller;

import com.example.urlshortener.dto.ShortenUrlRequest;
import com.example.urlshortener.dto.ShortenUrlResponse;
import com.example.urlshortener.model.Url;
import com.example.urlshortener.service.UrlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.stereotype.Controller;

import java.util.Map;

@Controller

public class UrlController {
    
    @Autowired
    private UrlService urlService;
    
    
    
    @PostMapping("/shorten")
    public ModelAndView shortenUrl(@Valid @ModelAttribute ShortenUrlRequest request) {
        ModelAndView modelAndView = new ModelAndView("index");
        try {
            Url url = urlService.shortenUrl(request.getUrl());
            String shortUrl = url.getShortCode();
            modelAndView.addObject("shortUrl", shortUrl);
            modelAndView.addObject("success", true);
        } catch (IllegalArgumentException e) {
            modelAndView.addObject("error", e.getMessage());
        }
        modelAndView.addObject("urls", urlService.getAllUrls()); // If you have a method to get all URLs
        return modelAndView;
    }

    @PostMapping(value = "/api/shorten", produces = "application/json")
    public ResponseEntity<ShortenUrlResponse> shortenUrl(@Valid @RequestBody ShortenUrlRequest request, 
                                       HttpServletRequest httpRequest) {
        try {
            Url url = urlService.shortenUrl(request.getUrl());
            String baseUrl = getBaseUrl(httpRequest);
            String shortUrl = baseUrl + "/" + url.getShortCode();
            ShortenUrlResponse response = new ShortenUrlResponse(
                url.getOriginalUrl(), 
                shortUrl, 
                url.getShortCode()
            );
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
    
    @GetMapping("/{shortCode}")
    public ResponseEntity<?> redirectToOriginalUrl(@PathVariable String shortCode) {
        String originalUrl = urlService.getOriginalUrl(shortCode);
        if (originalUrl != null) {
            return ResponseEntity.status(HttpStatus.MOVED_PERMANENTLY)
                    .header("Location", originalUrl)
                    .build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    private String getBaseUrl(HttpServletRequest request) {
        String scheme = request.getScheme();
        String serverName = request.getServerName();
        int serverPort = request.getServerPort();
        StringBuilder baseUrl = new StringBuilder();
        baseUrl.append(scheme).append("://").append(serverName);
        if ((scheme.equals("http") && serverPort != 80) || (scheme.equals("https") && serverPort != 443)) {
            baseUrl.append(":" + serverPort);
        }
        baseUrl.append(""); // Remove "/api" from the base URL so the short URL is correct
        return baseUrl.toString();
    }
}