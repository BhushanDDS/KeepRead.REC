package mypack;


import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class AddBookWindow extends JFrame {
    private JTextField titleField;
    private JTextField authorField;
    private JTextField genreField;
    private JTextField priceField;
    private JCheckBox readOrNotCheckBox;

    public AddBookWindow() {
        setTitle("Add Book");
        setSize(600, 400);
        setLocationRelativeTo(null);

        // Create text fields and checkbox
        titleField = new JTextField(20);
        authorField = new JTextField(20);
        genreField = new JTextField(20);
        priceField = new JTextField(20);
        readOrNotCheckBox = new JCheckBox("Read or Not");

        // Create button for adding the book
        JButton addButton = new JButton("Add Book");
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addBookToDatabase();
            }
        });

        // Create buttons for navigation
        JButton homeButton = new JButton("Go to HomePage");
        homeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose(); // Close the current window
                new HomePage().setVisible(true); // Open HomePage
            }
        });

        JButton displayButton = new JButton("Go to DisplayPage");
        displayButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose(); // Close the current window
                new DisplayWindow().setVisible(true); // Open DisplayPage
            }
        });

        // Create panel for components
        JPanel panel = new JPanel(new GridLayout(7, 2));
        panel.add(new JLabel("Title:"));
        panel.add(titleField);
        panel.add(new JLabel("Author:"));
        panel.add(authorField);
        panel.add(new JLabel("Genre:"));
        panel.add(genreField);
        panel.add(new JLabel("Price:"));
        panel.add(priceField);
        panel.add(new JLabel("Read or Not:"));
        panel.add(readOrNotCheckBox);
        panel.add(new JLabel()); // Empty label for spacing
        panel.add(addButton);
        panel.add(homeButton);
        panel.add(displayButton);

        // Add panel to the frame
        add(panel);
    }

    private void addBookToDatabase() {
        String title = titleField.getText();
        String author = authorField.getText();
        String genre = genreField.getText();
        double price = Double.parseDouble(priceField.getText());
        boolean readOrNot = readOrNotCheckBox.isSelected();

        try {
            // Establish connection to the database
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/bookr", "root", "");

            // Create SQL INSERT statement
            String sql = "INSERT INTO book (title, author, genre, price, readOrNot) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);

            // Set parameters for the INSERT statement
            statement.setString(1, title);
            statement.setString(2, author);
            statement.setString(3, genre);
            statement.setDouble(4, price);
            statement.setBoolean(5, readOrNot);

            // Execute the INSERT statement
            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                JOptionPane.showMessageDialog(this, "Book added successfully!");
            } else {
                JOptionPane.showMessageDialog(this, "Failed to add book!");
            }

            // Close the connection
            connection.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}

