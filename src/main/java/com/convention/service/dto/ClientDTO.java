package com.convention.service.dto;

import com.convention.domain.enumeration.QualiteSignature;
import com.convention.domain.enumeration.TypeInstitution;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.convention.domain.ClientEntity} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ClientDTO implements Serializable {

    private Long id;

    @NotNull
    private Long numClient;

    @NotNull
    @Size(max = 100)
    private String nomClient;

    @Size(max = 255)
    private String adresseClient;

    @Pattern(regexp = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")
    private String emailClient;

    @Size(max = 20)
    private String whatsClient;

    @Lob
    private String obsClient;

    private Instant dateCreation;

    @NotNull
    private Boolean actif;

    private TypeInstitution typeInstitution;

    @Size(max = 50)
    private String nif;

    @Size(max = 50)
    private String rc;

    @Size(max = 150)
    private String nomRepresentant;

    @Size(max = 100)
    private String fonctionRepresentant;

    private QualiteSignature qualiteSignature;

    @Size(max = 100)
    private String wilaya;

    @Size(max = 100)
    private String commune;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getNumClient() {
        return numClient;
    }

    public void setNumClient(Long numClient) {
        this.numClient = numClient;
    }

    public String getNomClient() {
        return nomClient;
    }

    public void setNomClient(String nomClient) {
        this.nomClient = nomClient;
    }

    public String getAdresseClient() {
        return adresseClient;
    }

    public void setAdresseClient(String adresseClient) {
        this.adresseClient = adresseClient;
    }

    public String getEmailClient() {
        return emailClient;
    }

    public void setEmailClient(String emailClient) {
        this.emailClient = emailClient;
    }

    public String getWhatsClient() {
        return whatsClient;
    }

    public void setWhatsClient(String whatsClient) {
        this.whatsClient = whatsClient;
    }

    public String getObsClient() {
        return obsClient;
    }

    public void setObsClient(String obsClient) {
        this.obsClient = obsClient;
    }

    public Instant getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(Instant dateCreation) {
        this.dateCreation = dateCreation;
    }

    public Boolean getActif() {
        return actif;
    }

    public void setActif(Boolean actif) {
        this.actif = actif;
    }

    public TypeInstitution getTypeInstitution() {
        return typeInstitution;
    }

    public void setTypeInstitution(TypeInstitution typeInstitution) {
        this.typeInstitution = typeInstitution;
    }

    public String getNif() {
        return nif;
    }

    public void setNif(String nif) {
        this.nif = nif;
    }

    public String getRc() {
        return rc;
    }

    public void setRc(String rc) {
        this.rc = rc;
    }

    public String getNomRepresentant() {
        return nomRepresentant;
    }

    public void setNomRepresentant(String nomRepresentant) {
        this.nomRepresentant = nomRepresentant;
    }

    public String getFonctionRepresentant() {
        return fonctionRepresentant;
    }

    public void setFonctionRepresentant(String fonctionRepresentant) {
        this.fonctionRepresentant = fonctionRepresentant;
    }

    public QualiteSignature getQualiteSignature() {
        return qualiteSignature;
    }

    public void setQualiteSignature(QualiteSignature qualiteSignature) {
        this.qualiteSignature = qualiteSignature;
    }

    public String getWilaya() {
        return wilaya;
    }

    public void setWilaya(String wilaya) {
        this.wilaya = wilaya;
    }

    public String getCommune() {
        return commune;
    }

    public void setCommune(String commune) {
        this.commune = commune;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ClientDTO)) {
            return false;
        }

        ClientDTO clientDTO = (ClientDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, clientDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ClientDTO{" +
            "id=" + getId() +
            ", numClient=" + getNumClient() +
            ", nomClient='" + getNomClient() + "'" +
            ", adresseClient='" + getAdresseClient() + "'" +
            ", emailClient='" + getEmailClient() + "'" +
            ", whatsClient='" + getWhatsClient() + "'" +
            ", obsClient='" + getObsClient() + "'" +
            ", dateCreation='" + getDateCreation() + "'" +
            ", actif='" + getActif() + "'" +
            "}";
    }
}
