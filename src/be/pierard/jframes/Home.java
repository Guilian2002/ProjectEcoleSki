package be.pierard.jframes;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import be.pierard.dao.EcoleSkiConnection;
import be.pierard.dao.SkierDAO;

public class Home extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    Home frame = new Home();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public Home() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 800, 600);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        contentPane.setLayout(new BorderLayout(10, 10));
        setContentPane(contentPane);

        JLabel lblTitle = new JLabel("Welcome to the Ski Management System", JLabel.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
        contentPane.add(lblTitle, BorderLayout.NORTH);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.gridy = 0;
        contentPane.add(mainPanel, BorderLayout.CENTER);

        JPanel panelSkier = new JPanel();
        panelSkier.setBorder(BorderFactory.createTitledBorder("Skier"));
        panelSkier.setLayout(new GridBagLayout());
        mainPanel.add(panelSkier, gbc);

        GridBagConstraints skierGbc = new GridBagConstraints();
        skierGbc.insets = new Insets(10, 10, 10, 10);
        skierGbc.gridx = 0;
        skierGbc.gridy = 0;

        JButton btnAddSkier = new JButton("Add a Skier");
        btnAddSkier.setFont(new Font("Arial", Font.PLAIN, 18));
        btnAddSkier.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
                new AddSkier().setVisible(true);
            }
        });
        panelSkier.add(btnAddSkier, skierGbc);

        skierGbc.gridy = 1;
        JButton btnShowAllSkiers = new JButton("Show All Skiers");
        btnShowAllSkiers.setFont(new Font("Arial", Font.PLAIN, 18));
        btnShowAllSkiers.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
                new SeeAllSkiers(new SkierDAO(EcoleSkiConnection.getInstance())).setVisible(true);
            }
        });
        panelSkier.add(btnShowAllSkiers, skierGbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        JPanel panelPeriod = new JPanel();
        panelPeriod.setBorder(BorderFactory.createTitledBorder("Period"));
        panelPeriod.setLayout(new GridBagLayout());
        mainPanel.add(panelPeriod, gbc);

        GridBagConstraints periodGbc = new GridBagConstraints();
        periodGbc.insets = new Insets(10, 10, 10, 10);
        periodGbc.gridx = 0;
        periodGbc.gridy = 0;

        JButton btnAddPeriod = new JButton("Add a Period");
        btnAddPeriod.setFont(new Font("Arial", Font.PLAIN, 18));
        btnAddPeriod.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
                new AddPeriod().setVisible(true);
            }
        });
        panelPeriod.add(btnAddPeriod, periodGbc);

        periodGbc.gridy = 1;
        JButton btnShowAllPeriods = new JButton("Show All Periods");
        btnShowAllPeriods.setFont(new Font("Arial", Font.PLAIN, 18));
        btnShowAllPeriods.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
                new SeeAllPeriods().setVisible(true);
            }
        });
        panelPeriod.add(btnShowAllPeriods, periodGbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        JPanel panelInstructor = new JPanel();
        panelInstructor.setBorder(BorderFactory.createTitledBorder("Instructor"));
        panelInstructor.setLayout(new GridBagLayout());
        mainPanel.add(panelInstructor, gbc);

        GridBagConstraints instructorGbc = new GridBagConstraints();
        instructorGbc.insets = new Insets(10, 10, 10, 10);
        instructorGbc.gridx = 0;
        instructorGbc.gridy = 0;
        
        JButton btnAddInstructor = new JButton("Add an Instructor");
        btnAddInstructor.setFont(new Font("Arial", Font.PLAIN, 18));
        btnAddInstructor.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
                new AddInstructor().setVisible(true);
            }
        });
        panelInstructor.add(btnAddInstructor, instructorGbc);

        instructorGbc.gridy = 1;
        JButton btnShowAllInstructors = new JButton("Show All Instructors");
        btnShowAllInstructors.setFont(new Font("Arial", Font.PLAIN, 18));
        btnShowAllInstructors.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
                new SeeAllInstructors().setVisible(true);
            }
        });
        panelInstructor.add(btnShowAllInstructors, instructorGbc);
    }
}
