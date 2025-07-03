package controllers;

import vues.LoginView;
import vues.AdminDashboardView;
import util.ConnexionBDD;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;
public class LoginController {
    private LoginView loginView;
    public LoginController(LoginView view) {
        this.loginView = view;
        view.loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String nom = view.nomField.getText();
                String password = new String(view.passwordField.getPassword());
                try {
                    Connection conn = ConnexionBDD.getConnection();
                    PreparedStatement ps = conn.prepareStatement("SELECT * FROM utilisateur WHERE nom=? AND password=? AND role='responsable'");
                    ps.setString(1, nom);
                    ps.setString(2, password);
                    ResultSet rs = ps.executeQuery();
                    if (rs.next()) {
                        view.dispose();
                        new AdminDashboardView();
                    } else {
                        JOptionPane.showMessageDialog(view, "Nom ou mot de passe incorrect.");
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });
    }
}