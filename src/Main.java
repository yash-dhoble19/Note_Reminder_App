import java.sql.*;
import java.util.Scanner;

public class Main {

    private static final String url = "jdbc:mysql://127.0.0.1:3306/notesdb";
    private static final String username = "root";
    private static final String password = "Yash@2005";

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        while (true) {

            System.out.println("\n===== NOTE REMINDER APP =====");
            System.out.println("1. Add Note");
            System.out.println("2. View Notes");
            System.out.println("3. Update Note");
            System.out.println("4. Delete Note");
            System.out.println("5. Exit");
            System.out.print("Enter choice: ");

            int choice = sc.nextInt();
            sc.nextLine(); // clear buffer

            switch (choice) {
                case 1:
                    addNote();
                    break;
                case 2:
                    viewNotes();
                    break;
                case 3:
                    updateNote();
                    break;
                case 4:
                    deleteNote();
                    break;
                case 5:
                    System.out.println("Exiting...");
                    System.exit(0);
                default:
                    System.out.println("Invalid Choice!");
            }
        }
    }

    // ============================
    // ADD NOTE
    // ============================
    private static void addNote() {
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter Title: ");
        String title = sc.nextLine();

        System.out.print("Enter Description: ");
        String description = sc.nextLine();

        System.out.print("Enter Reminder Date (YYYY-MM-DD) or press Enter to skip: ");
        String dateInput = sc.nextLine();

        java.sql.Date reminderDate = null;
        if (!dateInput.isEmpty()) {
            reminderDate = java.sql.Date.valueOf(dateInput);
        }

        try (Connection con = DriverManager.getConnection(url, username, password)) {

            String query = "INSERT INTO notes(title, description, reminder_date) VALUES (?, ?, ?)";
            PreparedStatement pst = con.prepareStatement(query);

            pst.setString(1, title);
            pst.setString(2, description);

            if (reminderDate != null)
                pst.setDate(3, reminderDate);
            else
                pst.setNull(3, Types.DATE);

            pst.executeUpdate();
            System.out.println("Note Added Successfully!");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // ============================
    // VIEW NOTES
    // ============================
    private static void viewNotes() {

        try (Connection con = DriverManager.getConnection(url, username, password)) {

            String query = "SELECT * FROM notes";
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(query);

            System.out.println("\n----- All Notes -----");

            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("id"));
                System.out.println("Title: " + rs.getString("title"));
                System.out.println("Description: " + rs.getString("description"));
                System.out.println("Reminder: " + rs.getDate("reminder_date"));
                System.out.println("---------------------------");
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // ============================
    // UPDATE NOTE
    // ============================
    private static void updateNote() {

        Scanner sc = new Scanner(System.in);

        System.out.print("Enter Note ID to Update: ");
        int id = sc.nextInt();
        sc.nextLine();

        System.out.print("Enter New Title: ");
        String title = sc.nextLine();

        System.out.print("Enter New Description: ");
        String desc = sc.nextLine();

        System.out.print("Enter New Reminder Date (YYYY-MM-DD) or press Enter to remove: ");
        String dateInput = sc.nextLine();

        java.sql.Date reminderDate = null;
        if (!dateInput.isEmpty()) {
            reminderDate = java.sql.Date.valueOf(dateInput);
        }

        try (Connection con = DriverManager.getConnection(url, username, password)) {

            String query = "UPDATE notes SET title=?, description=?, reminder_date=? WHERE id=?";
            PreparedStatement pst = con.prepareStatement(query);

            pst.setString(1, title);
            pst.setString(2, desc);

            if (reminderDate != null)
                pst.setDate(3, reminderDate);
            else
                pst.setNull(3, Types.DATE);

            pst.setInt(4, id);

            int updated = pst.executeUpdate();

            if (updated > 0)
                System.out.println("Note Updated Successfully!");
            else
                System.out.println("Note ID Not Found!");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // ============================
    // DELETE NOTE
    // ============================
    private static void deleteNote() {

        Scanner sc = new Scanner(System.in);

        System.out.print("Enter Note ID to Delete: ");
        int id = sc.nextInt();

        try (Connection con = DriverManager.getConnection(url, username, password)) {

            String query = "DELETE FROM notes WHERE id=?";
            PreparedStatement pst = con.prepareStatement(query);

            pst.setInt(1, id);
            int deleted = pst.executeUpdate();

            if (deleted > 0)
                System.out.println("Note Deleted Successfully!");
            else
                System.out.println("Note ID Not Found!");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
