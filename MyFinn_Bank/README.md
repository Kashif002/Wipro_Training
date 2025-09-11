# 🏦 MyFinn Bank - Digital Banking Solution

[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.5-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Spring Cloud](https://img.shields.io/badge/Spring%20Cloud-2025.0.0-blue.svg)](https://spring.io/projects/spring-cloud)
[![MySQL](https://img.shields.io/badge/MySQL-8.0-blue.svg)](https://www.mysql.com/)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

A comprehensive digital banking platform built with modern microservices architecture, providing secure banking operations, loan management, investment options, and real-time customer support.

## 📋 Table of Contents

- [Features](#-features)
- [Architecture](#-architecture)
- [Technology Stack](#-technology-stack)
- [Prerequisites](#-prerequisites)
- [Installation](#-installation)
- [Configuration](#-configuration)
- [API Documentation](#-api-documentation)
- [Database Schema](#-database-schema)
- [Usage](#-usage)
- [Project Structure](#-project-structure)
- [Contributing](#-contributing)
- [License](#-license)

## ✨ Features

### 🏦 Core Banking Features
- **Account Management**: Deposit, withdrawal, and fund transfer
- **Loan System**: Personal, home, vehicle, education, and business loans
- **Investment Options**: Fixed Deposits (FD) and Recurring Deposits (RD)
- **Transaction History**: Complete transaction tracking and statements
- **Balance Inquiry**: Real-time account balance checking

### 👥 User Management
- **Customer Portal**: Secure customer registration and authentication
- **Admin Dashboard**: Comprehensive administrative controls
- **Role-based Access**: Different access levels for customers and admins
- **Profile Management**: User profile updates and settings

### 💬 Customer Support
- **Real-time Chat**: Live chat support between customers and admins
- **Message History**: Complete chat conversation tracking
- **Status Indicators**: Read/unread message status
- **Priority Support**: Admin response management

### 📧 Communication
- **Email Notifications**: Automated email alerts and confirmations
- **Welcome Emails**: New customer onboarding emails
- **Loan Status Updates**: Application status notifications
- **Account Alerts**: Balance and transaction notifications

### 🔐 Security Features
- **JWT Authentication**: Secure token-based authentication
- **Password Encryption**: BCrypt password hashing
- **Session Management**: Secure session handling
- **CSRF Protection**: Cross-site request forgery protection
- **Input Validation**: Comprehensive data validation

## 🏗️ Architecture

### Microservices Architecture
```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   API Gateway   │    │  Eureka Server  │    │  Email Service  │
│   (Port: 8080)  │    │  (Port: 8761)   │    │  (Port: 8083)   │
└─────────────────┘    └─────────────────┘    └─────────────────┘
         │                       │                       │
         └───────────────────────┼───────────────────────┘
                                 │
         ┌───────────────────────┼───────────────────────┐
         │                       │                       │
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│ Customer Service│    │  Admin Service  │    │   MySQL DB      │
│  (Port: 8081)   │    │  (Port: 8082)   │    │  (Port: 3306)   │
└─────────────────┘    └─────────────────┘    └─────────────────┘
```

### Service Components
- **Eureka Server**: Service discovery and registration
- **API Gateway**: Single entry point with routing and load balancing
- **Customer Service**: Customer operations and account management
- **Admin Service**: Administrative functions and loan management
- **Email Service**: Email notifications and template management
- **MySQL Database**: Shared database with proper data isolation

## 🛠️ Technology Stack

### Backend Technologies
- **Java 17**: Programming language
- **Spring Boot 3.5.5**: Main application framework
- **Spring Cloud 2025.0.0**: Microservices framework
- **Spring Security**: Authentication and authorization
- **Spring Data JPA**: Database operations and ORM
- **MySQL 8.0**: Primary database
- **JWT (JSON Web Tokens)**: Authentication tokens
- **Maven**: Build and dependency management
- **Lombok**: Code generation and boilerplate reduction

### Frontend Technologies
- **Thymeleaf**: Server-side template engine
- **Bootstrap 5.3.2**: CSS framework for responsive design
- **jQuery 3.7.1**: JavaScript library
- **Font Awesome 6.4.0**: Icon library
- **HTML5/CSS3**: Modern web standards
- **JavaScript ES6+**: Client-side scripting

### Microservices Components
- **Netflix Eureka**: Service discovery and registration
- **Spring Cloud Gateway**: API Gateway with routing
- **Spring Boot Actuator**: Application monitoring and health checks
- **Swagger/OpenAPI 3.0**: API documentation
- **Spring Mail**: Email functionality
- **Thymeleaf**: Email template processing

## 📋 Prerequisites

Before running the application, ensure you have the following installed:

- **Java 17** or higher
- **Maven 3.6+**
- **MySQL 8.0** or higher
- **Git** (for cloning the repository)
- **IDE** (IntelliJ IDEA, Eclipse, or VS Code recommended)

## 🚀 Installation

### 1. Clone the Repository
```bash
git clone https://github.com/yourusername/MyFinn_Bank.git
cd MyFinn_Bank
```

### 2. Database Setup
```bash
# Create MySQL database
mysql -u root -p
CREATE DATABASE myfin_bank;
USE myfin_bank;

# Run the database setup script
mysql -u root -p myfin_bank < database/myfin_bank\ db.sql
```

### 3. Build the Project
```bash
# Build all services
mvn clean install

# Or build individual services
cd eureka-server && mvn clean install
cd ../api-gateway && mvn clean install
cd ../customer-service && mvn clean install
cd ../admin-service && mvn clean install
cd ../email-service && mvn clean install
```

## ⚙️ Configuration

### 1. Database Configuration
Update the database connection details in each service's `application-dev.yml`:

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/myfin_bank
    username: your_username
    password: your_password
```

### 2. Email Configuration
Configure email settings in `email-service/src/main/resources/application-dev.yml`:

```yaml
spring:
  mail:
    host: smtp.gmail.com
    port: 587
    username: your-email@gmail.com
    password: your-app-password
    properties:
      mail:
        smtp:
          auth: true
          starttls:
            enable: true
```

### 3. JWT Configuration
Update JWT secret in each service's configuration:

```yaml
jwt:
  secret: your-secret-key-here
  expiration: 86400000 # 24 hours in milliseconds
```

## 🚀 Running the Application

### Startup Sequence
Start the services in the following order:

```bash
# 1. Start MySQL Database
sudo systemctl start mysql

# 2. Start Eureka Server
cd eureka-server
mvn spring-boot:run

# 3. Start Email Service
cd ../email-service
mvn spring-boot:run

# 4. Start Customer Service
cd ../customer-service
mvn spring-boot:run

# 5. Start Admin Service
cd ../admin-service
mvn spring-boot:run

# 6. Start API Gateway
cd ../api-gateway
mvn spring-boot:run
```

### Access URLs
- **API Gateway**: http://localhost:8080
- **Customer Portal**: http://localhost:8081
- **Admin Dashboard**: http://localhost:8082
- **Eureka Dashboard**: http://localhost:8761
- **Swagger Documentation**:
  - Customer Service: http://localhost:8081/swagger-ui.html
  - Admin Service: http://localhost:8082/swagger-ui.html
  - Email Service: http://localhost:8083/swagger-ui.html

## 📚 API Documentation

### Customer Service APIs

#### Authentication
- `POST /api/auth/register` - Customer registration
- `POST /api/auth/login` - Customer login
- `GET /api/auth/validate-session` - Session validation

#### Account Management
- `GET /api/customer/account/balance` - Get account balance
- `POST /api/customer/account/deposit` - Deposit money
- `POST /api/customer/account/withdraw` - Withdraw money
- `POST /api/customer/account/transfer` - Transfer money

#### Loan Management
- `POST /api/customer/loans/apply` - Apply for loan
- `GET /api/customer/loans/my-loans` - Get customer loans
- `GET /api/customer/loans/calculate-emi` - Calculate EMI

#### Investment Management
- `POST /api/customer/deposits/fd` - Create fixed deposit
- `POST /api/customer/deposits/rd` - Create recurring deposit
- `GET /api/customer/deposits/my-investments` - Get investments

#### Chat Support
- `POST /api/customer/chat/send` - Send message
- `GET /api/customer/chat/history` - Get chat history
- `GET /api/customer/chat/new-messages` - Get new messages

### Admin Service APIs

#### Authentication
- `POST /api/admin/login` - Admin login
- `POST /api/admin/register` - Admin registration

#### Customer Management
- `GET /api/admin/customers` - Get all customers
- `GET /api/admin/customers/{id}` - Get customer details
- `PUT /api/admin/customers/{id}/status` - Update customer status

#### Loan Management
- `GET /api/admin/loans/pending` - Get pending loans
- `POST /api/admin/loans/{id}/approve` - Approve loan
- `POST /api/admin/loans/{id}/reject` - Reject loan

#### Transaction Management
- `GET /api/admin/transactions` - Get all transactions
- `GET /api/admin/transactions/customer/{id}` - Get customer transactions

#### Chat Management
- `GET /api/admin/chat/messages` - Get all chat messages
- `POST /api/admin/chat/respond` - Respond to customer

### Email Service APIs

#### Email Operations
- `POST /api/emails/send` - Send custom email
- `POST /api/emails/welcome` - Send welcome email
- `POST /api/emails/loan-approval` - Send loan approval email
- `POST /api/emails/loan-rejection` - Send loan rejection email

## 🗄️ Database Schema

### Core Tables

#### Customers Table
```sql
CREATE TABLE customers (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    customer_id VARCHAR(255) UNIQUE,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    phone VARCHAR(255) UNIQUE,
    address VARCHAR(500),
    active BOOLEAN DEFAULT TRUE,
    email_verified BOOLEAN DEFAULT FALSE,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
```

#### Accounts Table
```sql
CREATE TABLE accounts (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    account_number VARCHAR(255) UNIQUE NOT NULL,
    customer_id BIGINT NOT NULL,
    balance DECIMAL(15,2) DEFAULT 0.00,
    account_type ENUM('SAVINGS', 'CURRENT', 'SALARY') DEFAULT 'SAVINGS',
    status ENUM('ACTIVE', 'INACTIVE', 'SUSPENDED', 'CLOSED') DEFAULT 'ACTIVE',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (customer_id) REFERENCES customers(id)
);
```

#### Loans Table
```sql
CREATE TABLE loans (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    loan_id VARCHAR(255) UNIQUE,
    customer_id BIGINT NOT NULL,
    loan_type ENUM('PERSONAL', 'HOME', 'VEHICLE', 'EDUCATION', 'BUSINESS') NOT NULL,
    requested_amount DECIMAL(15,2) NOT NULL,
    approved_amount DECIMAL(15,2),
    interest_rate DECIMAL(5,2),
    term_months INT,
    monthly_emi DECIMAL(15,2),
    status ENUM('APPLIED', 'UNDER_REVIEW', 'APPROVED', 'REJECTED', 'DISBURSED', 'CLOSED') DEFAULT 'APPLIED',
    purpose VARCHAR(500),
    monthly_income DECIMAL(15,2),
    employment_details VARCHAR(500),
    approved_by BIGINT,
    admin_remarks VARCHAR(500),
    applied_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    processed_at DATETIME,
    approved_date DATETIME,
    created_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (customer_id) REFERENCES customers(id),
    FOREIGN KEY (approved_by) REFERENCES admins(admin_id)
);
```

#### Transactions Table
```sql
CREATE TABLE transactions (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    transaction_id VARCHAR(255) UNIQUE NOT NULL,
    from_account_id BIGINT,
    to_account_id BIGINT,
    amount DECIMAL(15,2) NOT NULL,
    type ENUM('DEPOSIT', 'WITHDRAWAL', 'TRANSFER', 'LOAN_PAYMENT', 'FD_INVESTMENT', 'RD_INVESTMENT') NOT NULL,
    status ENUM('PENDING', 'COMPLETED', 'FAILED', 'CANCELLED') DEFAULT 'PENDING',
    description VARCHAR(500),
    reference VARCHAR(255),
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (from_account_id) REFERENCES accounts(id),
    FOREIGN KEY (to_account_id) REFERENCES accounts(id)
);
```

### Database Relationships
- **One-to-Many**: Customer → Accounts, Loans, Transactions
- **Many-to-One**: Loans → Admin (approved_by)
- **Many-to-Many**: Transactions between Accounts
- **Self-Referencing**: Chat Messages (customer ↔ admin)

## 💻 Usage

### Customer Portal
1. **Registration**: Create a new customer account
2. **Login**: Access your banking dashboard
3. **Account Management**: View balance, make deposits/withdrawals
4. **Loan Application**: Apply for various types of loans
5. **Investment**: Create FD/RD investments
6. **Chat Support**: Get help from admin support

### Admin Dashboard
1. **Login**: Access admin dashboard
2. **Customer Management**: View and manage customer accounts
3. **Loan Approval**: Review and approve/reject loan applications
4. **Transaction Monitoring**: Monitor all banking transactions
5. **Chat Support**: Respond to customer queries

### Test Credentials
```
Customer:
Email: rajesh.kumar@gmail.com
Password: Customer@123

Admin:
Email: admin@myfinbank.com
Password: Admin@123
```

## 📁 Project Structure

```
MyFinn_Bank/
├── eureka-server/                 # Service discovery server
│   ├── src/main/java/
│   └── src/main/resources/
├── api-gateway/                   # API Gateway service
│   ├── src/main/java/
│   └── src/main/resources/
├── customer-service/              # Customer operations service
│   ├── src/main/java/
│   │   └── com/myfinbank/customer/
│   │       ├── controller/        # REST controllers
│   │       ├── service/          # Business logic
│   │       ├── repository/       # Data access layer
│   │       ├── entity/           # JPA entities
│   │       ├── dto/              # Data transfer objects
│   │       ├── config/           # Configuration classes
│   │       └── exception/        # Custom exceptions
│   ├── src/main/resources/
│   │   ├── templates/            # Thymeleaf templates
│   │   ├── static/               # CSS, JS, images
│   │   └── application*.yml      # Configuration files
│   └── src/test/
├── admin-service/                 # Admin operations service
│   ├── src/main/java/
│   │   └── com/myfinbank/admin/
│   │       ├── controller/       # REST controllers
│   │       ├── service/          # Business logic
│   │       ├── repository/       # Data access layer
│   │       ├── entity/           # JPA entities
│   │       ├── dto/              # Data transfer objects
│   │       └── config/           # Configuration classes
│   ├── src/main/resources/
│   │   ├── templates/            # Thymeleaf templates
│   │   ├── static/               # CSS, JS, images
│   │   └── application*.yml      # Configuration files
│   └── src/test/
├── email-service/                 # Email notification service
│   ├── src/main/java/
│   │   └── com/myfinbank/email/
│   │       ├── controller/       # REST controllers
│   │       ├── service/          # Email services
│   │       ├── dto/              # Data transfer objects
│   │       └── config/           # Configuration classes
│   ├── src/main/resources/
│   │   ├── templates/            # Email templates
│   │   └── application*.yml      # Configuration files
│   └── src/test/
├── database/                      # Database scripts
│   ├── myfin_bank db.sql         # Main database schema
│   ├── complete_fix.sql          # Database fixes
│   └── LOGIN_CREDENTIALS.md      # Test credentials
├── MyFinn_Bank_Project_Documentation.md  # Detailed documentation
├── MyFinn_Bank_Presentation.html  # Project presentation
└── README.md                      # This file
```

## 🔧 Development

### Key Annotations Used

#### Spring Boot Core
- `@SpringBootApplication`: Main application class
- `@EnableDiscoveryClient`: Eureka service registration
- `@EnableAsync`: Asynchronous processing
- `@EnableTransactionManagement`: Database transactions

#### Security
- `@EnableWebSecurity`: Security configuration
- `@PreAuthorize`: Method-level security
- `@Secured`: Role-based access control

#### JPA/Hibernate
- `@Entity`: Database entity mapping
- `@Table`: Table name specification
- `@Id & @GeneratedValue`: Primary key definition
- `@ManyToOne & @OneToMany`: Entity relationships
- `@Enumerated`: Enum mapping

#### Controllers
- `@RestController`: REST API endpoints
- `@Controller`: Web page controllers
- `@RequestMapping`: URL mapping
- `@GetMapping/@PostMapping`: HTTP method mapping
- `@RequestBody/@RequestParam`: Request data binding

#### Services
- `@Service`: Business logic components
- `@Repository`: Data access layer
- `@Transactional`: Transaction management
- `@Async`: Asynchronous methods

### Business Logic Flow

#### Customer Registration & Authentication
1. Customer Registration → Password Encryption → Database Save → Welcome Email
2. Customer Login → JWT Token Generation → Secure Session
3. Request Authentication → JWT Validation → Access Granted

#### Account Management
1. Deposit: Amount Validation → Transaction Creation → Balance Update → Confirmation
2. Withdrawal: Balance Check → Amount Validation → Transaction Creation → Balance Update
3. Transfer: Source/Destination Validation → Balance Check → Transaction Creation → Both Account Updates

#### Loan Application Process
1. Application: Customer Submits → EMI Calculation → Interest Rate Assignment → Database Save → Email Notification
2. Review: Admin Reviews → Credit Check → Decision Making
3. Approval/Rejection: Status Update → Email Notification → Customer Notification → Account Update (if approved)

## 🤝 Contributing

We welcome contributions to MyFinn Bank! Please follow these steps:

1. **Fork the repository**
2. **Create a feature branch**: `git checkout -b feature/amazing-feature`
3. **Commit your changes**: `git commit -m 'Add some amazing feature'`
4. **Push to the branch**: `git push origin feature/amazing-feature`
5. **Open a Pull Request**

### Contribution Guidelines
- Follow the existing code style and conventions
- Write comprehensive tests for new features
- Update documentation for any API changes
- Ensure all tests pass before submitting
- Use meaningful commit messages

### Code Style
- Use Java 17 features appropriately
- Follow Spring Boot best practices
- Implement proper error handling
- Use meaningful variable and method names
- Add JavaDoc comments for public methods

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🙏 Acknowledgments

- **Spring Framework Team** for the excellent Spring Boot and Spring Cloud frameworks
- **MySQL Team** for the robust database system
- **Bootstrap Team** for the responsive CSS framework
- **Font Awesome** for the comprehensive icon library
- **Open Source Community** for various libraries and tools used in this project

## 📞 Support

If you have any questions or need help with the project:

- **Create an Issue**: [GitHub Issues](https://github.com/yourusername/MyFinn_Bank/issues)
- **Email**: support@myfinbank.com
- **Documentation**: Check the [detailed documentation](MyFinn_Bank_Project_Documentation.md)
- **Presentation**: View the [project presentation](MyFinn_Bank_Presentation.html)

## 🚀 Future Enhancements

- **Mobile Application**: iOS and Android apps
- **Advanced Analytics**: Business intelligence and reporting
- **Blockchain Integration**: Cryptocurrency support
- **AI-Powered Features**: Fraud detection and customer insights
- **Multi-Currency Support**: International banking
- **Advanced Investment Options**: Mutual funds, stocks, bonds
- **API Rate Limiting**: Enhanced security and performance
- **Microservices Monitoring**: Advanced observability
- **Load Balancing**: Horizontal scaling support
- **Containerization**: Docker and Kubernetes deployment

---

<div align="center">

**🏦 MyFinn Bank - Modern Digital Banking Solution**

Built with ❤️ using Spring Boot, Microservices, and Modern Web Technologies

[![GitHub stars](https://img.shields.io/github/stars/yourusername/MyFinn_Bank.svg?style=social&label=Star)](https://github.com/yourusername/MyFinn_Bank)
[![GitHub forks](https://img.shields.io/github/forks/yourusername/MyFinn_Bank.svg?style=social&label=Fork)](https://github.com/yourusername/MyFinn_Bank/fork)
[![GitHub watchers](https://img.shields.io/github/watchers/yourusername/MyFinn_Bank.svg?style=social&label=Watch)](https://github.com/yourusername/MyFinn_Bank)

</div>
