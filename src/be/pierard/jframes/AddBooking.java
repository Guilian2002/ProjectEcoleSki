package be.pierard.jframes;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.stream.Collectors;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import be.pierard.dao.BookingDAO;
import be.pierard.dao.EcoleSkiConnection;
import be.pierard.dao.SkierDAO;
import be.pierard.dao.PeriodDAO;
import be.pierard.pojo.Skier;
import be.pierard.pojo.Booking;
import be.pierard.pojo.Lesson;
import be.pierard.pojo.Period;

public class AddBooking extends JFrame {

	private static final long serialVersionUID = 7980908540667445594L;
	private JPanel contentPane;
    private JLabel lblSkierName;
    private JLabel lblPeriodName;
    private JLabel lblMessage;
    private JTable tableSkiers;
    private JTable tablePeriods;
    private DefaultTableModel skierTableModel;
    private DefaultTableModel periodTableModel;
    private String selectedSkierName = "";
    private String selectedPeriodName = "";
    private SkierDAO skierDAO;
    private PeriodDAO periodDAO;
    private ArrayList<Skier> skierList;
    private ArrayList<Period> periodList;
    private JTextField txtSearchSkier;
    private JTextField txtSearchPeriod;
    private JTextField txtGroupSize;
    private Lesson lesson;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					AddBooking frame = new AddBooking(null);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public AddBooking(Lesson lesson) {
		if (lesson == null) {
            dispose();
            new SeeAllLessons().setVisible(true);
            return;
        }
		this.lesson = lesson;
		skierDAO = new SkierDAO(EcoleSkiConnection.getInstance());
        periodDAO = new PeriodDAO(EcoleSkiConnection.getInstance());

        setTitle("Add a Booking");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 900, 500);
        contentPane = new JPanel();
        contentPane.setLayout(new GridLayout(1, 2, 10, 0));
        setContentPane(contentPane);

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridLayout(9, 2, 5, 5));
        contentPane.add(formPanel);

        formPanel.add(new JLabel("Group size:"));
        txtGroupSize = new JTextField();
        formPanel.add(txtGroupSize);

        formPanel.add(new JLabel("Skier:"));
        lblSkierName = new JLabel("None");
        formPanel.add(lblSkierName);

        formPanel.add(new JLabel("Period:"));
        lblPeriodName = new JLabel("None");
        formPanel.add(lblPeriodName);

        formPanel.add(new JLabel("Search Skier:"));
        txtSearchSkier = new JTextField(10);
        formPanel.add(txtSearchSkier);

        formPanel.add(new JLabel("Search Period:"));
        txtSearchPeriod = new JTextField(10);
        formPanel.add(txtSearchPeriod);

        JButton btnChoosePeriod = new JButton("Choose Period");
        btnChoosePeriod.addActionListener(new ChoosePeriodActionListener());
        formPanel.add(btnChoosePeriod);
        
        JButton btnChooseSkier = new JButton("Choose Skier");
        btnChooseSkier.addActionListener(new ChooseSkierActionListener());
        formPanel.add(btnChooseSkier);

        JButton btnAddBooking = new JButton("Add Booking");
        btnAddBooking.addActionListener(new AddBookingActionListener());
        formPanel.add(btnAddBooking);

        lblMessage = new JLabel("");
        lblMessage.setFont(new Font("Tahoma", Font.PLAIN, 14));
        formPanel.add(lblMessage);

        JButton btnGoBack = new JButton("Go Back");
        btnGoBack.addActionListener(e -> {
            dispose();
            SeeAllLessons seeAllLessonsWindow = new SeeAllLessons();
            seeAllLessonsWindow.setVisible(true);
        });
        formPanel.add(btnGoBack);

        JPanel tablePanel = new JPanel(new GridLayout(1, 2, 10, 0));
        contentPane.add(tablePanel);

        skierTableModel = new DefaultTableModel(new Object[] { "Skier Name" }, 0);
        tableSkiers = new JTable(skierTableModel);
        JScrollPane SkierScrollPane = new JScrollPane(tableSkiers);
        tablePanel.add(SkierScrollPane);

        periodTableModel = new DefaultTableModel(new Object[] { "Period" }, 0);
        tablePeriods = new JTable(periodTableModel);
        JScrollPane PeriodScrollPane = new JScrollPane(tablePeriods);
        tablePanel.add(PeriodScrollPane);

        loadSkiers();
        loadPeriods();

        txtSearchSkier.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String searchText = txtSearchSkier.getText().toLowerCase();
                filterSkiers(searchText);
            }
        });

        txtSearchPeriod.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String searchText = txtSearchPeriod.getText().toLowerCase();
                filterPeriods(searchText);
            }
        });
    }

    private void loadSkiers() {
    	ArrayList<Skier> newSkierList = Skier.findAllSkier(skierDAO);
    	skierList = (ArrayList<Skier>) newSkierList.stream()
    		    .filter(skier -> skier.getLevel().equals(lesson.getLessonType().getFullLevel()))
    		    .collect(Collectors.toList());
        for (Skier skier : skierList) {
            skierTableModel.addRow(new Object[] { skier.getLastname() + " " + skier.getFirstname() + " "+ skier.getLevel() });
        }
    }

    private void loadPeriods() {
    	periodList = Period.findAllPeriod(periodDAO);
        for (Period period : periodList) {
        	periodTableModel.addRow(new Object[] { period.getStartDate().toString() + " " + period.getEndDate().toString() });
        }
    }

    private void filterSkiers(String query) {
        skierTableModel.setRowCount(0);
        for (Skier skier : skierList) {
            if (skier.getLastname().toLowerCase().contains(query) || skier.getFirstname().toLowerCase().contains(query)) {
                skierTableModel.addRow(new Object[] { skier.getLastname() + " " + skier.getFirstname() + " "+ skier.getLevel() });
            }
        }
    }

    private void filterPeriods(String query) {
        periodTableModel.setRowCount(0);
        for (Period period : periodList) {
            if (period.getStartDate().toString().toLowerCase().contains(query) || period.getEndDate().toString().toLowerCase().contains(query)) {
            	periodTableModel.addRow(new Object[] { period.getStartDate().toString() + " " + period.getEndDate().toString() });
            }
        }
    }

    private class ChooseSkierActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int selectedRow = tableSkiers.getSelectedRow();
            if (selectedRow >= 0) {
                String skierName = (String) skierTableModel.getValueAt(selectedRow, 0);
                selectedSkierName = skierName;
                lblSkierName.setText(selectedSkierName);
            } else {
                JOptionPane.showMessageDialog(null, "Please select a Skier from the table.");
            }
        }
    }

    private class ChoosePeriodActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int selectedRow = tablePeriods.getSelectedRow();
            if (selectedRow >= 0) {
            	String periodName = (String) periodTableModel.getValueAt(selectedRow, 0);
                selectedPeriodName = periodName;
                lblPeriodName.setText(selectedPeriodName);
            } else {
                JOptionPane.showMessageDialog(null, "Please select a period from the table.");
            }
        }
    }

    private class AddBookingActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
        	String groupSizeStr = txtGroupSize.getText().trim();

            if (selectedSkierName.isEmpty() || selectedPeriodName.isEmpty() || groupSizeStr.isEmpty()) {
                lblMessage.setText("Error: Please choose a Skier, a period and add a GroupSize.");
                return;
            }

            Skier selectedSkier = findSkierByName(selectedSkierName);
            if (selectedSkier == null) {
                lblMessage.setText("Error: Skier not found.");
                return;
            }

            Period selectedPeriod = findPeriodByName(selectedPeriodName);
            if (selectedPeriod == null) {
                lblMessage.setText("Error: Invalid period selected.");
                return;
            }
            
            int groupSize = Integer.parseInt(groupSizeStr);

            Booking newBooking = new Booking(0, null, 0, 0.0, groupSize, false, lesson.getInstructor(), lesson, selectedPeriod, selectedSkier);
            boolean success = newBooking.makeBooking(new BookingDAO(EcoleSkiConnection.getInstance()));

            if (success) {
                lblMessage.setText("Booking added successfully!");
            } else {
                lblMessage.setText("Error: Could not add booking.");
            }
        }
    }

    private Skier findSkierByName(String name) {
        for (Skier skier : skierList) {
            if ((skier.getLastname() + " " + skier.getFirstname() + " "+ skier.getLevel()).equals(name)) {
                return skier;
            }
        }
        return null;
    }

    private Period findPeriodByName(String name) {
    	for (Period period : periodList) {
            if ((period.getStartDate().toString() + " " + period.getEndDate().toString()).equals(name)) {
                return period;
            }
        }
        return null;
    }
}
