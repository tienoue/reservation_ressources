//package vues.responsablePanels;
//
//import util.ConnexionBDD;
//
//import javax.swing.*;
//import javax.swing.table.DefaultTableModel;
//import java.awt.*;
//import java.sql.Connection;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.sql.Statement;
//
//public class HistoriquePanel {
//    private JPanel contentPanel;
//
//    public HistoriquePanel(JPanel contentPanel) {
//        this.contentPanel = contentPanel;
//    }
//
//    public void afficherHistoriquePanel() {
//        contentPanel.removeAll();
//
//        JPanel panel = new JPanel(new BorderLayout());
//        String[] columnNames = {"ID", "Utilisateur ID", "Salle ID", "Date", "Heure Début", "Heure Fin", "État"};
//        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
//
//        try (Connection conn = ConnexionBDD.getConnection();
//             Statement stmt = conn.createStatement();
//             ResultSet rs = stmt.executeQuery("SELECT * FROM reservation WHERE etat = 'validee'")) {
//
//            while (rs.next()) {
//                Object[] row = {
//                        rs.getInt("id"),
//                        rs.getInt("id_utilisateur"),
//                        rs.getInt("id_salle"),
//                        rs.getDate("date"),
//                        rs.getTime("heure_debut"),
//                        rs.getTime("heure_fin"),
//                        rs.getString("etat")
//                };
//                model.addRow(row);
//            }
//
//            JTable table = new JTable(model);
//            JScrollPane scrollPane = new JScrollPane(table);
//            panel.add(scrollPane, BorderLayout.CENTER);
//            contentPanel.add(panel, BorderLayout.CENTER);
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//            contentPanel.add(new JLabel("Erreur lors de la récupération des réservations.", SwingConstants.CENTER), BorderLayout.CENTER);
//        }
//
//        contentPanel.revalidate();
//        contentPanel.repaint();
//    }
//}

package vues.responsablePanels;

import util.ConnexionBDD;
import com.toedter.calendar.JDateChooser; // Importer la classe JDateChooser

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

public class HistoriquePanel {
    private JPanel contentPanel;
    private DefaultTableModel model; // Déclarez le modèle de table ici

    public HistoriquePanel(JPanel contentPanel) {
        this.contentPanel = contentPanel;
        this.model = new DefaultTableModel(new String[]{"Nom Utilisateur", "Nom Salle", "Date", "Heure Début", "Heure Fin", "État", "Ressources Réservées"}, 0);
    }

    public void afficherHistoriquePanel() {
        contentPanel.removeAll();

        JPanel panel = new JPanel(new BorderLayout());
        JLabel label = new JLabel("Historique des réservations", SwingConstants.CENTER);
        panel.add(label, BorderLayout.NORTH);

        // Ajouter le sélecteur de date
        JPanel datePanel = new JPanel();
        JDateChooser dateChooser = new JDateChooser();
        dateChooser.setPreferredSize(new Dimension(150, 30));
        JButton filterButton = new JButton("Filtrer par Date");

        filterButton.addActionListener(e -> {
            Date selectedDate = dateChooser.getDate();
            if (selectedDate != null) {
                remplirTable(selectedDate); // Remplir le tableau avec la date sélectionnée
            }
        });

        datePanel.add(dateChooser);
        datePanel.add(filterButton);
        panel.add(datePanel, BorderLayout.SOUTH);

        // Remplir le modèle de table initialement
        remplirTable(null);

        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);
        contentPanel.add(panel, BorderLayout.CENTER);

        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private void remplirTable(Date date) {
        model.setRowCount(0); // Réinitialiser le modèle de table

        try (Connection conn = ConnexionBDD.getConnection();
             Statement stmt = conn.createStatement()) {

            String query = "SELECT u.nom AS nom_utilisateur, s.nom AS nom_salle, r.date, r.heure_debut, r.heure_fin, r.etat, " +
                    "(SELECT GROUP_CONCAT(re.nom SEPARATOR ', ') FROM reservation_ressource rr " +
                    "JOIN ressource re ON rr.id_ressource = re.id " +
                    "WHERE rr.id_reservation = r.id) AS ressources " +
                    "FROM reservation r " +
                    "JOIN utilisateur u ON r.id_utilisateur = u.id " +
                    "JOIN salle s ON r.id_salle = s.id " +
                    "WHERE r.etat IN ('validee', 'rejetee')";

            if (date != null) {
                // Filtrer par date sélectionnée
                java.sql.Date sqlDate = new java.sql.Date(date.getTime());
                query += " AND r.date = '" + sqlDate + "'";
            }

            query += " ORDER BY r.date DESC, r.heure_debut DESC";

            ResultSet rs = stmt.executeQuery(query);
            boolean hasData = false;

            while (rs.next()) {
                hasData = true;
                String ressources = rs.getString("ressources") != null ? rs.getString("ressources") : "Aucune";
                Object[] row = {
                        rs.getString("nom_utilisateur"),
                        rs.getString("nom_salle"),
                        rs.getDate("date"),
                        rs.getTime("heure_debut"),
                        rs.getTime("heure_fin"),
                        rs.getString("etat"),
                        ressources
                };
                model.addRow(row);
            }

            if (!hasData) {
                model.addRow(new Object[]{"Aucune réservation", "", "", "", "", "", ""});
            }

        } catch (SQLException e) {
            e.printStackTrace();
            contentPanel.add(new JLabel("Erreur lors de la récupération des réservations.", SwingConstants.CENTER), BorderLayout.CENTER);
        }
    }
}