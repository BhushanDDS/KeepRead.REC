package mypack;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class DisplayWindow extends JFrame {
    private JTextField searchField;
    private JTable bookTable;

    public DisplayWindow() {
        setTitle("Display Books");
        setSize(600, 400);
        setLocationRelativeTo(null);

        // Create search field
        searchField = new JTextField(20);
        searchField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                searchBooksByAuthor();
            }
        });

        // Create table model with column names
        DefaultTableModel tableModel = new DefaultTableModel();
        tableModel.addColumn("ID");
        tableModel.addColumn("Title");
        tableModel.addColumn("Author");
        tableModel.addColumn("Genre");
        tableModel.addColumn("Price");
        tableModel.addColumn("Read or Not");

        // Create table and set model
        bookTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(bookTable);

        // Add components to panel
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(searchField, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Add button to go back to HomePage
        JButton backButton = new JButton("Back to HomePage");
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose(); // Close the current window
                new HomePage().setVisible(true); // Open HomePage
            }
        });
        panel.add(backButton, BorderLayout.SOUTH);

        // Add panel to frame
        add(panel);

        // Load all books into table initially
        loadBooks();
    }

    private void loadBooks() {
        try {
            // Establish connection to the database
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/bookr", "root", "");

            // Create SQL SELECT statement
            String sql = "SELECT * FROM book";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            // Populate table with book records
            DefaultTableModel tableModel = (DefaultTableModel) bookTable.getModel();
            tableModel.setRowCount(0); // Clear existing rows
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String title = resultSet.getString("title");
                String author = resultSet.getString("author");
                String genre = resultSet.getString("genre");
                double price = resultSet.getDouble("price");
                boolean readOrNot = resultSet.getBoolean("readOrNot");

                // Add row to table model
                tableModel.addRow(new Object[]{id, title, author, genre, price, readOrNot});
            }

            // Close the connection
            connection.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to load books: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void searchBooksByAuthor() {
        String author = searchField.getText();
        if (author.trim().isEmpty()) {
            // If search field is empty, load all books
            loadBooks();
            return;
        }

        try {
            // Establish connection to the database
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/bookr", "root", "");

            // Create SQL SELECT statement with parameterized query
            String sql = "SELECT * FROM book WHERE author LIKE ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, "%" + author + "%");

            // Execute query
            ResultSet resultSet = statement.executeQuery();

            // Populate table with matching book records
            DefaultTableModel tableModel = (DefaultTableModel) bookTable.getModel();
            tableModel.setRowCount(0); // Clear existing rows
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String title = resultSet.getString("title");
                String foundAuthor = resultSet.getString("author");
                String genre = resultSet.getString("genre");
                double price = resultSet.getDouble("price");
                boolean readOrNot = resultSet.getBoolean("readOrNot");

                // Add row to table model
                tableModel.addRow(new Object[]{id, title, foundAuthor, genre, price, readOrNot});
            }

            // Close the connection
            connection.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to search books: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new DisplayWindow().setVisible(true);
            }
        });
    }
}
