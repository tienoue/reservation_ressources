package vues.responsablePanels;

import util.ConnexionBDD;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AfficherRessourcePanel {

    public static JPanel afficherRessourcesDisponibles() {
        JPanel panel = new JPanel(new BorderLayout());
        try {
            Connection conn = ConnexionBDD.getConnection();
            String sql = "SELECT * FROM ressource WHERE quantiteDisponible > 0";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            String[] columnNames = {"ID", "Nom", "Quantité Totale", "Quantité Disponible"};
            DefaultTableModel model = new DefaultTableModel(columnNames, 0);

            while (rs.next()) {
                int id = rs.getInt("id");
                String nom = rs.getString("nom");
                int qt = rs.getInt("quantiteTotale");
                int qd = rs.getInt("quantiteDisponible");
                model.addRow(new Object[]{id, nom, qt, qd});
            }

            JTable table = new JTable(model);
            JScrollPane scrollPane = new JScrollPane(table);
            panel.add(scrollPane, BorderLayout.CENTER);
            panel.add(new JLabel("Ressources Disponibles", SwingConstants.CENTER), BorderLayout.NORTH);
        } catch (SQLException e) {
            e.printStackTrace();
            panel.add(new JLabel("Erreur lors de la récupération des ressources.", SwingConstants.CENTER), BorderLayout.CENTER);
        }

        return panel; // Retourner le panneau pour l'afficher dans le dashboard
    }
}