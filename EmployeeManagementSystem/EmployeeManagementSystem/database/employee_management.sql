-- employee_management.sql
-- Run this entire file in MySQL to create the database, tables, and sample data.

CREATE DATABASE IF NOT EXISTS employee_management;
USE employee_management;

-- ---------------- USERS (admin login) ----------------
CREATE TABLE IF NOT EXISTS users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL
);

-- ---------------- DEPARTMENTS ----------------
CREATE TABLE IF NOT EXISTS departments (
    department_id INT AUTO_INCREMENT PRIMARY KEY,
    department_name VARCHAR(50) NOT NULL UNIQUE
);

-- ---------------- DESIGNATIONS ----------------
CREATE TABLE IF NOT EXISTS designations (
    designation_id INT AUTO_INCREMENT PRIMARY KEY,
    designation_name VARCHAR(50) NOT NULL UNIQUE
);

-- ---------------- EMPLOYEES ----------------
CREATE TABLE IF NOT EXISTS employees (
    employee_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    phone VARCHAR(15) NOT NULL,
    gender VARCHAR(10),
    dob DATE,
    address VARCHAR(255),
    department_id INT,
    designation_id INT,
    joining_date DATE,
    salary DECIMAL(10,2) NOT NULL DEFAULT 0,
    status VARCHAR(20) DEFAULT 'Active',
    FOREIGN KEY (department_id) REFERENCES departments(department_id) ON DELETE SET NULL,
    FOREIGN KEY (designation_id) REFERENCES designations(designation_id) ON DELETE SET NULL
);

-- ---------------- LEAVES ----------------
CREATE TABLE IF NOT EXISTS leaves (
    leave_id INT AUTO_INCREMENT PRIMARY KEY,
    employee_id INT NOT NULL,
    leave_type VARCHAR(30) NOT NULL,
    from_date DATE NOT NULL,
    to_date DATE NOT NULL,
    reason VARCHAR(255),
    status VARCHAR(20) DEFAULT 'Pending',
    FOREIGN KEY (employee_id) REFERENCES employees(employee_id) ON DELETE CASCADE
);

-- ---------------- SALARY ----------------
CREATE TABLE IF NOT EXISTS salary (
    salary_id INT AUTO_INCREMENT PRIMARY KEY,
    employee_id INT NOT NULL,
    basic_salary DECIMAL(10,2) NOT NULL,
    allowance DECIMAL(10,2) DEFAULT 0,
    deduction DECIMAL(10,2) DEFAULT 0,
    net_salary DECIMAL(10,2) NOT NULL,
    payment_date DATE NOT NULL,
    FOREIGN KEY (employee_id) REFERENCES employees(employee_id) ON DELETE CASCADE
);

-- ================= SAMPLE DATA =================

-- Admin login (username: admin, password: admin123)
INSERT INTO users (username, password) VALUES ('admin', 'admin123');

-- Departments
INSERT INTO departments (department_name) VALUES
('IT'), ('HR'), ('Finance'), ('Marketing'), ('Sales');

-- Designations
INSERT INTO designations (designation_name) VALUES
('Software Developer'), ('HR Executive'), ('Accountant'), ('Manager'), ('Sales Executive');

-- Employees
INSERT INTO employees (name, email, phone, gender, dob, address, department_id, designation_id, joining_date, salary, status)
VALUES
('Rahul Sharma', 'rahul.sharma@example.com', '9876543210', 'Male', '1996-05-14', 'Bhubaneswar, Odisha', 1, 1, '2022-01-10', 45000, 'Active'),
('Priya Nair', 'priya.nair@example.com', '9876543211', 'Female', '1994-11-02', 'Cuttack, Odisha', 2, 2, '2021-06-15', 35000, 'Active'),
('Amit Verma', 'amit.verma@example.com', '9876543212', 'Male', '1990-03-22', 'Pune, Maharashtra', 3, 3, '2020-09-01', 40000, 'Active'),
('Sneha Rao', 'sneha.rao@example.com', '9876543213', 'Female', '1997-07-19', 'Hyderabad, Telangana', 4, 4, '2023-02-20', 55000, 'Active'),
('Vikram Singh', 'vikram.singh@example.com', '9876543214', 'Male', '1993-12-30', 'Delhi', 5, 5, '2022-11-05', 38000, 'Inactive');

-- Leave requests
INSERT INTO leaves (employee_id, leave_type, from_date, to_date, reason, status) VALUES
(1, 'Casual Leave', '2026-09-01', '2026-09-02', 'Family function', 'Approved'),
(2, 'Sick Leave', '2026-09-10', '2026-09-12', 'Fever', 'Pending'),
(3, 'Earned Leave', '2026-10-05', '2026-10-10', 'Vacation', 'Pending');

-- Salary records
INSERT INTO salary (employee_id, basic_salary, allowance, deduction, net_salary, payment_date) VALUES
(1, 40000, 6000, 1000, 45000, '2026-08-31'),
(2, 30000, 6000, 1000, 35000, '2026-08-31'),
(3, 35000, 6000, 1000, 40000, '2026-08-31');
