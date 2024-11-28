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

public class Home extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;

    /**
     * Launch the application.
     */
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

    /**
     * Create the frame.
     */
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

        JPanel panelSkier = new JPanel();
        panelSkier.setBorder(BorderFactory.createTitledBorder("Skier"));
        panelSkier.setLayout(new GridBagLayout());
        contentPane.add(panelSkier, BorderLayout.CENTER);

        JButton btnAddSkier = new JButton("Add a Skier");
        btnAddSkier.setFont(new Font("Arial", Font.PLAIN, 18));
        btnAddSkier.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	dispose();
                new AddSkier().setVisible(true);
            }
        });
        panelSkier.add(btnAddSkier);
    }
}
