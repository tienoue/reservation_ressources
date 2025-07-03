package vues;

import javax.swing.*;
import java.awt.*;

import controllers.LoginController;
import vues.responsablePanels.AfficherReservations;
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
        menuPanel.setBackground(new Color(20, 20, 255));
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS)); // pour aligner verticalement ses enfants
        menuPanel.setPreferredSize(new Dimension(200, getHeight())); // force la largeur à gauche
        //menuPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Titre "Responsable"
        JLabel titleLabel = new JLabel("Responsable");
        titleLabel.setForeground(new Color(255, 255, 255));
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        // Boutons pour naviguer entre les panneaux
        JButton historiqueBtn = new JButton("Historique");
        JButton calendrierBtn = new JButton("Calendrier");
        JButton ressourcesBtn = new JButton("Afficher Ressources");
        JButton btnDeconnexion = new JButton("Deconnexion");
        JButton AfficherReservationBtn = new JButton("Afficher reservation");


        JButton btnModifierInfos = new JButton("Modifier mes infos");
        btnModifierInfos.addActionListener(e -> ModifierInfoDialog.afficher(utilisateurId));


        // Configuration des boutons
        AfficherReservationBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        historiqueBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        calendrierBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        ressourcesBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnModifierInfos.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnDeconnexion.setAlignmentX(Component.CENTER_ALIGNMENT);


        AfficherReservationBtn.setMaximumSize(new Dimension(180, 30));
        historiqueBtn.setMaximumSize(new Dimension(180, 30));
        calendrierBtn.setMaximumSize(new Dimension(180, 30));
        ressourcesBtn.setMaximumSize(new Dimension(180, 30));
        btnModifierInfos.setMaximumSize(new Dimension(180, 30));
        btnDeconnexion.setMaximumSize(new Dimension(180, 30));

        // Ajout des composants au menu
        menuPanel.add(Box.createVerticalStrut(20));
        menuPanel.add(titleLabel);
        menuPanel.add(Box.createVerticalStrut(10));
        menuPanel.add(AfficherReservationBtn);
        menuPanel.add(Box.createVerticalStrut(10));
        menuPanel.add(ressourcesBtn);
        menuPanel.add(Box.createVerticalStrut(10));
        menuPanel.add(calendrierBtn);
        menuPanel.add(Box.createVerticalStrut(10));
        menuPanel.add(historiqueBtn);
        menuPanel.add(Box.createVerticalStrut(10));
        menuPanel.add(btnModifierInfos);
        menuPanel.add(Box.createVerticalStrut(10)); // espace entre boutons
        menuPanel.add(btnDeconnexion);



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
        AfficherReservationBtn.addActionListener(e -> afficherReservationPanel());
        historiqueBtn.addActionListener(e -> afficherHistoriquePanel());
        calendrierBtn.addActionListener(e -> afficherCalendrierPanel());
        ressourcesBtn.addActionListener(e -> afficherRessourcesDisponibles()); // Action pour afficher les ressources
        btnDeconnexion.addActionListener(e -> deconnecter());

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
    private void afficherReservationPanel() {
        contentPanel.removeAll();
        contentPanel.add(new AfficherReservations(), BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }
    private void deconnecter() {
        // ✅ Ajout de la boîte de confirmation
        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Êtes-vous sûr de vouloir vous déconnecter ?",
                "Confirmation de déconnexion",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm != JOptionPane.YES_OPTION) {
            return; // si l'utilisateur choisit 'Non', on arrête la suppression
        }


        this.dispose();

        LoginView view = new LoginView();
        view.setVisible(true);
        new LoginController(view);
    }
}