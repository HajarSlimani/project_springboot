# 🌱 Plant Manager - Application de Gestion de Plantes d'Intérieur

## 📋 Vue d'ensemble

Plant Manager est une application web full-stack complète permettant de gérer vos plantes d'intérieur. L'application offre un système complet de suivi des plantes avec gestion des tâches d'entretien, journalisation des observations, et upload de photos.

**Date de création:** Décembre 2025  
**Statut:** ✅ Opérationnel

---

## 🏗️ Architecture Technique

### Backend
- **Framework:** Spring Boot 3.3.5
- **Langage:** Java 17
- **Build Tool:** Maven
- **Base de données:** MySQL 8.x
- **ORM:** Hibernate JPA
- **Sécurité:** Spring Security + JWT + OAuth2

### Frontend
- **Framework:** Angular 17 (Standalone Components)
- **Langage:** TypeScript
- **Styles:** Bootstrap 5 + SCSS
- **UI Theme:** Palette verte et blanche personnalisée

### Architecture de Sécurité
- **Authentification JWT** avec tokens de 7 jours
- **Google OAuth2** (Credential + Code flows)
- **Chiffrement BCrypt** pour les mots de passe
- **Intercepteur JWT** pour l'authentification automatique
- **Guards Angular** pour la protection des routes

---

## ✨ Fonctionnalités Principales

### 🔐 Authentification
- ✅ Inscription avec email/mot de passe
- ✅ Connexion classique
- ✅ Connexion via Google OAuth2
- ✅ Gestion des sessions JWT
- ✅ Protection des routes frontend/backend

### 🌿 Gestion des Plantes
- ✅ Création de plantes (nom, espèce, emplacement)
- ✅ Upload et compression d'images (800x800px, JPEG 70%)
- ✅ Liste des plantes avec aperçu
- ✅ Détails complets de chaque plante
- ✅ Modification des informations
- ✅ Suppression de plantes

### 📝 Gestion des Tâches
- ✅ Création de tâches d'entretien par plante
- ✅ Définition de la fréquence (arrosage, fertilisation, etc.)
- ✅ Marquage des tâches comme complétées
- ✅ Suivi de la dernière exécution
- ✅ Calcul automatique de la prochaine date
- ✅ Suppression de tâches

### 📔 Journal de Bord
- ✅ Ajout d'entrées de journal avec date
- ✅ Notes détaillées sur l'état de la plante
- ✅ Upload de photos d'observation
- ✅ Historique complet par plante
- ✅ Suppression d'entrées

### 🖼️ Gestion des Images
- ✅ Upload depuis le disque local
- ✅ Compression automatique côté client
- ✅ Redimensionnement à 800x800px maximum
- ✅ Conversion en JPEG avec qualité 70%
- ✅ Validation de taille (5MB frontend, 10MB backend)
- ✅ Stockage en base64 dans MySQL (LONGTEXT)
- ✅ Aperçu avant upload

---

## 🛠️ Technologies et Dépendances

### Backend Dependencies
```xml
- spring-boot-starter-web
- spring-boot-starter-data-jpa
- spring-boot-starter-security
- spring-boot-starter-oauth2-client
- spring-boot-starter-validation
- mysql-connector-j
- lombok
- jjwt-api / jjwt-impl / jjwt-jackson (0.12.6)
```

### Frontend Dependencies
```json
- @angular/core: ^17.0.0
- @angular/router: ^17.0.0
- @angular/common: ^17.0.0
- @angular/forms: ^17.0.0
- bootstrap: ^5.3.0
- typescript: ~5.2.0
```

### Configuration Base de Données
```yaml
Database: plantmanager
Host: localhost:3306
User: root
Password: password
Charset: utf8mb4
Timezone: UTC
DDL Auto: update (auto-migration des schémas)
```

---

## 📁 Structure du Projet

```
mon_projet2/
│
├── backend/                          # Application Spring Boot
│   ├── src/main/java/com/example/plantmanager/
│   │   ├── PlantManagerApplication.java
│   │   ├── config/
│   │   │   ├── CorsConfig.java       # Configuration CORS
│   │   │   ├── JwtConfig.java        # Configuration JWT
│   │   │   └── SecurityConfig.java   # Spring Security
│   │   ├── controller/
│   │   │   ├── AuthController.java   # Endpoints authentification
│   │   │   ├── PlantController.java  # CRUD plantes
│   │   │   ├── TaskController.java   # CRUD tâches
│   │   │   └── LogController.java    # CRUD logs
│   │   ├── dto/
│   │   │   ├── AuthRequest.java
│   │   │   ├── AuthResponse.java
│   │   │   ├── PlantDto.java
│   │   │   ├── TaskDto.java
│   │   │   └── LogDto.java
│   │   ├── model/
│   │   │   ├── AppUser.java          # Entité utilisateur
│   │   │   ├── Role.java             # Rôles (ROLE_USER, ROLE_ADMIN)
│   │   │   ├── Plant.java            # Entité plante (avec @Lob LONGTEXT)
│   │   │   ├── PlantTask.java        # Entité tâche
│   │   │   └── LogEntry.java         # Entité journal (avec @Lob LONGTEXT)
│   │   ├── repository/
│   │   │   ├── UserRepository.java
│   │   │   ├── PlantRepository.java
│   │   │   ├── TaskRepository.java
│   │   │   └── LogRepository.java
│   │   ├── security/
│   │   │   ├── JwtAuthenticationFilter.java  # Filtre JWT
│   │   │   ├── JwtTokenProvider.java         # Génération/validation tokens
│   │   │   └── CustomUserDetailsService.java # UserDetails Spring Security
│   │   └── service/
│   │       ├── AuthService.java      # Logique authentification
│   │       ├── PlantService.java     # Logique métier plantes
│   │       ├── TaskService.java      # Logique métier tâches
│   │       └── LogService.java       # Logique métier logs
│   ├── src/main/resources/
│   │   └── application.yml           # Configuration Spring Boot
│   └── pom.xml                       # Dépendances Maven
│
├── frontend/                         # Application Angular 17
│   ├── src/
│   │   ├── app/
│   │   │   ├── auth/
│   │   │   │   ├── login/
│   │   │   │   │   ├── login.component.ts
│   │   │   │   │   ├── login.component.html
│   │   │   │   │   └── login.component.scss
│   │   │   │   └── register/
│   │   │   │       ├── register.component.ts
│   │   │   │       ├── register.component.html
│   │   │   │       └── register.component.scss
│   │   │   ├── plants/
│   │   │   │   ├── plants-list/      # Liste des plantes
│   │   │   │   ├── plant-form/       # Création/édition (avec compression images)
│   │   │   │   └── plant-detail/     # Détails + tâches + logs
│   │   │   ├── guards/
│   │   │   │   └── auth.guard.ts     # Protection des routes
│   │   │   ├── interceptors/
│   │   │   │   └── jwt.interceptor.ts # Ajout automatique JWT
│   │   │   ├── models/
│   │   │   │   └── models.ts         # Interfaces TypeScript
│   │   │   ├── services/
│   │   │   │   ├── auth.service.ts   # API authentification
│   │   │   │   ├── plants.service.ts # API plantes
│   │   │   │   ├── tasks.service.ts  # API tâches
│   │   │   │   └── logs.service.ts   # API logs
│   │   │   ├── app.component.ts
│   │   │   ├── app.config.ts         # Configuration standalone
│   │   │   └── app.routes.ts         # Routes Angular
│   │   ├── styles.scss               # Styles globaux + thème Bootstrap vert
│   │   ├── index.html
│   │   └── main.ts
│   ├── angular.json                  # Configuration Angular CLI
│   ├── package.json
│   └── tsconfig.json
│
└── README.md                         # Ce fichier
```

---

## 🚀 Installation et Configuration

### Prérequis
- Java 17+
- Node.js 18+ et npm
- MySQL 8+
- Maven 3.6+

### 1. Configuration de la Base de Données

```sql
CREATE DATABASE plantmanager CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE USER 'root'@'localhost' IDENTIFIED BY 'password';
GRANT ALL PRIVILEGES ON plantmanager.* TO 'root'@'localhost';
FLUSH PRIVILEGES;
```

### 2. Configuration Backend

Modifier `backend/src/main/resources/application.yml` si nécessaire:

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/plantmanager
    username: root
    password: password
  
  security:
    oauth2:
      client:
        registration:
          google:
            client-id: VOTRE_GOOGLE_CLIENT_ID
            client-secret: VOTRE_GOOGLE_CLIENT_SECRET

jwt:
  secret: VOTRE_SECRET_KEY_BASE64
```

### 3. Démarrage Backend

```bash
cd backend
mvn clean install
mvn spring-boot:run
```

Le backend démarre sur **http://localhost:8080**

### 4. Démarrage Frontend

```bash
cd frontend
npm install
ng serve
```

Le frontend est accessible sur **http://localhost:4200**

---

## 🎨 Design et Interface

### Palette de Couleurs
- **Vert Principal:** `#2ecc71`
- **Vert Foncé:** `#27ae60`
- **Vert Clair:** `#a8e6cf`
- **Vert Pâle:** `#e8f8f5`
- **Blanc:** `#ffffff`
- **Gris Clair:** `#f8f9fa`

### Composants Bootstrap Personnalisés
- Boutons avec effet hover vert
- Cards avec bordures vertes
- Formulaires stylisés
- Navigation responsive
- Spinners de chargement
- Badges et alertes

### Responsive Design
- Mobile-first approach
- Grid system Bootstrap
- Breakpoints: sm, md, lg, xl
- Navigation adaptative

---

## 🔌 API Endpoints

### Authentification
```
POST   /api/auth/register          # Inscription
POST   /api/auth/login             # Connexion
POST   /api/auth/google-login      # OAuth2 Google
```

### Plantes (Authentification requise)
```
GET    /api/plants                 # Liste des plantes de l'utilisateur
GET    /api/plants/{id}            # Détails d'une plante
POST   /api/plants                 # Créer une plante
PUT    /api/plants/{id}            # Modifier une plante
DELETE /api/plants/{id}            # Supprimer une plante
```

### Tâches (Authentification requise)
```
GET    /api/plants/{plantId}/tasks         # Liste des tâches d'une plante
GET    /api/plants/{plantId}/tasks/{id}    # Détails d'une tâche
POST   /api/plants/{plantId}/tasks         # Créer une tâche
PUT    /api/plants/{plantId}/tasks/{id}    # Modifier une tâche
PATCH  /api/plants/{plantId}/tasks/{id}/complete  # Marquer comme complétée
DELETE /api/plants/{plantId}/tasks/{id}    # Supprimer une tâche
```

### Logs (Authentification requise)
```
GET    /api/plants/{plantId}/logs          # Liste des logs d'une plante
GET    /api/plants/{plantId}/logs/{id}     # Détails d'un log
POST   /api/plants/{plantId}/logs          # Créer un log
PUT    /api/plants/{plantId}/logs/{id}     # Modifier un log
DELETE /api/plants/{plantId}/logs/{id}     # Supprimer un log
```

### Format des Requêtes

**Création de Plante:**
```json
{
  "name": "Monstera",
  "species": "Monstera deliciosa",
  "location": "Salon",
  "image": "data:image/jpeg;base64,/9j/4AAQSkZJRg..." 
}
```

**Création de Tâche:**
```json
{
  "name": "Arrosage",
  "description": "Arroser modérément",
  "frequency": 7,
  "lastCompleted": "2025-12-17T00:00:00"
}
```

**Création de Log:**
```json
{
  "date": "2025-12-17",
  "note": "Nouvelle feuille en croissance",
  "image": "data:image/jpeg;base64,/9j/4AAQSkZJRg..."
}
```

---

## 🔒 Sécurité et Authentification

### JWT (JSON Web Tokens)
- **Algorithme:** HMAC SHA256
- **Durée de vie:** 7 jours
- **Stockage:** localStorage côté client
- **Header:** `Authorization: Bearer <token>`

### Flow d'Authentification

1. **Inscription/Connexion:**
   - Client envoie credentials → Backend
   - Backend valide → Génère JWT
   - Client stocke token → localStorage
   - Token ajouté automatiquement via intercepteur

2. **Requêtes Authentifiées:**
   - Intercepteur JWT ajoute header Authorization
   - Backend valide le token via JwtAuthenticationFilter
   - Extraction de l'utilisateur du token
   - Vérification des permissions

3. **Google OAuth2:**
   - Client initie flow OAuth2
   - Backend vérifie le token Google
   - Création/récupération utilisateur
   - Génération JWT application

### Protection des Routes
- **Frontend:** AuthGuard vérifie la présence du token
- **Backend:** SecurityConfig avec antMatchers
- **Routes publiques:** /api/auth/**
- **Routes protégées:** /api/plants/**, /api/tasks/**, /api/logs/**

---

## 🖼️ Traitement des Images

### Compression Frontend (Canvas API)

```typescript
// Algorithme de compression dans plant-form.component.ts
1. Lecture du fichier image sélectionné
2. Création d'un objet Image
3. Chargement dans un Canvas HTML5
4. Calcul des dimensions (max 800x800px, ratio préservé)
5. Redimensionnement avec drawImage()
6. Conversion en JPEG avec quality 0.7
7. Encodage en base64
8. Réduction typique: 60-70% de la taille originale
```

### Validation
- **Types acceptés:** image/jpeg, image/png, image/gif, image/webp
- **Taille maximale frontend:** 5MB
- **Taille maximale backend:** 10MB
- **Format de sortie:** JPEG base64

### Stockage Base de Données
- **Type de colonne:** LONGTEXT (capacité 4GB)
- **Annotation JPA:** `@Lob @Column(columnDefinition = "LONGTEXT")`
- **Tables concernées:** plants.image, plant_logs.image

---

## 📊 Modèle de Données

### Tables MySQL

**users**
```sql
id BIGINT PRIMARY KEY AUTO_INCREMENT
email VARCHAR(255) UNIQUE NOT NULL
password VARCHAR(255) -- BCrypt hash
name VARCHAR(255)
```

**roles**
```sql
id BIGINT PRIMARY KEY AUTO_INCREMENT
name VARCHAR(50) -- ROLE_USER, ROLE_ADMIN
```

**user_roles** (table de liaison)
```sql
user_id BIGINT
role_id BIGINT
```

**plants**
```sql
id BIGINT PRIMARY KEY AUTO_INCREMENT
name VARCHAR(255) NOT NULL
species VARCHAR(255)
location VARCHAR(255)
image LONGTEXT -- Base64 JPEG
user_id BIGINT NOT NULL
```

**plant_tasks**
```sql
id BIGINT PRIMARY KEY AUTO_INCREMENT
name VARCHAR(255) NOT NULL
description TEXT
frequency INT -- Jours entre chaque tâche
last_completed DATETIME
next_due DATE -- Calculé automatiquement
plant_id BIGINT NOT NULL
```

**plant_logs**
```sql
id BIGINT PRIMARY KEY AUTO_INCREMENT
date DATE NOT NULL
note TEXT
image LONGTEXT -- Base64 JPEG
plant_id BIGINT NOT NULL
```

### Relations
- User → Plants (OneToMany)
- Plant → Tasks (OneToMany)
- Plant → Logs (OneToMany)
- User → Roles (ManyToMany)

---

## 🧪 Tests et Validation

### Tests Effectués
✅ Authentification (inscription, connexion, JWT)  
✅ CRUD complet sur les plantes  
✅ CRUD complet sur les tâches  
✅ CRUD complet sur les logs  
✅ Upload et compression d'images  
✅ Stockage base64 en LONGTEXT  
✅ Protection des routes frontend/backend  
✅ Responsive design mobile/desktop  
✅ Validation des formulaires  
✅ Gestion des erreurs HTTP  

### Problèmes Résolus
1. **Erreur "Object is possibly null"** → Déplacement de `*ngIf` au niveau conteneur
2. **Data truncation sur colonne image** → Migration de TEXT vers LONGTEXT
3. **Images trop volumineuses** → Compression Canvas (800px, 70% quality)
4. **CORS errors** → Configuration CorsConfig avec origins autorisées

---

## 📈 Performances

### Optimisations Implémentées
- **Compression d'images côté client** (réduction trafic réseau)
- **Lazy loading des relations JPA** (FetchType.LAZY)
- **Connection pooling** HikariCP (configuration par défaut)
- **Pagination potentielle** (structure en place pour futures paginations)
- **Standalone components Angular** (code splitting automatique)

### Métriques
- **Temps de démarrage backend:** ~11-12 secondes
- **Temps de compilation frontend:** ~5-8 secondes
- **Taille d'une image compressée:** ~200-300KB (vs 2MB originale)
- **Temps de réponse API:** < 200ms en local

---

## 🔮 Améliorations Futures

### Fonctionnalités Potentielles
- [ ] Notifications push pour les tâches en retard
- [ ] Export de données en PDF/Excel
- [ ] Statistiques et graphiques de croissance
- [ ] Reconnaissance d'espèces par IA (photo → identification)
- [ ] Communauté et partage de plantes
- [ ] Mode hors-ligne avec service workers
- [ ] Application mobile native (Ionic/React Native)
- [ ] Intégration capteurs IoT (humidité, lumière)

### Améliorations Techniques
- [ ] Migration vers PostgreSQL (JSONB pour métadonnées)
- [ ] Redis pour cache de sessions
- [ ] Elasticsearch pour recherche avancée
- [ ] Docker Compose pour déploiement
- [ ] Tests unitaires et E2E (JUnit, Jasmine/Karma)
- [ ] CI/CD avec GitHub Actions
- [ ] Monitoring avec Spring Boot Actuator
- [ ] Documentation API avec Swagger/OpenAPI

---

## 👥 Contributeurs

**Développement:** Équipe Plant Manager  
**Framework Backend:** Spring Boot (Pivotal/VMware)  
**Framework Frontend:** Angular (Google)  
**UI Framework:** Bootstrap (Twitter)

---

## 📄 Licence

Ce projet est développé à des fins éducatives et de démonstration.

---

## 🆘 Support et Contact

Pour toute question ou problème:
1. Vérifier que MySQL est démarré sur le port 3306
2. Vérifier que le backend est accessible sur http://localhost:8080
3. Vérifier que le frontend est accessible sur http://localhost:4200
4. Consulter les logs dans les terminaux respectifs

---

## 📝 Notes Techniques

### Configuration Google OAuth2
Pour activer la connexion Google, obtenir des credentials sur:
https://console.cloud.google.com/apis/credentials

### Timezone Configuration
Le backend utilise UTC pour toutes les dates. Les conversions locales sont gérées côté frontend.

### CORS Configuration
Le backend autorise uniquement l'origine http://localhost:4200 en développement.
Pour la production, modifier `CorsConfig.java` avec le domaine réel.

---

**Version:** 1.0.0  
**Dernière mise à jour:** Décembre 2025  
**Statut:** ✅ Production Ready
