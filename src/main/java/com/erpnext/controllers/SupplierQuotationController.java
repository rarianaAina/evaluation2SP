package com.erpnext.controllers;

import com.erpnext.dto.SupplierQuotationDTO;
import com.erpnext.services.ErpNextService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class SupplierQuotationController {

    @Autowired
    private ErpNextService erpNextService;

    @GetMapping("/quotations")
    public String viewQuotations(Model model) {
        String supplier = "Zuckerman Security Ltd.";
        List<SupplierQuotationDTO> quotations = erpNextService.getQuotationsBySupplier(supplier);
        model.addAttribute("quotations", quotations);
        return "quotations";
    }
}
