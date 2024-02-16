package mypack;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class HomePage extends JFrame {
    private JButton addButton;
    private JButton displayButton;
    private JButton readOrNotButton;
    private JButton deleteButton;
    private JButton exportButton; // New export button

    public HomePage() {
        setTitle("KeepRead.REC");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(600, 200);
        setLocationRelativeTo(null);

        // Create buttons
        addButton = new JButton("Add Book");
        displayButton = new JButton("Display");
        readOrNotButton = new JButton("Read or Not");
        deleteButton = new JButton("Delete Book");
        exportButton = new JButton("Export"); // Initialize export button

        // Create panel for buttons
        JPanel buttonPanel = new JPanel(new GridLayout(1, 5)); // Increase grid layout to accommodate the export button
        buttonPanel.add(addButton);
        buttonPanel.add(displayButton);
        buttonPanel.add(readOrNotButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(exportButton); // Add export button to the panel

        // Add action listeners to buttons
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Open AddBookWindow
                new AddBookWindow().setVisible(true);
            }
        });

        displayButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Open DisplayWindow
                new DisplayWindow().setVisible(true);
            }
        });

        readOrNotButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Open ReadOrNotWindow
                new ReadOrNotWindow().setVisible(true);
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Open DeleteBookWindow
                new DeleteBookWindow().setVisible(true);
            }
        });

        exportButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Open ExportData window
                new ExportData().setVisible(true);
            }
        });

        // Set layout for the frame
        setLayout(new BorderLayout());

        // Add button panel to the frame
        add(buttonPanel, BorderLayout.CENTER);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new HomePage().setVisible(true);
            }
        });
    }
}
