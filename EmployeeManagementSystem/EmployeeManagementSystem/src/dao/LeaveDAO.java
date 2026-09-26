package dao;

import model.Leave;
import util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

// LeaveDAO.java
// Handles all database operations for the "leaves" table.
public class LeaveDAO {

    public boolean applyLeave(Leave leave) {
        String sql = "INSERT INTO leaves (employee_id, leave_type, from_date, to_date, reason, status) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, leave.getEmployeeId());
            ps.setString(2, leave.getLeaveType());
            ps.setString(3, leave.getFromDate());
            ps.setString(4, leave.getToDate());
            ps.setString(5, leave.getReason());
            ps.setString(6, leave.getStatus()); // usually "Pending" when first applied
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error while applying leave: " + e.getMessage());
            return false;
        }
    }

    // Returns all leave requests, joined with employees so we can show the employee name.
    public List<Leave> getAllLeaves() {
        List<Leave> list = new ArrayList<>();
        String sql = "SELECT l.*, e.name AS employee_name FROM leaves l " +
                "JOIN employees e ON l.employee_id = e.employee_id " +
                "ORDER BY l.leave_id DESC";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(new Leave(
                        rs.getInt("leave_id"),
                        rs.getInt("employee_id"),
                        rs.getString("employee_name"),
                        rs.getString("leave_type"),
                        rs.getString("from_date"),
                        rs.getString("to_date"),
                        rs.getString("reason"),
                        rs.getString("status")
                ));
            }
        } catch (SQLException e) {
            System.out.println("Error while fetching leaves: " + e.getMessage());
        }
        return list;
    }

    public List<Leave> getLeavesByStatus(String status) {
        List<Leave> list = new ArrayList<>();
        String sql = "SELECT l.*, e.name AS employee_name FROM leaves l " +
                "JOIN employees e ON l.employee_id = e.employee_id " +
                "WHERE l.status = ? ORDER BY l.leave_id DESC";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, status);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new Leave(
                            rs.getInt("leave_id"),
                            rs.getInt("employee_id"),
                            rs.getString("employee_name"),
                            rs.getString("leave_type"),
                            rs.getString("from_date"),
                            rs.getString("to_date"),
                            rs.getString("reason"),
                            rs.getString("status")
                    ));
                }
            }
        } catch (SQLException e) {
            System.out.println("Error while fetching leaves by status: " + e.getMessage());
        }
        return list;
    }

    // Used by Approve / Reject buttons - just updates the status column.
    public boolean updateLeaveStatus(int leaveId, String newStatus) {
        String sql = "UPDATE leaves SET status = ? WHERE leave_id = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, newStatus);
            ps.setInt(2, leaveId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error while updating leave status: " + e.getMessage());
            return false;
        }
    }

    public int getEmployeesOnLeaveCount() {
        String sql = "SELECT COUNT(DISTINCT employee_id) FROM leaves WHERE status = 'Approved' " +
                "AND CURDATE() BETWEEN from_date AND to_date";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            System.out.println("Error while counting employees on leave: " + e.getMessage());
        }
        return 0;
    }
}
