package viewer;

import javax.swing.*;
import javax.swing.table.TableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class SQLiteViewer extends JFrame {

    final String PROGRAM_TITLE = "SQLite Viewer";
    final String BTN_OPEN = "Open";
    final String BTN_EXEC = "Execute";
    DB db;

    public SQLiteViewer() {
        setTitle(PROGRAM_TITLE);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 800);
        setLayout(null);
        setResizable(false);
        setLocationRelativeTo(null);

        JTextField fileNameTextField = new JTextField();
        fileNameTextField.setName("FileNameTextField");
        fileNameTextField.setBounds(20, 20, getWidth() - 130, 30);
        add(fileNameTextField);

        JButton openFileButton = new JButton(BTN_OPEN);
        openFileButton.setName("OpenFileButton");
        openFileButton.setBounds(getWidth() - 100, 20, 70, 30);
        add(openFileButton);

        JComboBox<String> tablesComboBox = new JComboBox<>();
        tablesComboBox.setName("TablesComboBox");
        tablesComboBox.setBounds(20, 70, getWidth() - 50, 30);
        add(tablesComboBox);

        JTextArea queryTextArea = new JTextArea();
        queryTextArea.setName("QueryTextArea");
        queryTextArea.setBounds(20, 120, getWidth() - 170, 80);
        queryTextArea.setEnabled(false);
        add(queryTextArea);

        JButton executeQueryButton = new JButton(BTN_EXEC);
        executeQueryButton.setName("ExecuteQueryButton");
        executeQueryButton.setBounds(getWidth() - 140, 120, 110, 35);
        executeQueryButton.setEnabled(false);
        add(executeQueryButton);

        JTable table = new JTable();
        table.setName("Table");

        JScrollPane sp = new JScrollPane(table);
        sp.setBounds(20, 220, getWidth() - 55, 100);
        add(sp);

        openFileButton.addActionListener(e -> {
            try {
                tablesComboBox.setEnabled(true);
                queryTextArea.setEnabled(true);
                executeQueryButton.setEnabled(true);
                db = new DB(fileNameTextField.getText());
                tablesComboBox.removeAllItems();
                List<String> tables = db.getTables();
                tables.forEach(tablesComboBox::addItem);
            } catch (SQLException ex) {
                tablesComboBox.setEnabled(false);
                queryTextArea.setEnabled(false);
                executeQueryButton.setEnabled(false);
                queryTextArea.removeAll();
                JOptionPane.showMessageDialog(new Frame(), "File doesn't exist!");
            }
        });

        tablesComboBox.addActionListener(e -> {
            queryTextArea.removeAll();
            if (tablesComboBox.getSelectedItem() != null) {
                String selectAllQuery = String.format("SELECT * FROM %s;", tablesComboBox.getSelectedItem());
                queryTextArea.setText(selectAllQuery);
            }
        });

        executeQueryButton.addActionListener(e -> {
            if (!queryTextArea.getText().isBlank()) {
                db = new DB(fileNameTextField.getText());
                try {
                    TableModel tableModel = new CustomTableModel(queryTextArea.getText().trim(), db);
                    table.setModel(tableModel);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(new Frame(), "Invalid query");
                }
            }
        });

        setVisible(true);
    }
}
