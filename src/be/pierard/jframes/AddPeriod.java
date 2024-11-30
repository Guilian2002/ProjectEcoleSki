package be.pierard.jframes;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javax.swing.*;

import be.pierard.dao.EcoleSkiConnection;
import be.pierard.dao.PeriodDAO;
import be.pierard.pojo.Period;

public class AddPeriod extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField txtStartDate;
    private JTextField txtEndDate;
    
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    AddPeriod frame = new AddPeriod();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public AddPeriod() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 595, 400);
        contentPane = new JPanel();
        contentPane.setLayout(null);
        setContentPane(contentPane);
        
        JLabel lblTitle = new JLabel("Add a New Period");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 18));
        lblTitle.setBounds(200, 10, 158, 30);
        contentPane.add(lblTitle);
        
        JLabel lblStationDates = new JLabel("The station opens from Saturday 06/12/2024 to Sunday 03/05/2025");
        lblStationDates.setFont(new Font("Arial", Font.PLAIN, 14));
        lblStationDates.setBounds(50, 50, 439, 20);
        contentPane.add(lblStationDates);

        JLabel lblStartDate = new JLabel("Start Date (yyyy-mm-dd):");
        lblStartDate.setBounds(50, 90, 150, 20);
        contentPane.add(lblStartDate);
        
        txtStartDate = new JTextField();
        txtStartDate.setBounds(200, 90, 150, 20);
        contentPane.add(txtStartDate);
        txtStartDate.setColumns(10);
        
        JLabel lblEndDate = new JLabel("End Date (yyyy-mm-dd):");
        lblEndDate.setBounds(50, 130, 150, 20);
        contentPane.add(lblEndDate);
        
        txtEndDate = new JTextField();
        txtEndDate.setBounds(200, 130, 150, 20);
        contentPane.add(txtEndDate);
        txtEndDate.setColumns(10);
        
        JButton btnAddPeriod = new JButton("Add a Period");
        btnAddPeriod.setFont(new Font("Arial", Font.PLAIN, 14));
        btnAddPeriod.setBounds(50, 180, 150, 30);
        contentPane.add(btnAddPeriod);
        
        JButton btnGoBack = new JButton("Go Back");
        btnGoBack.setFont(new Font("Arial", Font.PLAIN, 14));
        btnGoBack.setBounds(210, 180, 150, 30);
        contentPane.add(btnGoBack);

        btnAddPeriod.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addPeriodAction();
            }
        });
        
        btnGoBack.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
                new Home().setVisible(true);
            }
        });
    }

    private void addPeriodAction() {
        String startDateStr = txtStartDate.getText();
        String endDateStr = txtEndDate.getText();
        
        if (startDateStr.isEmpty() || endDateStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Error: Please fill in both start date and end date.", 
                                          "Input Error", JOptionPane.ERROR_MESSAGE);
            resetForm();
            return;
        }
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate startDate = null;
        LocalDate endDate = null;
        try {
            startDate = LocalDate.parse(startDateStr, formatter);
            endDate = LocalDate.parse(endDateStr, formatter);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: Invalid date format. Please use yyyy-MM-dd.", 
                                          "Date Format Error", JOptionPane.ERROR_MESSAGE);
            resetForm();
            return;
        }

        Period period = new Period(0, startDate, endDate, false);
        PeriodDAO periodDAO = new PeriodDAO(EcoleSkiConnection.getInstance());
        boolean success = period.makePeriod(periodDAO);

        if (!success) {
            JOptionPane.showMessageDialog(this, "Error: Failed to add period.", 
                                          "Database Error", JOptionPane.ERROR_MESSAGE);
            resetForm();
        }
    }

    private void resetForm() {
        txtStartDate.setText("");
        txtEndDate.setText("");
    }
}
