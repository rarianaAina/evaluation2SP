package com.erpnext.controllers;

import com.erpnext.dto.Fournisseur;
import com.erpnext.dto.Utilisateur;
import com.erpnext.services.FournisseurService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeController {

    @Autowired
    FournisseurService fournisseurService;

    @GetMapping("/dashboard")
    public String home(HttpSession session, Model model) {

        Utilisateur utilisateur = (Utilisateur) session.getAttribute("utilisateur");

        if (utilisateur == null) {
            return "redirect:/";
        }

        List<Fournisseur> fournisseurs = fournisseurService.getFournisseurs(utilisateur.getCookies());

        model.addAttribute("utilisateur", utilisateur);
        model.addAttribute("fournisseurs", fournisseurs);
        //model.addAttribute("message", "Bienvenue sur la page d'accueil !");
        return "home";
    }
}

