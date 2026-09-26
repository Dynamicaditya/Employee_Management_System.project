package dao;

import model.Department;
import util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

// DepartmentDAO.java
// Handles all CRUD operations for the "departments" table.
public class DepartmentDAO {

    public boolean addDepartment(Department dept) {
        String sql = "INSERT INTO departments (department_name) VALUES (?)";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, dept.getDepartmentName());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error while adding department: " + e.getMessage());
            return false;
        }
    }

    public List<Department> getAllDepartments() {
        List<Department> list = new ArrayList<>();
        String sql = "SELECT * FROM departments ORDER BY department_id";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new Department(rs.getInt("department_id"), rs.getString("department_name")));
            }
        } catch (SQLException e) {
            System.out.println("Error while fetching departments: " + e.getMessage());
        }
        return list;
    }

    public boolean updateDepartment(Department dept) {
        String sql = "UPDATE departments SET department_name = ? WHERE department_id = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, dept.getDepartmentName());
            ps.setInt(2, dept.getDepartmentId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error while updating department: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteDepartment(int departmentId) {
        String sql = "DELETE FROM departments WHERE department_id = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, departmentId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error while deleting department: " + e.getMessage());
            return false;
        }
    }

    public int getTotalDepartments() {
        String sql = "SELECT COUNT(*) FROM departments";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            System.out.println("Error while counting departments: " + e.getMessage());
        }
        return 0;
    }
}
