package dao;

import model.Employee;
import util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

// EmployeeDAO.java
// DAO = Data Access Object.
// This class is the ONLY place that talks to the "employees" table.
// It contains all CRUD operations (Create, Read, Update, Delete) for Employee.
// Every method uses PreparedStatement (never string concatenation) to avoid SQL injection.

public class EmployeeDAO {

    // ---- CREATE ----
    public boolean addEmployee(Employee emp) {
        String sql = "INSERT INTO employees " +
                "(name, email, phone, gender, dob, address, department_id, designation_id, joining_date, salary, status) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, emp.getName());
            ps.setString(2, emp.getEmail());
            ps.setString(3, emp.getPhone());
            ps.setString(4, emp.getGender());
            ps.setString(5, emp.getDob());
            ps.setString(6, emp.getAddress());
            ps.setInt(7, emp.getDepartmentId());
            ps.setInt(8, emp.getDesignationId());
            ps.setString(9, emp.getJoiningDate());
            ps.setDouble(10, emp.getSalary());
            ps.setString(11, emp.getStatus());

            int rows = ps.executeUpdate(); // returns number of rows inserted
            return rows > 0;

        } catch (SQLException e) {
            System.out.println("Error while adding employee: " + e.getMessage());
            return false;
        }
    }

    // ---- READ (all employees) ----
    public List<Employee> getAllEmployees() {
        List<Employee> list = new ArrayList<>();
        String sql = "SELECT * FROM employees ORDER BY employee_id";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(mapRowToEmployee(rs));
            }

        } catch (SQLException e) {
            System.out.println("Error while fetching employees: " + e.getMessage());
        }
        return list;
    }

    // ---- READ (single employee by id) ----
    public Employee getEmployeeById(int employeeId) {
        String sql = "SELECT * FROM employees WHERE employee_id = ?";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, employeeId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRowToEmployee(rs);
                }
            }

        } catch (SQLException e) {
            System.out.println("Error while fetching employee: " + e.getMessage());
        }
        return null; // not found
    }

    // ---- SEARCH by id, name, or department (used by search page) ----
    public List<Employee> searchEmployees(String keyword) {
        List<Employee> list = new ArrayList<>();
        // Search across id (as text), name, and department_id join not needed here,
        // department search is handled by joining departments table by name.
        String sql = "SELECT e.* FROM employees e " +
                "LEFT JOIN departments d ON e.department_id = d.department_id " +
                "WHERE e.employee_id = ? OR e.name LIKE ? OR d.department_name LIKE ?";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            // If the keyword is not a valid number, use 0 so the id comparison simply fails.
            int idSearch;
            try {
                idSearch = Integer.parseInt(keyword);
            } catch (NumberFormatException e) {
                idSearch = 0;
            }

            ps.setInt(1, idSearch);
            ps.setString(2, "%" + keyword + "%");
            ps.setString(3, "%" + keyword + "%");

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRowToEmployee(rs));
                }
            }

        } catch (SQLException e) {
            System.out.println("Error while searching employees: " + e.getMessage());
        }
        return list;
    }

    // ---- UPDATE ----
    public boolean updateEmployee(Employee emp) {
        String sql = "UPDATE employees SET name=?, email=?, phone=?, gender=?, dob=?, address=?, " +
                "department_id=?, designation_id=?, joining_date=?, salary=?, status=? " +
                "WHERE employee_id=?";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, emp.getName());
            ps.setString(2, emp.getEmail());
            ps.setString(3, emp.getPhone());
            ps.setString(4, emp.getGender());
            ps.setString(5, emp.getDob());
            ps.setString(6, emp.getAddress());
            ps.setInt(7, emp.getDepartmentId());
            ps.setInt(8, emp.getDesignationId());
            ps.setString(9, emp.getJoiningDate());
            ps.setDouble(10, emp.getSalary());
            ps.setString(11, emp.getStatus());
            ps.setInt(12, emp.getEmployeeId());

            int rows = ps.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            System.out.println("Error while updating employee: " + e.getMessage());
            return false;
        }
    }

    // ---- DELETE ----
    public boolean deleteEmployee(int employeeId) {
        String sql = "DELETE FROM employees WHERE employee_id = ?";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, employeeId);
            int rows = ps.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            System.out.println("Error while deleting employee: " + e.getMessage());
            return false;
        }
    }

    // ---- Small helper counts, used by the Dashboard ----
    public int getTotalEmployees() {
        return countWithQuery("SELECT COUNT(*) FROM employees");
    }

    public int getActiveEmployeeCount() {
        return countWithQuery("SELECT COUNT(*) FROM employees WHERE status = 'Active'");
    }

    private int countWithQuery(String sql) {
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.out.println("Error while counting employees: " + e.getMessage());
        }
        return 0;
    }

    // Private helper: converts one ResultSet row into an Employee object.
    // Avoids repeating this mapping code in every method above.
    private Employee mapRowToEmployee(ResultSet rs) throws SQLException {
        return new Employee(
                rs.getInt("employee_id"),
                rs.getString("name"),
                rs.getString("email"),
                rs.getString("phone"),
                rs.getString("gender"),
                rs.getString("dob"),
                rs.getString("address"),
                rs.getInt("department_id"),
                rs.getInt("designation_id"),
                rs.getString("joining_date"),
                rs.getDouble("salary"),
                rs.getString("status")
        );
    }
}
