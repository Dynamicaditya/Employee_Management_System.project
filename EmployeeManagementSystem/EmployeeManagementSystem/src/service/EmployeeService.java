package service;

import dao.EmployeeDAO;
import model.Employee;

import java.util.List;

// EmployeeService.java
// The SERVICE layer sits between the "web page logic" and the DAO layer.
// Its job: apply business rules / validation BEFORE talking to the database.
// This keeps validation logic out of the DAO (which should only do SQL)
// and out of the UI (which should only collect input).

public class EmployeeService {

    private final EmployeeDAO employeeDAO = new EmployeeDAO();

    // Validates employee data, then calls the DAO to insert it.
    // Returns an error message, or null if everything succeeded.
    public String addEmployee(Employee emp) {
        String validationError = validate(emp);
        if (validationError != null) {
            return validationError;
        }
        boolean success = employeeDAO.addEmployee(emp);
        return success ? null : "Could not save employee. Please try again.";
    }

    public String updateEmployee(Employee emp) {
        String validationError = validate(emp);
        if (validationError != null) {
            return validationError;
        }
        boolean success = employeeDAO.updateEmployee(emp);
        return success ? null : "Could not update employee. Please try again.";
    }

    public boolean deleteEmployee(int employeeId) {
        return employeeDAO.deleteEmployee(employeeId);
    }

    public List<Employee> getAllEmployees() {
        return employeeDAO.getAllEmployees();
    }

    public Employee getEmployeeById(int employeeId) {
        return employeeDAO.getEmployeeById(employeeId);
    }

    public List<Employee> searchEmployees(String keyword) {
        return employeeDAO.searchEmployees(keyword);
    }

    // ---- Simple, beginner-friendly validation (no JavaScript, pure Java) ----
    private String validate(Employee emp) {
        if (emp.getName() == null || emp.getName().trim().isEmpty()) {
            return "Name is required.";
        }
        if (emp.getEmail() == null || !emp.getEmail().matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")) {
            return "Please enter a valid email address.";
        }
        if (emp.getPhone() == null || !emp.getPhone().matches("^[0-9]{10}$")) {
            return "Phone number must be exactly 10 digits.";
        }
        if (emp.getSalary() < 0) {
            return "Salary cannot be negative.";
        }
        if (emp.getJoiningDate() == null || emp.getJoiningDate().trim().isEmpty()) {
            return "Joining date is required.";
        }
        return null; // no errors
    }
}
