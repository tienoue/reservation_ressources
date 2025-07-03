package vues;

import vues.responsablePanels.AfficherReservations;

import javax.swing.*;
import java.awt.*;

public class ResponsableDashboardView extends JFrame {
    private JPanel contentPanel;
    public ResponsableDashboardView() {
        setTitle("Responsable Dashboard - Gestion de reservation");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 500);
        setLocationRelativeTo(null);

        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new GridLayout(3,1,10,10));
        JButton AfficherReservationBtn = new JButton("Afficher reservation");
        JButton AfficherRessourceBtn = new JButton("Afficher Ressource");
        JButton AfficherSallesLibresBtn = new JButton("Afficher Salles Libres");
        menuPanel.add(AfficherReservationBtn);
        menuPanel.add(AfficherRessourceBtn);
        menuPanel.add(AfficherSallesLibresBtn);

        contentPanel = new JPanel(new BorderLayout());
        contentPanel.add(new JLabel("Bienvenue dans le dashboard responsable", SwingConstants.CENTER), BorderLayout.CENTER);

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(menuPanel, BorderLayout.WEST);
        getContentPane().add(contentPanel, BorderLayout.CENTER);

        AfficherReservationBtn.addActionListener(e -> afficherReservationPanel());
        AfficherRessourceBtn.addActionListener(e -> afficherRessourcePanel());
        AfficherSallesLibresBtn.addActionListener(e -> afficherSallesLibresPanel());

        setVisible(true);
    }

    private void afficherSallesLibresPanel() {
        contentPanel.removeAll();
        contentPanel.add(new JLabel("Afficher les salles libres ici", SwingConstants.CENTER), BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private void afficherRessourcePanel() {
        contentPanel.removeAll();
        contentPanel.add(new JLabel("Afficher Ressources ici", SwingConstants.CENTER), BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private void afficherReservationPanel() {
        contentPanel.removeAll();
        contentPanel.add(new AfficherReservations(), BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }
}
