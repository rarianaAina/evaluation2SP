package com.erpnext.controllers;

import com.erpnext.dto.RateUpdateRequestDTO;
import com.erpnext.dto.SupplierQuotationDTO;
import com.erpnext.dto.Utilisateur;
import com.erpnext.services.ErpNextService;
import com.erpnext.services.SupplierQuotationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
public class SupplierQuotationController {

    @Autowired
    private ErpNextService erpNextService;

    @Autowired
    private SupplierQuotationService quotationService;

    @GetMapping("/quotations")
    public String viewQuotations(@RequestParam("supplier") String supplier, Model model, HttpSession session) {

        Utilisateur utilisateur = (Utilisateur) session.getAttribute("utilisateur");

        if (utilisateur == null) {
            return "redirect:/";
        }

        List<SupplierQuotationDTO> quotations = erpNextService.getQuotationsBySupplier(supplier);
        model.addAttribute("utilisateur", utilisateur);
        model.addAttribute("quotations", quotations);
        return "quotations";
    }

/*    @GetMapping("/quotations/{name}")
    public String showQuotationDetails(@PathVariable String name, Model model, HttpSession session) {

        Utilisateur utilisateur = (Utilisateur) session.getAttribute("utilisateur");

        if (utilisateur == null) {
            return "redirect:/";
        }

        SupplierQuotationDTO quotation = quotationService.getSupplierQuotationDetails(name);
        model.addAttribute("utilisateur", utilisateur);
        model.addAttribute("quotation", quotation);
        return "quotation-details";
    }*/

    @GetMapping("/quotations/{name}")
    public String showQuotationDetails(@PathVariable String name, Model model, HttpSession session) {

        Utilisateur utilisateur = (Utilisateur) session.getAttribute("utilisateur");
        String cookies = (String) session.getAttribute("cookies");

        if (utilisateur == null || cookies == null) {
            return "redirect:/";
        }

        SupplierQuotationDTO quotation = quotationService.getSupplierQuotationDetails(name, cookies);

        model.addAttribute("utilisateur", utilisateur);
        model.addAttribute("quotation", quotation);

        return "quotation-details";
    }

    @Autowired
    private SupplierQuotationService supplierQuotationService;
/*

    @PostMapping("/update-rate")
    public ResponseEntity<String> updateRate(@RequestBody RateUpdateRequestDTO dto,
                                             HttpServletRequest request) {
        String cookies = request.getHeader("Cookie");

        boolean updated = supplierQuotationService.updateItemRate(
                dto.getQuotationName(),
                dto.getItemCode(),
                dto.getNewRate(),
                cookies
        );

        if (updated) {
            return ResponseEntity.ok("Rate mis à jour avec succès !");
        } else {
            return ResponseEntity.status(500).body("Échec de la mise à jour du rate.");
        }
    }
*/

    @PostMapping("/update-rate")
    @ResponseBody
    public ResponseEntity<?> updateRate(@RequestBody RateUpdateRequestDTO dto) {
        boolean success = quotationService.updateItemRate(dto.getQuotationName(), dto.getItemCode(), dto.getNewRate(),  dto.getItemName(),dto.getQty(), dto.getUom());
        return success ? ResponseEntity.ok(Map.of("success", true)) :
                ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "Échec de mise à jour"));
    }



}
