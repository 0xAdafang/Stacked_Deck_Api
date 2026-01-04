# 🃏 Stacked Deck API

![Java](https://img.shields.io/badge/Java-17%2B-orange)
![Spring Boot](https://img.shields.io/badge/Framework-Spring%20Boot-green)
![Security](https://img.shields.io/badge/Security-JWT%20%2F%20Spring%20Security-red)
![Payment](https://img.shields.io/badge/Payment-Stripe-blueviolet)

**Stacked Deck API** is the robust and secure backend for the *Stacked Deck* e-commerce platform, specialized in Pokémon TCG sales.

Built with **Spring Boot**, this API manages the entire sales lifecycle: from precise inventory management (card condition, rarity) to secure payments via Stripe, including a complete support ticket system for users.

---

## 📑 Table of Contents

- [Key Features](#-key-features)
- [Modular Architecture](#-modular-architecture)
- [Tech Stack](#-tech-stack)
- [Installation](#-installation)
- [API Documentation](#-api-documentation)
- [Configuration](#-configuration)

---

## 🚀 Key Features

### 📦 Catalog & Inventory
* **Advanced Product Management:** Support for variants (Condition: *Mint, Played*, Language, Rarity).
* **Public Navigation:** Dedicated controllers for public catalog display (`PublicController`).
* **Inventory Tracking:** Precise tracking of stock entries/exits via `InventoryMovement`.

### 🛒 Purchase & Payment
* **Complete Purchase Flow:** Cart management (`Cart`), order validation (`Checkout`), and shipping (`Shipping`).
* **Secure Payment:** Full integration with **Stripe**, including Webhook handling for real-time status updates.
* **Promotions Engine:** Promo code system and dynamic pricing logic.

### 🛡️ Security & Users
* **Strong Authentication:** System based on **JWT** (JSON Web Tokens) with *Refresh Token* management.
* **Account Management:** Registration, email verification, password reset, and profile management.
* **Role-Based Access:** Strict separation between Client and Administrator access.

### 🎫 Support & Admin
* **Ticketing System:** Integrated support ticket system (`Support/Ticket`) to handle user inquiries.
* **Admin Dashboard:** Sales statistics and activity overview via `AdminDashboardController`.

---

## 🏗 Modular Architecture

The project follows a clear `package-by-feature` architecture:

```text
com.stackeddeck
├── admin/          # Administration (Dashboard, Orders & Catalog Management)
├── auth/           # Authentication (Login, Register, Refresh Token)
├── catalog/        # Product Logic (Categories, Inventory, Enums: Rarity/Condition)
├── checkout/       # Purchase Flow (Cart, Order, Shipping)
├── common/         # Global Utilities (Global Exception Handler)
├── config/         # Configuration (CORS, Swagger, Jackson)
├── notifications/  # Notification System
├── payment/        # Stripe Integration & Webhooks
├── pricing/        # Pricing Logic & Promotions
├── security/       # Spring Security Config & JWT Filters
├── support/        # Customer Service (Tickets)
└── user/           # User Profile Management
```

## 🛠 Tech Stack

* **Core:** Java 17+, Spring Boot 3.x
* **Data:** Spring Data JPA, Hibernate, MySQL/PostgreSQL
* **Security:** Spring Security 6, JJWT (Java JWT)
* **Payment:** Stripe API
* **Documentation:** Swagger / OpenAPI (via `SwaggerConfig`)
* **Build:** Maven

---

## 💻 Installation

1.  **Clone the repository:**
    ```bash
    git clone [https://github.com/0xAdafang/Stacked_Deck_Api.git](https://github.com/0xAdafang/Stacked_Deck_Api.git)
    ```

2.  **Configuration:**
    Update `src/main/resources/application.properties` with your environment variables (Database, Stripe Keys, JWT Secret).

3.  **Run the application:**
    ```bash
    ./mvnw spring-boot:run
    ```

<h2>📡 API Documentation</h2>
<p>Once the application is running, the interactive <strong>Swagger UI</strong> documentation is available at:</p>
<blockquote>
  <a href="http://localhost:8080/swagger-ui.html">http://localhost:8080/swagger-ui.html</a>
</blockquote>

<h3>Main Endpoints Overview</h3>
<table>
  <thead>
    <tr>
      <th>Module</th>
      <th>Base Endpoint</th>
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
      <td>Read-only access to catalog</td>
    </tr>
    <tr>
      <td><strong>Cart</strong></td>
      <td><code>/api/cart</code></td>
      <td>Current cart management</td>
    </tr>
    <tr>
      <td><strong>Checkout</strong></td>
      <td><code>/api/checkout</code></td>
      <td>Order validation</td>
    </tr>
    <tr>
      <td><strong>Payment</strong></td>
      <td><code>/api/payment</code></td>
      <td>Stripe payment initialization</td>
    </tr>
    <tr>
      <td><strong>Admin</strong></td>
      <td><code>/api/admin</code></td>
      <td>Restricted operations (Product CRUD, Stats)</td>
    </tr>
    <tr>
      <td><strong>Support</strong></td>
      <td><code>/api/support</code></td>
      <td>Ticket creation and tracking</td>
    </tr>
  </tbody>
</table>

<h2>⚙️ Required Configuration</h2>
<p>Ensure you define the following properties for the application to run correctly (especially for Stripe and JWT):</p>

<pre><code class="language-properties"># Database
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
<p><em>Developed by <a href="https://github.com/0xAdafang">0xAdafang</a></em></p>
