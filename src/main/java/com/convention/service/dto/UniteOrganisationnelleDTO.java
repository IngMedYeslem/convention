package com.convention.service.dto;

import com.convention.domain.enumeration.NiveauHierarchique;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;

public class UniteOrganisationnelleDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 150)
    private String nom;

    @Size(max = 30)
    private String code;

    @NotNull
    private NiveauHierarchique niveau;

    private Long parentId;

    private String parentNom;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public NiveauHierarchique getNiveau() {
        return niveau;
    }

    public void setNiveau(NiveauHierarchique niveau) {
        this.niveau = niveau;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getParentNom() {
        return parentNom;
    }

    public void setParentNom(String parentNom) {
        this.parentNom = parentNom;
    }

    @Override
    public String toString() {
        return "UniteOrganisationnelleDTO{id=" + id + ", nom='" + nom + "', niveau=" + niveau + "}";
    }
}
