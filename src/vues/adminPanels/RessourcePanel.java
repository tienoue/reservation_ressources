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
    private JButton ajouterButton, supprimerButton, modifierButton;

    public RessourcePanel() {
        setLayout(new BorderLayout());

        tableModel = new DefaultTableModel(new Object[]{"#", "Nom", "Quantité"}, 0);
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        ajouterButton = new JButton("Ajouter Ressource");
        supprimerButton = new JButton("Supprimer Ressource");
        modifierButton = new JButton("Modifier Ressource");

        JPanel formPanel = new JPanel(new GridLayout(3, 3));
        formPanel.add(new JLabel("Nom:"));
        nomField = new JTextField();
        formPanel.add(nomField);
        formPanel.add(ajouterButton);

        formPanel.add(new JLabel("Quantité:"));
        quantiteTotaleField = new JTextField();
        formPanel.add(quantiteTotaleField);
        formPanel.add(modifierButton);


        formPanel.add(new JLabel());
        formPanel.add(new JLabel());
        formPanel.add(supprimerButton);



        add(formPanel, BorderLayout.SOUTH);

        chargerRessources();

        ajouterButton.addActionListener(e -> ajouterRessource());
        supprimerButton.addActionListener(e -> supprimerRessource());
        modifierButton.addActionListener(e -> modifierRessource());

        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {
                    try {
                        List<Ressource> ressources = Ressource.getAll();
                        Ressource r = ressources.get(selectedRow);
                        nomField.setText(r.getNom());
                        quantiteTotaleField.setText(String.valueOf(r.getQuantiteTotale()));
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(null, "Erreur lors du chargement: " + ex.getMessage());
                    }
                }
            }
        });
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

        int quantiteTotale;
        try {
            quantiteTotale = Integer.parseInt(quantiteTotaleText);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Veuillez entrer un nombre valide pour la quantité.");
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

        // ✅ Ajout de la boîte de confirmation
        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Êtes-vous sûr de vouloir supprimer cette ressource ?",
                "Confirmation de suppression",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm != JOptionPane.YES_OPTION) {
            return; // si l'utilisateur choisit 'Non', on arrête la suppression
        }

        try {
            List<Ressource> ressources = Ressource.getAll();
            Ressource r = ressources.get(selectedRow);
            r.delete();
            chargerRessources();

            nomField.setText("");
            quantiteTotaleField.setText("");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erreur lors de la suppression: " + e.getMessage());
        }
    }

    private void modifierRessource() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner une ressource à modifier.");
            return;
        }

        String nom = nomField.getText();
        String quantiteTotaleText = quantiteTotaleField.getText();

        if (nom.isEmpty() || quantiteTotaleText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Veuillez remplir tous les champs pour la modification.");
            return;
        }

        int quantiteTotale;
        try {
            quantiteTotale = Integer.parseInt(quantiteTotaleText);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Veuillez entrer un nombre valide pour la quantité.");
            return;
        }

        try {
            List<Ressource> ressources = Ressource.getAll();
            Ressource r = ressources.get(selectedRow);
            r.setNom(nom);
            r.setQuantiteTotale(quantiteTotale);
            r.setQuantiteDisponible(quantiteTotale);
            r.update();
            chargerRessources();
            nomField.setText("");
            quantiteTotaleField.setText("");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erreur lors de la modification: " + e.getMessage());
        }
    }
}
