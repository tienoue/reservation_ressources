//package controllers;
//
//import vues.*;
//import vues.AdminDashboardView;
//import vues.DemandeurDashboardView;
//import vues.ResponsableDashboardView;
//import util.*;
//
//import java.awt.event.*;
//import java.sql.*;
//import javax.swing.*;
//
//public class LoginController {
//    private LoginView loginView;
//
//    public LoginController(LoginView view) {
//        this.loginView = view;
//
//        view.loginButton.addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent e) {
//                String nom = view.nomField.getText();
//                String password = new String(view.passwordField.getPassword());
//
//                try {
//                    Connection conn = ConnexionBDD.getConnection();
//                    PreparedStatement ps = conn.prepareStatement(
//                            "SELECT * FROM utilisateur WHERE nom=? AND password=?"
//                    );
//                    ps.setString(1, nom);
//                    ps.setString(2, password);
//
//                    ResultSet rs = ps.executeQuery();
//
//                    if (rs.next()) {
//                        String role = rs.getString("role");
//                        int idUtilisateur = rs.getInt("id");
//                        view.dispose();
//
//                        switch (role) {
//                            case "admin":
//                                new AdminDashboardView();
//                                break;
//                            case "demandeur":
//                                new DemandeurDashboardView();
//                                break;
//                            case "responsable":
//                                new ResponsableDashboardView();
//                                break;
//                            default:
//                                JOptionPane.showMessageDialog(view, "Rôle non reconnu.");
//                        }
//                    } else {
//                        JOptionPane.showMessageDialog(view, "Nom ou mot de passe incorrect.");
//                    }
//                } catch (SQLException ex) {
//                    ex.printStackTrace();
//                }
//            }
//        });
//    }
//
//    // Action pour le bouton "Changer de mot de passe"
//        vues.changePasswordButton.addActionListener(new ActionListener() {
//        public void actionPerformed(ActionEvent e) {
//            JTextField nomField = new JTextField();
//            JPasswordField ancienPwdField = new JPasswordField();
//            JPasswordField nouveauPwdField = new JPasswordField();
//
//            Object[] fields = {
//                    "Nom d'utilisateur:", nomField,
//                    "Ancien mot de passe:", ancienPwdField,
//                    "Nouveau mot de passe:", nouveauPwdField
//            };
//
//            int result = JOptionPane.showConfirmDialog(view, fields, "Changer le mot de passe", JOptionPane.OK_CANCEL_OPTION);
//
//            if (result == JOptionPane.OK_OPTION) {
//                String nom = nomField.getText();
//                String ancienPwd = new String(ancienPwdField.getPassword());
//                String nouveauPwd = new String(nouveauPwdField.getPassword());
//
//                changerMotDePasse(nom, ancienPwd, nouveauPwd);
//            }
//        }
//    });
//}
//
//private void changerMotDePasse(String nom, String ancienMotDePasse, String nouveauMotDePasse) {
//    try {
//        Connection conn = DBConnection.getConnection();
//        PreparedStatement ps = conn.prepareStatement("SELECT * FROM utilisateur WHERE nom=? AND password=?");
//        ps.setString(1, nom);
//        ps.setString(2, ancienMotDePasse);
//        ResultSet rs = ps.executeQuery();
//
//        if (rs.next()) {
//            PreparedStatement updatePs = conn.prepareStatement("UPDATE utilisateur SET password=? WHERE nom=?");
//            updatePs.setString(1, nouveauMotDePasse);
//            updatePs.setString(2, nom);
//            updatePs.executeUpdate();
//            JOptionPane.showMessageDialog(null, "Mot de passe mis à jour avec succès.");
//        } else {
//            JOptionPane.showMessageDialog(null, "Ancien mot de passe incorrect.");
//        }
//    } catch (SQLException e) {
//        e.printStackTrace();
//    }
//}
//}
//
//        }

package controllers;

import util.ConnexionBDD;
import vues.LoginView;
import vues.AdminDashboardView;
import vues.DemandeurDashboardView;
import vues.ResponsableDashboardView;

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
                    PreparedStatement ps = conn.prepareStatement(
                            "SELECT * FROM utilisateur WHERE nom=? AND password=?"
                    );
                    ps.setString(1, nom);
                    ps.setString(2, password);
                    ResultSet rs = ps.executeQuery();

                    if (rs.next()) {
                        String role = rs.getString("role");
                        int idUtilisateur = rs.getInt("id");
                        view.dispose();

                        switch (role) {
                            case "admin":
                                new AdminDashboardView(idUtilisateur);
                                break;
                            case "demandeur":
                                new DemandeurDashboardView(idUtilisateur);
                                break;
                            case "responsable":
                                new ResponsableDashboardView(idUtilisateur);
                                break;
                            default:
                                JOptionPane.showMessageDialog(view, "Rôle non reconnu.");
                        }
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
