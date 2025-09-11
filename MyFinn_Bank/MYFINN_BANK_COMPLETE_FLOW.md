# 🏦 MyFinn Bank - Complete Application Flow

## 📋 **Complete Flow: Frontend → Server → JWT → Controller → Service → Repository**

---

## 🔐 **1. AUTHENTICATION FLOW**

### **Frontend Login Process:**
```
1. User enters credentials in login form
2. JavaScript captures form data
3. Fetch API call to /api/auth/login
4. Credentials sent as JSON in request body
```

### **Backend Authentication Flow:**
```
1. Controller receives login request
2. AuthenticationManager validates credentials
3. JWT Token generated if valid
4. Token stored in cookies and returned to frontend
5. Frontend stores token in sessionStorage/localStorage
```

### **JWT Token Flow:**
```
1. Token generated with user details and expiration
2. Token sent as HTTP-only cookie for security
3. Token also returned in response body for AJAX calls
4. Subsequent requests include token in Authorization header
5. JwtAuthenticationFilter validates token on each request
```

---

## 🏗️ **2. LAYERED ARCHITECTURE FLOW**

### **Controller Layer (Presentation Layer):**
```java
@RestController
@RequestMapping("/api/customer/account")
public class AccountController {
    
    private final AccountService accountService; // Dependency Injection
    
    @GetMapping("/balance")
    public ResponseEntity<Map<String, Object>> getBalance() {
        // 1. Receive HTTP request
        // 2. Extract user info from JWT token
        // 3. Call service layer
        // 4. Return HTTP response
        BigDecimal balance = accountService.getCurrentBalance();
        return ResponseEntity.ok(Map.of("balance", balance));
    }
}
```

### **Service Layer (Business Logic Layer):**
```java
@Service
@Transactional
public class AccountService {
    
    private final AccountRepository accountRepository; // Dependency Injection
    
    public BigDecimal getCurrentBalance() {
        // 1. Get current authenticated user
        // 2. Business logic validation
        // 3. Call repository layer
        // 4. Process and return data
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        Account account = accountRepository.findByCustomerEmail(email);
        return account.getBalance();
    }
}
```

### **Repository Layer (Data Access Layer):**
```java
@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    
    // 1. Spring Data JPA provides CRUD operations
    // 2. Custom query methods
    // 3. Database interaction
    Account findByCustomerEmail(String email);
}
```

---

## 🔄 **3. COMPLETE REQUEST FLOW**

### **Step-by-Step Process:**

#### **Step 1: Frontend Request**
```javascript
// Frontend JavaScript
const response = await fetch('/api/customer/account/balance', {
    method: 'GET',
    headers: {
        'Authorization': `Bearer ${token}`,
        'Content-Type': 'application/json'
    }
});
```

#### **Step 2: Security Filter**
```java
// JwtAuthenticationFilter
@Override
protected void doFilterInternal(HttpServletRequest request, 
                              HttpServletResponse response, 
                              FilterChain filterChain) {
    // 1. Extract JWT token from request
    String jwt = getJwtFromRequest(request);
    
    // 2. Validate token
    if (jwtTokenProvider.validateToken(jwt)) {
        // 3. Set authentication in SecurityContext
        String username = jwtTokenProvider.getUsernameFromJWT(jwt);
        UserDetails userDetails = customerService.loadUserByUsername(username);
        Authentication authentication = new UsernamePasswordAuthenticationToken(
            userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
    
    // 4. Continue to controller
    filterChain.doFilter(request, response);
}
```

#### **Step 3: Controller Processing**
```java
@GetMapping("/balance")
public ResponseEntity<Map<String, Object>> getBalance() {
    // 1. Method automatically called by Spring MVC
    // 2. SecurityContext contains authenticated user
    // 3. Call service layer
    BigDecimal balance = accountService.getCurrentBalance();
    
    // 4. Return JSON response
    return ResponseEntity.ok(Map.of("balance", balance));
}
```

#### **Step 4: Service Layer Business Logic**
```java
public BigDecimal getCurrentBalance() {
    // 1. Get authenticated user from SecurityContext
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    String email = auth.getName();
    
    // 2. Business logic validation
    if (email == null) {
        throw new AuthenticationException("User not authenticated");
    }
    
    // 3. Call repository
    Account account = accountRepository.findByCustomerEmail(email);
    
    // 4. Process data and return
    return account.getBalance();
}
```

#### **Step 5: Repository Data Access**
```java
// Spring Data JPA automatically implements this method
Account findByCustomerEmail(String email);

// Equivalent to SQL:
// SELECT * FROM accounts WHERE customer_email = ?
```

#### **Step 6: Database Interaction**
```sql
-- Hibernate generates and executes:
SELECT * FROM accounts WHERE customer_email = 'user@example.com';
```

#### **Step 7: Response Flow Back**
```
Database → Repository → Service → Controller → JSON Response → Frontend
```

---

## 🏛️ **4. MICROSERVICES ARCHITECTURE**

### **Service Communication:**
```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   API Gateway   │    │  Eureka Server  │    │  Email Service  │
│   (Port 8080)   │    │   (Port 8761)   │    │   (Port 8083)   │
└─────────────────┘    └─────────────────┘    └─────────────────┘
         │                       │                       │
         │                       │                       │
┌─────────────────┐    ┌─────────────────┐              │
│ Customer Service│    │  Admin Service  │              │
│   (Port 8081)   │    │   (Port 8082)   │              │
└─────────────────┘    └─────────────────┘              │
         │                       │                       │
         └───────────────────────┼───────────────────────┘
                                 │
                    ┌─────────────────┐
                    │   MySQL DB      │
                    │   (Port 3306)   │
                    └─────────────────┘
```

### **Inter-Service Communication:**
```java
// Customer Service calling Email Service
@Service
public class CustomerService {
    
    @Autowired
    private RestTemplate restTemplate;
    
    public void sendWelcomeEmail(String email) {
        // 1. Prepare email data
        Map<String, Object> emailData = Map.of(
            "to", email,
            "subject", "Welcome to MyFinn Bank",
            "template", "welcome-email"
        );
        
        // 2. Call Email Service via RestTemplate
        restTemplate.postForObject(
            "http://email-service/api/email/send", 
            emailData, 
            String.class
        );
    }
}
```

---

## 🔧 **5. KEY ANNOTATIONS AND THEIR ROLES**

### **Controller Annotations:**
```java
@RestController          // REST API controller
@RequestMapping("/api")  // Base URL mapping
@GetMapping("/balance")  // HTTP GET endpoint
@PostMapping("/deposit") // HTTP POST endpoint
@RequestBody            // JSON request body
@RequestParam           // Query parameters
@PathVariable           // URL path variables
```

### **Service Annotations:**
```java
@Service                // Business logic component
@Transactional          // Database transaction management
@Async                  // Asynchronous method execution
@Lazy                   // Lazy initialization
```

### **Repository Annotations:**
```java
@Repository             // Data access layer
@Query                  // Custom SQL queries
@Modifying              // Data modification queries
```

### **Security Annotations:**
```java
@EnableWebSecurity      // Security configuration
@PreAuthorize           // Method-level security
@Secured                // Role-based access
```

---

## 📊 **6. DATABASE FLOW**

### **Entity Relationships:**
```java
@Entity
@Table(name = "customers")
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    private List<Account> accounts;
    
    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    private List<Loan> loans;
}

@Entity
@Table(name = "accounts")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;
}
```

### **Database Operations:**
```java
// Create
customerRepository.save(customer);

// Read
Optional<Customer> customer = customerRepository.findById(id);
List<Customer> customers = customerRepository.findAll();

// Update
customer.setEmail("new@email.com");
customerRepository.save(customer);

// Delete
customerRepository.deleteById(id);
```

---

## 🚀 **7. COMPLETE EXAMPLE: DEPOSIT MONEY**

### **Frontend:**
```javascript
// 1. User clicks deposit button
// 2. Form data captured
const depositData = { amount: 1000 };

// 3. API call made
const response = await fetch('/api/customer/account/deposit', {
    method: 'POST',
    headers: {
        'Authorization': `Bearer ${token}`,
        'Content-Type': 'application/json'
    },
    body: JSON.stringify(depositData)
});
```

### **Backend Flow:**
```java
// 1. Controller receives request
@PostMapping("/deposit")
public ResponseEntity<Map<String, Object>> deposit(@RequestParam BigDecimal amount) {
    // 2. Call service layer
    Transaction transaction = accountService.deposit(amount);
    
    // 3. Return response
    return ResponseEntity.ok(Map.of(
        "success", true,
        "transaction", transaction
    ));
}

// 4. Service layer processes
@Service
public class AccountService {
    public Transaction deposit(BigDecimal amount) {
        // 5. Get authenticated user
        String email = getCurrentUserEmail();
        
        // 6. Get account from repository
        Account account = accountRepository.findByCustomerEmail(email);
        
        // 7. Business logic
        account.setBalance(account.getBalance().add(amount));
        
        // 8. Save to database
        accountRepository.save(account);
        
        // 9. Create transaction record
        Transaction transaction = new Transaction();
        transaction.setAmount(amount);
        transaction.setType(TransactionType.DEPOSIT);
        transactionRepository.save(transaction);
        
        return transaction;
    }
}
```

---

## 🔒 **8. SECURITY FLOW**

### **JWT Token Lifecycle:**
```
1. Login Request → AuthenticationManager → JWT Generation
2. Token stored in HTTP-only cookie + sessionStorage
3. Each request includes token in Authorization header
4. JwtAuthenticationFilter validates token
5. SecurityContext populated with user details
6. Controller methods access authenticated user
7. Token expires after 24 hours
```

### **Authorization Flow:**
```
1. User makes request with JWT token
2. Filter validates token and extracts user info
3. SecurityContext contains user authorities
4. @PreAuthorize checks user permissions
5. Method executed if authorized
6. Response returned to user
```

---

## 📈 **9. ERROR HANDLING FLOW**

### **Exception Handling:**
```java
@ControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Map<String, Object>> handleAuthException(AuthenticationException e) {
        return ResponseEntity.status(401).body(Map.of(
            "error", "Authentication failed",
            "message", e.getMessage()
        ));
    }
    
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<Map<String, Object>> handleValidationException(ValidationException e) {
        return ResponseEntity.status(400).body(Map.of(
            "error", "Validation failed",
            "message", e.getMessage()
        ));
    }
}
```

---

## 🎯 **10. SUMMARY OF COMPLETE FLOW**

### **Request Journey:**
```
Frontend (JavaScript) 
    ↓ (HTTP Request with JWT)
API Gateway (Port 8080)
    ↓ (Route to service)
Customer Service (Port 8081)
    ↓ (JWT Filter validates token)
Controller Layer (@RestController)
    ↓ (Business logic call)
Service Layer (@Service)
    ↓ (Data access call)
Repository Layer (@Repository)
    ↓ (SQL query execution)
MySQL Database (Port 3306)
    ↓ (Data returned)
Repository → Service → Controller → JSON Response → Frontend
```

### **Key Components:**
- **Frontend**: HTML/CSS/JavaScript with fetch API
- **API Gateway**: Spring Cloud Gateway for routing
- **Microservices**: Customer, Admin, Email services
- **Security**: JWT tokens with Spring Security
- **Database**: MySQL with JPA/Hibernate
- **Service Discovery**: Eureka Server
- **Communication**: RestTemplate for inter-service calls

This architecture provides **scalability**, **security**, **maintainability**, and **separation of concerns** following Spring Boot best practices! 🚀
