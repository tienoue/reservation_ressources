//package vues.responsablePanels;
//
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
//public class CalendrierPanel {
//    private JPanel contentPanel;
//
//    public CalendrierPanel(JPanel contentPanel) {
//        this.contentPanel = contentPanel;
//    }
//
//    public void afficherCalendrierPanel() {
//        contentPanel.removeAll();
//
//        JPanel panel = new JPanel();
//        panel.setLayout(new BorderLayout());
//
//        JLabel label = new JLabel("Calendrier des réservations", SwingConstants.CENTER);
//        panel.add(label, BorderLayout.NORTH);
//
//        String[] columnNames = {"Date", "Salle", "Heure Début", "Heure Fin"};
//        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
//
//        try (Connection conn = ConnexionBDD.getConnection();
//             Statement stmt = conn.createStatement();
//             ResultSet rs = stmt.executeQuery("SELECT r.date, s.nom AS salle, r.heure_debut, r.heure_fin " +
//                     "FROM reservation r JOIN salle s ON r.id_salle = s.id WHERE r.etat = 'validee'")) {
//
//            while (rs.next()) {
//                Object[] row = {
//                        rs.getDate("date"),
//                        rs.getString("salle"),
//                        rs.getTime("heure_debut"),
//                        rs.getTime("heure_fin")
//                };
//                model.addRow(row);
//            }
//
//            JTable table = new JTable(model);
//            JScrollPane scrollPane = new JScrollPane(table);
//            panel.add(scrollPane, BorderLayout.CENTER);
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//            panel.add(new JLabel("Erreur lors de la récupération des réservations.", SwingConstants.CENTER), BorderLayout.CENTER);
//        }
//
//        contentPanel.add(panel, BorderLayout.CENTER);
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
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

public class CalendrierPanel {
    private JPanel contentPanel;
    private DefaultTableModel model; // Déclarez le modèle de table ici

    public CalendrierPanel(JPanel contentPanel) {
        this.contentPanel = contentPanel;
        this.model = new DefaultTableModel(new String[]{"Nom Utilisateur", "Salle", "Date", "Heure Début", "Heure Fin", "Ressources Réservées"}, 0);
    }

    public void afficherCalendrierPanel() {
        contentPanel.removeAll();

        JPanel panel = new JPanel(new BorderLayout());
        JLabel label = new JLabel("Calendrier des réservations à venir", SwingConstants.CENTER);
        panel.add(label, BorderLayout.NORTH);

        // Ajouter le sélecteur de date
        JPanel datePanel = new JPanel();
        JDateChooser dateChooser = new JDateChooser();
        dateChooser.setPreferredSize(new Dimension(150, 30));
        JButton filterButton = new JButton("Filtrer par Date");

        filterButton.addActionListener(e -> {
            Date selectedDate = dateChooser.getDate();
            remplirTable(selectedDate); // Remplir le tableau avec la date sélectionnée
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

            String query = "SELECT u.nom AS nom_utilisateur, s.nom AS salle, r.date, r.heure_debut, r.heure_fin, " +
                    "(SELECT GROUP_CONCAT(re.nom SEPARATOR ', ') FROM reservation_ressource rr " +
                    "JOIN ressource re ON rr.id_ressource = re.id " +
                    "WHERE rr.id_reservation = r.id) AS ressources " +
                    "FROM reservation r " +
                    "JOIN salle s ON r.id_salle = s.id " +
                    "JOIN utilisateur u ON r.id_utilisateur = u.id " +
                    "WHERE r.etat = 'validee' AND r.date >= CURDATE()";

            if (date != null) {
                // Convertir java.util.Date en java.sql.Date
                java.sql.Date sqlDate = new java.sql.Date(date.getTime());
                query += " AND r.date = '" + sqlDate + "'";
            }

            query += " ORDER BY r.date ASC, r.heure_debut ASC";

            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                Object[] row = {
                        rs.getString("nom_utilisateur"),
                        rs.getString("salle"),
                        rs.getDate("date"),
                        rs.getTime("heure_debut"),
                        rs.getTime("heure_fin"),
                        rs.getString("ressources") != null ? rs.getString("ressources") : "Aucune" // Gérer les cas où il n'y a pas de ressources
                };
                model.addRow(row);
            }

            // Si aucun résultat trouvé, ajouter une ligne pour l'indiquer
            if (model.getRowCount() == 0) {
                model.addRow(new Object[]{"Aucune réservation", "", "", "", "", "Aucune"});
            }

        } catch (SQLException e) {
            e.printStackTrace();
            contentPanel.add(new JLabel("Erreur lors de la récupération des réservations.", SwingConstants.CENTER), BorderLayout.CENTER);
        }
    }
}