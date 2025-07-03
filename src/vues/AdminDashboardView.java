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
        menuPanel.setLayout(new GridLayout(3, 1, 10, 10));
        JButton utilisateurBtn = new JButton("Utilisateur");
        JButton salleBtn = new JButton("Salle");
        JButton ressourceBtn = new JButton("Ressources");
        menuPanel.add(utilisateurBtn);
        menuPanel.add(salleBtn);
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
        //contentPanel.add(new RessourcePanel());
        contentPanel.revalidate();
        contentPanel.repaint();
    }
}