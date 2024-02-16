package mypack;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class ReadOrNotWindow extends JFrame {
    private JTextField bookIdField;

    public ReadOrNotWindow() {
        setTitle("Set Read Status");
        setSize(600, 400);
        setLocationRelativeTo(null);

        // Create label and text field for book ID
        JLabel bookIdLabel = new JLabel("Enter Book ID:");
        bookIdField = new JTextField(10);

        // Create button to update read status
        JButton updateButton = new JButton("Update Read Status");
        updateButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                updateReadStatus();
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
        JPanel panel = new JPanel(new FlowLayout());
        panel.add(bookIdLabel);
        panel.add(bookIdField);
        panel.add(updateButton);
        panel.add(backButton);

        // Add panel to frame
        add(panel);
    }

    private void updateReadStatus() {
        String bookIdText = bookIdField.getText();
        try {
            int bookId = Integer.parseInt(bookIdText);

            // Establish connection to the database
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/bookr", "root", "");

            // Create SQL UPDATE statement
            String sql = "UPDATE book SET readOrNot = ? WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setBoolean(1, true);
            statement.setInt(2, bookId);

            // Execute update
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "Read status updated successfully.");
            } else {
                JOptionPane.showMessageDialog(this, "No book found with ID: " + bookId, "Error", JOptionPane.ERROR_MESSAGE);
            }

            // Close the connection
            connection.close();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid book ID.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to update read status: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new ReadOrNotWindow().setVisible(true);
            }
        });
    }
}
