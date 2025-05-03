package com.erpnext.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Modèle pour un fournisseur
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Fournisseur {
    private String id;
    private String name;
    private String adresse;
    private String telephone;
    private String email;
    private String statut;
}