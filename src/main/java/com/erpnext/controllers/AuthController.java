package com.erpnext.controllers;

import com.erpnext.dto.LoginRequest;
import com.erpnext.services.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/login")
    public String showLoginForm() {
        return "login";  // Affiche la page de connexion
    }

    @PostMapping("/login")
    public String login(@ModelAttribute LoginRequest request, HttpServletResponse response, Model model) {
        try {
            String sessionCookie = authService.loginToErpNext(request.getUsername(), request.getPassword());
            return "redirect:/quotations";
        } catch (Exception e) {
            model.addAttribute("error", "Login échoué : " + e.getMessage());
            return "login"; // retourne la page de login avec erreur
        }
    }


}
