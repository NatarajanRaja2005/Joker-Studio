# 🎬 Joker Studio — Event Management & E-Commerce Platform

A unified Spring Boot REST API backend built for a real photography/event studio client, helping the business transition from offline operations to a full digital platform — covering **event booking**, **product e-commerce**, and **portfolio showcase** — all in one system.

![Java](https://img.shields.io/badge/Java-17-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-brightgreen)
![MySQL](https://img.shields.io/badge/MySQL-Database-blue)
![Status](https://img.shields.io/badge/Status-In%20Progress-yellow)
![License](https://img.shields.io/badge/License-MIT-lightgrey)

---

## 📌 Overview

Joker Studio's backend replaces manual, offline event-booking and sales workflows with a single RESTful platform. It brings together **event scheduling**, **accessories & product catalog**, **shopping cart & orders**, **secure user accounts**, and **portfolio media management** into 10+ cohesive modules — built for real client use, not just as a tutorial project.

**Base URL:** `http://localhost:8080/api/v1`

---

## ✨ Key Features

### 👤 User Management
- Registration, login, and account deletion
- Email OTP verification with **BCrypt-hashed OTP storage** (no plaintext OTPs in DB)
- Profile retrieval and updates
- Role-based access control (`ADMIN`, `USER`)

### 📅 Event & Agenda Management
- Book, update, and cancel events
- Status-driven lifecycle: `UPCOMING`, `PENDING`, `BOOKED`, `CANCELLED`
- Retrieve events by date, status, or user — with separate admin and user views
- Agenda system linking bookings to accessories with dynamic price calculation
  (`Event → Agenda → AgendaDetails → EventAccessories`)

### 🛍️ Product & Catalog
- Full CRUD for products and product types/categories
- Search by name/type, product count by category
- Image upload, retrieval, update, and deletion per product

### 🛒 Cart & Orders
- Add/remove cart items, clear cart, retrieve cart contents
- Place orders by cart or direct product purchase
- Order address management (embedded address, not a separate table)
- Order status lifecycle tracking and admin status updates

### 🖼️ Portfolio Showcase
- Upload and manage portfolio media (images, videos, presentation files)
- **Hybrid storage model:** media files on local file system, metadata (filename, size, type, download URL) in MySQL — keeping large binary data out of the transactional database
- Filter portfolios by associated event agenda
- Download portfolio files directly via API

### 🔐 Security
- JWT-based stateless authentication
- Spring Security role-based authorization on all protected endpoints
- BCrypt password and OTP hashing
- Automated email notifications for account and booking events

---

## 🛠️ Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 17 |
| Framework | Spring Boot 3.x |
| Security | Spring Security, JWT |
| Data Access | Spring Data JPA, Hibernate |
| Database | MySQL |
| API Docs | Swagger / OpenAPI |
| Build Tool | Maven |
| Testing | Postman |

---

## 🏗️ Architecture

The project follows a clean **layered architecture**:

```
Controller  →  Service  →  Repository  →  MySQL
```

Core entity relationships:

```
User ──1:1── Cart ──1:N── CartItem ──N:1── Product ──N:1── ProductType
User ──1:N── Event ──N:1── Agenda ──1:N── AgendaDetails ──N:1── EventAccessories
User ──1:N── Order ──1:N── OrderDetails ──N:1── Product
Portfolio ──1:1── Agenda
Portfolio ──1:N── PortfolioMedia
Product ──1:N── Image
```

**Design highlights:**
- `EventAccessories` uses a Hibernate JSON-typed column (`@JdbcTypeCode(SqlTypes.JSON)`) to map accessory pricing per event type, allowing new pricing rules without schema migrations.
- Sensitive relational fields use `@JsonIgnore` to prevent circular serialization and unwanted data exposure in API responses.
- Cascading (`CascadeType.ALL`, `orphanRemoval`) is applied carefully across parent-child entities (e.g., `Agenda → AgendaDetails`, `Portfolio → PortfolioMedia`) to keep data consistent.

---

## 📁 Project Structure

```text
src
├── controller
├── service
│   └── impl
├── repository
├── model
├── enums
├── dto
│   ├── request
│   └── response
├── security
│   ├── config
│   └── jwt
├── exception
├── config
└── JokerStudioApplication.java
```

> Update this tree to match your actual package layout before pushing.

---

## 🗺️ System Architecture

<p align="center">
    <img src="docs/SystemArchitectureDiagram.png" width="900"/>
</p>

*(Add a simple architecture diagram to `/docs` — even a quick Excalidraw export works well here.)*

---

## 🗄️ Database Design

**Main Entities:** User, Role, Cart, CartItem, Product, ProductType, Image, Event, Agenda, AgendaDetails, EventAccessories, Order, OrderDetails, OrderAddress, Portfolio, PortfolioMedia, EmailVerification, SmsVerification.

<p align="center">
    <img src="docs/ER-Diagram.png" width="900"/>
</p>

*(Export an ER diagram from MySQL Workbench or dbdiagram.io and save it as `docs/ER-Diagram.png` — avoid spaces in filenames, they can break GitHub's image rendering.)*

---

## 🔑 JWT Authentication Flow

<p align="center">
    <img src="docs/JwtFlowDiagram.png" width="900"/>
</p>

---

## 📂 Module Summary

| Module | Responsibility |
|---|---|
| User | Auth, verification, profile |
| Product | Catalog CRUD, search |
| Product Type | Category management |
| Image | Product image handling |
| Accessories | Event accessory catalog |
| Cart | Cart items and totals |
| Orders | Order placement and tracking |
| Event | Booking, scheduling, status |
| Agenda | Event-accessory linkage & pricing |
| Portfolio | Media showcase & downloads |

---

## 🚀 Getting Started

### Prerequisites
- Java 17+
- Maven 3.8+
- MySQL 8+

### Setup

```bash
# Clone the repository
git clone https://github.com/NatarajanRaja2005/joker-studio-backend.git
cd joker-studio-backend

# Configure your database credentials in
# src/main/resources/application.properties

# Build and run
mvn clean install
mvn spring-boot:run
```

The API will be available at `http://localhost:8080/api/v1`, with Swagger docs at `http://localhost:8080/swagger-ui.html`.

### Environment Variables

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/joker_studio
spring.datasource.username=YOUR_DB_USERNAME
spring.datasource.password=YOUR_DB_PASSWORD
jwt.secret=YOUR_JWT_SECRET
spring.mail.username=YOUR_EMAIL
spring.mail.password=YOUR_EMAIL_APP_PASSWORD
```

---

## 📮 API Testing

A complete Postman collection covering all modules (User, Product, Cart, Orders, Event, Agenda, Portfolio) is included for testing. JWT tokens are stored via the `json_web_token` collection variable and auto-applied to authenticated requests.

---

## 📖 API Documentation

Interactive Swagger UI is available after startup:

```
http://localhost:8080/swagger-ui/index.html
```

<p align="center">
    <img src="docs/swagger-ui.png" width="900"/>
</p>

---

## 🗺️ Roadmap

- [ ] Forgot password flow
- [ ] Phone/SMS OTP verification
- [ ] AWS deployment (EC2 + RDS)
- [ ] Rate limiting on OTP endpoints

---

## 👤 Author

**Natarajan R**
Java Backend Developer | [LinkedIn](https://linkedin.com/in/natarajanraja2005) | [Portfolio](https://natarajanraja2005.github.io/Natarajanportfolio/) | [LeetCode](https://leetcode.com/u/NatarajanRaja2005)

---

## 📄 License

This project is built for a real client engagement. Code structure and architecture are shared here for portfolio purposes.

---

⭐ If you found this project useful, consider giving it a Star on GitHub.
