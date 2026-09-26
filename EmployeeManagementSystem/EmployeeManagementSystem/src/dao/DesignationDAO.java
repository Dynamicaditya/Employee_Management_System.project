package dao;

import model.Designation;
import util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

// DesignationDAO.java
// Handles all CRUD operations for the "designations" table.
// Structure is intentionally almost identical to DepartmentDAO -
// this repetition is normal in beginner JDBC projects (no framework to reduce it).
public class DesignationDAO {

    public boolean addDesignation(Designation designation) {
        String sql = "INSERT INTO designations (designation_name) VALUES (?)";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, designation.getDesignationName());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error while adding designation: " + e.getMessage());
            return false;
        }
    }

    public List<Designation> getAllDesignations() {
        List<Designation> list = new ArrayList<>();
        String sql = "SELECT * FROM designations ORDER BY designation_id";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new Designation(rs.getInt("designation_id"), rs.getString("designation_name")));
            }
        } catch (SQLException e) {
            System.out.println("Error while fetching designations: " + e.getMessage());
        }
        return list;
    }

    public boolean updateDesignation(Designation designation) {
        String sql = "UPDATE designations SET designation_name = ? WHERE designation_id = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, designation.getDesignationName());
            ps.setInt(2, designation.getDesignationId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error while updating designation: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteDesignation(int designationId) {
        String sql = "DELETE FROM designations WHERE designation_id = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, designationId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error while deleting designation: " + e.getMessage());
            return false;
        }
    }
}
