package mypack;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class DeleteBookWindow extends JFrame {
    private JTable bookTable;
    private JTextField bookIdField;

    public DeleteBookWindow() {
        setTitle("Delete Book");
        setSize(600, 400);
        setLocationRelativeTo(null);

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

        // Create label and text field for book ID
        JLabel bookIdLabel = new JLabel("Enter Book ID:");
        bookIdField = new JTextField(10);

        // Create button to delete book
        JButton deleteButton = new JButton("Delete Book");
        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                deleteBook();
            }
        });

        // Create button to go back to HomePage
        JButton backButton = new JButton("Back to HomePage");
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose(); // Close the current window
                new HomePage().setVisible(true); // Open HomePage
            }
        });

        // Create panel to hold components
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(scrollPane, BorderLayout.CENTER);

        JPanel deletePanel = new JPanel(new FlowLayout());
        deletePanel.add(bookIdLabel);
        deletePanel.add(bookIdField);
        deletePanel.add(deleteButton);
        panel.add(deletePanel, BorderLayout.SOUTH);

        // Add back button to the delete panel
        deletePanel.add(backButton);

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

    private void deleteBook() {
        String bookIdText = bookIdField.getText();
        try {
            int bookId = Integer.parseInt(bookIdText);

            // Establish connection to the database
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/bookr", "username", "password");

            // Create SQL DELETE statement
            String sql = "DELETE FROM book WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, bookId);

            // Execute update
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "Book deleted successfully.");
                loadBooks(); // Refresh the table
            } else {
                JOptionPane.showMessageDialog(this, "No book found with ID: " + bookId, "Error", JOptionPane.ERROR_MESSAGE);
            }

            // Close the connection
            connection.close();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid book ID.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to delete book: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new DeleteBookWindow().setVisible(true);
            }
        });
    }
}
