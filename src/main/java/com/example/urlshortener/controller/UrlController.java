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
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.access.prepost.PreAuthorize;
import com.example.urlshortener.model.User;
import com.example.urlshortener.repository.UrlRepository;

import java.util.Map;

@Controller

public class UrlController {

@Autowired
private UrlRepository urlRepository;
    
    @Autowired
    private UrlService urlService;
    
    
    
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/shorten")
    public ModelAndView shortenUrl(@Valid @ModelAttribute ShortenUrlRequest request, @AuthenticationPrincipal User user) {
        if (user == null) {
            ModelAndView modelAndView = new ModelAndView("index");
            modelAndView.addObject("error", "User must be authenticated to shorten URLs.");
            modelAndView.addObject("urls", urlRepository.findByUser(user));
            return modelAndView;
        }
        try {
            urlService.shortenUrl(request.getUrl(), user);
            return new ModelAndView("redirect:/dashboard");
        } catch (IllegalArgumentException e) {
            ModelAndView modelAndView = new ModelAndView("index");
            modelAndView.addObject("error", e.getMessage());
            modelAndView.addObject("urls", urlRepository.findByUser(user));
            return modelAndView;
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
    
    
}