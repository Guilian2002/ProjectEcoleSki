package be.pierard.jframes;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import be.pierard.dao.AccreditationDAO;
import be.pierard.dao.SkierDAO;
import be.pierard.dao.EcoleSkiConnection;
import be.pierard.pojo.Accreditation;
import be.pierard.pojo.LessonType;
import be.pierard.pojo.Skier;

public class AddSkier extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField txtLastname, txtFirstname, txtAge, txtAddress, txtEmail;
    private JCheckBox chkInsurance;
    private JLabel lblSelectedLevel;
    private JTable tableLevels;
    private DefaultTableModel tableModel;
    private String selectedLevel = null;

    private SkierDAO skierDAO;
    private AccreditationDAO accreditationDAO;

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                AddSkier frame = new AddSkier();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public AddSkier() {
        skierDAO = new SkierDAO(EcoleSkiConnection.getInstance());
        accreditationDAO = new AccreditationDAO(EcoleSkiConnection.getInstance());

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 900, 500);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new GridLayout(1, 2, 10, 0));
        setContentPane(contentPane);

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridLayout(8, 2, 5, 5));
        contentPane.add(formPanel);

        formPanel.add(new JLabel("Last Name:"));
        txtLastname = new JTextField();
        formPanel.add(txtLastname);

        formPanel.add(new JLabel("First Name:"));
        txtFirstname = new JTextField();
        formPanel.add(txtFirstname);

        formPanel.add(new JLabel("Age:"));
        txtAge = new JTextField();
        formPanel.add(txtAge);

        formPanel.add(new JLabel("Address:"));
        txtAddress = new JTextField();
        formPanel.add(txtAddress);

        formPanel.add(new JLabel("Email:"));
        txtEmail = new JTextField();
        formPanel.add(txtEmail);

        formPanel.add(new JLabel("Insurance:"));
        chkInsurance = new JCheckBox();
        formPanel.add(chkInsurance);

        formPanel.add(new JLabel("Selected Level:"));
        lblSelectedLevel = new JLabel("None");
        formPanel.add(lblSelectedLevel);

        JButton btnAddSkier = new JButton("Add a Skier");
        btnAddSkier.addActionListener(new AddSkierActionListener());
        formPanel.add(btnAddSkier);

        JButton btnGoBack = new JButton("Go Back");
        btnGoBack.addActionListener(e -> {
        	dispose();
            Home homeWindow = new Home();
            homeWindow.setVisible(true);
        });
        formPanel.add(btnGoBack);

        JPanel tablePanel = new JPanel(new BorderLayout());
        contentPane.add(tablePanel);

        tableModel = new DefaultTableModel(new Object[] { "Level" }, 0);
        tableLevels = new JTable(tableModel);
        loadAccreditationLevels();
        tablePanel.add(new JScrollPane(tableLevels), BorderLayout.CENTER);

        JButton btnChooseLevel = new JButton("Choose a Level");
        btnChooseLevel.addActionListener(new ChooseLevelActionListener());
        tablePanel.add(btnChooseLevel, BorderLayout.SOUTH);
    }

    private void loadAccreditationLevels() {
        ArrayList<Accreditation> accreditations = Accreditation.findAllAccreditation(accreditationDAO);
        for (Accreditation acc : accreditations) {
            for (LessonType lessonType : acc.getLessonTypeList()) {
                tableModel.addRow(new Object[] { lessonType.getFullLevel() });
            }
        }
    }

    private class ChooseLevelActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int selectedRow = tableLevels.getSelectedRow();
            if (selectedRow >= 0) {
                selectedLevel = (String) tableModel.getValueAt(selectedRow, 0);
                lblSelectedLevel.setText(selectedLevel);
            } else {
                JOptionPane.showMessageDialog(null, "Please select a level from the table.");
            }
        }
    }

    private class AddSkierActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String lastname = txtLastname.getText().trim();
            String firstname = txtFirstname.getText().trim();
            String ageStr = txtAge.getText().trim();
            String address = txtAddress.getText().trim();
            String email = txtEmail.getText().trim();
            boolean insurance = chkInsurance.isSelected();

            if (lastname.isEmpty() || firstname.isEmpty() || ageStr.isEmpty() ||
                    address.isEmpty() || email.isEmpty() || selectedLevel == null) {
                JOptionPane.showMessageDialog(null, "All fields must be filled, and a level must be selected.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                int age = Integer.parseInt(ageStr);
                Skier skier = new Skier(0, lastname, firstname, age, address, email, insurance, selectedLevel);
                boolean success = skier.makeSkier(skierDAO, false);

                if (success) {
                    resetForm();
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Age must be a valid number.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        private void resetForm() {
            txtLastname.setText("");
            txtFirstname.setText("");
            txtAge.setText("");
            txtAddress.setText("");
            txtEmail.setText("");
            chkInsurance.setSelected(false);
            lblSelectedLevel.setText("None");
            selectedLevel = null;
        }
    }
}
