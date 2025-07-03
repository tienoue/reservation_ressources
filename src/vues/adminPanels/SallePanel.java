package vues.adminPanels;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import models.Salle;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.util.List;

public class SallePanel extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField nomField;
    private JTextField capaciteField;
    private JTextField typeField;
    private JButton ajouterButton, supprimerButton;

    public SallePanel() {
        setLayout(new BorderLayout());

        tableModel = new DefaultTableModel(new Object[]{"ID", "Nom", "Capacité", "Type"}, 0);
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        JPanel formPanel = new JPanel(new GridLayout(4, 2));
        formPanel.add(new JLabel("Nom:"));
        nomField = new JTextField();
        formPanel.add(nomField);

        formPanel.add(new JLabel("Capacité:"));
        capaciteField = new JTextField();
        formPanel.add(capaciteField);

        formPanel.add(new JLabel("Type:"));
        typeField = new JTextField();
        formPanel.add(typeField);

        ajouterButton = new JButton("Ajouter Salle");
        supprimerButton = new JButton("Supprimer Salle");
        formPanel.add(ajouterButton);
        formPanel.add(supprimerButton);

        add(formPanel, BorderLayout.SOUTH);

        chargerSalles();

        ajouterButton.addActionListener(e -> ajouterSalle());
        supprimerButton.addActionListener(e -> supprimerSalle());
    }

    private void chargerSalles() {
        try {
            tableModel.setRowCount(0);
            List<Salle> salles = Salle.getAll();
            for (Salle s : salles) {
                tableModel.addRow(new Object[]{s.getId(), s.getNom(), s.getCapacite(), s.getType()});
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erreur de chargement: " + e.getMessage());
        }
    }

    private void ajouterSalle() {
        String nom = nomField.getText();
        String capaciteText = capaciteField.getText();
        String type = typeField.getText();

        if (nom.isEmpty() || capaciteText.isEmpty() || type.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Veuillez remplir tous les champs.");
            return;
        }

        int capacite;
        try {
            capacite = Integer.parseInt(capaciteText);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Veuillez entrer un nombre valide pour la capacité.");
            return;
        }

        Salle s = new Salle();
        s.setNom(nom);
        s.setCapacite(capacite);
        s.setType(type);
        try {
            s.add();
            chargerSalles();
            nomField.setText("");
            capaciteField.setText("");
            typeField.setText("");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erreur lors de l'ajout: " + e.getMessage());
        }
    }

    private void supprimerSalle() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner une salle à supprimer.");
            return;
        }
        int id = (int) tableModel.getValueAt(selectedRow, 0);
        Salle s = new Salle();
        s.setId(id);
        try {
            s.delete();
            chargerSalles();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erreur lors de la suppression: " + e.getMessage());
        }
    }
}
