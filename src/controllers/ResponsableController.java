package controllers;

import util.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;

public class ResponsableController {

       public static void afficherRessourcesDisponibles() {
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
            JOptionPane.showMessageDialog(null, scrollPane, "Ressources Disponibles", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
