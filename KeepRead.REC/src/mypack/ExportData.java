package mypack;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ExportData extends JFrame {
    public ExportData() {
        setTitle("Export Data");
        setSize(400, 300);
        setLocationRelativeTo(null);

        JButton exportButton = new JButton("Export to Excel");
        exportButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                exportToExcel();
            }
        });

        JPanel panel = new JPanel();
        panel.add(exportButton);
        add(panel);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Close only this window when closed
    }

    private void exportToExcel() {
        try {
            // Establish connection to the database
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/bookr", "root", "");

            // Create SQL statement to fetch data from the book table
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM book");

            // Create Excel workbook
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("Book Data");

            // Write table headers to Excel
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("ID");
            headerRow.createCell(1).setCellValue("Title");
            headerRow.createCell(2).setCellValue("Author");
            headerRow.createCell(3).setCellValue("Genre");
            headerRow.createCell(4).setCellValue("Price");
            headerRow.createCell(5).setCellValue("Read or Not");

            // Write table data to Excel
            int rowNum = 1;
            while (resultSet.next()) {
                Row dataRow = sheet.createRow(rowNum++);
                dataRow.createCell(0).setCellValue(resultSet.getInt("id"));
                dataRow.createCell(1).setCellValue(resultSet.getString("title"));
                dataRow.createCell(2).setCellValue(resultSet.getString("author"));
                dataRow.createCell(3).setCellValue(resultSet.getString("genre"));
                dataRow.createCell(4).setCellValue(resultSet.getDouble("price"));
                dataRow.createCell(5).setCellValue(resultSet.getBoolean("readOrNot"));
            }

            // Save Excel file
            FileOutputStream fileOut = new FileOutputStream("book_data.xlsx");
            workbook.write(fileOut);
            fileOut.close();

            workbook.close();
            resultSet.close();
            statement.close();
            connection.close();

            JOptionPane.showMessageDialog(this, "Data exported to Excel successfully.", "Export Complete", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException | IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to export data to Excel.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new ExportData().setVisible(true);
            }
        });
    }
}
