# MyFinn Bank - Complete Project Documentation

## Table of Contents
1. [Project Overview](#project-overview)
2. [Architecture Design](#architecture-design)
3. [Microservices Structure](#microservices-structure)
4. [Database Schema](#database-schema)
5. [Technology Stack](#technology-stack)
6. [Key Annotations and Their Usage](#key-annotations-and-their-usage)
7. [Business Logic Flow](#business-logic-flow)
8. [Security Implementation](#security-implementation)
9. [API Endpoints](#api-endpoints)
10. [Frontend Components](#frontend-components)
11. [Email Service Integration](#email-service-integration)
12. [Deployment and Configuration](#deployment-and-configuration)

---

## Project Overview

MyFinn Bank is a comprehensive digital banking application built using microservices architecture. It provides banking services including account management, loan processing, investment options (FD/RD), and customer support through a chat system.

### Key Features:
- **Customer Portal**: Account management, loan applications, investment tracking
- **Admin Dashboard**: Customer management, loan approval, transaction monitoring
- **Real-time Chat**: Customer support system
- **Email Notifications**: Automated email alerts and confirmations
- **Secure Authentication**: JWT-based authentication system

---

## Architecture Design

### Microservices Architecture
The application follows a microservices pattern with the following services:

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

### Service Communication Flow:
1. **Client Request** → API Gateway
2. **API Gateway** → Routes to appropriate microservice
3. **Microservices** → Register with Eureka Server
4. **Email Service** → Handles all email notifications
5. **Database** → Shared MySQL database for all services

---

## Microservices Structure

### 1. Eureka Server (Service Discovery)
- **Port**: 8761
- **Purpose**: Service registry and discovery
- **Key Annotations**:
  - `@EnableEurekaServer`: Enables Eureka server functionality
  - `@SpringBootApplication`: Main application class

### 2. API Gateway
- **Port**: 8080
- **Purpose**: Single entry point for all client requests
- **Key Features**:
  - Route requests to appropriate microservices
  - Load balancing
  - Cross-cutting concerns (logging, monitoring)
- **Key Annotations**:
  - `@EnableDiscoveryClient`: Registers with Eureka
  - `@SpringBootApplication`: Main application class

### 3. Customer Service
- **Port**: 8081
- **Purpose**: Handles all customer-related operations
- **Key Features**:
  - Customer authentication and registration
  - Account management (deposit, withdrawal, transfer)
  - Loan applications
  - Investment management (FD/RD)
  - Chat support
- **Key Annotations**:
  - `@EnableDiscoveryClient`: Service discovery
  - `@EnableAsync`: Asynchronous processing
  - `@EnableTransactionManagement`: Transaction management
  - `@SpringBootApplication`: Main application class

### 4. Admin Service
- **Port**: 8082
- **Purpose**: Administrative operations and loan management
- **Key Features**:
  - Admin authentication
  - Customer management
  - Loan approval/rejection
  - Transaction monitoring
  - Chat response system
- **Key Annotations**:
  - `@EnableDiscoveryClient`: Service discovery
  - `@EnableAsync`: Asynchronous processing
  - `@EnableTransactionManagement`: Transaction management
  - `@SpringBootApplication`: Main application class

### 5. Email Service
- **Port**: 8083
- **Purpose**: Email notifications and templates
- **Key Features**:
  - Welcome emails
  - Loan status notifications
  - Account alerts
  - HTML email templates
- **Key Annotations**:
  - `@EnableDiscoveryClient`: Service discovery
  - `@EnableAsync`: Asynchronous email processing
  - `@SpringBootApplication`: Main application class

---

## Database Schema

### Core Tables and Relationships:

#### 1. Customers Table
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

#### 2. Admins Table
```sql
CREATE TABLE admins (
    admin_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    phone_number VARCHAR(255) NOT NULL,
    role VARCHAR(50) DEFAULT 'ADMIN',
    is_active BOOLEAN DEFAULT TRUE,
    created_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    last_login_date DATETIME
);
```

#### 3. Accounts Table
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

#### 4. Loans Table
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

#### 5. Transactions Table
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

#### 6. Fixed Deposits Table
```sql
CREATE TABLE fixed_deposits (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    fd_number VARCHAR(255) UNIQUE,
    customer_id BIGINT NOT NULL,
    principal_amount DECIMAL(15,2) NOT NULL,
    interest_rate DECIMAL(5,2),
    term_months INT,
    maturity_amount DECIMAL(15,2),
    start_date DATE,
    maturity_date DATE,
    status ENUM('ACTIVE', 'MATURED', 'CLOSED_PREMATURELY') DEFAULT 'ACTIVE',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (customer_id) REFERENCES customers(id)
);
```

#### 7. Recurring Deposits Table
```sql
CREATE TABLE recurring_deposits (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    rd_number VARCHAR(255) UNIQUE,
    customer_id BIGINT NOT NULL,
    monthly_amount DECIMAL(15,2) NOT NULL,
    interest_rate DECIMAL(5,2),
    term_months INT,
    maturity_amount DECIMAL(15,2),
    start_date DATE,
    maturity_date DATE,
    next_installment_date DATE,
    status ENUM('ACTIVE', 'MATURED', 'CLOSED_PREMATURELY') DEFAULT 'ACTIVE',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (customer_id) REFERENCES customers(id)
);
```

#### 8. Chat Messages Table
```sql
CREATE TABLE chat_messages (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    message_id VARCHAR(255) UNIQUE,
    customer_id BIGINT NOT NULL,
    admin_id BIGINT,
    message TEXT NOT NULL,
    sender_type ENUM('CUSTOMER', 'ADMIN') NOT NULL,
    sender_id BIGINT,
    is_read BOOLEAN DEFAULT FALSE,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
);
```

---

## Technology Stack

### Backend Technologies:
- **Java 17**: Programming language
- **Spring Boot 3.5.5**: Main framework
- **Spring Cloud 2025.0.0**: Microservices framework
- **Spring Security**: Authentication and authorization
- **Spring Data JPA**: Database operations
- **MySQL**: Primary database
- **JWT (JSON Web Tokens)**: Authentication tokens
- **Maven**: Build tool
- **Lombok**: Code generation

### Frontend Technologies:
- **Thymeleaf**: Server-side template engine
- **Bootstrap 5.3.2**: CSS framework
- **jQuery 3.7.1**: JavaScript library
- **Font Awesome 6.4.0**: Icons
- **HTML5/CSS3**: Markup and styling

### Microservices Components:
- **Netflix Eureka**: Service discovery
- **Spring Cloud Gateway**: API Gateway
- **Spring Boot Actuator**: Monitoring and health checks
- **Swagger/OpenAPI**: API documentation

### Email Service:
- **Spring Mail**: Email functionality
- **Thymeleaf**: Email templates
- **JavaMail**: SMTP integration

---

## Key Annotations and Their Usage

### Spring Boot Core Annotations:

#### @SpringBootApplication
```java
@SpringBootApplication
public class CustomerServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(CustomerServiceApplication.class, args);
    }
}
```
- **Purpose**: Combines @Configuration, @EnableAutoConfiguration, and @ComponentScan
- **Usage**: Main application class annotation

#### @EnableDiscoveryClient
```java
@EnableDiscoveryClient
public class CustomerServiceApplication {
    // Service registration with Eureka
}
```
- **Purpose**: Registers the service with Eureka server
- **Usage**: All microservices except Eureka server

#### @EnableAsync
```java
@EnableAsync
public class CustomerServiceApplication {
    // Enables asynchronous processing
}
```
- **Purpose**: Enables asynchronous method execution
- **Usage**: For email sending and other async operations

#### @EnableTransactionManagement
```java
@EnableTransactionManagement
public class CustomerServiceApplication {
    // Enables declarative transaction management
}
```
- **Purpose**: Enables Spring's transaction management
- **Usage**: For database transaction handling

### Security Annotations:

#### @EnableWebSecurity
```java
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    // Security configuration
}
```
- **Purpose**: Enables Spring Security
- **Usage**: Security configuration classes

#### @PreAuthorize
```java
@PreAuthorize("hasRole('ADMIN')")
public void adminOnlyMethod() {
    // Admin-only operations
}
```
- **Purpose**: Method-level security
- **Usage**: Restricting access to specific methods

### JPA/Hibernate Annotations:

#### @Entity
```java
@Entity
@Table(name = "customers")
public class Customer {
    // Entity definition
}
```
- **Purpose**: Marks class as JPA entity
- **Usage**: All database entity classes

#### @Table
```java
@Table(name = "customers")
public class Customer {
    // Maps to database table
}
```
- **Purpose**: Specifies database table name
- **Usage**: When table name differs from class name

#### @Id and @GeneratedValue
```java
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id;
```
- **Purpose**: Primary key definition
- **Usage**: All entity primary keys

#### @Column
```java
@Column(unique = true, nullable = false)
private String email;
```
- **Purpose**: Column mapping and constraints
- **Usage**: Custom column properties

#### @ManyToOne and @OneToMany
```java
@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "customer_id", nullable = false)
private Customer customer;

@OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
private List<Account> accounts;
```
- **Purpose**: Entity relationships
- **Usage**: Defining foreign key relationships

#### @Enumerated
```java
@Enumerated(EnumType.STRING)
private AccountType accountType;
```
- **Purpose**: Enum mapping
- **Usage**: Storing enum values in database

### Service Layer Annotations:

#### @Service
```java
@Service
@RequiredArgsConstructor
public class CustomerService {
    // Business logic
}
```
- **Purpose**: Marks class as service component
- **Usage**: All service layer classes

#### @Repository
```java
@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    // Data access methods
}
```
- **Purpose**: Marks interface as repository
- **Usage**: All data access interfaces

#### @Transactional
```java
@Transactional
public String transfer(String toAccount, BigDecimal amount) {
    // Transactional method
}
```
- **Purpose**: Declares method as transactional
- **Usage**: Methods that modify database state

### Controller Annotations:

#### @RestController
```java
@RestController
@RequestMapping("/api/customer")
public class CustomerController {
    // REST endpoints
}
```
- **Purpose**: REST controller with JSON responses
- **Usage**: API controllers

#### @Controller
```java
@Controller
@RequestMapping("/admin")
public class AdminController {
    // Web page controllers
}
```
- **Purpose**: Web controller with view rendering
- **Usage**: Controllers returning HTML pages

#### @RequestMapping
```java
@RequestMapping("/api/customer/account")
public class AccountController {
    // Base path for all methods
}
```
- **Purpose**: Base URL mapping
- **Usage**: Controller-level URL mapping

#### @GetMapping, @PostMapping, @PutMapping, @DeleteMapping
```java
@GetMapping("/balance")
public ResponseEntity<Map<String, Object>> getBalance() {
    // GET endpoint
}

@PostMapping("/deposit")
public ResponseEntity<Map<String, Object>> deposit(@RequestParam BigDecimal amount) {
    // POST endpoint
}
```
- **Purpose**: HTTP method mapping
- **Usage**: Specific endpoint methods

#### @RequestBody and @RequestParam
```java
@PostMapping("/transfer")
public ResponseEntity<String> transfer(@RequestBody TransferRequest request) {
    // JSON request body
}

@GetMapping("/balance")
public ResponseEntity<BigDecimal> getBalance(@RequestParam String accountNumber) {
    // Query parameter
}
```
- **Purpose**: Request data binding
- **Usage**: Extracting data from HTTP requests

### Validation Annotations:

#### @Valid
```java
@PostMapping("/register")
public ResponseEntity<String> register(@Valid @RequestBody CustomerRegistrationDTO dto) {
    // Validates request body
}
```
- **Purpose**: Triggers validation
- **Usage**: Request validation

#### @NotNull, @NotBlank, @Email
```java
public class CustomerRegistrationDTO {
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;
    
    @NotNull(message = "Amount is required")
    private BigDecimal amount;
}
```
- **Purpose**: Field validation
- **Usage**: DTO validation

### Configuration Annotations:

#### @Configuration
```java
@Configuration
public class SecurityConfig {
    // Configuration class
}
```
- **Purpose**: Marks class as configuration
- **Usage**: Configuration classes

#### @Bean
```java
@Bean
public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
}
```
- **Purpose**: Defines Spring bean
- **Usage**: Creating Spring-managed objects

#### @Value
```java
@Value("${spring.mail.username}")
private String fromEmail;
```
- **Purpose**: Injects property values
- **Usage**: Configuration property injection

### Lombok Annotations:

#### @Data
```java
@Data
public class Customer {
    // Generates getters, setters, toString, equals, hashCode
}
```
- **Purpose**: Generates boilerplate code
- **Usage**: Entity and DTO classes

#### @RequiredArgsConstructor
```java
@Service
@RequiredArgsConstructor
public class CustomerService {
    private final CustomerRepository customerRepository;
    // Constructor injection
}
```
- **Purpose**: Generates constructor for final fields
- **Usage**: Dependency injection

#### @NoArgsConstructor and @AllArgsConstructor
```java
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessage {
    // Constructors
}
```
- **Purpose**: Generates constructors
- **Usage**: Entity classes

---

## Business Logic Flow

### 1. Customer Registration and Authentication Flow:

```
1. Customer Registration:
   Customer → POST /api/auth/register → CustomerService.register()
   → Password Encryption → Database Save → Welcome Email

2. Customer Login:
   Customer → POST /api/auth/login → AuthenticationManager
   → JWT Token Generation → Token Response

3. JWT Authentication:
   Request → JwtAuthenticationFilter → Token Validation
   → SecurityContext → Authenticated Request
```

### 2. Account Management Flow:

```
1. Deposit:
   Customer → POST /api/customer/account/deposit → AccountService.deposit()
   → Transaction Creation → Balance Update → Transaction Record

2. Withdrawal:
   Customer → POST /api/customer/account/withdraw → AccountService.withdraw()
   → Balance Check → Transaction Creation → Balance Update

3. Transfer:
   Customer → POST /api/customer/account/transfer → AccountService.transfer()
   → Source Account Check → Destination Account Validation
   → Transaction Creation → Both Account Updates
```

### 3. Loan Application Flow:

```
1. Loan Application:
   Customer → POST /api/customer/loans/apply → LoanService.applyForLoan()
   → EMI Calculation → Interest Rate Assignment → Database Save
   → Email Notification → Admin Notification

2. Loan Approval:
   Admin → POST /api/admin/loans/approve → LoanService.approveLoan()
   → Status Update → Email Notification → Customer Notification

3. Loan Rejection:
   Admin → POST /api/admin/loans/reject → LoanService.rejectLoan()
   → Status Update → Email Notification → Customer Notification
```

### 4. Investment Management Flow:

```
1. Fixed Deposit:
   Customer → POST /api/customer/deposits/fd → DepositService.createFD()
   → Maturity Calculation → Database Save → Email Confirmation

2. Recurring Deposit:
   Customer → POST /api/customer/deposits/rd → DepositService.createRD()
   → Installment Schedule → Database Save → Email Confirmation
```

### 5. Chat Support Flow:

```
1. Customer Message:
   Customer → POST /api/customer/chat/send → ChatService.sendMessage()
   → Message Storage → Admin Notification

2. Admin Response:
   Admin → POST /api/admin/chat/respond → ChatService.respondToCustomer()
   → Message Storage → Customer Notification
```

---

## Security Implementation

### 1. JWT Authentication:

#### JWT Token Provider:
```java
@Component
public class JwtTokenProvider {
    public String generateToken(Authentication authentication) {
        // Token generation logic
    }
    
    public boolean validateToken(String token) {
        // Token validation logic
    }
    
    public String getUsernameFromJWT(String token) {
        // Username extraction
    }
}
```

#### JWT Authentication Filter:
```java
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, 
                                  HttpServletResponse response, 
                                  FilterChain filterChain) {
        // JWT validation and authentication setup
    }
}
```

### 2. Security Configuration:

#### Customer Service Security:
```java
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) {
        return http
            .cors().and().csrf().disable()
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .authorizeHttpRequests()
            .requestMatchers("/api/auth/**").permitAll()
            .requestMatchers("/api/customer/**").authenticated()
            .anyRequest().authenticated()
            .and()
            .addFilterBefore(jwtAuthenticationFilter, 
                           UsernamePasswordAuthenticationFilter.class)
            .build();
    }
}
```

### 3. Password Encryption:
```java
@Bean
public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
}
```

---

## API Endpoints

### Customer Service Endpoints:

#### Authentication:
- `POST /api/auth/register` - Customer registration
- `POST /api/auth/login` - Customer login
- `GET /api/auth/validate-session` - Session validation

#### Account Management:
- `GET /api/customer/account/balance` - Get account balance
- `POST /api/customer/account/deposit` - Deposit money
- `POST /api/customer/account/withdraw` - Withdraw money
- `POST /api/customer/account/transfer` - Transfer money

#### Loan Management:
- `POST /api/customer/loans/apply` - Apply for loan
- `GET /api/customer/loans/my-loans` - Get customer loans
- `GET /api/customer/loans/calculate-emi` - Calculate EMI

#### Investment Management:
- `POST /api/customer/deposits/fd` - Create fixed deposit
- `POST /api/customer/deposits/rd` - Create recurring deposit
- `GET /api/customer/deposits/my-investments` - Get investments

#### Chat Support:
- `POST /api/customer/chat/send` - Send message
- `GET /api/customer/chat/history` - Get chat history
- `GET /api/customer/chat/new-messages` - Get new messages

### Admin Service Endpoints:

#### Authentication:
- `POST /api/admin/login` - Admin login
- `POST /api/admin/register` - Admin registration

#### Customer Management:
- `GET /api/admin/customers` - Get all customers
- `GET /api/admin/customers/{id}` - Get customer details
- `PUT /api/admin/customers/{id}/status` - Update customer status

#### Loan Management:
- `GET /api/admin/loans/pending` - Get pending loans
- `POST /api/admin/loans/{id}/approve` - Approve loan
- `POST /api/admin/loans/{id}/reject` - Reject loan

#### Transaction Management:
- `GET /api/admin/transactions` - Get all transactions
- `GET /api/admin/transactions/customer/{id}` - Get customer transactions

#### Chat Management:
- `GET /api/admin/chat/messages` - Get all chat messages
- `POST /api/admin/chat/respond` - Respond to customer

### Email Service Endpoints:

#### Email Operations:
- `POST /api/emails/send` - Send custom email
- `POST /api/emails/welcome` - Send welcome email
- `POST /api/emails/loan-approval` - Send loan approval email
- `POST /api/emails/loan-rejection` - Send loan rejection email

---

## Frontend Components

### 1. Customer Portal:

#### Dashboard:
- Account balance display
- Recent transactions
- Quick actions (deposit, withdraw, transfer)
- Loan status overview
- Investment summary

#### Account Management:
- Balance inquiry
- Transaction history
- Fund transfer interface
- Account statements

#### Loan Management:
- Loan application form
- EMI calculator
- Loan status tracking
- Payment history

#### Investment Management:
- FD/RD application forms
- Investment calculator
- Maturity tracking
- Investment history

#### Chat Support:
- Real-time chat interface
- Message history
- File attachment support
- Status indicators

### 2. Admin Dashboard:

#### Overview:
- Customer statistics
- Loan approval queue
- Transaction monitoring
- System health status

#### Customer Management:
- Customer list and search
- Customer details view
- Account management
- Status updates

#### Loan Management:
- Pending loan applications
- Approval/rejection interface
- Loan details view
- Admin remarks

#### Transaction Monitoring:
- Transaction logs
- Suspicious activity alerts
- Customer transaction history
- Financial reports

#### Chat Management:
- Customer support queue
- Message response interface
- Chat history
- Priority management

---

## Email Service Integration

### 1. Email Templates:

#### Welcome Email:
```html
<!DOCTYPE html>
<html>
<head>
    <title>Welcome to MyFin Bank</title>
</head>
<body>
    <h1>Welcome {{customerName}}!</h1>
    <p>Your account has been successfully created.</p>
    <p>Account Number: {{accountNumber}}</p>
</body>
</html>
```

#### Loan Approval Email:
```html
<!DOCTYPE html>
<html>
<head>
    <title>Loan Approved</title>
</head>
<body>
    <h1>Congratulations {{customerName}}!</h1>
    <p>Your {{loanType}} loan application has been approved.</p>
    <p>Approved Amount: ₹{{approvedAmount}}</p>
    <p>Monthly EMI: ₹{{monthlyEmi}}</p>
</body>
</html>
```

### 2. Email Service Implementation:

#### Email Service:
```java
@Service
public class EmailService {
    @Async("emailTaskExecutor")
    public CompletableFuture<Void> sendHtmlEmail(EmailRequest request) {
        // Async email sending
    }
    
    public void sendWelcomeEmail(String to, String customerName) {
        // Welcome email logic
    }
}
```

#### Template Service:
```java
@Service
public class TemplateService {
    public String processTemplate(String templateName, Map<String, Object> variables) {
        // Template processing with Thymeleaf
    }
}
```

---

## Deployment and Configuration

### 1. Application Properties:

#### Customer Service (application-dev.yml):
```yaml
server:
  port: 8081

spring:
  application:
    name: customer-service
  datasource:
    url: jdbc:mysql://localhost:3306/myfin_bank
    username: root
    password: password
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true

eureka:
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka
```

#### Admin Service (application-dev.yml):
```yaml
server:
  port: 8082

spring:
  application:
    name: admin-service
  datasource:
    url: jdbc:mysql://localhost:3306/myfin_bank
    username: root
    password: password

eureka:
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka
```

#### Email Service (application-dev.yml):
```yaml
server:
  port: 8083

spring:
  application:
    name: email-service
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

eureka:
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka
```

### 2. Startup Sequence:

1. **Start MySQL Database** (Port 3306)
2. **Start Eureka Server** (Port 8761)
3. **Start Email Service** (Port 8083)
4. **Start Customer Service** (Port 8081)
5. **Start Admin Service** (Port 8082)
6. **Start API Gateway** (Port 8080)

### 3. Access URLs:

- **API Gateway**: http://localhost:8080
- **Customer Portal**: http://localhost:8081
- **Admin Dashboard**: http://localhost:8082
- **Eureka Dashboard**: http://localhost:8761
- **Swagger Documentation**: 
  - Customer Service: http://localhost:8081/swagger-ui.html
  - Admin Service: http://localhost:8082/swagger-ui.html
  - Email Service: http://localhost:8083/swagger-ui.html

---

## Conclusion

MyFinn Bank is a comprehensive digital banking solution built with modern microservices architecture. It demonstrates:

- **Scalable Architecture**: Microservices with service discovery
- **Security**: JWT-based authentication and authorization
- **Real-time Communication**: Chat support system
- **Automated Notifications**: Email service integration
- **Modern UI**: Responsive web interface
- **Database Design**: Normalized schema with proper relationships
- **API Documentation**: Swagger/OpenAPI integration

The project showcases enterprise-level development practices and can serve as a foundation for real-world banking applications.

---

*This documentation provides a comprehensive overview of the MyFinn Bank project, covering all aspects from architecture to implementation details.*
