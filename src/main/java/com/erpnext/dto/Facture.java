package com.erpnext.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

/**
 * Modèle pour une facture
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Facture {
    private String id;
    private String reference;
    private String fournisseur;
    private LocalDate dateEmission;
    private LocalDate dateEcheance;
    private String statut;
    private double montantTotal;
    private boolean estPayee;
    private LocalDate datePaiement;
    private List<LigneFacture> lignes;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LigneFacture {
        private String description;
        private int quantite;
        private double prixUnitaire;
        private double montant;
    }
}
