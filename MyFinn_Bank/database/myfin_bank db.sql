-- =====================================================
-- Complete Database Setup for MyFin Bank
-- Version: 1.0
-- Description: Creates all tables with proper structure and dummy data
-- =====================================================

-- Create and use the new database
DROP DATABASE IF EXISTS myfin_bank;
CREATE DATABASE myfin_bank;
USE myfin_bank;

-- =====================================================
-- Table 1: CUSTOMERS
-- =====================================================
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

-- =====================================================
-- Table 2: ADMINS
-- =====================================================
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

-- =====================================================
-- Table 3: ACCOUNTS
-- =====================================================
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

-- =====================================================
-- Table 4: LOANS
-- =====================================================
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

-- =====================================================
-- Table 5: TRANSACTIONS
-- =====================================================
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

-- =====================================================
-- Table 6: FIXED_DEPOSITS
-- =====================================================
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

-- =====================================================
-- Table 7: RECURRING_DEPOSITS
-- =====================================================
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

-- =====================================================
-- Table 8: CHAT_MESSAGES
-- =====================================================
CREATE TABLE chat_messages (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    customer_id BIGINT NOT NULL,
    admin_id BIGINT,
    message TEXT NOT NULL,
    sender_type ENUM('CUSTOMER', 'ADMIN') NOT NULL,
    sender_id BIGINT,
    is_read BOOLEAN DEFAULT FALSE,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (customer_id) REFERENCES customers(id),
    FOREIGN KEY (admin_id) REFERENCES admins(admin_id)
);

-- =====================================================
-- DUMMY DATA INSERTION
-- =====================================================

-- Insert Admin Users
-- Password for all admins: Admin@123 (you should encode this with BCrypt in your application)
-- BCrypt encoded password for 'Admin@123': $2a$10$EixZaYVK1fsbw1ZfbX3OXePaWxn96p36vbaG3s5rJVmqB8zDmvJ3O
INSERT INTO admins (email, password, first_name, last_name, phone_number, role, is_active) VALUES
('admin@myfinbank.com', '$2a$10$EixZaYVK1fsbw1ZfbX3OXePaWxn96p36vbaG3s5rJVmqB8zDmvJ3O', 'John', 'Smith', '9876543210', 'ADMIN', TRUE),
('manager@myfinbank.com', '$2a$10$EixZaYVK1fsbw1ZfbX3OXePaWxn96p36vbaG3s5rJVmqB8zDmvJ3O', 'Sarah', 'Johnson', '9876543211', 'ADMIN', TRUE),
('support@myfinbank.com', '$2a$10$EixZaYVK1fsbw1ZfbX3OXePaWxn96p36vbaG3s5rJVmqB8zDmvJ3O', 'Mike', 'Williams', '9876543212', 'ADMIN', TRUE);

-- Insert Customer Users
-- Password for all customers: Customer@123
-- BCrypt encoded password for 'Customer@123': $2a$10$N9qo8uLOickgx2ZMRZoHK.IjQ1vL6lkRA5rkdQxLZFmPQzFfBJ7XS
INSERT INTO customers (customer_id, email, password, first_name, last_name, phone, address, active, email_verified) VALUES
('CUST001', 'rajesh.kumar@gmail.com', '$2a$10$N9qo8uLOickgx2ZMRZoHK.IjQ1vL6lkRA5rkdQxLZFmPQzFfBJ7XS', 'Rajesh', 'Kumar', '9988776655', '123 Main Street, Mumbai, Maharashtra 400001', TRUE, TRUE),
('CUST002', 'priya.sharma@gmail.com', '$2a$10$N9qo8uLOickgx2ZMRZoHK.IjQ1vL6lkRA5rkdQxLZFmPQzFfBJ7XS', 'Priya', 'Sharma', '9988776656', '456 Park Avenue, Delhi, Delhi 110001', TRUE, TRUE),
('CUST003', 'amit.patel@gmail.com', '$2a$10$N9qo8uLOickgx2ZMRZoHK.IjQ1vL6lkRA5rkdQxLZFmPQzFfBJ7XS', 'Amit', 'Patel', '9988776657', '789 Lake View, Bangalore, Karnataka 560001', TRUE, TRUE),
('CUST004', 'sneha.verma@gmail.com', '$2a$10$N9qo8uLOickgx2ZMRZoHK.IjQ1vL6lkRA5rkdQxLZFmPQzFfBJ7XS', 'Sneha', 'Verma', '9988776658', '321 Green Park, Chennai, Tamil Nadu 600001', TRUE, TRUE),
('CUST005', 'vikram.singh@gmail.com', '$2a$10$N9qo8uLOickgx2ZMRZoHK.IjQ1vL6lkRA5rkdQxLZFmPQzFfBJ7XS', 'Vikram', 'Singh', '9988776659', '654 River Side, Pune, Maharashtra 411001', TRUE, TRUE);

-- Insert Accounts for Customers
INSERT INTO accounts (account_number, customer_id, balance, account_type, status) VALUES
('ACC10001001', 1, 50000.00, 'SAVINGS', 'ACTIVE'),
('ACC10001002', 1, 150000.00, 'CURRENT', 'ACTIVE'),
('ACC10002001', 2, 75000.00, 'SAVINGS', 'ACTIVE'),
('ACC10002002', 2, 200000.00, 'SALARY', 'ACTIVE'),
('ACC10003001', 3, 100000.00, 'SAVINGS', 'ACTIVE'),
('ACC10004001', 4, 45000.00, 'SAVINGS', 'ACTIVE'),
('ACC10004002', 4, 80000.00, 'CURRENT', 'ACTIVE'),
('ACC10005001', 5, 250000.00, 'SALARY', 'ACTIVE'),
('ACC10005002', 5, 500000.00, 'SAVINGS', 'ACTIVE');

-- Insert Loan Applications
INSERT INTO loans (loan_id, customer_id, loan_type, requested_amount, approved_amount, interest_rate, term_months, monthly_emi, status, purpose, monthly_income, employment_details, approved_by, admin_remarks, processed_at, approved_date) VALUES
('LOAN001', 1, 'PERSONAL', 500000.00, 500000.00, 10.50, 60, 10748.00, 'DISBURSED', 'Home Renovation', 75000.00, 'Software Engineer at TCS, 5 years experience', 1, 'Good credit history, approved', NOW(), NOW()),
('LOAN002', 2, 'HOME', 5000000.00, 4500000.00, 8.50, 240, 44228.00, 'APPROVED', 'Purchase of 3BHK Flat', 150000.00, 'Senior Manager at Infosys, 10 years experience', 1, 'Approved with 90% of requested amount', NOW(), NOW()),
('LOAN003', 3, 'VEHICLE', 800000.00, NULL, 9.00, 60, NULL, 'UNDER_REVIEW', 'Purchase of Honda City', 60000.00, 'Marketing Executive at Wipro, 3 years', NULL, NULL, NULL, NULL),
('LOAN004', 4, 'EDUCATION', 300000.00, NULL, 11.00, 48, NULL, 'APPLIED', 'MBA from IIM', 45000.00, 'Junior Analyst at Deloitte, 2 years', NULL, NULL, NULL, NULL),
('LOAN005', 5, 'BUSINESS', 2000000.00, NULL, 12.00, 84, NULL, 'REJECTED', 'Startup - Tech Company', 200000.00, 'Self Employed, Running IT Consultancy', 2, 'Insufficient collateral', NOW(), NULL),
('LOAN006', 1, 'PERSONAL', 200000.00, NULL, 10.50, 36, NULL, 'APPLIED', 'Medical Emergency', 75000.00, 'Software Engineer at TCS, 5 years experience', NULL, NULL, NULL, NULL);

ALTER TABLE loans 
MODIFY COLUMN status ENUM('APPLIED', 'UNDER_REVIEW', 'APPROVED', 'REJECTED', 'DISBURSED', 'PENDING') 
DEFAULT 'APPLIED';

UPDATE loans
SET status = 'PENDING'
WHERE loan_id = 'LOAN001';

describe loans;

-- Insert Transactions
INSERT INTO transactions (transaction_id, from_account_id, to_account_id, amount, type, status, description, reference) VALUES
('TXN202401001', NULL, 1, 10000.00, 'DEPOSIT', 'COMPLETED', 'Cash Deposit at Branch', 'REF001'),
('TXN202401002', 1, NULL, 5000.00, 'WITHDRAWAL', 'COMPLETED', 'ATM Withdrawal', 'REF002'),
('TXN202401003', 1, 3, 2000.00, 'TRANSFER', 'COMPLETED', 'Transfer to Priya Sharma', 'REF003'),
('TXN202401004', 2, 5, 15000.00, 'TRANSFER', 'COMPLETED', 'Payment for Services', 'REF004'),
('TXN202401005', NULL, 4, 25000.00, 'DEPOSIT', 'COMPLETED', 'Salary Credit', 'REF005'),
('TXN202401006', 5, NULL, 10000.00, 'WITHDRAWAL', 'COMPLETED', 'Cash Withdrawal', 'REF006'),
('TXN202401007', 6, 7, 5000.00, 'TRANSFER', 'COMPLETED', 'Rent Payment', 'REF007'),
('TXN202401008', 8, NULL, 20000.00, 'WITHDRAWAL', 'FAILED', 'Insufficient Balance', 'REF008'),
('TXN202401009', NULL, 9, 100000.00, 'DEPOSIT', 'COMPLETED', 'Fixed Deposit Maturity', 'REF009'),
('TXN202401010', 3, NULL, 8000.00, 'LOAN_PAYMENT', 'COMPLETED', 'EMI Payment - LOAN001', 'REF010');

-- Insert Fixed Deposits
INSERT INTO fixed_deposits (fd_number, customer_id, principal_amount, interest_rate, term_months, maturity_amount, start_date, maturity_date, status) VALUES
('FD001001', 1, 100000.00, 7.50, 12, 107500.00, '2024-01-01', '2025-01-01', 'ACTIVE'),
('FD002001', 2, 250000.00, 7.75, 24, 289375.00, '2023-12-01', '2025-12-01', 'ACTIVE'),
('FD003001', 3, 500000.00, 8.00, 36, 644000.00, '2023-06-01', '2026-06-01', 'ACTIVE'),
('FD004001', 4, 50000.00, 7.25, 6, 51812.50, '2024-01-15', '2024-07-15', 'MATURED'),
('FD005001', 5, 1000000.00, 8.25, 60, 1512500.00, '2022-01-01', '2027-01-01', 'ACTIVE');

-- Insert Recurring Deposits
INSERT INTO recurring_deposits (rd_number, customer_id, monthly_amount, interest_rate, term_months, maturity_amount, start_date, maturity_date, next_installment_date, status) VALUES
('RD001001', 1, 5000.00, 6.50, 24, 127800.00, '2024-01-01', '2026-01-01', '2024-02-01', 'ACTIVE'),
('RD002001', 2, 10000.00, 6.75, 36, 395100.00, '2023-06-01', '2026-06-01', '2024-02-01', 'ACTIVE'),
('RD003001', 3, 3000.00, 6.50, 12, 37380.00, '2024-01-15', '2025-01-15', '2024-02-15', 'ACTIVE'),
('RD004001', 4, 2000.00, 6.25, 18, 37125.00, '2023-08-01', '2025-02-01', '2024-02-01', 'ACTIVE'),
('RD005001', 5, 15000.00, 7.00, 48, 829200.00, '2022-01-01', '2026-01-01', '2024-02-01', 'ACTIVE');

-- Insert Chat Messages
INSERT INTO chat_messages (customer_id, admin_id, message, sender_type, sender_id, is_read) VALUES
(1, NULL, 'Hi, I need help with my loan application status', 'CUSTOMER', 1, TRUE),
(1, 1, 'Hello Rajesh, I can see your loan LOAN001 has been disbursed successfully.', 'ADMIN', 1, TRUE),
(1, NULL, 'Thank you for the update!', 'CUSTOMER', 1, TRUE),
(2, NULL, 'What documents are required for home loan?', 'CUSTOMER', 2, TRUE),
(2, 2, 'For home loan, you need: 1) ID Proof 2) Address Proof 3) Income Proof 4) Property Documents', 'ADMIN', 2, TRUE),
(3, NULL, 'My debit card is not working', 'CUSTOMER', 3, FALSE),
(4, NULL, 'I want to increase my FD amount', 'CUSTOMER', 4, FALSE),
(5, NULL, 'Please explain why my business loan was rejected', 'CUSTOMER', 5, TRUE),
(5, 1, 'Your business loan was rejected due to insufficient collateral. You can reapply with a guarantor.', 'ADMIN', 1, TRUE);



-- =====================================================
-- Create Indexes for Better Performance
-- =====================================================
CREATE INDEX idx_customers_email ON customers(email);
CREATE INDEX idx_accounts_customer ON accounts(customer_id);
CREATE INDEX idx_loans_customer ON loans(customer_id);
CREATE INDEX idx_loans_status ON loans(status);
CREATE INDEX idx_transactions_date ON transactions(created_at);
CREATE INDEX idx_chat_customer ON chat_messages(customer_id);

-- =====================================================
-- Display Summary
-- =====================================================
SELECT 'Database Setup Complete!' as Status;
SELECT '=== LOGIN CREDENTIALS ===' as Info;
SELECT 'Admin Login:' as Type, 'admin@myfinbank.com' as Email, 'Admin@123' as Password
UNION ALL
SELECT 'Admin Login:', 'manager@myfinbank.com', 'Admin@123'
UNION ALL
SELECT 'Admin Login:', 'support@myfinbank.com', 'Admin@123'
UNION ALL
SELECT 'Customer Login:', 'rajesh.kumar@gmail.com', 'Customer@123'
UNION ALL
SELECT 'Customer Login:', 'priya.sharma@gmail.com', 'Customer@123'
UNION ALL
SELECT 'Customer Login:', 'amit.patel@gmail.com', 'Customer@123'
UNION ALL
SELECT 'Customer Login:', 'sneha.verma@gmail.com', 'Customer@123'
UNION ALL
SELECT 'Customer Login:', 'vikram.singh@gmail.com', 'Customer@123';

select* from customers;
select * from admins;

select * from loans;
describe loans;

select * from chat_messages;

UPDATE loans SET status = 'PENDING' WHERE status = 'APPLIED';
UPDATE loans SET status = 'PENDING' WHERE status = 'UNDER_REVIEW';
UPDATE loans SET status = 'APPROVED' WHERE status = 'DISBURSED';

ALTER TABLE loans 
MODIFY status ENUM('PENDING','APPROVED','REJECTED') NOT NULL DEFAULT 'PENDING';



set foreign_key_checks=1;
delete from loans where id=8;

select * from chat_messages;

delete from customers where id=10;

set foreign_key_checks=0;

update loans SET status='PENDING' where loan_id=LOAN003;

-- Show table counts
SELECT 'Data Summary:' as Info;
SELECT 'Admins:' as Table_Name, COUNT(*) as Count FROM admins
UNION ALL
SELECT 'Customers:', COUNT(*) FROM customers
UNION ALL
SELECT 'Accounts:', COUNT(*) FROM accounts
UNION ALL
SELECT 'Loans:', COUNT(*) FROM loans
UNION ALL
SELECT 'Transactions:', COUNT(*) FROM transactions
UNION ALL
SELECT 'Fixed Deposits:', COUNT(*) FROM fixed_deposits
UNION ALL
SELECT 'Recurring Deposits:', COUNT(*) FROM recurring_deposits
UNION ALL
SELECT 'Chat Messages:', COUNT(*) FROM chat_messages;

-- Update Admin passwords to Admin@123
-- BCrypt hash for Admin@123: $2a$10$EixZaYVK1fsbw1ZfbX3OXePaWxn96p36vbaG3s5rJVmqB8zDmvJ3O
UPDATE admins 
SET password = '$2a$10$EixZaYVK1fsbw1ZfbX3OXePaWxn96p36vbaG3s5rJVmqB8zDmvJ3O'
WHERE email IN ('admin@myfinbank.com', 'manager@myfinbank.com', 'support@myfinbank.com');

-- Update Customer passwords to Customer@123  
-- BCrypt hash for Customer@123: $2a$10$N9qo8uLOickgx2ZMRZoHK.IjQ1vL6lkRA5rkdQxLZFmPQzFfBJ7XS
UPDATE customers 
SET password = '$2a$10$N9qo8uLOickgx2ZMRZoHK.IjQ1vL6lkRA5rkdQxLZFmPQzFfBJ7XS'
WHERE email IN ('rajesh.kumar@gmail.com', 'priya.sharma@gmail.com', 'amit.patel@gmail.com', 
                'sneha.verma@gmail.com', 'vikram.singh@gmail.com', 'kashifanum004@gmail.com');

-- Verify the updates
SELECT email, LEFT(password, 20) as password_preview FROM admins;
SELECT email, LEFT(password, 20) as password_preview FROM customers;
