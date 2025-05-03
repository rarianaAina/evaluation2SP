package com.erpnext.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

/**
 * Modèle pour une demande de devis
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DemandeDevis {
    private String id;
    private String nomFournisseur;
    private LocalDate dateCreation;
    private String statut;
    private List<ArticleDevis> articles;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ArticleDevis {
        private String id;
        private String nom;
        private String description;
        private int quantite;
        private double prix;
        private double prixTotal;
    }
}