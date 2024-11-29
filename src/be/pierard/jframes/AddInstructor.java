package be.pierard.jframes;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import be.pierard.dao.AccreditationDAO;
import be.pierard.dao.EcoleSkiConnection;
import be.pierard.dao.InstructorDAO;
import be.pierard.pojo.Accreditation;
import be.pierard.pojo.LessonType;
import be.pierard.pojo.Instructor;

public class AddInstructor extends JFrame {

    private static final long serialVersionUID = 4460565302616646140L;
    private JPanel contentPane;
    private JTextField txtLastname;
    private JTextField txtFirstname;
    private JTextField txtAge;
    private JTextField txtAddress;
    private JTextField txtEmail;
    private JTextField txtHourlyRate;

    private JTable availableTable;
    private JTable selectedTable;

    private DefaultTableModel availableTableModel;
    private DefaultTableModel selectedTableModel;

    private ArrayList<LessonType> availableLessonTypes;
    private ArrayList<LessonType> selectedLessonTypes;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                AddInstructor frame = new AddInstructor();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public AddInstructor() {
        setTitle("Add Instructor");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 1000, 600);

        contentPane = new JPanel(new BorderLayout());
        setContentPane(contentPane);

        JPanel formPanel = new JPanel(new GridLayout(8, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        contentPane.add(formPanel, BorderLayout.WEST);

        txtLastname = addTextField("Lastname", formPanel);
        txtFirstname = addTextField("Firstname", formPanel);
        txtAge = addTextField("Age", formPanel);
        txtAddress = addTextField("Address", formPanel);
        txtEmail = addTextField("Email", formPanel);
        txtHourlyRate = addTextField("Hourly Rate", formPanel);

        JButton btnAddInstructor = new JButton("Add Instructor");
        formPanel.add(btnAddInstructor);

        JButton btnGoBack = new JButton("Go Back");
        btnGoBack.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		dispose();
                Home homeWindow = new Home();
                homeWindow.setVisible(true);
        	}
        });
        formPanel.add(btnGoBack);

        JPanel tablePanel = new JPanel(new GridLayout(1, 2, 10, 10));
        tablePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        contentPane.add(tablePanel, BorderLayout.CENTER);

        availableTableModel = new DefaultTableModel(new Object[]{"Accreditation", "Lesson Level"}, 0);
        availableTable = new JTable(availableTableModel);
        tablePanel.add(new JScrollPane(availableTable));

        selectedTableModel = new DefaultTableModel(new Object[]{"Accreditation", "Lesson Level"}, 0);
        selectedTable = new JTable(selectedTableModel);
        tablePanel.add(new JScrollPane(selectedTable));

        JPanel buttonPanel = new JPanel(new GridLayout(2, 1, 10, 10));
        contentPane.add(buttonPanel, BorderLayout.EAST);

        JButton btnChooseLevel = new JButton("Choose a Level");
        JButton btnRemoveLevel = new JButton("Remove Level");

        buttonPanel.add(btnChooseLevel);
        buttonPanel.add(btnRemoveLevel);

        availableLessonTypes = new ArrayList<>();
        selectedLessonTypes = new ArrayList<>();

        loadAvailableLessonTypes();
        populateAvailableTable();
        
        btnChooseLevel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	int selectedRow = availableTable.getSelectedRow();
                if (selectedRow >= 0) {
                    LessonType selectedLessonType = availableLessonTypes.get(selectedRow);
                    ArrayList<LessonType> lessonTypeList = new ArrayList<LessonType>();
                    
                    LessonType copiedLessonType = new LessonType();
                    copiedLessonType.setId(selectedLessonType.getId());
                    copiedLessonType.setLevel(selectedLessonType.getLevel());
                    copiedLessonType.setPrice(selectedLessonType.getPrice());
                    
                    Accreditation copiedAccreditation = new Accreditation();
                    copiedAccreditation.setId(selectedLessonType.getAccreditation().getId());
                    copiedAccreditation.setName(selectedLessonType.getAccreditation().getName());
                    copiedLessonType.setAccreditation(copiedAccreditation);
                    lessonTypeList.add(copiedLessonType);
                    copiedAccreditation.setLessonTypeList(lessonTypeList);
                    
                    if (!selectedLessonTypes.contains(copiedLessonType)) {
                        selectedLessonTypes.add(copiedLessonType);
                        availableLessonTypes.remove(selectedRow);
                        availableTableModel.removeRow(selectedRow);
                        selectedTableModel.addRow(new Object[]{
                            copiedLessonType.getAccreditation().getName(),
                            copiedLessonType.getLevel()
                        });
                    }
                }
            }
        });

        btnRemoveLevel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = selectedTable.getSelectedRow();
                if (selectedRow >= 0) {
                    LessonType removedLessonType = selectedLessonTypes.remove(selectedRow);
                    availableLessonTypes.add(removedLessonType);
                    selectedTableModel.removeRow(selectedRow);
                    availableTableModel.addRow(new Object[]{removedLessonType.getAccreditation().getName(), removedLessonType.getLevel()});
                }
            }
        });

        btnAddInstructor.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (validateInput()) {
                    String lastname = txtLastname.getText();
                    String firstname = txtFirstname.getText();
                    int age = Integer.parseInt(txtAge.getText());
                    String address = txtAddress.getText();
                    String email = txtEmail.getText();
                    double hourlyRate = Double.parseDouble(txtHourlyRate.getText());

                    ArrayList<Accreditation> uniqueAccreditations = new ArrayList<>();
                    for (LessonType lessonType : selectedLessonTypes) {
                        Accreditation accreditation = lessonType.getAccreditation();
                        if (!uniqueAccreditations.contains(accreditation)) {
                            uniqueAccreditations.add(accreditation);
                        }
                    }

                    Instructor instructor = new Instructor(0, lastname, firstname, age, address, email, hourlyRate, uniqueAccreditations);

                    boolean success = instructor.makeInstructor(new InstructorDAO(EcoleSkiConnection.getInstance()), false);
                    if (success) {
                        JOptionPane.showMessageDialog(null, "Instructor added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                        clearForm();
                    }
                }
            }
        });
    }

    private JTextField addTextField(String label, JPanel panel) {
        panel.add(new JLabel(label));
        JTextField textField = new JTextField();
        panel.add(textField);
        return textField;
    }

    private boolean validateInput() {
        if (txtLastname.getText().isEmpty() || txtFirstname.getText().isEmpty() || txtAge.getText().isEmpty() ||
                txtAddress.getText().isEmpty() || txtEmail.getText().isEmpty() || txtHourlyRate.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        try {
            Integer.parseInt(txtAge.getText());
            Double.parseDouble(txtHourlyRate.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Age and Hourly Rate must be numeric.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    private void loadAvailableLessonTypes() {
        for (Accreditation accreditation : Accreditation.findAllAccreditation(new AccreditationDAO(EcoleSkiConnection.getInstance()))) {
            availableLessonTypes.addAll(accreditation.getLessonTypeList());
        }
    }

    private void populateAvailableTable() {
        for (LessonType lessonType : availableLessonTypes) {
            availableTableModel.addRow(new Object[]{lessonType.getAccreditation().getName(), lessonType.getLevel()});
        }
    }

    private void clearForm() {
        txtLastname.setText("");
        txtFirstname.setText("");
        txtAge.setText("");
        txtAddress.setText("");
        txtEmail.setText("");
        txtHourlyRate.setText("");
        selectedTableModel.setRowCount(0);
        selectedLessonTypes.clear();
    }
}
