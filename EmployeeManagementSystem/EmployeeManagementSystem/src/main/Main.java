package main;

import dao.UserDAO;
import model.*;
import service.EmployeeService;
import service.LeaveService;
import service.SalaryService;

import java.util.List;
import java.util.Scanner;

// Main.java
// This is the entry point of the Core Java application.
//
// IMPORTANT (read this in the interview!):
// Plain HTML files CANNOT call Java code directly, because HTML/CSS alone
// has no way to run server-side logic - that normally needs a servlet
// container (Tomcat) or a framework (Spring Boot), which this project is
// intentionally NOT using.
//
// So this project uses TWO separate, clearly explained pieces:
//   1) The "web/" folder - static HTML+CSS pages that show the SCREENS/UI design.
//   2) This Main.java - a console-based menu that runs the REAL Core Java + JDBC
//      logic (login, CRUD, leave, salary) against the MySQL database.
//
// This is a common and honest way beginners structure a "Core Java + JDBC +
// HTML/CSS" project without a web server, and it still lets you demonstrate
// every required concept (OOP, JDBC, CRUD, exception handling, SQL).
//
// If you later add a servlet container, the same DAO/Service/Model classes
// can be reused as-is behind servlets - nothing here would need to change.

public class Main {

    private static final Scanner sc = new Scanner(System.in);
    private static final UserDAO userDAO = new UserDAO();
    private static final EmployeeService employeeService = new EmployeeService();
    private static final LeaveService leaveService = new LeaveService();
    private static final SalaryService salaryService = new SalaryService();

    public static void main(String[] args) {
        System.out.println("=== Employee Management System ===");

        if (!login()) {
            System.out.println("Too many failed attempts. Exiting.");
            return;
        }

        boolean running = true;
        while (running) {
            printMenu();
            String choice = sc.nextLine().trim();

            switch (choice) {
                case "1": addEmployee(); break;
                case "2": viewEmployees(); break;
                case "3": searchEmployee(); break;
                case "4": updateEmployee(); break;
                case "5": deleteEmployee(); break;
                case "6": applyLeave(); break;
                case "7": viewLeaves(); break;
                case "8": addSalary(); break;
                case "9": viewSalaries(); break;
                case "0":
                    running = false;
                    System.out.println("Logged out. Goodbye!");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    // ---- LOGIN ----
    private static boolean login() {
        int attempts = 0;
        while (attempts < 3) {
            System.out.print("Username: ");
            String username = sc.nextLine();
            System.out.print("Password: ");
            String password = sc.nextLine();

            User user = userDAO.validateLogin(username, password);
            if (user != null) {
                System.out.println("Login successful! Welcome, " + user.getUsername() + ".");
                return true;
            } else {
                attempts++;
                System.out.println("Invalid username or password. Attempts left: " + (3 - attempts));
            }
        }
        return false;
    }

    private static void printMenu() {
        System.out.println("\n--- MAIN MENU ---");
        System.out.println("1. Add Employee");
        System.out.println("2. View All Employees");
        System.out.println("3. Search Employee");
        System.out.println("4. Update Employee");
        System.out.println("5. Delete Employee");
        System.out.println("6. Apply Leave");
        System.out.println("7. View Leave Requests");
        System.out.println("8. Add Salary Record");
        System.out.println("9. View Salary Records");
        System.out.println("0. Logout / Exit");
        System.out.print("Enter choice: ");
    }

    // ---- EMPLOYEE ----
    private static void addEmployee() {
        try {
            System.out.print("Name: "); String name = sc.nextLine();
            System.out.print("Email: "); String email = sc.nextLine();
            System.out.print("Phone: "); String phone = sc.nextLine();
            System.out.print("Gender: "); String gender = sc.nextLine();
            System.out.print("DOB (yyyy-MM-dd): "); String dob = sc.nextLine();
            System.out.print("Address: "); String address = sc.nextLine();
            System.out.print("Department ID: "); int deptId = Integer.parseInt(sc.nextLine());
            System.out.print("Designation ID: "); int desigId = Integer.parseInt(sc.nextLine());
            System.out.print("Joining Date (yyyy-MM-dd): "); String joiningDate = sc.nextLine();
            System.out.print("Salary: "); double salary = Double.parseDouble(sc.nextLine());
            System.out.print("Status (Active/Inactive): "); String status = sc.nextLine();

            Employee emp = new Employee(name, email, phone, gender, dob, address,
                    deptId, desigId, joiningDate, salary, status);

            String error = employeeService.addEmployee(emp);
            if (error == null) {
                System.out.println("Employee added successfully.");
            } else {
                System.out.println("Error: " + error);
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid number entered. Please try again.");
        }
    }

    private static void viewEmployees() {
        List<Employee> employees = employeeService.getAllEmployees();
        if (employees.isEmpty()) {
            System.out.println("No employees found.");
            return;
        }
        for (Employee e : employees) {
            System.out.println(e);
        }
    }

    private static void searchEmployee() {
        System.out.print("Enter Employee ID, Name, or Department to search: ");
        String keyword = sc.nextLine();
        List<Employee> results = employeeService.searchEmployees(keyword);
        if (results.isEmpty()) {
            System.out.println("No matching employees found.");
        } else {
            for (Employee e : results) {
                System.out.println(e);
            }
        }
    }

    private static void updateEmployee() {
        System.out.print("Enter Employee ID to update: ");
        int id = Integer.parseInt(sc.nextLine());
        Employee existing = employeeService.getEmployeeById(id);
        if (existing == null) {
            System.out.println("Employee not found.");
            return;
        }
        System.out.println("Leave a field blank to keep the current value.");

        System.out.print("Name [" + existing.getName() + "]: ");
        String name = sc.nextLine();
        if (!name.isBlank()) existing.setName(name);

        System.out.print("Salary [" + existing.getSalary() + "]: ");
        String salaryStr = sc.nextLine();
        if (!salaryStr.isBlank()) existing.setSalary(Double.parseDouble(salaryStr));

        System.out.print("Status [" + existing.getStatus() + "]: ");
        String status = sc.nextLine();
        if (!status.isBlank()) existing.setStatus(status);

        String error = employeeService.updateEmployee(existing);
        System.out.println(error == null ? "Employee updated successfully." : "Error: " + error);
    }

    private static void deleteEmployee() {
        System.out.print("Enter Employee ID to delete: ");
        int id = Integer.parseInt(sc.nextLine());
        System.out.print("Are you sure? (yes/no): ");
        if (sc.nextLine().equalsIgnoreCase("yes")) {
            boolean success = employeeService.deleteEmployee(id);
            System.out.println(success ? "Employee deleted." : "Delete failed.");
        } else {
            System.out.println("Delete cancelled.");
        }
    }

    // ---- LEAVE ----
    private static void applyLeave() {
        System.out.print("Employee ID: "); int empId = Integer.parseInt(sc.nextLine());
        System.out.print("Leave Type (Casual Leave/Sick Leave/Earned Leave): "); String type = sc.nextLine();
        System.out.print("From Date (yyyy-MM-dd): "); String from = sc.nextLine();
        System.out.print("To Date (yyyy-MM-dd): "); String to = sc.nextLine();
        System.out.print("Reason: "); String reason = sc.nextLine();

        Leave leave = new Leave(empId, type, from, to, reason, "Pending");
        String error = leaveService.applyLeave(leave);
        System.out.println(error == null ? "Leave request submitted." : "Error: " + error);
    }

    private static void viewLeaves() {
        List<Leave> leaves = leaveService.getAllLeaves();
        if (leaves.isEmpty()) {
            System.out.println("No leave requests found.");
            return;
        }
        for (Leave l : leaves) {
            System.out.println(l.getLeaveId() + " | " + l.getEmployeeName() + " | " + l.getLeaveType()
                    + " | " + l.getFromDate() + " to " + l.getToDate() + " | " + l.getStatus());
        }
        System.out.print("Enter Leave ID to Approve/Reject (or press Enter to skip): ");
        String input = sc.nextLine();
        if (!input.isBlank()) {
            int leaveId = Integer.parseInt(input);
            System.out.print("Approve or Reject? (A/R): ");
            String decision = sc.nextLine();
            boolean success = decision.equalsIgnoreCase("A")
                    ? leaveService.approveLeave(leaveId)
                    : leaveService.rejectLeave(leaveId);
            System.out.println(success ? "Leave status updated." : "Update failed.");
        }
    }

    // ---- SALARY ----
    private static void addSalary() {
        System.out.print("Employee ID: "); int empId = Integer.parseInt(sc.nextLine());
        System.out.print("Basic Salary: "); double basic = Double.parseDouble(sc.nextLine());
        System.out.print("Allowance: "); double allowance = Double.parseDouble(sc.nextLine());
        System.out.print("Deduction: "); double deduction = Double.parseDouble(sc.nextLine());
        System.out.print("Payment Date (yyyy-MM-dd): "); String date = sc.nextLine();

        Salary salary = new Salary(empId, basic, allowance, deduction, date);
        String error = salaryService.addSalary(salary);
        System.out.println(error == null
                ? "Salary record saved. Net Salary = " + salary.getNetSalary()
                : "Error: " + error);
    }

    private static void viewSalaries() {
        List<Salary> salaries = salaryService.getAllSalaries();
        if (salaries.isEmpty()) {
            System.out.println("No salary records found.");
            return;
        }
        for (Salary s : salaries) {
            System.out.println(s.getSalaryId() + " | " + s.getEmployeeName()
                    + " | Basic: " + s.getBasicSalary()
                    + " | Allowance: " + s.getAllowance()
                    + " | Deduction: " + s.getDeduction()
                    + " | Net: " + s.getNetSalary()
                    + " | Date: " + s.getPaymentDate());
        }
    }
}
