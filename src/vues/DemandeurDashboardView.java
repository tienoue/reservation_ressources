package vues;

import javax.swing.*;
import java.awt.*;

public class DemandeurDashboardView extends JFrame {
    private int utilisateurId;

    public DemandeurDashboardView(int utilisateurId) {
        this.utilisateurId = utilisateurId;
        setTitle("Tableau de bord - Demandeur");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout());
        JLabel welcomeLabel = new JLabel("Bienvenue Demandeur #" + utilisateurId, SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 16));

        JButton btnModifierInfos = new JButton("Modifier mes infos");
        btnModifierInfos.addActionListener(e -> ModifierInfoDialog.afficher(utilisateurId));

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(btnModifierInfos);

        mainPanel.add(welcomeLabel, BorderLayout.NORTH);
        mainPanel.add(buttonPanel, BorderLayout.CENTER);

        add(mainPanel);
        setVisible(true);
    }
}
