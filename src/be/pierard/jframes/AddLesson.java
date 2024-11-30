package be.pierard.jframes;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.table.*;

import be.pierard.dao.EcoleSkiConnection;
import be.pierard.dao.InstructorDAO;
import be.pierard.dao.LessonDAO;
import be.pierard.pojo.*;

public class AddLesson extends JFrame {

    private static final long serialVersionUID = -541654061225968568L;
    private JPanel contentPane;
    private JLabel lblInstructorName;
    private JLabel lblLessonType;
    private JLabel lblMessage;
    private JTable tableInstructors;
    private JTable tableLessonTypes;
    private DefaultTableModel instructorTableModel;
    private DefaultTableModel lessonTypeTableModel;
    private String selectedInstructorName = "";
    private String selectedLessonType = "";
    private String selectedSchedule = "";
    private InstructorDAO instructorDAO;
    private LessonDAO lessonDAO;
    private ArrayList<Instructor> instructorList;
    private Instructor selectedInstructor;
    private JComboBox<String> comboBoxSchedule;
    private JTextField txtSearchInstructor;
    private JTextField txtSearchLessonType;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    AddLesson frame = new AddLesson();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public AddLesson() {
        instructorDAO = new InstructorDAO(EcoleSkiConnection.getInstance());
        lessonDAO = new LessonDAO(EcoleSkiConnection.getInstance());

        setTitle("Add a Lesson");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 900, 500);
        contentPane = new JPanel();
        contentPane.setLayout(new GridLayout(1, 2, 10, 0));
        setContentPane(contentPane);

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridLayout(9, 2, 5, 5));
        contentPane.add(formPanel);

        formPanel.add(new JLabel("Schedule:"));
        comboBoxSchedule = new JComboBox<>(new String[] {"noon 1/2", "noon 2/2", "morning", "afternoon", "morning+afternoon"});
        formPanel.add(comboBoxSchedule);

        formPanel.add(new JLabel("Instructor:"));
        lblInstructorName = new JLabel("None");
        formPanel.add(lblInstructorName);

        formPanel.add(new JLabel("Lesson Type:"));
        lblLessonType = new JLabel("None");
        formPanel.add(lblLessonType);

        formPanel.add(new JLabel("Search Instructor:"));
        txtSearchInstructor = new JTextField(10);
        formPanel.add(txtSearchInstructor);

        formPanel.add(new JLabel("Search Lesson Type:"));
        txtSearchLessonType = new JTextField(10);
        formPanel.add(txtSearchLessonType);

        JButton btnChooseLessonType = new JButton("Choose Lesson Type");
        btnChooseLessonType.addActionListener(new ChooseLessonTypeActionListener());
        formPanel.add(btnChooseLessonType);
        
        JButton btnChooseInstructor = new JButton("Choose Instructor");
        btnChooseInstructor.addActionListener(new ChooseInstructorActionListener());
        formPanel.add(btnChooseInstructor);

        JButton btnAddLesson = new JButton("Add Lesson");
        btnAddLesson.addActionListener(new AddLessonActionListener());
        formPanel.add(btnAddLesson);

        lblMessage = new JLabel("");
        lblMessage.setFont(new Font("Tahoma", Font.PLAIN, 14));
        formPanel.add(lblMessage);

        JButton btnGoBack = new JButton("Go Back");
        btnGoBack.addActionListener(e -> {
            dispose();
            Home homeWindow = new Home();
            homeWindow.setVisible(true);
        });
        formPanel.add(btnGoBack);

        JPanel tablePanel = new JPanel(new GridLayout(1, 2, 10, 0));
        contentPane.add(tablePanel);

        instructorTableModel = new DefaultTableModel(new Object[] { "Instructor Name" }, 0);
        tableInstructors = new JTable(instructorTableModel);
        JScrollPane instructorScrollPane = new JScrollPane(tableInstructors);
        tablePanel.add(instructorScrollPane);

        lessonTypeTableModel = new DefaultTableModel(new Object[] { "Level" }, 0);
        tableLessonTypes = new JTable(lessonTypeTableModel);
        JScrollPane lessonTypeScrollPane = new JScrollPane(tableLessonTypes);
        tablePanel.add(lessonTypeScrollPane);

        loadInstructors();

        txtSearchInstructor.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String searchText = txtSearchInstructor.getText().toLowerCase();
                filterInstructors(searchText);
            }
        });

        txtSearchLessonType.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String searchText = txtSearchLessonType.getText().toLowerCase();
                filterLessonTypes(searchText);
            }
        });
    }

    private void loadInstructors() {
        instructorList = Instructor.findAllInstructor(instructorDAO);
        for (Instructor instructor : instructorList) {
            instructorTableModel.addRow(new Object[] { instructor.getLastname() + " " + instructor.getFirstname() });
        }
    }

    private void loadLessonTypes() {
        lessonTypeTableModel.setRowCount(0);
        ArrayList<Accreditation> accreditations = selectedInstructor.getAccreditationList();
        for (Accreditation accreditation : accreditations) {
            for (LessonType lessonType : accreditation.getLessonTypeList()) {
                lessonTypeTableModel.addRow(new Object[] { lessonType.getFullLevel() });
            }
        }
    }

    private void filterInstructors(String query) {
        instructorTableModel.setRowCount(0);
        for (Instructor instructor : instructorList) {
            if (instructor.getLastname().toLowerCase().contains(query) || instructor.getFirstname().toLowerCase().contains(query)) {
                instructorTableModel.addRow(new Object[] { instructor.getLastname() + " " + instructor.getFirstname() });
            }
        }
    }

    private void filterLessonTypes(String query) {
        lessonTypeTableModel.setRowCount(0);
        for (Accreditation accreditation : selectedInstructor.getAccreditationList()) {
            for (LessonType lessonType : accreditation.getLessonTypeList()) {
                if (lessonType.getFullLevel().toLowerCase().contains(query)) {
                    lessonTypeTableModel.addRow(new Object[] { lessonType.getFullLevel() });
                }
            }
        }
    }

    private class ChooseInstructorActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int selectedRow = tableInstructors.getSelectedRow();
            if (selectedRow >= 0) {
                lblLessonType.setText("None");
                String instructorName = (String) instructorTableModel.getValueAt(selectedRow, 0);
                selectedInstructorName = instructorName;
                lblInstructorName.setText(selectedInstructorName);
                selectedInstructor = findInstructorByName(instructorName);
                loadLessonTypes();
            } else {
                JOptionPane.showMessageDialog(null, "Please select an instructor from the table.");
            }
        }
    }

    private class ChooseLessonTypeActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int selectedRow = tableLessonTypes.getSelectedRow();
            if (selectedRow >= 0) {
                selectedLessonType = (String) lessonTypeTableModel.getValueAt(selectedRow, 0);
                lblLessonType.setText(selectedLessonType);
            } else {
                JOptionPane.showMessageDialog(null, "Please select a lesson type from the table.");
            }
        }
    }

    private class AddLessonActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            selectedSchedule = (String) comboBoxSchedule.getSelectedItem();

            if (selectedInstructorName.isEmpty() || selectedLessonType.isEmpty() || selectedSchedule.isEmpty()) {
                lblMessage.setText("Error: Please choose an instructor, a lesson type, and a schedule.");
                return;
            }

            Instructor selectedInstructor = findInstructorByName(selectedInstructorName);
            if (selectedInstructor == null) {
                lblMessage.setText("Error: Instructor not found.");
                return;
            }

            LessonType lessonType = findLessonTypeByFullName(selectedLessonType, selectedInstructor);
            if (lessonType == null) {
                lblMessage.setText("Error: Invalid lesson type selected.");
                return;
            }

            Lesson newLesson = new Lesson(0, 0, 0, selectedSchedule, lessonType, selectedInstructor);
            boolean success = newLesson.makeLesson(lessonDAO, false);

            if (success) {
                lblMessage.setText("Lesson added successfully!");
            } else {
                lblMessage.setText("Error: Could not add lesson.");
            }
        }
    }

    private Instructor findInstructorByName(String name) {
        for (Instructor instructor : instructorList) {
            if ((instructor.getLastname() + " " + instructor.getFirstname()).equals(name)) {
                return instructor;
            }
        }
        return null;
    }

    private LessonType findLessonTypeByFullName(String fullName, Instructor selectedInstructor) {
        for (Accreditation accreditation : selectedInstructor.getAccreditationList()) {
            for (LessonType lessonType : accreditation.getLessonTypeList()) {
                if (lessonType.getFullLevel().equals(fullName)) {
                    return lessonType;
                }
            }
        }
        return null;
    }
}
