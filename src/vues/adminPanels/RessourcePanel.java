package vues.adminPanels;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import models.Ressource;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.util.List;

public class RessourcePanel extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField nomField;
    private JTextField quantiteTotaleField;
    private JButton ajouterButton, supprimerButton;

    public RessourcePanel() {
        setLayout(new BorderLayout());

        tableModel = new DefaultTableModel(new Object[]{"#", "Nom", "Quantité"}, 0);
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        JPanel formPanel = new JPanel(new GridLayout(4, 2));
        formPanel.add(new JLabel("Nom:"));
        nomField = new JTextField();
        formPanel.add(nomField);

        formPanel.add(new JLabel("Quantité:"));
        quantiteTotaleField = new JTextField();
        formPanel.add(quantiteTotaleField);

        ajouterButton = new JButton("Ajouter Ressource");
        supprimerButton = new JButton("Supprimer Ressource");
        formPanel.add(ajouterButton);
        formPanel.add(supprimerButton);

        add(formPanel, BorderLayout.SOUTH);

        chargerRessources();

        ajouterButton.addActionListener(e -> ajouterRessource());
        supprimerButton.addActionListener(e -> supprimerRessource());
    }

    private void chargerRessources() {
        try {
            tableModel.setRowCount(0);
            List<Ressource> ressources = Ressource.getAll();
            int position = 1;
            for (Ressource r : ressources) {
                tableModel.addRow(new Object[]{position++, r.getNom(), r.getQuantiteTotale()});
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erreur de chargement: " + e.getMessage());
        }
    }

    private void ajouterRessource() {
        String nom = nomField.getText();
        String quantiteTotaleText = quantiteTotaleField.getText();

        if (nom.isEmpty() || quantiteTotaleText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Veuillez remplir tous les champs.");
            return;
        }

        int quantiteTotale, quantiteDisponible;
        try {
            quantiteTotale = Integer.parseInt(quantiteTotaleText);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Veuillez entrer des nombres valides pour les quantités.");
            return;
        }

        Ressource r = new Ressource();
        r.setNom(nom);
        r.setQuantiteTotale(quantiteTotale);
        r.setQuantiteDisponible(quantiteTotale);
        try {
            r.add();
            chargerRessources();
            nomField.setText("");
            quantiteTotaleField.setText("");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erreur lors de l'ajout: " + e.getMessage());
        }
    }

    private void supprimerRessource() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner une ressource à supprimer.");
            return;
        }

        try {
            List<Ressource> ressources = Ressource.getAll();
            Ressource r = ressources.get(selectedRow);
            r.delete();
            chargerRessources();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erreur lors de la suppression: " + e.getMessage());
        }
    }
}
