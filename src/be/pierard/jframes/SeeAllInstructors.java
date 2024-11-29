package be.pierard.jframes;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import be.pierard.dao.EcoleSkiConnection;
import be.pierard.dao.InstructorDAO;
import be.pierard.pojo.Instructor;

public class SeeAllInstructors extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField txtSearch;
    private JTable table;
    private DefaultTableModel tableModel;
    private ArrayList<Instructor> instructorsList;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    SeeAllInstructors frame = new SeeAllInstructors();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public SeeAllInstructors() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 800, 600);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        setContentPane(contentPane);
        contentPane.setLayout(new BorderLayout(10, 10));

        tableModel = new DefaultTableModel(new Object[] {"Lastname", "Firstname", "Age", "Hourly Rate"}, 0);
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        contentPane.add(scrollPane, BorderLayout.CENTER);

        JPanel searchPanel = new JPanel();
        contentPane.add(searchPanel, BorderLayout.NORTH);

        JLabel lblSearch = new JLabel("Search:");
        searchPanel.add(lblSearch);

        txtSearch = new JTextField(20);
        searchPanel.add(txtSearch);

        JButton btnSearch = new JButton("Search");
        searchPanel.add(btnSearch);

        JButton btnGoBack = new JButton("Go Back");
        contentPane.add(btnGoBack, BorderLayout.SOUTH);

        JButton btnUpdateInstructor = new JButton("Update Instructor");
        contentPane.add(btnUpdateInstructor, BorderLayout.EAST);

        InstructorDAO instructorDAO = new InstructorDAO(EcoleSkiConnection.getInstance());
        instructorsList = Instructor.findAllInstructor(instructorDAO);
        populateTable(instructorsList);

        btnSearch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String searchQuery = txtSearch.getText().toLowerCase();
                ArrayList<Instructor> filteredInstructors = new ArrayList<>();

                if (searchQuery.isEmpty()) {
                    filteredInstructors = instructorsList;
                } else {
                    for (Instructor instructor : instructorsList) {
                        if (instructor.getLastname().toLowerCase().contains(searchQuery) ||
                            instructor.getFirstname().toLowerCase().contains(searchQuery) ||
                            String.valueOf(instructor.getAge()).contains(searchQuery) ||
                            String.valueOf(instructor.getHourlyRate()).contains(searchQuery)) {
                            filteredInstructors.add(instructor);
                        }
                    }
                }
                populateTable(filteredInstructors);
            }
        });

        btnGoBack.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new Home().setVisible(true);
            }
        });

        btnUpdateInstructor.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {
                	dispose();
                    Instructor selectedInstructor = instructorsList.get(selectedRow);
                    new UpdateInstructor(selectedInstructor).setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(null, "Please select an instructor to update.", "No Instructor Selected",
                    		JOptionPane.WARNING_MESSAGE);
                }
            }
        });
    }

    private void populateTable(ArrayList<Instructor> instructors) {
        tableModel.setRowCount(0);
        for (Instructor instructor : instructors) {
            tableModel.addRow(new Object[]{
                instructor.getLastname(),
                instructor.getFirstname(),
                instructor.getAge(),
                instructor.getHourlyRate()
            });
        }
    }
}
