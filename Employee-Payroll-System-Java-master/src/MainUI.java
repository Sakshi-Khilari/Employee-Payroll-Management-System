import javax.swing.*;
import java.awt.*;
import java.sql.*;
import javax.swing.table.DefaultTableModel;

// ===== DB Connection =====
class DBConnection {
    public static Connection getConnection() throws Exception {
        return DriverManager.getConnection(
                "jdbc:mysql://localhost:3307/payroll",
                "root",
                ""
        );
    }
}

// ===== UI =====
public class MainUI {
    public static void main(String[] args) {

        JFrame frame = new JFrame("Employee Payroll Management System");
        frame.setSize(900, 550);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // 🔥 TITLE
        JLabel title = new JLabel("Employee Payroll Management System", JLabel.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
        title.setForeground(Color.BLUE);
        frame.add(title, BorderLayout.NORTH);

        // 🔥 INPUT FIELDS
        JTextField nameField = new JTextField();
        JTextField idField = new JTextField();
        JTextField salaryField = new JTextField();

        nameField.setPreferredSize(new Dimension(200, 30));
        idField.setPreferredSize(new Dimension(200, 30));
        salaryField.setPreferredSize(new Dimension(200, 30));

        // 🔥 FORM PANEL (CENTERED 🔥)
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Enter Employee Details"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 20, 10, 20);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Name
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1;
        formPanel.add(nameField, gbc);

        // ID
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("ID:"), gbc);
        gbc.gridx = 1;
        formPanel.add(idField, gbc);

        // Salary
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Salary:"), gbc);
        gbc.gridx = 1;
        formPanel.add(salaryField, gbc);

        // 🔥 BUTTON PANEL
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 15));

        JButton addBtn = new JButton("Add");
        JButton showBtn = new JButton("Show");
        JButton deleteBtn = new JButton("Delete");
        JButton updateBtn = new JButton("Update");

        addBtn.setBackground(new Color(46, 204, 113));
        showBtn.setBackground(new Color(52, 152, 219));
        deleteBtn.setBackground(new Color(231, 76, 60));
        updateBtn.setBackground(new Color(241, 196, 15));

        btnPanel.add(addBtn);
        btnPanel.add(showBtn);
        btnPanel.add(deleteBtn);
        btnPanel.add(updateBtn);

        // 🔥 TABLE
        String[] columns = {"Name", "ID", "Salary"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);

        JTable table = new JTable(model);
        table.setRowHeight(25);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JScrollPane scroll = new JScrollPane(table);

        // 🔥 MAIN PANEL (CENTER ALIGN 🔥)
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 150, 10, 150));

        mainPanel.add(formPanel, BorderLayout.NORTH);
        mainPanel.add(btnPanel, BorderLayout.CENTER);
        mainPanel.add(scroll, BorderLayout.SOUTH);

        frame.add(mainPanel, BorderLayout.CENTER);

        // ===== LOGIC =====

        // ADD
        addBtn.addActionListener(e -> {
            try {
                String name = nameField.getText();
                int id = Integer.parseInt(idField.getText());
                double salary = Double.parseDouble(salaryField.getText());

                Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(
                        "INSERT INTO employee VALUES (?, ?, ?)"
                );

                ps.setInt(1, id);
                ps.setString(2, name);
                ps.setDouble(3, salary);

                ps.executeUpdate();
                JOptionPane.showMessageDialog(frame, "Employee Added!");

                con.close();

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        // SHOW
        showBtn.addActionListener(e -> {
            try {
                model.setRowCount(0);

                Connection con = DBConnection.getConnection();
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery("SELECT * FROM employee");

                while (rs.next()) {
                    model.addRow(new Object[]{
                            rs.getString("name"),
                            rs.getInt("id"),
                            rs.getDouble("salary")
                    });
                }

                con.close();

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        // DELETE
        deleteBtn.addActionListener(e -> {
            try {
                int id = Integer.parseInt(idField.getText());

                Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(
                        "DELETE FROM employee WHERE id=?"
                );

                ps.setInt(1, id);
                ps.executeUpdate();

                JOptionPane.showMessageDialog(frame, "Deleted!");

                con.close();
                showBtn.doClick();

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        // UPDATE
        updateBtn.addActionListener(e -> {
            try {
                int id = Integer.parseInt(idField.getText());
                double salary = Double.parseDouble(salaryField.getText());

                Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(
                        "UPDATE employee SET salary=? WHERE id=?"
                );

                ps.setDouble(1, salary);
                ps.setInt(2, id);

                ps.executeUpdate();

                JOptionPane.showMessageDialog(frame, "Updated!");

                con.close();
                showBtn.doClick();

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}