package com.example.urlshortener.controller;

import com.example.urlshortener.repository.UrlRepository;
import com.example.urlshortener.model.Url;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ViewController {

    private final UrlRepository urlRepository;

    public ViewController(UrlRepository urlRepository) {
        this.urlRepository = urlRepository;
    }

    @GetMapping("/")
    public ModelAndView index() {
        List   <Url> urls = urlRepository.findAll();
        ModelAndView modelAndView = new ModelAndView("index");
        modelAndView.addObject("urls", urls);
        return modelAndView;
        // return "redirect:/index.html"; // Uncomment if you want to redirect to a static HTML file
    }
}
