package com.example.urlshortener.controller;



import com.example.urlshortener.dto.UserRegistrationDto;
import com.example.urlshortener.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthController {

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String home() {
        return "home";
    }

    @GetMapping("/login")
    public String login(Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            return "redirect:/dashboard";
        }
        return "login";
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new UserRegistrationDto());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute("user") UserRegistrationDto userDto,
                              BindingResult result, 
                              Model model,
                              RedirectAttributes redirectAttributes) {
        
        // Check for validation errors
        if (result.hasErrors()) {
            return "register";
        }

        // Check if passwords match
        if (!userDto.getPassword().equals(userDto.getConfirmPassword())) {
            model.addAttribute("passwordError", "Passwords do not match");
            return "register";
        }

        try {
            userService.registerUser(userDto);
            redirectAttributes.addFlashAttribute("successMessage", "Registration successful! Please login.");
            return "redirect:/login";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "register";
        }
    }

    // @GetMapping("/dashboard")
    // public String dashboard(Authentication authentication, Model model) {
    //     model.addAttribute("username", authentication.getName());
    //     model.addAttribute("authorities", authentication.getAuthorities());
    //     return "dashboard";
    // }

    @GetMapping("/admin")
    public String admin(Model model) {
        model.addAttribute("message", "Welcome to Admin Panel");
        return "admin";
    }
}
