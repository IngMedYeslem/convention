package com.convention.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Table(name = "convention_document")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ConventionDocumentEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 255)
    @Column(name = "nom_fichier", length = 255, nullable = false)
    private String nomFichier;

    @NotNull
    @Size(max = 100)
    @Column(name = "type_document", length = 100, nullable = false)
    private String typeDocument;

    @Size(max = 100)
    @Column(name = "content_type", length = 100)
    private String contentType;

    @NotNull
    @Column(name = "chemin_fichier", length = 500, nullable = false)
    private String cheminFichier;

    @Column(name = "taille_fichier")
    private Long tailleFichier;

    @Column(name = "date_depot")
    private Instant dateDepot;

    @Size(max = 255)
    @Column(name = "depose_par", length = 255)
    private String deposePar;

    @Size(max = 500)
    @Column(name = "observations", length = 500)
    private String observations;

    @ManyToOne(optional = false)
    @NotNull
    private ConventionEntity convention;

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

    public ConventionEntity getConvention() {
        return convention;
    }

    public void setConvention(ConventionEntity convention) {
        this.convention = convention;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ConventionDocumentEntity)) return false;
        return getId() != null && getId().equals(((ConventionDocumentEntity) o).getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
