package controllers;

import vues.LoginView;
import vues.AdminDashboardView;
import util.ConnexionBDD;
import vues.ResponsableDashboardView;

import java.awt.event.*;
import java.sql.*;
import javax.swing.*;

public class LoginController {
    private LoginView loginView;

    public LoginController(LoginView view) {
        this.loginView = view;
        // Ajout du listener pour le bouton de connexion
        view.loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                authenticateUser();
            }
        });
    }

    private void authenticateUser() {
        String nom = loginView.nomField.getText();
        String password = new String(loginView.passwordField.getPassword());

        // Vérification des champs vides
        if (nom.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(loginView, "Veuillez remplir tous les champs.");
            return;
        }

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            // Établir la connexion à la base de données
            conn = ConnexionBDD.getConnection();
            // Requête SQL pour vérifier l'utilisateur sans restriction sur le rôle
            String query = "SELECT role FROM utilisateur WHERE nom = ? AND password = ?";
            ps = conn.prepareStatement(query);
            ps.setString(1, nom);
            ps.setString(2, password);
            rs = ps.executeQuery();

            if (rs.next()) {
                String role = rs.getString("role");
                loginView.dispose(); // Fermer la fenêtre de connexion

                // Redirection en fonction du rôle
                switch (role.toLowerCase()) {
                    case "admin":
                        new AdminDashboardView();
                        break;
                    case "responsable":
                        new ResponsableDashboardView();
                        break;
                    default:
                        JOptionPane.showMessageDialog(null, "Rôle non reconnu : " + role);
                        break;
                }
            } else {
                JOptionPane.showMessageDialog(loginView, "Nom ou mot de passe incorrect.");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(loginView, "Erreur lors de la connexion à la base de données.");
        } finally {
            // Fermer les ressources JDBC
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (conn != null) conn.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }
}