package com.convention.domain;

import com.convention.domain.enumeration.QualiteSignature;
import com.convention.domain.enumeration.TypeInstitution;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ClientEntity.
 */
@Entity
@Table(name = "client")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ClientEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "num_client", nullable = false, unique = true)
    private Long numClient;

    @NotNull
    @Size(max = 100)
    @Column(name = "nom_client", length = 100, nullable = false)
    private String nomClient;

    @Size(max = 255)
    @Column(name = "adresse_client", length = 255)
    private String adresseClient;

    @Pattern(regexp = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")
    @Column(name = "email_client")
    private String emailClient;

    @Size(max = 20)
    @Column(name = "whats_client", length = 20)
    private String whatsClient;

    @Lob
    @Column(name = "obs_client")
    private String obsClient;

    @Column(name = "date_creation")
    private Instant dateCreation;

    @NotNull
    @Column(name = "actif", nullable = false)
    private Boolean actif;

    @Enumerated(EnumType.STRING)
    @Column(name = "type_institution")
    private TypeInstitution typeInstitution;

    @Size(max = 50)
    @Column(name = "nif", length = 50)
    private String nif;

    @Size(max = 50)
    @Column(name = "rc", length = 50)
    private String rc;

    @Size(max = 150)
    @Column(name = "nom_representant", length = 150)
    private String nomRepresentant;

    @Size(max = 100)
    @Column(name = "fonction_representant", length = 100)
    private String fonctionRepresentant;

    @Enumerated(EnumType.STRING)
    @Column(name = "qualite_signature")
    private QualiteSignature qualiteSignature;

    @Size(max = 100)
    @Column(name = "wilaya", length = 100)
    private String wilaya;

    @Size(max = 100)
    @Column(name = "commune", length = 100)
    private String commune;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ClientEntity id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getNumClient() {
        return this.numClient;
    }

    public ClientEntity numClient(Long numClient) {
        this.setNumClient(numClient);
        return this;
    }

    public void setNumClient(Long numClient) {
        this.numClient = numClient;
    }

    public String getNomClient() {
        return this.nomClient;
    }

    public ClientEntity nomClient(String nomClient) {
        this.setNomClient(nomClient);
        return this;
    }

    public void setNomClient(String nomClient) {
        this.nomClient = nomClient;
    }

    public String getAdresseClient() {
        return this.adresseClient;
    }

    public ClientEntity adresseClient(String adresseClient) {
        this.setAdresseClient(adresseClient);
        return this;
    }

    public void setAdresseClient(String adresseClient) {
        this.adresseClient = adresseClient;
    }

    public String getEmailClient() {
        return this.emailClient;
    }

    public ClientEntity emailClient(String emailClient) {
        this.setEmailClient(emailClient);
        return this;
    }

    public void setEmailClient(String emailClient) {
        this.emailClient = emailClient;
    }

    public String getWhatsClient() {
        return this.whatsClient;
    }

    public ClientEntity whatsClient(String whatsClient) {
        this.setWhatsClient(whatsClient);
        return this;
    }

    public void setWhatsClient(String whatsClient) {
        this.whatsClient = whatsClient;
    }

    public String getObsClient() {
        return this.obsClient;
    }

    public ClientEntity obsClient(String obsClient) {
        this.setObsClient(obsClient);
        return this;
    }

    public void setObsClient(String obsClient) {
        this.obsClient = obsClient;
    }

    public Instant getDateCreation() {
        return this.dateCreation;
    }

    public ClientEntity dateCreation(Instant dateCreation) {
        this.setDateCreation(dateCreation);
        return this;
    }

    public void setDateCreation(Instant dateCreation) {
        this.dateCreation = dateCreation;
    }

    public Boolean getActif() {
        return this.actif;
    }

    public ClientEntity actif(Boolean actif) {
        this.setActif(actif);
        return this;
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

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ClientEntity)) {
            return false;
        }
        return getId() != null && getId().equals(((ClientEntity) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ClientEntity{" +
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
