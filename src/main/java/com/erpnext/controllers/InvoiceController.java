package com.erpnext.controllers;

import com.erpnext.dto.InvoiceDTO;
import com.erpnext.dto.InvoiceDetailDTO;
import com.erpnext.dto.Utilisateur;
import com.erpnext.services.ErpNextService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class InvoiceController {

    @Autowired
    private ErpNextService erpNextService;

    @GetMapping("/invoices")
    public String afficherListeFactures(HttpSession session, Model model) {

        Utilisateur utilisateur = (Utilisateur) session.getAttribute("utilisateur");

        if (utilisateur == null) {
            return "redirect:/";
        }

        List<InvoiceDTO> factures = erpNextService.getAllInvoices();
        model.addAttribute("factures", factures);
        model.addAttribute("utilisateur", utilisateur);
        return "factures";
    }

    @GetMapping("/invoices/{name}")
    public String afficherDetailFacture(@PathVariable String name, Model model, HttpSession session) {

        Utilisateur utilisateur = (Utilisateur) session.getAttribute("utilisateur");

        if (utilisateur == null) {
            return "redirect:/";
        }

        InvoiceDetailDTO details = erpNextService.getInvoiceDetails(name);
        model.addAttribute("facture", details);
        model.addAttribute("utilisateur", utilisateur);
        return "facture-detail";
    }

    @PostMapping("/pay")
    public String payInvoice(
            @RequestParam String invoiceName,
            @RequestParam String supplier,
            @RequestParam double amount,
            RedirectAttributes redirectAttributes
    ) {
        System.out.println("Tonga eto");
        try {
            // Tu peux fixer le mode de paiement et le compte ici ou les rendre dynamiques
            erpNextService.payInvoice(invoiceName, amount, supplier, "Cash", "Creditors - RD", 0.0F);
            redirectAttributes.addFlashAttribute("message", "Paiement effectué pour la facture " + invoiceName);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Échec du paiement : " + e.getMessage());
        }
        return "redirect:/invoices";
    }

}
