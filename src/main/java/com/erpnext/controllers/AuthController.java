package com.erpnext.controllers;

import com.erpnext.dto.LoginRequest;
import com.erpnext.dto.Utilisateur;
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
@RequestMapping("/")
public class AuthController {

    private final AuthService authService;


    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping
    public String showLoginForm() {
        return "login";  // Affiche la page de connexion
    }

/*    @PostMapping("/login")
    public String login(@ModelAttribute LoginRequest request, HttpServletResponse response, Model model) {
        try {
            String sessionCookie = authService.loginToErpNext(request.getUsername(), request.getPassword());
            return "redirect:/quotations";
        } catch (Exception e) {
            model.addAttribute("error", "Login échoué : " + e.getMessage());
            return "login"; // retourne la page de login avec erreur
        }
    }*/

    @PostMapping("/auth/login")
    public String login(
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            HttpSession session,
            Model model) {

        Utilisateur utilisateur = authService.authentifier(username, password);

        if (utilisateur != null) {
            session.setAttribute("utilisateur", utilisateur);
            session.setAttribute("cookies", utilisateur.getCookies());

            return "redirect:/dashboard";
        } else {
            model.addAttribute("erreur", "Identifiants incorrects");
            return "login";
        }
    }


}
