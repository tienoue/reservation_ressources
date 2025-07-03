package vues;

import vues.adminPanels.RessourcePanel;
import vues.adminPanels.SallePanel;
import vues.adminPanels.UtilisateurPanel;

import javax.swing.*;
import java.awt.*;
public class AdminDashboardView extends JFrame {
    private JPanel contentPanel;
    public AdminDashboardView() {
        setTitle("Admin Dashboard - Gestion de réservation");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 500);
        setLocationRelativeTo(null);

        JPanel menuPanel = new JPanel();
        menuPanel.setBackground(new Color(20, 20, 255));
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS)); // pour aligner verticalement ses enfants
        menuPanel.setPreferredSize(new Dimension(200, getHeight())); // force la largeur à gauche

        JButton utilisateurBtn = new JButton("Utilisateurs");
        JButton salleBtn = new JButton("Salles");
        JButton ressourceBtn = new JButton("Ressources");

        Dimension btnSize = new Dimension(180, 30);
        utilisateurBtn.setMaximumSize(btnSize);
        salleBtn.setMaximumSize(btnSize);
        ressourceBtn.setMaximumSize(btnSize);

        utilisateurBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        salleBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        ressourceBtn.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel labelAdmin = new JLabel("Administrateur");
        labelAdmin.setForeground(new Color(255, 255, 255));
        labelAdmin.setFont(labelAdmin.getFont().deriveFont(20f)); //augmenter la police
        labelAdmin.setAlignmentX(Component.CENTER_ALIGNMENT); // centrage dans le BoxLayout

        menuPanel.add(Box.createVerticalStrut(20)); // espace entre boutons
        menuPanel.add(labelAdmin);
        menuPanel.add(Box.createVerticalStrut(40)); // espace entre boutons
        menuPanel.add(utilisateurBtn);
        menuPanel.add(Box.createVerticalStrut(10)); // espace entre boutons
        menuPanel.add(salleBtn);
        menuPanel.add(Box.createVerticalStrut(10)); // espace entre boutons
        menuPanel.add(ressourceBtn);


        contentPanel = new JPanel(new BorderLayout());
        contentPanel.add(new JLabel("Bienvenue dans le dashboard administrateur", SwingConstants.CENTER), BorderLayout.CENTER);

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(menuPanel, BorderLayout.WEST);
        getContentPane().add(contentPanel, BorderLayout.CENTER);

        utilisateurBtn.addActionListener(e -> afficherUtilisateurPanel());
        salleBtn.addActionListener(e -> afficherSallePanel());
        ressourceBtn.addActionListener(e -> afficherRessourcePanel());

        setVisible(true);
    }

    private void afficherUtilisateurPanel() {
        contentPanel.removeAll();
        contentPanel.add(new UtilisateurPanel());
        contentPanel.revalidate();
        contentPanel.repaint();
    }
    private void afficherSallePanel() {
        contentPanel.removeAll();
        contentPanel.add(new SallePanel());
        contentPanel.revalidate();
        contentPanel.repaint();
    }
    private void afficherRessourcePanel() {
        contentPanel.removeAll();
        contentPanel.add(new RessourcePanel());
        contentPanel.revalidate();
        contentPanel.repaint();
    }
}