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
    private JTextField passwordField;
    private JComboBox<String> roleBox;
    private JButton ajouterButton, supprimerButton;

    public UtilisateurPanel() {
        setLayout(new BorderLayout());

        tableModel = new DefaultTableModel(new Object[]{"ID", "Nom", "Role"}, 0);
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        JPanel formPanel = new JPanel(new GridLayout(4, 2));
        formPanel.add(new JLabel("Nom:"));
        nomField = new JTextField();
        formPanel.add(nomField);

        formPanel.add(new JLabel("Mot de passe:"));
        passwordField = new JTextField();
        formPanel.add(passwordField);

        formPanel.add(new JLabel("Role:"));
        roleBox = new JComboBox<>(new String[]{"demandeur", "responsable"});
        formPanel.add(roleBox);

        ajouterButton = new JButton("Ajouter Demandeur");
        supprimerButton = new JButton("Supprimer");
        formPanel.add(ajouterButton);
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
            for (Utilisateur u : utilisateurs) {
                tableModel.addRow(new Object[]{u.getId(), u.getNom(), u.getRole()});
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erreur de chargement: " + e.getMessage());
        }
    }

    private void ajouterUtilisateur() {
        String nom = nomField.getText();
        String password = passwordField.getText();
        String role = roleBox.getSelectedItem().toString();

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
            passwordField.setText("");
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
        int id = (int) tableModel.getValueAt(selectedRow, 0);
        String role = (String) tableModel.getValueAt(selectedRow, 2);
        if (!role.equals("demandeur")) {
            JOptionPane.showMessageDialog(this, "Vous ne pouvez supprimer que les demandeurs.");
            return;
        }
        Utilisateur u = new Utilisateur();
        u.setId(id);
        try {
            u.delete();
            chargerUtilisateurs();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erreur lors de la suppression: " + e.getMessage());
        }
    }
}
