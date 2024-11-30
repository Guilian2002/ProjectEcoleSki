package be.pierard.jframes;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

import be.pierard.pojo.Booking;
import be.pierard.dao.BookingDAO;
import be.pierard.dao.EcoleSkiConnection;

public class SeeAllBookings extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTable table;
    private JTextField searchField;
    private DefaultTableModel tableModel;
    private ArrayList<Booking> allBookings;
    private TableRowSorter<DefaultTableModel> rowSorter;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    SeeAllBookings frame = new SeeAllBookings();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public SeeAllBookings() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 930, 600);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        BookingDAO bookingDAO = new BookingDAO(EcoleSkiConnection.getInstance());
        allBookings = Booking.findAllBookings(bookingDAO);

        String[] columnNames = { 
        	"Date", "Duration in Days", "Price", "Group Size", 
            "Special", "Instructor", "Lesson", "Skier", 
            "Start Date", "End Date"
        };

        tableModel = new DefaultTableModel(columnNames, 0);
        table = new JTable(tableModel);
        rowSorter = new TableRowSorter<>(tableModel);
        table.setRowSorter(rowSorter);

        for (Booking booking : allBookings) {
            Object[] rowData = {
                booking.getDate(),
                booking.getDuration(),
                booking.getPrice(),
                booking.getGroupSize(),
                booking.isSpecial(),
                booking.getInstructor().getLastname() + " " + booking.getInstructor().getFirstname(),
                booking.getLesson().getLessonType().getFullLevel(),
                booking.getSkier().getFirstname() + " " + booking.getSkier().getLastname(),
                booking.getPeriod().getStartDate(),
                booking.getPeriod().getEndDate()
            };
            tableModel.addRow(rowData);
        }

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(10, 70, 876, 400);
        contentPane.add(scrollPane);

        searchField = new JTextField();
        searchField.setBounds(10, 20, 200, 30);
        contentPane.add(searchField);
        searchField.setColumns(10);

        searchField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent ke) {
                String query = searchField.getText();
                rowSorter.setRowFilter(RowFilter.regexFilter("(?i)" + query));
            }
        });

        JButton goBackButton = new JButton("Go back");
        goBackButton.setBounds(10, 480, 120, 30);
        contentPane.add(goBackButton);

        goBackButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
                Home.main(new String[0]);
            }
        });
    }
}
