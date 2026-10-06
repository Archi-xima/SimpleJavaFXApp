package com.studentapp.view;

import com.studentapp.model.Student;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * Graphical User Interface (Swing) for the Student Registration System.
 * Represents the View layer in the MVC pattern.
 */
public class StudentGuiView extends JFrame {

    // Input fields
    private final JTextField idField = new JTextField(15);
    private final JTextField nameField = new JTextField(15);
    private final JTextField emailField = new JTextField(15);
    private final JTextField courseField = new JTextField(15);

    // Buttons
    private final JButton registerButton = new JButton("Register Student");
    private final JButton clearButton = new JButton("Clear Form");
    private final JButton searchButton = new JButton("Search ID");
    private final JButton refreshButton = new JButton("Show All");

    // Search field
    private final JTextField searchField = new JTextField(10);

    // Table & Model
    private final DefaultTableModel tableModel;
    private final JTable studentTable = new JTable();
    private final JLabel countLabel = new JLabel("Total Students: 0");

    // Status Banner
    private final JLabel statusLabel = new JLabel("Ready");

    public StudentGuiView() {
        setTitle("Student Registration System (MVC)");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(960, 620);
        setMinimumSize(new Dimension(860, 500));
        setLocationRelativeTo(null);

        // Apply system look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        // Table Model setup
        String[] columns = {"Student ID", "Full Name", "Email Address", "Course/Major"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // read-only table
            }
        };
        studentTable.setModel(tableModel);
        customizeTable();

        // Root layout
        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        mainPanel.setBackground(new Color(245, 247, 250));

        // 1. Header Banner
        mainPanel.add(createHeaderPanel(), BorderLayout.NORTH);

        // 2. Center Content Split: Form on Left, Table on Right
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                createFormPanel(), createTablePanel());
        splitPane.setResizeWeight(0.38);
        splitPane.setDividerSize(6);
        splitPane.setBorder(null);
        splitPane.setBackground(new Color(245, 247, 250));
        mainPanel.add(splitPane, BorderLayout.CENTER);

        // 3. Status Bar at Bottom
        mainPanel.add(createStatusPanel(), BorderLayout.SOUTH);

        setContentPane(mainPanel);
    }

    private JPanel createHeaderPanel() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(33, 50, 94));
        header.setBorder(new EmptyBorder(14, 20, 14, 20));

        JLabel title = new JLabel("Student Registration System");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(Color.WHITE);

        JLabel subtitle = new JLabel("Model-View-Controller (MVC) Architecture");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        subtitle.setForeground(new Color(200, 215, 240));

        header.add(title, BorderLayout.NORTH);
        header.add(subtitle, BorderLayout.SOUTH);
        return header;
    }

    private JPanel createFormPanel() {
        JPanel container = new JPanel(new BorderLayout(10, 10));
        container.setBackground(Color.WHITE);
        container.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 224, 230), 1),
                new EmptyBorder(15, 15, 15, 15)
        ));

        JLabel formTitle = new JLabel("Register New Student");
        formTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        formTitle.setForeground(new Color(33, 50, 94));
        formTitle.setBorder(new EmptyBorder(0, 0, 10, 0));
        container.add(formTitle, BorderLayout.NORTH);

        JPanel fieldsPanel = new JPanel(new GridBagLayout());
        fieldsPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 4, 8, 4);

        int row = 0;
        addFormField(fieldsPanel, gbc, row++, "Student ID *", idField, "e.g. STU101");
        addFormField(fieldsPanel, gbc, row++, "Full Name *", nameField, "e.g. Alice Smith");
        addFormField(fieldsPanel, gbc, row++, "Email Address *", emailField, "e.g. alice@university.edu");
        addFormField(fieldsPanel, gbc, row++, "Course/Major *", courseField, "e.g. Computer Science");

        // Action Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        buttonPanel.setBackground(Color.WHITE);

        clearButton.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        clearButton.setFocusPainted(false);
        clearButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        registerButton.setFont(new Font("Segoe UI", Font.BOLD, 13));
        registerButton.setBackground(new Color(25, 118, 210));
        registerButton.setForeground(Color.WHITE);
        registerButton.setFocusPainted(false);
        registerButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        buttonPanel.add(clearButton);
        buttonPanel.add(registerButton);

        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 2;
        fieldsPanel.add(buttonPanel, gbc);

        container.add(fieldsPanel, BorderLayout.CENTER);
        return container;
    }

    private void addFormField(JPanel panel, GridBagConstraints gbc, int row,
                              String labelText, JTextField field, String tooltip) {
        gbc.gridwidth = 1;
        gbc.weightx = 0.0;
        gbc.gridx = 0;
        gbc.gridy = row;

        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Segoe UI", Font.BOLD, 13));
        panel.add(label, gbc);

        gbc.weightx = 1.0;
        gbc.gridx = 1;
        field.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        field.setToolTipText(tooltip);
        panel.add(field, gbc);
    }

    private JPanel createTablePanel() {
        JPanel container = new JPanel(new BorderLayout(10, 10));
        container.setBackground(Color.WHITE);
        container.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 224, 230), 1),
                new EmptyBorder(15, 15, 15, 15)
        ));

        // Search Bar at Top of Table
        JPanel searchBar = new JPanel(new BorderLayout(8, 0));
        searchBar.setBackground(Color.WHITE);

        JPanel searchControls = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        searchControls.setBackground(Color.WHITE);
        searchControls.add(new JLabel("Search ID:"));
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        searchControls.add(searchField);
        searchButton.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        refreshButton.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        searchControls.add(searchButton);
        searchControls.add(refreshButton);

        countLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        countLabel.setForeground(new Color(70, 80, 95));

        searchBar.add(searchControls, BorderLayout.WEST);
        searchBar.add(countLabel, BorderLayout.EAST);
        container.add(searchBar, BorderLayout.NORTH);

        // Table scroll
        JScrollPane scrollPane = new JScrollPane(studentTable);
        scrollPane.getViewport().setBackground(Color.WHITE);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(230, 235, 240)));
        container.add(scrollPane, BorderLayout.CENTER);

        return container;
    }

    private void customizeTable() {
        studentTable.setRowHeight(28);
        studentTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        studentTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        studentTable.setShowVerticalLines(false);
        studentTable.setGridColor(new Color(235, 240, 245));

        JTableHeader header = studentTable.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setBackground(new Color(240, 243, 246));
        header.setForeground(new Color(40, 50, 70));
        header.setReorderingAllowed(false);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        studentTable.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
    }

    private JPanel createStatusPanel() {
        JPanel statusPanel = new JPanel(new BorderLayout());
        statusPanel.setBackground(new Color(240, 243, 246));
        statusPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 224, 230), 1),
                new EmptyBorder(8, 12, 8, 12)
        ));

        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        statusLabel.setForeground(new Color(60, 70, 85));
        statusPanel.add(statusLabel, BorderLayout.WEST);
        return statusPanel;
    }

    // --- Accessors for Controller ---

    public String getIdInput() { return idField.getText(); }
    public String getNameInput() { return nameField.getText(); }
    public String getEmailInput() { return emailField.getText(); }
    public String getCourseInput() { return courseField.getText(); }
    public String getSearchInput() { return searchField.getText(); }

    public void clearFormFields() {
        idField.setText("");
        nameField.setText("");
        emailField.setText("");
        courseField.setText("");
        idField.requestFocus();
    }

    public void addRegisterListener(ActionListener listener) {
        registerButton.addActionListener(listener);
    }

    public void addClearListener(ActionListener listener) {
        clearButton.addActionListener(listener);
    }

    public void addSearchListener(ActionListener listener) {
        searchButton.addActionListener(listener);
    }

    public void addRefreshListener(ActionListener listener) {
        refreshButton.addActionListener(listener);
    }

    public void updateStudentTable(List<Student> students) {
        tableModel.setRowCount(0);
        for (Student s : students) {
            tableModel.addRow(new Object[]{s.getId(), s.getName(), s.getEmail(), s.getCourse()});
        }
        countLabel.setText("Total Students: " + students.size());
    }

    public void showSuccess(String message) {
        statusLabel.setText("✓ " + message);
        statusLabel.setForeground(new Color(20, 130, 60));
        JOptionPane.showMessageDialog(this, message, "Registration Successful", JOptionPane.INFORMATION_MESSAGE);
    }

    public void showError(String errorMessage) {
        statusLabel.setText("⚠ " + errorMessage);
        statusLabel.setForeground(new Color(180, 30, 30));
        JOptionPane.showMessageDialog(this, errorMessage, "Registration Error", JOptionPane.ERROR_MESSAGE);
    }

    public void showInfo(String infoMessage) {
        statusLabel.setText("ℹ " + infoMessage);
        statusLabel.setForeground(new Color(40, 60, 90));
    }
}
