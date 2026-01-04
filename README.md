# 🃏 Stacked Deck API

![Java](https://img.shields.io/badge/Java-17%2B-orange)
![Spring Boot](https://img.shields.io/badge/Framework-Spring%20Boot-green)
![Security](https://img.shields.io/badge/Security-JWT%20%2F%20Spring%20Security-red)
![Payment](https://img.shields.io/badge/Payment-Stripe-blueviolet)

**Stacked Deck API** est le backend robuste et sécurisé de la plateforme e-commerce *Stacked Deck*, spécialisée dans la vente de cartes Pokémon TCG.

Construite avec **Spring Boot**, cette API gère l'intégralité du cycle de vente : de la gestion précise de l'inventaire (état des cartes, rareté) au paiement sécurisé via Stripe, en passant par un système de tickets de support pour les utilisateurs.

---

## 📑 Table des Matières

- [Fonctionnalités Clés](#-fonctionnalités-clés)
- [Architecture Modulaire](#-architecture-modulaire)
- [Technologies & Stack](#-technologies--stack)
- [Installation](#-installation)
- [Documentation API](#-documentation-api)
- [Configuration](#-configuration)

---

## 🚀 Fonctionnalités Clés

### 📦 Catalogue & Inventaire
* **Gestion avancée des produits :** Support des variantes (État : *Mint, Played*, Langue, Rareté).
* **Navigation publique :** Contrôleurs dédiés pour l'affichage public du catalogue (`PublicController`).
* **Mouvements de stock :** Suivi précis des entrées/sorties via `InventoryMovement`.

### 🛒 Achat & Paiement
* **Parcours d'achat complet :** Gestion de panier (`Cart`), validation de commande (`Checkout`) et expédition (`Shipping`).
* **Paiement Sécurisé :** Intégration complète avec **Stripe**, incluant la gestion des Webhooks pour les mises à jour de statut en temps réel.
* **Gestion des Promotions :** Système de codes promo et logique de pricing dynamique.

### 🛡️ Sécurité & Utilisateurs
* **Authentification Forte :** Système basé sur **JWT** (JSON Web Tokens) avec gestion des *Refresh Tokens*.
* **Gestion de Compte :** Inscription, vérification d'email, réinitialisation de mot de passe et gestion de profil.
* **Rôles :** Séparation stricte entre les accès Clients et Administrateurs.

### 🎫 Support Client & Admin
* **Ticketing :** Système intégré de tickets de support (`Support/Ticket`) pour gérer les demandes utilisateurs.
* **Dashboard Admin :** Statistiques de vente et vue d'ensemble de l'activité via `AdminDashboardController`.

---

## 🏗 Architecture Modulaire

Le projet suit une architecture claire organisée par domaines fonctionnels (`package-by-feature`) :

```text
com.stackeddeck
├── admin/          # Administration (Dashboard, Gestion Commandes & Catalogue)
├── auth/           # Authentification (Login, Register, Refresh Token)
├── catalog/        # Logique Produit (Catégories, Inventory, Enums: Rarity/Condition)
├── checkout/       # Tunnel d'achat (Cart, Order, Shipping)
├── common/         # Utilitaires globaux (Global Exception Handler)
├── config/         # Configuration (CORS, Swagger, Jackson)
├── notifications/  # Système de notifications
├── payment/        # Intégration Stripe & Webhooks
├── pricing/        # Logique de prix et Promotions
├── security/       # Configuration Spring Security & Filtres JWT
├── support/        # Service client (Tickets)
└── user/           # Gestion des profils utilisateurs
```

## 🛠 Technologies & Stack
Core : Java 17+, Spring Boot 3.x

Data : Spring Data JPA, Hibernate, MySQL/PostgreSQL

Sécurité : Spring Security 6, JJWT (Java JWT)

Paiement : Stripe API

Documentation : Swagger / OpenAPI (disponible via SwaggerConfig)

Build : Maven

## Installation
Cloner le dépôt :

Bash

git clone [https://github.com/0xAdafang/Stacked_Deck_Api.git](https://github.com/0xAdafang/Stacked_Deck_Api.git)
Configuration : Mettez à jour src/main/resources/application.properties avec vos variables d'environnement (Base de données, Clés Stripe, JWT Secret).

Lancer l'application :

Bash

./mvnw spring-boot:run

<h2>📡 Documentation API</h2>
<p>Une fois l'application lancée, la documentation interactive <strong>Swagger UI</strong> est accessible (selon ta configuration) sur :</p>
<blockquote>
  <a href="http://localhost:8080/swagger-ui.html">http://localhost:8080/swagger-ui.html</a>
</blockquote>

<h3>Aperçu des Endpoints Principaux</h3>
<table>
  <thead>
    <tr>
      <th>Module</th>
      <th>Endpoint Base</th>
      <th>Description</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <td><strong>Auth</strong></td>
      <td><code>/api/auth</code></td>
      <td>Login, Register, Refresh Token</td>
    </tr>
    <tr>
      <td><strong>Public</strong></td>
      <td><code>/api/public</code></td>
      <td>Accès lecture seule au catalogue</td>
    </tr>
    <tr>
      <td><strong>Cart</strong></td>
      <td><code>/api/cart</code></td>
      <td>Gestion du panier courant</td>
    </tr>
    <tr>
      <td><strong>Checkout</strong></td>
      <td><code>/api/checkout</code></td>
      <td>Validation de commande</td>
    </tr>
    <tr>
      <td><strong>Payment</strong></td>
      <td><code>/api/payment</code></td>
      <td>Initialisation paiement Stripe</td>
    </tr>
    <tr>
      <td><strong>Admin</strong></td>
      <td><code>/api/admin</code></td>
      <td>Opérations restreintes (CRUD produits, Stats)</td>
    </tr>
    <tr>
      <td><strong>Support</strong></td>
      <td><code>/api/support</code></td>
      <td>Création et suivi de tickets</td>
    </tr>
  </tbody>
</table>

<h2>⚙️ Configuration Requise</h2>
<p>Assurez-vous de définir les propriétés suivantes pour le bon fonctionnement (notamment pour Stripe et JWT) :</p>

<pre><code class="language-properties"># Base de données
spring.datasource.url=...
spring.datasource.username=...

# JWT Configuration
application.security.jwt.secret-key=...
application.security.jwt.expiration=...

# Stripe
stripe.api.key=...
stripe.webhook.secret=...
</code></pre>

<hr>
<p><em>Développé par <a href="https://github.com/0xAdafang">0xAdafang</a></em></p>
