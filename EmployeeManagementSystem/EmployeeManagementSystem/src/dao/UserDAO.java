package dao;

import model.User;
import util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

// UserDAO.java
// Handles login checking against the "users" table.
public class UserDAO {

    // Returns the matching User if username+password are correct, otherwise null.
    // NOTE: For a real production app, passwords must be hashed (e.g. BCrypt) and
    // compared as hashes, never stored/compared as plain text. This project keeps
    // it simple on purpose for learning JDBC + login flow basics.
    public User validateLogin(String username, String password) {
        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, username);
            ps.setString(2, password);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new User(rs.getInt("id"), rs.getString("username"), rs.getString("password"));
                }
            }

        } catch (SQLException e) {
            System.out.println("Error while validating login: " + e.getMessage());
        }
        return null; // no match found -> invalid login
    }
}
