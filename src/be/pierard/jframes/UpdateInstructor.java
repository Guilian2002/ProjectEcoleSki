package be.pierard.jframes;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import be.pierard.dao.EcoleSkiConnection;
import be.pierard.dao.InstructorDAO;
import be.pierard.pojo.Instructor;

public class UpdateInstructor extends JFrame {

    private static final long serialVersionUID = 4460565302616646140L;
    private JPanel contentPane;
    private JTextField txtLastname;
    private JTextField txtFirstname;
    private JTextField txtAge;
    private JTextField txtAddress;
    private JTextField txtEmail;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UpdateInstructor frame = new UpdateInstructor(null);
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
    
    public UpdateInstructor(Instructor instructor) {
        if (instructor == null) {
            dispose();
            new SeeAllInstructors().setVisible(true);
            return;
        }

        setTitle("Update Instructor");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 500, 300);

        contentPane = new JPanel(new BorderLayout());
        setContentPane(contentPane);

        JPanel formPanel = new JPanel(new GridLayout(8, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        contentPane.add(formPanel, BorderLayout.WEST);

        txtLastname = addTextField("Lastname", formPanel, instructor != null ? instructor.getLastname() : "");
        txtFirstname = addTextField("Firstname", formPanel, instructor != null ? instructor.getFirstname() : "");
        txtAge = addTextField("Age", formPanel, instructor != null ? String.valueOf(instructor.getAge()) : "");
        txtAddress = addTextField("Address", formPanel, instructor != null ? instructor.getAddress() : "");
        txtEmail = addTextField("Email", formPanel, instructor != null ? instructor.getEmail() : "");

        JButton btnUpdateInstructor = new JButton("Update Instructor");
        formPanel.add(btnUpdateInstructor);

        JButton btnGoBack = new JButton("Go Back");
        btnGoBack.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
                new SeeAllInstructors().setVisible(true);
            }
        });
        formPanel.add(btnGoBack);

        btnUpdateInstructor.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (validateInput()) {
                    String lastname = txtLastname.getText();
                    String firstname = txtFirstname.getText();
                    int age = Integer.parseInt(txtAge.getText());
                    String address = txtAddress.getText();
                    String email = txtEmail.getText();
                    instructor.setLastname(lastname);
                    instructor.setFirstname(firstname);
                    instructor.setAge(age);
                    instructor.setAddress(address);
                    instructor.setEmail(email);
                    InstructorDAO instructorDAO = new InstructorDAO(EcoleSkiConnection.getInstance());
                    boolean success = instructor.makeInstructor(instructorDAO, true);

                    if (success) {
                        JOptionPane.showMessageDialog(null, "Instructor updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(null, "Failed to update instructor.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
    }

    private JTextField addTextField(String label, JPanel panel, String initialValue) {
        panel.add(new JLabel(label));
        JTextField textField = new JTextField(initialValue);
        panel.add(textField);
        return textField;
    }

    private boolean validateInput() {
        if (txtLastname.getText().isEmpty() || txtFirstname.getText().isEmpty() || txtAge.getText().isEmpty() ||
                txtAddress.getText().isEmpty() || txtEmail.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        try {
            Integer.parseInt(txtAge.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Age must be numeric.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }
}
