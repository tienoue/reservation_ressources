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
    private JButton ajouterButton, supprimerButton, modifierButton;

    public SallePanel() {
        setLayout(new BorderLayout());

        tableModel = new DefaultTableModel(new Object[]{"#", "Nom", "Capacité", "Type"}, 0);
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        ajouterButton = new JButton("Ajouter Salle");
        supprimerButton = new JButton("Supprimer Salle");
        modifierButton = new JButton("Modifier Salle");

        JPanel formPanel = new JPanel(new GridLayout(3, 3));

        formPanel.add(new JLabel("Nom:"));
        nomField = new JTextField();
        formPanel.add(nomField);
        formPanel.add(ajouterButton);

        formPanel.add(new JLabel("Capacité:"));
        capaciteField = new JTextField();
        formPanel.add(capaciteField);
        formPanel.add(modifierButton);

        formPanel.add(new JLabel("Type:"));
        typeField = new JTextField();
        formPanel.add(typeField);
        formPanel.add(supprimerButton);



        add(formPanel, BorderLayout.SOUTH);

        chargerSalles();

        ajouterButton.addActionListener(e -> ajouterSalle());
        supprimerButton.addActionListener(e -> supprimerSalle());
        modifierButton.addActionListener(e -> modifierSalle());

        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {
                    try {
                        List<Salle> salles = Salle.getAll();
                        Salle s = salles.get(selectedRow);
                        nomField.setText(s.getNom());
                        capaciteField.setText(String.valueOf(s.getCapacite()));
                        typeField.setText(s.getType());
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(null, "Erreur lors du chargement: " + ex.getMessage());
                    }
                }
            }
        });
    }

    private void chargerSalles() {
        try {
            tableModel.setRowCount(0);
            List<Salle> salles = Salle.getAll();
            int position = 1;
            for (Salle s : salles) {
                tableModel.addRow(new Object[]{position++, s.getNom(), s.getCapacite(), s.getType()});
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

        try {
            List<Salle> salles = Salle.getAll();
            Salle s = salles.get(selectedRow);
            s.delete();
            chargerSalles();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erreur lors de la suppression: " + e.getMessage());
        }
    }

    private void modifierSalle() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner une salle à modifier.");
            return;
        }

        String nom = nomField.getText();
        String capaciteText = capaciteField.getText();
        String type = typeField.getText();

        if (nom.isEmpty() || capaciteText.isEmpty() || type.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Veuillez remplir tous les champs pour la modification.");
            return;
        }

        int capacite;
        try {
            capacite = Integer.parseInt(capaciteText);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Veuillez entrer un nombre valide pour la capacité.");
            return;
        }

        try {
            List<Salle> salles = Salle.getAll();
            Salle s = salles.get(selectedRow);
            s.setNom(nom);
            s.setCapacite(capacite);
            s.setType(type);
            s.update();
            chargerSalles();
            nomField.setText("");
            capaciteField.setText("");
            typeField.setText("");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erreur lors de la modification: " + e.getMessage());
        }
    }
}
