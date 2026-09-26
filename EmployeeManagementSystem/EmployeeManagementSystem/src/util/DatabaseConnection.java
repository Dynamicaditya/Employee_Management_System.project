package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

// DatabaseConnection.java
// Single responsibility: create and return a JDBC Connection to MySQL.
// Every DAO class calls DatabaseConnection.getConnection() instead of
// writing connection code again and again. This avoids code duplication.

public class DatabaseConnection {

    // ---- Change these 3 values to match your own MySQL setup ----
    private static final String URL = "jdbc:mysql://localhost:3306/employee_management";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "kajal"; // <-- put your MySQL password here

    // Private constructor: nobody should create an object of this class.
    // We only ever call its static method. This is a simple "utility class" pattern.
    private DatabaseConnection() {
    }

    public static Connection getConnection() {
        Connection connection = null;
        try {
            // Load the MySQL JDBC driver class.
            // (Not strictly required with modern drivers, but good to show in an interview.)
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Ask the DriverManager to open a real connection to MySQL.
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);

        } catch (ClassNotFoundException e) {
            // This happens if the MySQL Connector/J .jar file is missing from the project.
            System.out.println("MySQL JDBC Driver not found. Did you add the connector JAR?");
            e.printStackTrace();
        } catch (SQLException e) {
            // This happens if MySQL is not running, or username/password/db name is wrong.
            System.out.println("Database connection failed. Check MySQL service and credentials.");
            e.printStackTrace();
        }
        return connection;
    }

    // Small helper so DAO classes can close a connection safely and consistently.
    public static void close(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
