package com.example.urlshortener.controller;

import com.example.urlshortener.repository.UrlRepository;
import com.example.urlshortener.model.Url;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

@Controller
public class ViewController {

    private final UrlRepository urlRepository;

    public ViewController(UrlRepository urlRepository) {
        this.urlRepository = urlRepository;
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/dashboard")
    public ModelAndView index(@AuthenticationPrincipal com.example.urlshortener.model.User user) {
        List<Url> urls = urlRepository.findByUser(user);
        ModelAndView modelAndView = new ModelAndView("index");
        modelAndView.addObject("urls", urls);
        return modelAndView;
        // return "redirect:/index.html"; // Uncomment if you want to redirect to a static HTML file
    }
}
