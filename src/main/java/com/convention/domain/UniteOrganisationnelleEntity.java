package com.convention.domain;

import com.convention.domain.enumeration.NiveauHierarchique;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;

@Entity
@Table(name = "unite_organisationnelle")
public class UniteOrganisationnelleEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(max = 150)
    @Column(name = "nom", length = 150, nullable = false)
    private String nom;

    @Size(max = 30)
    @Column(name = "code", length = 30, unique = true)
    private String code;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "niveau", nullable = false)
    private NiveauHierarchique niveau;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private UniteOrganisationnelleEntity parent;

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

    public UniteOrganisationnelleEntity getParent() {
        return parent;
    }

    public void setParent(UniteOrganisationnelleEntity parent) {
        this.parent = parent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UniteOrganisationnelleEntity)) return false;
        return id != null && id.equals(((UniteOrganisationnelleEntity) o).id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "UniteOrganisationnelleEntity{id=" + id + ", nom='" + nom + "', niveau=" + niveau + "}";
    }
}
