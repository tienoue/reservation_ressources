package vues.responsablePanels;

import controllers.ResponsableController;
import models.Reservation;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class AfficherReservations extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;

    public AfficherReservations() {
        setLayout(new BorderLayout());

        tableModel = new DefaultTableModel(new Object[]{"ID", "Date", "Heure de Debut", "Heure de Fin", "Salle", "Ressource", "Demandeur", "Etat", "Action"}, 0);
        table = new JTable(tableModel);

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        chargerReservations();
    }

    public void chargerReservations() {
        try {
            tableModel.setRowCount(0);
            List<Reservation> reservations = ResponsableController.getAll();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy"); // Format personnalisé
            for (Reservation r : reservations) {
                tableModel.addRow(new Object[]{
                        r.getId(),
                        r.getDate().format(formatter), // Conversion de LocalDate en String
                        r.getHeureDebut(),
                        r.getHeureFin(),
                        r.getId_Salle(),
                        r.getNomRessource(),
                        r.getId_Utilisateur(),
                        r.getEtat()
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erreur de chargement: " + e.getMessage());
        }
    }
}