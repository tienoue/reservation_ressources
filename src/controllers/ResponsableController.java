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
                        rs.getDate("date").toLocalDate(),
                        rs.getTime("heure_debut").toLocalTime(),
                        rs.getTime("heure_fin").toLocalTime(),
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

    public static void validerReservation(int reservationId) throws SQLException {
        Connection conn = null;
        PreparedStatement psCheckRoom = null;
        PreparedStatement psGetQuantite = null;
        PreparedStatement psUpdateRessource = null;
        PreparedStatement psUpdateReservation = null;
        ResultSet rs = null;
        ResultSet rsRoom = null;

        try {
            conn = ConnexionBDD.getConnection();
            conn.setAutoCommit(false); // Start transaction

            // Step 1: Get reservation details (room, date, time) to check room availability
            String getReservationQuery = "SELECT id_salle, date, heure_debut, heure_fin FROM reservation WHERE id = ?";
            PreparedStatement psGetReservation = conn.prepareStatement(getReservationQuery);
            psGetReservation.setInt(1, reservationId);
            rs = psGetReservation.executeQuery();

            if (!rs.next()) {
                throw new SQLException("Réservation non trouvée.");
            }

            int idSalle = rs.getInt("id_salle");
            LocalDate date = rs.getDate("date").toLocalDate();
            LocalTime heureDebut = rs.getTime("heure_debut").toLocalTime();
            LocalTime heureFin = rs.getTime("heure_fin").toLocalTime();
            rs.close();
            psGetReservation.close();

            // Step 2: Check for room availability (no overlapping validated reservations)
            String checkRoomQuery = "SELECT id FROM reservation WHERE id_salle = ? AND date = ? AND etat = 'Validé' " +
                    "AND ((heure_debut < ? AND heure_fin > ?) OR (heure_debut < ? AND heure_fin > ?) OR (heure_debut >= ? AND heure_debut < ?))";
            psCheckRoom = conn.prepareStatement(checkRoomQuery);
            psCheckRoom.setInt(1, idSalle);
            psCheckRoom.setDate(2, java.sql.Date.valueOf(date));
            psCheckRoom.setTime(3, java.sql.Time.valueOf(heureFin));
            psCheckRoom.setTime(4, java.sql.Time.valueOf(heureDebut));
            psCheckRoom.setTime(5, java.sql.Time.valueOf(heureFin));
            psCheckRoom.setTime(6, java.sql.Time.valueOf(heureDebut));
            psCheckRoom.setTime(7, java.sql.Time.valueOf(heureDebut));
            psCheckRoom.setTime(8, java.sql.Time.valueOf(heureFin));
            rsRoom = psCheckRoom.executeQuery();

            if (rsRoom.next()) {
                throw new SQLException("La salle est déjà réservée pour ce créneau horaire.");
            }
            rsRoom.close();
            psCheckRoom.close();

            // Step 3: Get all resources and quantities for the reservation
            String getQuantiteQuery = "SELECT rr.id_ressource, rr.quantite FROM reservation_ressource rr WHERE rr.id_reservation = ?";
            psGetQuantite = conn.prepareStatement(getQuantiteQuery);
            psGetQuantite.setInt(1, reservationId);
            rs = psGetQuantite.executeQuery();

            List<Integer> resourceIds = new ArrayList<>();
            List<Integer> quantitesReservees = new ArrayList<>();
            while (rs.next()) {
                resourceIds.add(rs.getInt("id_ressource"));
                quantitesReservees.add(rs.getInt("quantite"));
            }
            rs.close();
            psGetQuantite.close();

            if (resourceIds.isEmpty()) {
                throw new SQLException("Aucune ressource associée à cette réservation.");
            }

            // Step 4: Check and update resource quantities
            String checkRessourceQuery = "SELECT quantiteDisponible FROM ressource WHERE id = ?";
            String updateRessourceQuery = "UPDATE ressource SET quantiteDisponible = quantiteDisponible - ? WHERE id = ?";
            psUpdateRessource = conn.prepareStatement(updateRessourceQuery);

            for (int i = 0; i < resourceIds.size(); i++) {
                int idRessource = resourceIds.get(i);
                int quantiteReservee = quantitesReservees.get(i);

                // Check available quantity
                PreparedStatement psCheckRessource = conn.prepareStatement(checkRessourceQuery);
                psCheckRessource.setInt(1, idRessource);
                rs = psCheckRessource.executeQuery();
                if (rs.next()) {
                    int quantiteDisponible = rs.getInt("quantiteDisponible");
                    if (quantiteDisponible < quantiteReservee) {
                        throw new SQLException("Quantité disponible insuffisante pour la ressource ID " + idRessource + ".");
                    }
                } else {
                    throw new SQLException("Ressource ID " + idRessource + " non trouvée.");
                }
                rs.close();
                psCheckRessource.close();

                // Update resource quantity
                psUpdateRessource.setInt(1, quantiteReservee);
                psUpdateRessource.setInt(2, idRessource);
                psUpdateRessource.addBatch();
            }

            // Execute batch update for all resources
            psUpdateRessource.executeBatch();

            // Step 5: Update reservation state
            String updateReservationQuery = "UPDATE reservation SET etat = 'validee' WHERE id = ?";
            psUpdateReservation = conn.prepareStatement(updateReservationQuery);
            psUpdateReservation.setInt(1, reservationId);
            psUpdateReservation.executeUpdate();

            conn.commit(); // Commit transaction
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback(); // Rollback on error
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            throw e;
        } finally {
            if (rs != null) rs.close();
            if (rsRoom != null) rsRoom.close();
            if (psCheckRoom != null) psCheckRoom.close();
            if (psGetQuantite != null) psGetQuantite.close();
            if (psUpdateRessource != null) psUpdateRessource.close();
            if (psUpdateReservation != null) psUpdateReservation.close();
            if (conn != null) conn.close();
        }
    }

    public static void rejeterReservation(int reservationId) throws SQLException {
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = ConnexionBDD.getConnection();
            String query = "UPDATE reservation SET etat = 'rejetee' WHERE id = ?";
            ps = conn.prepareStatement(query);
            ps.setInt(1, reservationId);
            ps.executeUpdate();
        } finally {
            if (ps != null) ps.close();
            if (conn != null) conn.close();
        }
    }
}