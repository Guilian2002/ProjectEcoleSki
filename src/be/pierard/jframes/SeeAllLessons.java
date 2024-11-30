package be.pierard.jframes;

import be.pierard.dao.EcoleSkiConnection;
import be.pierard.dao.LessonDAO;
import be.pierard.pojo.Lesson;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class SeeAllLessons extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTable table;
    private JTextField searchField;
    private ArrayList<Lesson> lessons;
    private DefaultTableModel tableModel;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    SeeAllLessons frame = new SeeAllLessons();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public SeeAllLessons() {
        LessonDAO lessonDAO = new LessonDAO(EcoleSkiConnection.getInstance());
        lessons = Lesson.findAllLesson(lessonDAO);

        setTitle("See All Lessons");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 800, 600);
        contentPane = new JPanel();
        contentPane.setLayout(new BorderLayout(10, 10));
        setContentPane(contentPane);

        JLabel lblTitle = new JLabel("All Lessons", JLabel.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
        contentPane.add(lblTitle, BorderLayout.NORTH);

        JPanel searchPanel = new JPanel();
        searchPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        contentPane.add(searchPanel, BorderLayout.SOUTH);

        searchField = new JTextField(20);
        searchField.setFont(new Font("Arial", Font.PLAIN, 16));
        searchPanel.add(new JLabel("Search:"));
        searchPanel.add(searchField);

        String[] columnNames = {"Min Bookings", "Max Bookings", "Schedule", "Level", "Instructor"};
        tableModel = new DefaultTableModel(columnNames, 0);
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        contentPane.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        contentPane.add(buttonPanel, BorderLayout.NORTH);

        JButton btnGoBack = new JButton("Go Back");
        btnGoBack.setFont(new Font("Arial", Font.PLAIN, 16));
        btnGoBack.addActionListener(e -> {
            dispose();
            new Home().setVisible(true);
        });
        buttonPanel.add(btnGoBack);

        JButton btnAddBooking = new JButton("Add a Booking");
        btnAddBooking.setFont(new Font("Arial", Font.PLAIN, 16));
        btnAddBooking.addActionListener(e -> openAddBooking());
        buttonPanel.add(btnAddBooking);

        updateTable(lessons);

        searchField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                filterLessons();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                filterLessons();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                filterLessons();
            }
        });

        table.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    private void updateTable(ArrayList<Lesson> lessons) {
        tableModel.setRowCount(0);
        for (Lesson lesson : lessons) {
            tableModel.addRow(new Object[]{
                lesson.getMinBookings(),
                lesson.getMaxBookings(),
                lesson.getSchedule(),
                lesson.getLessonType().getFullLevel(),
                lesson.getInstructor().getFirstname() + " " + lesson.getInstructor().getLastname()
            });
        }
    }

    private void filterLessons() {
        String query = searchField.getText().toLowerCase();
        ArrayList<Lesson> filteredLessons = new ArrayList<>();

        for (Lesson lesson : lessons) {
            if (String.valueOf(lesson.getMinBookings()).contains(query) ||
                String.valueOf(lesson.getMaxBookings()).contains(query) ||
                lesson.getSchedule().toLowerCase().contains(query) ||
                lesson.getLessonType().getFullLevel().toLowerCase().contains(query) ||
                (lesson.getInstructor().getFirstname().toLowerCase() + " " + 
                lesson.getInstructor().getLastname().toLowerCase()).contains(query)) {
                filteredLessons.add(lesson);
            }
        }

        updateTable(filteredLessons);
    }

    private void openAddBooking() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            Lesson selectedLesson = lessons.get(selectedRow);
            dispose();
            new AddBooking(selectedLesson).setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Please select a lesson first.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
