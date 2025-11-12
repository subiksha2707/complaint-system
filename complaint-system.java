import java.sql.*;
import java.util.*;

public class ComplaintSystem {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/college_complaints";
    private static final String DB_USER = "root";
    private static final String DB_PASS = "yourpassword";

    private static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
            while (true) {
                System.out.println("\n=== COLLEGE COMPLAINT MANAGEMENT SYSTEM ===");
                System.out.println("1. User Registration");
                System.out.println("2. User Login");
                System.out.println("3. Admin Login");
                System.out.println("4. Exit");
                System.out.print("Enter choice: ");
                int choice = sc.nextInt();

                switch (choice) {
                    case 1 -> registerUser(conn);
                    case 2 -> userLogin(conn);
                    case 3 -> adminLogin(conn);
                    case 4 -> {
                        System.out.println("Thank you! Exiting...");
                        return;
                    }
                    default -> System.out.println("Invalid choice!");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // USER REGISTRATION
    private static void registerUser(Connection conn) throws SQLException {
        System.out.print("Enter your name: ");
        sc.nextLine();
        String name = sc.nextLine();
        System.out.print("Enter email: ");
        String email = sc.nextLine();
        System.out.print("Enter password: ");
        String password = sc.nextLine();

        String query = "INSERT INTO users(name, email, password) VALUES (?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, name);
            ps.setString(2, email);
            ps.setString(3, password);
            ps.executeUpdate();
            System.out.println("✅ Registration successful!");
        }
    }

    // USER LOGIN
    private static void userLogin(Connection conn) throws SQLException {
        System.out.print("Enter email: ");
        sc.nextLine();
        String email = sc.nextLine();
        System.out.print("Enter password: ");
        String password = sc.nextLine();

        String query = "SELECT * FROM users WHERE email=? AND password=?";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, email);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                System.out.println("\nWelcome " + rs.getString("name") + "!");
                userMenu(conn, rs.getInt("id"));
            } else {
                System.out.println("❌ Invalid credentials!");
            }
        }
    }

    // USER MENU
    private static void userMenu(Connection conn, int userId) throws SQLException {
        while (true) {
            System.out.println("\n--- USER MENU ---");
            System.out.println("1. Register Complaint");
            System.out.println("2. Track Complaint Status");
            System.out.println("3. Logout");
            System.out.print("Enter choice: ");
            int ch = sc.nextInt();
            sc.nextLine();

            switch (ch) {
                case 1 -> registerComplaint(conn, userId);
                case 2 -> trackComplaint(conn, userId);
                case 3 -> { return; }
                default -> System.out.println("Invalid choice!");
            }
        }
    }

    private static void registerComplaint(Connection conn, int userId) throws SQLException {
        System.out.print("Enter complaint title: ");
        String title = sc.nextLine();
        System.out.print("Enter complaint description: ");
        String desc = sc.nextLine();

        String query = "INSERT INTO complaints(user_id, title, description, status) VALUES (?, ?, ?, 'Pending')";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, userId);
            ps.setString(2, title);
            ps.setString(3, desc);
            ps.executeUpdate();
            System.out.println("✅ Complaint submitted successfully!");
        }
    }

    private static void trackComplaint(Connection conn, int userId) throws SQLException {
        String query = "SELECT * FROM complaints WHERE user_id=?";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            System.out.println("\n--- Your Complaints ---");
            while (rs.next()) {
                System.out.println("Complaint ID: " + rs.getInt("id"));
                System.out.println("Title: " + rs.getString("title"));
                System.out.println("Description: " + rs.getString("description"));
                System.out.println("Status: " + rs.getString("status"));
                System.out.println("----------------------");
            }
        }
    }

    // ADMIN LOGIN
    private static void adminLogin(Connection conn) throws SQLException {
        System.out.print("Enter admin username: ");
        sc.nextLine();
        String username = sc.nextLine();
        System.out.print("Enter admin password: ");
        String password = sc.nextLine();

        String query = "SELECT * FROM admin WHERE username=? AND password=?";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                System.out.println("Welcome Admin!");
                adminMenu(conn);
            } else {
                System.out.println("❌ Invalid admin credentials!");
            }
        }
    }

    // ADMIN MENU
    private static void adminMenu(Connection conn) throws SQLException {
        while (true) {
            System.out.println("\n--- ADMIN MENU ---");
            System.out.println("1. View All Complaints");
            System.out.println("2. Update Complaint Status");
            System.out.println("3. Logout");
            System.out.print("Enter choice: ");
            int ch = sc.nextInt();
            sc.nextLine();

            switch (ch) {
                case 1 -> viewAllComplaints(conn);
                case 2 -> updateComplaintStatus(conn);
                case 3 -> { return; }
                default -> System.out.println("Invalid choice!");
            }
        }
    }

    private static void viewAllComplaints(Connection conn) throws SQLException {
        String query = "SELECT c.id, u.name, c.title, c.description, c.status FROM complaints c JOIN users u ON c.user_id = u.id";
        try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(query)) {
            System.out.println("\n--- ALL COMPLAINTS ---");
            while (rs.next()) {
                System.out.println("Complaint ID: " + rs.getInt("id"));
                System.out.println("User: " + rs.getString("name"));
                System.out.println("Title: " + rs.getString("title"));
                System.out.println("Description: " + rs.getString("description"));
                System.out.println("Status: " + rs.getString("status"));
                System.out.println("----------------------");
            }
        }
    }

    private static void updateComplaintStatus(Connection conn) throws SQLException {
        System.out.print("Enter complaint ID to update: ");
        int id = sc.nextInt();
        sc.nextLine();
        System.out.print("Enter new status (Pending/In Progress/Resolved): ");
        String status = sc.nextLine();

        String query = "UPDATE complaints SET status=? WHERE id=?";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, status);
            ps.setInt(2, id);
            int rows = ps.executeUpdate();
            if (rows > 0)
                System.out.println("✅ Status updated successfully!");
            else
                System.out.println("❌ Complaint not found!");
        }
    }
}
