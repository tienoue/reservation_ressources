package controllers;

import models.Reservation;
import util.ConnexionBDD;
import vues.ResponsableDashboardView;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class ResponsableController {
    private ResponsableDashboardView dashboardView;

    public ResponsableController(ResponsableDashboardView dashboardView) {
        this.dashboardView = dashboardView;
    }

    public static List<Reservation> getAll() throws SQLException {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Reservation> reservations = new ArrayList<>();

        try {
            conn = ConnexionBDD.getConnection();
            String query = "SELECT r.id AS reservation_id, r.id_utilisateur, r.id_salle, r.date, r.heure_debut, r.heure_fin, r.etat, res.nom AS ressource_nom, rr.quantite AS quantite_reservee " +
                    "FROM reservation r " +
                    "INNER JOIN reservation_ressource rr ON r.id = rr.id_reservation " +
                    "INNER JOIN ressource res ON rr.id_ressource = res.id";
            ps = conn.prepareStatement(query);
            rs = ps.executeQuery();

            while (rs.next()) {
                Reservation reservation = new Reservation(
                        rs.getInt("reservation_id"),
                        rs.getInt("id_utilisateur"),
                        rs.getInt("id_salle"),
                        rs.getDate("date").toLocalDate(), // Conversion de java.sql.Date à LocalDate
                        rs.getTime("heure_debut").toLocalTime(),
                        rs.getTime("heure_fin").toLocalTime(), // Correction de "heur_fin" à "heure_fin"
                        rs.getString("etat")
                );
                reservation.setNomRessource(rs.getString("ressource_nom"));
                reservations.add(reservation);
            }
        } finally {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
            if (conn != null) conn.close();
        }
        return reservations;
    }
}