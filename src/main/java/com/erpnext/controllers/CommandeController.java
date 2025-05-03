package com.erpnext.controllers;

import com.erpnext.dto.PurchaseOrderDTO;
import com.erpnext.dto.Utilisateur;
import com.erpnext.services.ErpNextService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class CommandeController {

    @Autowired
    private ErpNextService erpNextService;

    @GetMapping("/orders")
    public String afficherCommandesParFournisseur(@RequestParam("supplier") String supplier, Model model, HttpSession session) {

        Utilisateur utilisateur = (Utilisateur) session.getAttribute("utilisateur");

        if (utilisateur == null) {
            return "redirect:/";
        }

        List<PurchaseOrderDTO> commandes = erpNextService.getOrdersBySupplier(supplier);
        model.addAttribute("commandes", commandes);
        model.addAttribute("supplier", supplier);
        model.addAttribute("utilisateur", utilisateur);
        return "commandes"; // le nom du fichier Thymeleaf (ex: commandes.html)
    }
}
