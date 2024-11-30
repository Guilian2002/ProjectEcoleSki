package be.pierard.jframes;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import be.pierard.dao.SkierDAO;
import be.pierard.pojo.Skier;

public class UpdateSkier extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField txtLastname, txtFirstname, txtAge, txtAddress, txtEmail;
    private JCheckBox chkInsurance;
    private JLabel lblSelectedLevel;
    private Skier skier;
    private SkierDAO skierDAO;

    public UpdateSkier(Skier skier, SkierDAO skierDAO) {
        this.skier = skier;
        this.skierDAO = skierDAO;

        if (this.skier == null) {
            JOptionPane.showMessageDialog(null, "No skier selected.", "Error", JOptionPane.ERROR_MESSAGE);
            dispose();
            new SeeAllSkiers(skierDAO).setVisible(true);
            return;
        }

        initComponents();
        loadSkierData();
    }

    private void initComponents() {
        setTitle("Update Skier");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 600, 400);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new GridLayout(8, 2, 5, 5));
        setContentPane(contentPane);

        contentPane.add(new JLabel("Last Name:"));
        txtLastname = new JTextField();
        contentPane.add(txtLastname);

        contentPane.add(new JLabel("First Name:"));
        txtFirstname = new JTextField();
        contentPane.add(txtFirstname);

        contentPane.add(new JLabel("Age:"));
        txtAge = new JTextField();
        contentPane.add(txtAge);

        contentPane.add(new JLabel("Address:"));
        txtAddress = new JTextField();
        contentPane.add(txtAddress);

        contentPane.add(new JLabel("Email:"));
        txtEmail = new JTextField();
        contentPane.add(txtEmail);

        contentPane.add(new JLabel("Insurance:"));
        chkInsurance = new JCheckBox();
        contentPane.add(chkInsurance);

        contentPane.add(new JLabel("Selected Level:"));
        lblSelectedLevel = new JLabel();
        contentPane.add(lblSelectedLevel);

        JButton btnUpdateSkier = new JButton("Update Skier");
        btnUpdateSkier.addActionListener(new UpdateSkierActionListener());
        contentPane.add(btnUpdateSkier);

        JButton btnGoBack = new JButton("Go Back");
        btnGoBack.addActionListener(e -> {
            dispose();
            new SeeAllSkiers(skierDAO).setVisible(true);
        });
        contentPane.add(btnGoBack);
    }

    private void loadSkierData() {
        txtLastname.setText(skier.getLastname());
        txtFirstname.setText(skier.getFirstname());
        txtAge.setText(String.valueOf(skier.getAge()));
        txtAddress.setText(skier.getAddress());
        txtEmail.setText(skier.getEmail());
        chkInsurance.setSelected(skier.isInsurance());
        lblSelectedLevel.setText(skier.getLevel());
    }

    private class UpdateSkierActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String lastname = txtLastname.getText().trim();
            String firstname = txtFirstname.getText().trim();
            String ageStr = txtAge.getText().trim();
            String address = txtAddress.getText().trim();
            String email = txtEmail.getText().trim();
            boolean insurance = chkInsurance.isSelected();

            if (lastname.isEmpty() || firstname.isEmpty() || ageStr.isEmpty() ||
                    address.isEmpty() || email.isEmpty()) {
                JOptionPane.showMessageDialog(null, "All fields must be filled.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                int age = Integer.parseInt(ageStr);
                skier.setLastname(lastname);
                skier.setFirstname(firstname);
                skier.setAge(age);
                skier.setAddress(address);
                skier.setEmail(email);
                skier.setInsurance(insurance);

                boolean success = skier.makeSkier(skierDAO, true);

                if (success) {
                    JOptionPane.showMessageDialog(null, "Skier updated successfully.");
                    loadSkierData();
                } else {
                    JOptionPane.showMessageDialog(null, "Failed to update skier.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    loadSkierData();
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Age must be a valid number.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                loadSkierData();
            }
        }
    }
}
