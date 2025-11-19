# 🎯 Guide d'utilisation - Application Convention

## 🚀 Démarrage de l'application

```bash
# 1. Démarrer le backend
./mvnw

# 2. Démarrer le frontend (nouveau terminal)
./npmw start

# 3. Accéder à l'application
http://localhost:8080
```

## 📋 Fonctionnalités disponibles

### 🏠 **Tableau de bord** (`/dashboard`)

- Vue d'ensemble des KPIs
- Actions rapides (génération factures, nouveaux clients/conventions)
- Navigation vers toutes les sections

### 📊 **Statistiques** (`/statistiques`)

- Graphiques des conventions par mois
- Chiffre d'affaires mensuel
- KPIs en temps réel

### 👥 **Gestion des Clients** (`/client`)

- ✅ Créer, modifier, supprimer des clients
- ✅ Liste avec recherche et pagination

### 📄 **Gestion des Conventions** (`/convention`)

- ✅ Créer des conventions (statut BROUILLON)
- ✅ **Workflow complet** :
  - 🟡 BROUILLON → 🟢 ACTIVE (bouton ▶️)
  - 🟢 ACTIVE → 🟠 SUSPENDUE (bouton ⏸️)
  - 🟠 SUSPENDUE → 🟢 ACTIVE (bouton ▶️)
- ✅ **Génération PDF** avec QR code (bouton 📄)
- ✅ **Génération automatique de factures** (bouton 🧾)

### 🧾 **Gestion des Factures** (`/facture`)

- ✅ Liste des factures générées
- ✅ Détails des factures (`/detail-facture`)
- ✅ Génération automatique depuis conventions actives

### 💳 **Gestion des Paiements** (`/payment`)

- ✅ Enregistrer les paiements
- ✅ Modes : ESPECES, CHEQUE, VIREMENT, CARTE_BANCAIRE
- ✅ Lien avec les factures

## 🔄 Workflow complet d'utilisation

### 1. **Créer un client**

```
Menu → Entités → Client → Créer
```

### 2. **Créer une convention**

```
Menu → Entités → Convention → Créer
- Sélectionner le client
- Statut automatique : BROUILLON
```

### 3. **Activer la convention**

```
Liste des conventions → Bouton ▶️ (Activer)
```

### 4. **Générer une facture**

```
Liste des conventions → Bouton 🧾 (Générer facture)
OU
Tableau de bord → "Générer toutes les factures"
```

### 5. **Enregistrer un paiement**

```
Menu → Entités → Paiements → Nouveau
- Sélectionner la facture
- Montant et mode de paiement
```

### 6. **Télécharger le PDF**

```
Liste des conventions → Bouton 📄 (PDF)
```

## 🎛️ Actions disponibles par statut

| Statut       | Actions disponibles                      |
| ------------ | ---------------------------------------- |
| 🟡 BROUILLON | ▶️ Activer, ✏️ Modifier, 📄 PDF          |
| 🟢 ACTIVE    | ⏸️ Suspendre, 🧾 Générer facture, 📄 PDF |
| 🟠 SUSPENDUE | ▶️ Réactiver, 📄 PDF                     |

## 📱 Navigation rapide

- **Tableau de bord** : Vue d'ensemble + actions rapides
- **Statistiques** : Graphiques et métriques
- **Menu Entités** : Accès à toutes les données
- **Actions directes** : Boutons dans les listes pour workflow

## 🔧 APIs disponibles

### Workflow des conventions

```bash
PUT /api/convention-workflow/{id}/activate
PUT /api/convention-workflow/{id}/suspend
PUT /api/convention-workflow/{id}/reactivate
```

### Génération de factures

```bash
POST /api/facture-generation/convention/{id}
POST /api/facture-generation/active-conventions
```

### PDF avec QR code

```bash
GET /api/pdf/convention/{id}
```

### Statistiques

```bash
GET /api/statistiques/dashboard
```

## ✨ Fonctionnalités automatiques

- 🔢 **Numérotation automatique** des factures
- 📊 **Calculs automatiques** TVA/TTC
- 📈 **Mise à jour temps réel** des statistiques
- 🔄 **Workflow sécurisé** avec validations
- 📱 **Interface responsive** pour tous écrans

L'application est maintenant complète avec toutes les fonctionnalités demandées ! 🎉
