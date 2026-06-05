# Guide d'utilisation - Scanner PDF Convention

## Vue d'ensemble

La fonctionnalité de scan PDF permet d'extraire automatiquement les informations d'une convention depuis un fichier PDF et de créer une nouvelle convention dans le système.

## Fonctionnalités

### 1. Scan automatique des données

- **Extraction de texte** : Analyse le contenu textuel du PDF
- **Lecture QR Code** : Détecte et décode les QR codes présents dans le PDF
- **Reconnaissance de patterns** : Identifie automatiquement les champs de convention

### 2. Données extraites

- Numéro de convention
- Date de signature
- Date de début
- Date d'échéance
- Montant de la redevance
- Nom du responsable
- Statut de la convention
- Informations client

## Comment utiliser

### Étape 1 : Accéder au scanner

1. Depuis le **tableau de bord** : cliquez sur "Scanner PDF Convention"
2. Depuis la **liste des conventions** : cliquez sur "Scanner PDF"
3. URL directe : `/convention/scan`

### Étape 2 : Uploader le PDF

1. Cliquez sur "Sélectionner un fichier PDF"
2. Choisissez votre fichier PDF (format .pdf uniquement)
3. Cliquez sur "Scanner le PDF"

### Étape 3 : Vérifier les données

1. Les informations extraites s'affichent automatiquement
2. Vérifiez la précision des données détectées
3. Choisissez une action :
   - **Créer la Convention** : Crée directement avec les données extraites
   - **Modifier avant création** : Ouvre le formulaire de création pré-rempli

## Types de PDF supportés

### PDF avec QR Code

- **Priorité haute** : Les QR codes sont traités en premier
- **Format attendu** : `Convention-{numéro}-{id}`
- **Avantage** : Extraction plus précise et rapide

### PDF avec texte structuré

- **Patterns reconnus** :
  - `CONVENTION N° {numéro}`
  - `Date de signature: {date}`
  - `Date de début: {date}`
  - `Date d'échéance: {date}`
  - `Redevance: {montant} MRU`
  - `Responsable: {nom}`
  - `Statut: {statut}`
  - `Client: {nom_client}`

### Formats de date supportés

- `dd/MM/yyyy` (ex: 15/03/2024)
- `dd-MM-yyyy` (ex: 15-03-2024)
- `yyyy-MM-dd` (ex: 2024-03-15)

## Conseils pour de meilleurs résultats

### 1. Qualité du PDF

- Utilisez des PDFs avec du texte sélectionnable (pas des images scannées)
- Assurez-vous que le texte est lisible et bien structuré
- Évitez les PDFs avec des polices trop stylisées

### 2. Structure recommandée

```
CONVENTION N° 12345

Date de signature: 15/03/2024
Date de début: 01/04/2024
Date d'échéance: 31/03/2025
Redevance: 50000 MRU
Responsable: Ahmed Mohamed
Statut: ACTIVE
Client: Société ABC
```

### 3. QR Code (optionnel)

- Placez le QR code sur la première page
- Assurez-vous qu'il soit bien visible et non déformé
- Le QR code doit contenir : `Convention-{numéro}-{id}`

## Gestion des erreurs

### Erreurs communes

1. **"Fichier non valide"** : Vérifiez que c'est bien un PDF
2. **"Aucune donnée extraite"** : Le PDF ne contient pas de texte reconnaissable
3. **"Client non trouvé"** : Le nom du client ne correspond à aucun client existant

### Solutions

1. **Vérifiez le format** : Seuls les fichiers .pdf sont acceptés
2. **Contrôlez le contenu** : Le PDF doit contenir du texte, pas seulement des images
3. **Créez le client** : Ajoutez d'abord le client dans le système si nécessaire

## Sécurité

- Les fichiers PDF ne sont pas stockés sur le serveur
- Seules les données extraites sont conservées
- L'upload est limité aux utilisateurs authentifiés
- Validation des types de fichiers côté serveur

## API Endpoint

```
POST /api/pdf/scan
Content-Type: multipart/form-data
Parameter: file (PDF file)
Response: ConventionDTO
```

## Limitations

- Taille maximale du fichier : définie par la configuration Spring Boot
- Formats supportés : PDF uniquement
- Langues : Français (patterns de reconnaissance)
- Encoding : UTF-8

## Support technique

En cas de problème :

1. Vérifiez les logs de l'application
2. Testez avec un PDF simple et bien structuré
3. Contactez l'équipe de développement avec un exemple de PDF problématique
