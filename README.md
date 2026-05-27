# SweetBite Bakery Management System
## SLIIT SE1020 - Object-Oriented Programming Group Project

### Project Overview
A full-featured Bakery Management System built with **Java (Spring Boot + Thymeleaf)** covering all 6 member modules.

---

## How to Run

### Requirements
- Java 17+
- Maven 3.6+

### Steps
```bash
# 1. Navigate to project root
cd bakery

# 2. Build the project
mvn clean package -DskipTests

# 3. Run the application
mvn spring-boot:run

# 4. Open browser
http://localhost:8080
```

---

## Default Login Credentials

| Role | Username | Password |
|------|----------|----------|
| Staff/Admin | admin | admin123 |
| Staff/Baker | baker01 | baker123 |
| Customer | john | john123 |
| Customer | sarah | sarah123 |
| Customer | priya | priya123 |

---

## OOP Concepts Applied (SE1020 Lecture Mapping)

| Concept | Location | Lecture |
|---------|----------|---------|
| **Encapsulation** (private fields + getters/setters) | All model classes | Lecture 01, 02 |
| **Abstraction** (abstract class + abstract methods) | `User`, `Product`, `Supplier`, `PaymentMethod` | Lecture 02, 05 |
| **Inheritance** | `Customer`/`Staff` extends `User`, `BirthdayCake`/`WeddingCake` extends `CustomCake` | Lecture 04 |
| **Polymorphism** | `getDisplayPrice()` on `PerishableProduct` vs `NonPerishableProduct`; `getDiscountRate()` on suppliers | Lecture 04 |
| **Method Overloading** | Constructors in all model classes | Lecture 02 |
| **Method Overriding** | `toString()`, `getDashboardPath()`, `calculateBasePrice()` | Lecture 04 |
| **Static members/methods** | `FileStorage.generateId()`, static initializer block | Lecture 05 |
| **Abstract Classes** | `User`, `Product`, `Supplier`, `PaymentMethod` | Lecture 05 |
| **Interfaces** | `CommandLineRunner` (DataSeeder) | Lecture 05 |
| **Exception Handling** | `try-catch` in all services, custom `BakeryException` hierarchy | Lecture 06 |
| **ArrayList + Generics** | All service classes (`List<Product>`, `List<Order>`, etc.) | Lecture 08 |
| **File I/O** | `FileStorage.java` uses `BufferedReader`, `PrintWriter`, `FileWriter` | Lecture 07+ |
| **Information Hiding** | `Review.getMaskedUsername()` masks user identity | Lecture 01 |
| **Composition** | `Order` contains `List<OrderItem>` | Lecture 04 |

---

## Module Breakdown

| Member | Module | Key Classes | UIs |
|--------|--------|-------------|-----|
| **M01** | User & Profile Management | `User`, `Customer`, `Staff` | Login, Register, Profile, User-List |
| **M02** | Bakery Product Catalog | `Product`, `PerishableProduct`, `NonPerishableProduct` | Product List, Product Detail, Product Manage |
| **M03** | Order Processing | `Order`, `OrderItem`, `PaymentMethod`, `CashOnPickupPayment`, `OnlinePayment` | Cart, Checkout, Order History, Admin Orders |
| **M04** | Custom Cake Booking | `CustomCake`, `BirthdayCake`, `WeddingCake` | Cake Request, Booking Status, Cake Edit, Admin Bookings |
| **M05** | Inventory & Supplier | `Supplier`, `LocalSupplier`, `WholesaleSupplier`, `Ingredient` | Supplier Directory, Inventory Dashboard, Add Ingredient |
| **M06** | Review & Feedback | `Review`, `VerifiedPurchaseReview` | Review Submit, My Reviews, Admin Moderation |

---

## Data Storage
All data is persisted in `.txt` files in the `data/` directory (auto-created on startup):
- `users.txt` — user accounts
- `products.txt` — bakery product catalog
- `orders.txt` — customer orders
- `cakes.txt` — custom cake bookings
- `suppliers.txt` — supplier directory
- `ingredients.txt` — inventory ingredients
- `reviews.txt` — customer reviews

---

## Architecture
- **Backend**: Java 17, Spring Boot 3.2, MVC pattern
- **Frontend**: Thymeleaf templates + Bootstrap 5 + custom CSS
- **Storage**: Plain text `.txt` files (CSV format)
- **Session**: HTTP Session for login state and shopping cart

---

*Generated for SLIIT SE1020 OOP Group Project*
