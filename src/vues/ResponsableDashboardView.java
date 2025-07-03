package vues;
//
//import javax.swing.*;
//import java.awt.*;
//
////public class ResponsableDashboardView extends JFrame {
////    private JPanel contentPanel;
////
////    public ResponsableDashboardView() {
////        setTitle("Responsable Dashboard - Gestion de réservation");
////        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
////        setSize(800, 500);
////        setLocationRelativeTo(null);
////
////        JPanel menuPanel = new JPanel();
////        menuPanel.setLayout(new GridLayout(2, 1, 10, 10)); // Ajusté à 2 lignes
////
////        JButton historiqueBtn = new JButton("Historique des réservations");
////        JButton calendrierBtn = new JButton("Calendrier des réservations");
////
////        menuPanel.add(historiqueBtn); // Bouton Historique
////        menuPanel.add(calendrierBtn); // Bouton Calendrier
////
////        contentPanel = new JPanel(new BorderLayout());
////        contentPanel.add(new JLabel("Bienvenue dans le Responsable", SwingConstants.CENTER), BorderLayout.CENTER);
////
////        getContentPane().setLayout(new BorderLayout());
////        getContentPane().add(menuPanel, BorderLayout.WEST);
////        getContentPane().add(contentPanel, BorderLayout.CENTER);
////
////        historiqueBtn.addActionListener(e -> afficherHistoriquePanel());
////        calendrierBtn.addActionListener(e -> afficherCalendrierPanel());
////
////        setVisible(true);
////    }
////
////    private void afficherHistoriquePanel() {
////        contentPanel.removeAll();
////        contentPanel.add(new JLabel("Historique des réservations passées", SwingConstants.CENTER), BorderLayout.CENTER);
////        // Vous pouvez ajouter un tableau ou une liste ici
////        contentPanel.revalidate();
////        contentPanel.repaint();
////    }
////
////    private void afficherCalendrierPanel() {
////        contentPanel.removeAll();
////        contentPanel.add(new JLabel("Calendrier des réservations", SwingConstants.CENTER), BorderLayout.CENTER);
////        // Vous pouvez ajouter un composant de calendrier ici
////        contentPanel.revalidate();
////        contentPanel.repaint();
////    }
////
////    public static void main(String[] args) {
////        SwingUtilities.invokeLater(() -> new ResponsableDashboardView());
////    }
////}
//
//
//import vues.responsablePanels.CalendrierPanel;
//import vues.responsablePanels.HistoriquePanel;
//
//public class ResponsableDashboardView extends JFrame {
//    private JPanel contentPanel;
//    private CalendrierPanel calendrierPanel;
//    private HistoriquePanel historiquePanel;
//
//    public ResponsableDashboardView() {
//        setTitle("Responsable Dashboard - Gestion de réservation");
//        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        setSize(800, 500);
//        setLocationRelativeTo(null);
//
//        // Panneau de menu avec un layout vertical
//        JPanel menuPanel = new JPanel();
//        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
//        menuPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
//
//        // Titre "Responsable"
//        JLabel titleLabel = new JLabel("Responsable");
//        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
//        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
//        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
//
//        // Boutons pour naviguer entre les panneaux (plus petits)
//        JButton historiqueBtn = new JButton("Historique");
//        JButton calendrierBtn = new JButton("Calendrier");
//        JButton AfficherRessources = new JButton("Afficher Ressources");
//
//        // Configuration des boutons
//        historiqueBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
//        calendrierBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
//        historiqueBtn.setMaximumSize(new Dimension(120, 30));
//        calendrierBtn.setMaximumSize(new Dimension(120, 30));
//        historiqueBtn.setPreferredSize(new Dimension(120, 30));
//        calendrierBtn.setPreferredSize(new Dimension(120, 30));
//
//        // Ajout des composants au menu
//        menuPanel.add(titleLabel);
//        menuPanel.add(Box.createVerticalStrut(10));
//        menuPanel.add(historiqueBtn);
//        menuPanel.add(Box.createVerticalStrut(10));
//        menuPanel.add(calendrierBtn);
//
//        // Panneau principal pour le contenu
//        contentPanel = new JPanel(new BorderLayout());
//        contentPanel.add(new JLabel("Bienvenue dans le dashboard responsable", SwingConstants.CENTER), BorderLayout.CENTER);
//
//        // Configuration de la fenêtre principale
//        getContentPane().setLayout(new BorderLayout());
//        getContentPane().add(menuPanel, BorderLayout.WEST);
//        getContentPane().add(contentPanel, BorderLayout.CENTER);
//
//        // Initialisation des panneaux
//        historiquePanel = new HistoriquePanel(contentPanel);
//        calendrierPanel = new CalendrierPanel(contentPanel);
//
//        // Action pour afficher l'historique
//        historiqueBtn.addActionListener(e -> historiquePanel.afficherHistoriquePanel());
//        // Action pour afficher le calendrier
//        calendrierBtn.addActionListener(e -> calendrierPanel.afficherCalendrierPanel());
//
//        // Affichage de la fenêtre
//        setVisible(true);
//    }
//
//    public static void main(String[] args) {
//        SwingUtilities.invokeLater(() -> new ResponsableDashboardView());
//    }
//}

import javax.swing.*;
import java.awt.*;

import vues.responsablePanels.AfficherRessourcePanel; // Importer le panel des ressources
import vues.responsablePanels.CalendrierPanel;
import vues.responsablePanels.HistoriquePanel;

public class ResponsableDashboardView extends JFrame {
    private JPanel contentPanel;
    private CalendrierPanel calendrierPanel;
    private HistoriquePanel historiquePanel;
    private int utilisateurId;

    public ResponsableDashboardView(int utilisateurId) {
        this.utilisateurId = utilisateurId;
        setTitle("Responsable Dashboard - Gestion de réservation");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 500);
        setLocationRelativeTo(null);

        // Panneau de menu avec un layout vertical
        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        menuPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Titre "Responsable"
        JLabel titleLabel = new JLabel("Responsable");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        // Boutons pour naviguer entre les panneaux
        JButton historiqueBtn = new JButton("Historique");
        JButton calendrierBtn = new JButton("Calendrier");
        JButton ressourcesBtn = new JButton("Afficher Ressources");

        JButton btnModifierInfos = new JButton("Modifier mes infos");
        btnModifierInfos.addActionListener(e -> ModifierInfoDialog.afficher(utilisateurId));

        // Configuration des boutons
        historiqueBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        calendrierBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        ressourcesBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        historiqueBtn.setMaximumSize(new Dimension(120, 30));
        calendrierBtn.setMaximumSize(new Dimension(120, 30));
        ressourcesBtn.setMaximumSize(new Dimension(120, 30));

        // Ajout des composants au menu
        menuPanel.add(titleLabel);
        menuPanel.add(Box.createVerticalStrut(10));
        menuPanel.add(ressourcesBtn);
        menuPanel.add(Box.createVerticalStrut(10));
        menuPanel.add(calendrierBtn);
        menuPanel.add(Box.createVerticalStrut(10));
        menuPanel.add(historiqueBtn);
        menuPanel.add(btnModifierInfos);



        // Panneau principal pour le contenu
        contentPanel = new JPanel(new BorderLayout());
        contentPanel.add(new JLabel("Bienvenue dans le dashboard responsable", SwingConstants.CENTER), BorderLayout.CENTER);

        // Configuration de la fenêtre principale
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(menuPanel, BorderLayout.WEST);
        getContentPane().add(contentPanel, BorderLayout.CENTER);

        // Initialisation des panneaux
        historiquePanel = new HistoriquePanel(contentPanel);
        calendrierPanel = new CalendrierPanel(contentPanel);

        // Actions pour afficher les différents panneaux
        historiqueBtn.addActionListener(e -> afficherHistoriquePanel());
        calendrierBtn.addActionListener(e -> afficherCalendrierPanel());
        ressourcesBtn.addActionListener(e -> afficherRessourcesDisponibles()); // Action pour afficher les ressources

        // Affichage de la fenêtre
        setVisible(true);
    }

    private void afficherHistoriquePanel() {
        contentPanel.removeAll();
        historiquePanel.afficherHistoriquePanel();
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private void afficherCalendrierPanel() {
        contentPanel.removeAll();
        calendrierPanel.afficherCalendrierPanel();
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private void afficherRessourcesDisponibles() {
        contentPanel.removeAll();
        contentPanel.add(AfficherRessourcePanel.afficherRessourcesDisponibles(), BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

//    public static void main(String[] args) {
//        SwingUtilities.invokeLater(() -> new ResponsableDashboardView());
//    }
}