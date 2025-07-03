package vues;

import util.ConnexionBDD;

import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ModifierInfoDialog {

    public static void afficher(int utilisateurId) {
        try {
            Connection conn = ConnexionBDD.getConnection();
            PreparedStatement ps = conn.prepareStatement("SELECT nom, password FROM utilisateur WHERE id = ?");
            ps.setInt(1, utilisateurId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String nomActuel = rs.getString("nom");
                String motDePasseActuel = rs.getString("password");

                JTextField nomField = new JTextField(nomActuel);
                JPasswordField passwordField = new JPasswordField(motDePasseActuel);

                Object[] fields = {
                        "Nom :", nomField,
                        "Mot de passe :", passwordField
                };

                int result = JOptionPane.showConfirmDialog(null, fields, "Modifier mes informations", JOptionPane.OK_CANCEL_OPTION);

                if (result == JOptionPane.OK_OPTION) {
                    String nouveauNom = nomField.getText();
                    String nouveauMotDePasse = new String(passwordField.getPassword());

                    PreparedStatement updatePs = conn.prepareStatement("UPDATE utilisateur SET nom = ?, password = ? WHERE id = ?");
                    updatePs.setString(1, nouveauNom);
                    updatePs.setString(2, nouveauMotDePasse);
                    updatePs.setInt(3, utilisateurId);
                    updatePs.executeUpdate();

                    JOptionPane.showMessageDialog(null, "Informations mises à jour avec succès.");
                }
            } else {
                JOptionPane.showMessageDialog(null, "Utilisateur non trouvé.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erreur lors de la mise à jour des informations.");
        }
    }
}

