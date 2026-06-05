package com.convention.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

public class ConventionDocumentDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 255)
    private String nomFichier;

    @NotNull
    @Size(max = 100)
    private String typeDocument;

    @Size(max = 100)
    private String contentType;

    @NotNull
    private String cheminFichier;

    private Long tailleFichier;

    private Instant dateDepot;

    @Size(max = 255)
    private String deposePar;

    @Size(max = 500)
    private String observations;

    @NotNull
    private ConventionDTO convention;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomFichier() {
        return nomFichier;
    }

    public void setNomFichier(String nomFichier) {
        this.nomFichier = nomFichier;
    }

    public String getTypeDocument() {
        return typeDocument;
    }

    public void setTypeDocument(String typeDocument) {
        this.typeDocument = typeDocument;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getCheminFichier() {
        return cheminFichier;
    }

    public void setCheminFichier(String cheminFichier) {
        this.cheminFichier = cheminFichier;
    }

    public Long getTailleFichier() {
        return tailleFichier;
    }

    public void setTailleFichier(Long tailleFichier) {
        this.tailleFichier = tailleFichier;
    }

    public Instant getDateDepot() {
        return dateDepot;
    }

    public void setDateDepot(Instant dateDepot) {
        this.dateDepot = dateDepot;
    }

    public String getDeposePar() {
        return deposePar;
    }

    public void setDeposePar(String deposePar) {
        this.deposePar = deposePar;
    }

    public String getObservations() {
        return observations;
    }

    public void setObservations(String observations) {
        this.observations = observations;
    }

    public ConventionDTO getConvention() {
        return convention;
    }

    public void setConvention(ConventionDTO convention) {
        this.convention = convention;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ConventionDocumentDTO)) return false;
        ConventionDocumentDTO that = (ConventionDocumentDTO) o;
        if (this.id == null) return false;
        return Objects.equals(this.id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }
}
