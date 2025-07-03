package vues.adminPanels;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import models.Utilisateur;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.util.List;

public class UtilisateurPanel extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField nomField;
    private JButton ajouterButton, supprimerButton;

    public UtilisateurPanel() {
        setLayout(new BorderLayout());

        tableModel = new DefaultTableModel(new Object[]{"#", "Nom"}, 0);
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        ajouterButton = new JButton("Ajouter Demandeur");
        supprimerButton = new JButton("Supprimer");

        JPanel formPanel = new JPanel(new GridLayout(2, 3));
        formPanel.add(new JLabel("Nom:"));
        nomField = new JTextField();
        formPanel.add(nomField);
        formPanel.add(ajouterButton);

        formPanel.add(new JLabel("Mot de passe:"));
        formPanel.add(new JLabel("00000000"));
        formPanel.add(supprimerButton);

        add(formPanel, BorderLayout.SOUTH);

        chargerUtilisateurs();

        ajouterButton.addActionListener(e -> ajouterUtilisateur());
        supprimerButton.addActionListener(e -> supprimerUtilisateur());
    }

    private void chargerUtilisateurs() {
        try {
            tableModel.setRowCount(0);
            List<Utilisateur> utilisateurs = Utilisateur.getAll();
            int position = 1;
            for (Utilisateur u : utilisateurs) {
                tableModel.addRow(new Object[]{position++, u.getNom()});
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erreur de chargement: " + e.getMessage());
        }
    }

    private void ajouterUtilisateur() {
        String nom = nomField.getText();
        String password = "00000000";
        String role = "demandeur";

        if (nom.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Veuillez remplir tous les champs.");
            return;
        }

        Utilisateur u = new Utilisateur();
        u.setNom(nom);
        u.setPassword(password);
        u.setRole(role);
        try {
            u.add();
            chargerUtilisateurs();
            nomField.setText("");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erreur lors de l'ajout: " + e.getMessage());
        }
    }

    private void supprimerUtilisateur() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un utilisateur à supprimer.");
            return;
        }


        try {
            List<Utilisateur> utilisateurs = Utilisateur.getAll();
            Utilisateur u = utilisateurs.get(selectedRow);
            u.delete();
            chargerUtilisateurs();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erreur lors de la suppression: " + e.getMessage());
        }
    }
}
