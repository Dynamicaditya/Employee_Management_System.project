package dao;

import model.Salary;
import util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

// SalaryDAO.java
// Handles all database operations for the "salary" table.
public class SalaryDAO {

    public boolean addSalary(Salary salary) {
        // netSalary is already calculated in Core Java (see Salary.calculateNetSalary())
        // before this method is ever called - the DAO only stores it.
        String sql = "INSERT INTO salary (employee_id, basic_salary, allowance, deduction, net_salary, payment_date) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, salary.getEmployeeId());
            ps.setDouble(2, salary.getBasicSalary());
            ps.setDouble(3, salary.getAllowance());
            ps.setDouble(4, salary.getDeduction());
            ps.setDouble(5, salary.getNetSalary());
            ps.setString(6, salary.getPaymentDate());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error while adding salary record: " + e.getMessage());
            return false;
        }
    }

    public List<Salary> getAllSalaries() {
        List<Salary> list = new ArrayList<>();
        String sql = "SELECT s.*, e.name AS employee_name FROM salary s " +
                "JOIN employees e ON s.employee_id = e.employee_id " +
                "ORDER BY s.salary_id DESC";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(mapRowToSalary(rs));
            }
        } catch (SQLException e) {
            System.out.println("Error while fetching salaries: " + e.getMessage());
        }
        return list;
    }

    public List<Salary> getSalariesByEmployee(int employeeId) {
        List<Salary> list = new ArrayList<>();
        String sql = "SELECT s.*, e.name AS employee_name FROM salary s " +
                "JOIN employees e ON s.employee_id = e.employee_id " +
                "WHERE s.employee_id = ? ORDER BY s.payment_date DESC";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, employeeId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRowToSalary(rs));
                }
            }
        } catch (SQLException e) {
            System.out.println("Error while searching salary: " + e.getMessage());
        }
        return list;
    }

    public boolean updateSalary(Salary salary) {
        String sql = "UPDATE salary SET basic_salary=?, allowance=?, deduction=?, net_salary=?, payment_date=? " +
                "WHERE salary_id=?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setDouble(1, salary.getBasicSalary());
            ps.setDouble(2, salary.getAllowance());
            ps.setDouble(3, salary.getDeduction());
            ps.setDouble(4, salary.getNetSalary());
            ps.setString(5, salary.getPaymentDate());
            ps.setInt(6, salary.getSalaryId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error while updating salary: " + e.getMessage());
            return false;
        }
    }

    private Salary mapRowToSalary(ResultSet rs) throws SQLException {
        return new Salary(
                rs.getInt("salary_id"),
                rs.getInt("employee_id"),
                rs.getString("employee_name"),
                rs.getDouble("basic_salary"),
                rs.getDouble("allowance"),
                rs.getDouble("deduction"),
                rs.getDouble("net_salary"),
                rs.getString("payment_date")
        );
    }
}
