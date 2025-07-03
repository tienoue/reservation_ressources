package vues;

import controllers.ResponsableController;


import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ResponsableDashboardView extends JFrame {
    private int utilisateurId;

    public ResponsableDashboardView(int utilisateurId) {
        this.utilisateurId = utilisateurId;
        setTitle("Tableau de bord - Responsable");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Créer les composants
        JPanel mainPanel = new JPanel(new BorderLayout());
        JLabel welcomeLabel = new JLabel("Bienvenue Responsable #" + utilisateurId, SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 16));

        JButton btnVoirRessources = new JButton("Voir les ressources disponibles");
        btnVoirRessources.addActionListener(e -> ResponsableController.afficherRessourcesDisponibles());



        JButton btnModifierInfos = new JButton("Modifier mes infos");
        btnModifierInfos.addActionListener(e -> ModifierInfoDialog.afficher(utilisateurId));

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(btnVoirRessources);

        buttonPanel.add(btnModifierInfos);

        mainPanel.add(welcomeLabel, BorderLayout.NORTH);
        mainPanel.add(buttonPanel, BorderLayout.CENTER);

        add(mainPanel);
        setVisible(true);
    }

}
