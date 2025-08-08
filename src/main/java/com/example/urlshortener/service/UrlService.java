package com.example.urlshortener.service;

import com.example.urlshortener.model.Url;
import com.example.urlshortener.model.User;
import com.example.urlshortener.repository.UrlRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class UrlService {
    
    @Autowired
    private UrlRepository urlRepository;
    
    private static final String CHARSET = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int SHORT_CODE_LENGTH = 6;
    
    public Url shortenUrl(String originalUrl, User user) {
        // Validate URL
        if (!isValidUrl(originalUrl)) {
            throw new IllegalArgumentException("Invalid URL");
        }
        String shortCode = generateUniqueShortCode();
        Url url = new Url(originalUrl, shortCode, user);
        return urlRepository.save(url);
    }
    
    public String getOriginalUrl(String shortCode) {
        Optional<Url> url = urlRepository.findByShortCode(shortCode);
        if (url.isPresent()) {
            // Increment click count
            Url urlEntity = url.get();
            urlEntity.setClickCount(urlEntity.getClickCount() + 1);
            urlRepository.save(urlEntity);
            return urlEntity.getOriginalUrl();
        }
        return null;
    }
    
    public List<Url> getAllUrls() {
        return urlRepository.findAll();
    }
    
    private String generateUniqueShortCode() {
        String shortCode;
        do {
            shortCode = generateRandomString();
        } while (urlRepository.existsByShortCode(shortCode));
        return shortCode;
    }
    
    private String generateRandomString() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < SHORT_CODE_LENGTH; i++) {
            sb.append(CHARSET.charAt(random.nextInt(CHARSET.length())));
        }
        return sb.toString();
    }
    
    private boolean isValidUrl(String url) {
        try {
            new URL(url);
            return url.startsWith("http://") || url.startsWith("https://");
        } catch (MalformedURLException e) {
            return false;
        }
    }
}