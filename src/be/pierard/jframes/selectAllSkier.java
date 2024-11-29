package be.pierard.jframes;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.stream.Collectors;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import be.pierard.pojo.Skier;
import be.pierard.dao.EcoleSkiConnection;
import be.pierard.dao.SkierDAO;

public class SelectAllSkier extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTable table;
    private JTextField searchField;
    private SkierDAO skierDAO;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    SelectAllSkier frame = new SelectAllSkier(new SkierDAO(EcoleSkiConnection.getInstance()));
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public SelectAllSkier(SkierDAO skierDAO) {
        this.skierDAO = skierDAO;
        setTitle("All Skiers");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 800, 400);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(10, 50, 760, 250);
        contentPane.add(scrollPane);

        table = new JTable();
        table.setModel(new DefaultTableModel(
            new Object[][] {},
            new String[] { "Lastname", "Firstname", "Age", "Level", "Insurance" }
        ));
        scrollPane.setViewportView(table);

        populateTable(Skier.findAllSkier(skierDAO));

        searchField = new JTextField();
        searchField.setBounds(10, 10, 200, 30);
        contentPane.add(searchField);
        searchField.setColumns(10);

        JButton searchButton = new JButton("Search");
        searchButton.setBounds(220, 10, 100, 30);
        contentPane.add(searchButton);
        searchButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String query = searchField.getText().trim().toLowerCase();
                if (!query.isEmpty()) {
                    searchSkier(query);
                } else {
                    populateTable(Skier.findAllSkier(skierDAO));
                }
            }
        });

        JButton goBackButton = new JButton("Go Back");
        goBackButton.setBounds(670, 10, 100, 30);
        contentPane.add(goBackButton);
        goBackButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
                new Home().setVisible(true);
            }
        });

        JButton updateSkierButton = new JButton("Update Skier");
        updateSkierButton.setBounds(10, 320, 150, 30);
        contentPane.add(updateSkierButton);
        updateSkierButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow == -1) {
                    JOptionPane.showMessageDialog(null, "Please select a skier to update.");
                } else {
                    Skier selectedSkier = getSelectedSkier(selectedRow);
                    if (selectedSkier != null) {
                        dispose();
                        new UpdateSkier(selectedSkier, skierDAO).setVisible(true);
                    }
                }
            }
        });
    }

    private void populateTable(ArrayList<Skier> skiers) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0);
        for (Skier skier : skiers) {
            model.addRow(new Object[] {
                skier.getLastname(),
                skier.getFirstname(),
                skier.getAge(),
                skier.getLevel(),
                skier.isInsurance() ? "Yes" : "No"
            });
        }
    }

    private void searchSkier(String query) {
        ArrayList<Skier> skiers = Skier.findAllSkier(skierDAO);
        ArrayList<Skier> filteredSkiers = skiers.stream()
                .filter(skier -> skier.getLastname().toLowerCase().contains(query)
                        || skier.getFirstname().toLowerCase().contains(query)
                        || String.valueOf(skier.getAge()).contains(query)
                        || skier.getLevel().toLowerCase().contains(query)
                        || (skier.isInsurance() ? "yes" : "no").contains(query))
                .collect(Collectors.toCollection(ArrayList::new));
        populateTable(filteredSkiers);
    }

    private Skier getSelectedSkier(int row) {
        String lastname = (String) table.getValueAt(row, 0);
        String firstname = (String) table.getValueAt(row, 1);

        ArrayList<Skier> skiers = Skier.findAllSkier(skierDAO);
        return skiers.stream()
                .filter(skier -> skier.getLastname().equals(lastname) && skier.getFirstname().equals(firstname))
                .findFirst()
                .orElse(null);
    }
}
