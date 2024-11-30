package be.pierard.jframes;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import be.pierard.dao.EcoleSkiConnection;
import be.pierard.dao.PeriodDAO;
import be.pierard.pojo.Period;

public class SeeAllPeriods extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField txtSearch;
    private JTable table;
    private DefaultTableModel tableModel;
    private ArrayList<Period> allPeriods;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    SeeAllPeriods frame = new SeeAllPeriods();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public SeeAllPeriods() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 681, 400);
        contentPane = new JPanel();
        contentPane.setLayout(null);
        setContentPane(contentPane);

        JLabel lblTitle = new JLabel("List of Periods");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 18));
        lblTitle.setBounds(230, 10, 150, 30);
        contentPane.add(lblTitle);

        JLabel lblSearch = new JLabel("Search Period (Start Date):");
        lblSearch.setBounds(20, 50, 175, 20);
        contentPane.add(lblSearch);

        txtSearch = new JTextField();
        txtSearch.setBounds(180, 51, 150, 20);
        contentPane.add(txtSearch);

        JButton btnSearch = new JButton("Search");
        btnSearch.setBounds(380, 48, 100, 25);
        contentPane.add(btnSearch);

        JButton btnGoBack = new JButton("Go Back");
        btnGoBack.setBounds(536, 48, 100, 25);
        contentPane.add(btnGoBack);

        String[] columnNames = {"Start Date", "End Date", "Is Vacation"};
        tableModel = new DefaultTableModel(columnNames, 0);
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(20, 100, 640, 200);
        contentPane.add(scrollPane);

        allPeriods = new ArrayList<>();
        loadPeriods();

        btnSearch.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                searchPeriodAction();
            }
        });

        btnGoBack.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
                new Home().setVisible(true);
            }
        });
    }

    private void loadPeriods() {
        PeriodDAO periodDAO = new PeriodDAO(EcoleSkiConnection.getInstance());
        allPeriods = Period.findAllPeriod(periodDAO);
        updateTable(allPeriods);
    }

    private void searchPeriodAction() {
        String searchText = txtSearch.getText().trim();
        if (searchText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a start date to search.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        ArrayList<Period> filteredPeriods = new ArrayList<>();
        for (Period period : allPeriods) {
            if (period.getStartDate().toString().contains(searchText)) {
                filteredPeriods.add(period);
            }
        }

        if (filteredPeriods.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No periods found for the search criteria.", "Search Result", JOptionPane.INFORMATION_MESSAGE);
        }

        updateTable(filteredPeriods);
    }

    private void updateTable(ArrayList<Period> periods) {
        tableModel.setRowCount(0);

        for (Period period : periods) {
            Object[] rowData = {
                period.getStartDate().toString(),
                period.getEndDate().toString(),
                period.isVacation() ? "Yes" : "No"
            };
            tableModel.addRow(rowData);
        }
    }
}
