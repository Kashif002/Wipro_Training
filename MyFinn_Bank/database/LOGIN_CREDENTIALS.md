# MyFin Bank - Login Credentials & Test Data

## 🔐 Admin Login Credentials

| Email | Password | Name | Role |
|-------|----------|------|------|
| **admin@myfinbank.com** | **Admin@123** | John Smith | ADMIN |
| manager@myfinbank.com | Admin@123 | Sarah Johnson | ADMIN |
| support@myfinbank.com | Admin@123 | Mike Williams | ADMIN |

## 👤 Customer Login Credentials

| Email | Password | Name | Customer ID | Account Balance |
|-------|----------|------|-------------|-----------------|
| **rajesh.kumar@gmail.com** | **Customer@123** | Rajesh Kumar | CUST001 | ₹200,000 (2 accounts) |
| priya.sharma@gmail.com | Customer@123 | Priya Sharma | CUST002 | ₹275,000 (2 accounts) |
| amit.patel@gmail.com | Customer@123 | Amit Patel | CUST003 | ₹100,000 (1 account) |
| sneha.verma@gmail.com | Customer@123 | Sneha Verma | CUST004 | ₹125,000 (2 accounts) |
| vikram.singh@gmail.com | Customer@123 | Vikram Singh | CUST005 | ₹750,000 (2 accounts) |

## 📊 Test Data Summary

### Database: `myfin_bank`

| Table | Records | Description |
|-------|---------|-------------|
| **admins** | 3 | Admin users for dashboard access |
| **customers** | 5 | Customer accounts with verified emails |
| **accounts** | 9 | Bank accounts (Savings, Current, Salary) |
| **loans** | 6 | Various loan applications in different states |
| **transactions** | 10 | Sample transactions including transfers, deposits, withdrawals |
| **fixed_deposits** | 5 | FD investments with different tenures |
| **recurring_deposits** | 5 | RD investments with monthly deposits |
| **chat_messages** | 9 | Customer support chat history |

## 🎯 Quick Test Scenarios

### Admin Dashboard Testing
1. **Login as Admin**: Use `admin@myfinbank.com` / `Admin@123`
2. **View Dashboard**: Check statistics for customers, loans, transactions
3. **Loan Management**: 
   - LOAN003 is "Under Review" - can be approved/rejected
   - LOAN004 is "Applied" - needs processing
   - LOAN001 is "Disbursed" - fully processed
4. **Customer Management**: View and manage 5 active customers
5. **Chat Support**: 2 unread messages waiting for response

### Customer Portal Testing
1. **Login as Customer**: Use `rajesh.kumar@gmail.com` / `Customer@123`
2. **View Accounts**: 
   - Savings Account: ₹50,000
   - Current Account: ₹150,000
3. **Loan Status**: 
   - LOAN001: Disbursed (₹5,00,000)
   - LOAN006: Applied (₹2,00,000)
4. **Investments**:
   - Fixed Deposit: ₹1,00,000
   - Recurring Deposit: ₹5,000/month
5. **Transaction History**: View recent transactions

## 🔧 How to Use

### Step 1: Create Database
```sql
-- Run the complete setup script
mysql -u root -p < complete_setup_myfin_bank.sql
```

### Step 2: Update Application Configuration
Update your `application-dev.yml` in both services:

```yaml
spring:
  datasource:
    url: jdbc:mysql://127.0.0.1:3306/myfin_bank  # Changed from myfinbank
    username: Your DB Username
    password: Your DB Password
```

### Step 3: Start Services
1. Start Eureka Server (port 8761)
2. Start Customer Service (port 8081)
3. Start Admin Service (port 8082)
4. Start API Gateway (port 8080)

### Step 4: Access Applications
- **Admin Dashboard**: http://localhost:8082/login
- **Customer Portal**: http://localhost:8081/login
- **API Gateway**: http://localhost:8080

## 📝 Important Notes

1. **Password Encoding**: All passwords are BCrypt encoded in the database
   - Admin passwords: BCrypt hash of "Admin@123"
   - Customer passwords: BCrypt hash of "Customer@123"

2. **Test Data Relationships**:
   - Each customer has at least one account
   - Loans are linked to customers
   - Transactions are linked to accounts
   - Chat messages show customer-admin communication

3. **Sample Business Scenarios**:
   - Rajesh has an active personal loan and applied for another
   - Priya has an approved home loan waiting for disbursement
   - Vikram's business loan was rejected
   - Multiple customers have active FDs and RDs

## 🚀 Quick Commands

```bash
# Check if the database is created
mysql -u root -p"password" -e "USE myfin_bank; SHOW TABLES;" //Replace password with your DB Password("remove double quotes")

# Verify admin users
mysql -u root -p"password" -e "USE myfin_bank; SELECT email, first_name, last_name FROM admins;"

# Verify customer users
mysql -u root -p"password" -e "USE myfin_bank; SELECT email, first_name, last_name, active FROM customers;"

# Check loan applications
mysql -u root -p"password" -e "USE myfin_bank; SELECT loan_id, status, requested_amount FROM loans;"
```
