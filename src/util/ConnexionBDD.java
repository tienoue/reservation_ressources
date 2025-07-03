package util;
import java.sql.*;

public class ConnexionBDD {
    private static final String URL = "jdbc:mysql://localhost:3306/";
    private static final String USER = "root"; // Remplacez par votre nom d'utilisateur
    private static final String PASSWORD = ""; // Remplacez par votre mot de passe
    private static final String DB_NAME = "gestion_reservations";

    private static Connection conn;
    public static Connection getConnection() throws SQLException {
        if (conn == null || conn.isClosed()) {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/"+DB_NAME, USER, PASSWORD);
        }
        return conn;
    }

    public static void main(String[] args) {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            // Vérifier si la base de données existe
            if (!databaseExists(conn, DB_NAME)) {
                createDatabase(conn);
                System.out.println("Base de données créée : " + DB_NAME);
                createTables(conn);
                populateDatabase(conn);
            } else {
                System.out.println("La base de données existe déjà : " + DB_NAME);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static boolean databaseExists(Connection conn, String dbName) throws SQLException {
        Statement stmt = conn.createStatement();
        var rs = stmt.executeQuery("SHOW DATABASES LIKE '" + dbName + "'");
        return rs.next();
    }

    private static void createDatabase(Connection conn) throws SQLException {
        Statement stmt = conn.createStatement();
        stmt.executeUpdate("CREATE DATABASE " + DB_NAME);
    }

    private static void createTables(Connection conn) throws SQLException {
        conn.setCatalog(DB_NAME); // Sélectionner la base de données
        Statement stmt = conn.createStatement();

        stmt.executeUpdate("CREATE TABLE IF NOT EXISTS salle (\n" +
                "    id INT AUTO_INCREMENT PRIMARY KEY,\n" +
                "    nom VARCHAR(100) NOT NULL UNIQUE,\n" +
                "    capacite INT NOT NULL,\n" +
                "    type VARCHAR(50) NOT NULL\n" +
                ")");

        stmt.executeUpdate("CREATE TABLE IF NOT EXISTS ressource (\n" +
                "    id INT AUTO_INCREMENT PRIMARY KEY,\n" +
                "    nom VARCHAR(100) NOT NULL UNIQUE,\n" +
                "    quantiteTotale INT NOT NULL,\n" +
                "    quantiteDisponible INT NOT NULL\n" +
                ")");

        stmt.executeUpdate("CREATE TABLE IF NOT EXISTS utilisateur (\n" +
                "    id INT AUTO_INCREMENT PRIMARY KEY,\n" +
                "    nom VARCHAR(100) NOT NULL UNIQUE,\n" +
                "    password VARCHAR(255) NOT NULL,\n" +
                "    role ENUM('admin', 'responsable', 'demandeur') NOT NULL\n" +
                ")");

        stmt.executeUpdate("CREATE TABLE reservation (" +
                "id INT AUTO_INCREMENT PRIMARY KEY," +
                "id_utilisateur INT," +
                "id_salle INT," +
                "date DATE," +
                "heure_debut TIME," +
                "heure_fin TIME," +
                "etat ENUM('en attente', 'validee', 'rejetee')," +
                "FOREIGN KEY (id_utilisateur) REFERENCES utilisateur(id)," +
                "FOREIGN KEY (id_salle) REFERENCES salle(id))");

        stmt.executeUpdate("CREATE TABLE reservation_ressource (" +
                "id_reservation INT," +
                "id_ressource INT," +
                "quantite INT," +
                "FOREIGN KEY (id_reservation) REFERENCES reservation(id)," +
                "FOREIGN KEY (id_ressource) REFERENCES ressource(id))");
    }

    private static void populateDatabase(Connection conn) throws SQLException {
        conn.setCatalog(DB_NAME); // Sélectionner la base de données

        insertSalles(conn);
        insertRessources(conn);
        insertUtilisateurs(conn);
        insertReservations(conn);
        insertReservationRessources(conn);
    }

    private static void insertSalles(Connection conn) throws SQLException {
        String sql = "INSERT INTO salle (nom, capacite, type) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "Salle 203");
            stmt.setInt(2, 30);
            stmt.setString(3, "Cours");
            stmt.executeUpdate();

            stmt.setString(1, "Salle 204");
            stmt.setInt(2, 40);
            stmt.setString(3, "Réunion");
            stmt.executeUpdate();

            stmt.setString(1, "Salle 205");
            stmt.setInt(2, 50);
            stmt.setString(3, "Labo");
            stmt.executeUpdate();
        }
    }

    private static void insertRessources(Connection conn) throws SQLException {
        String sql = "INSERT INTO ressource (nom, quantiteTotale, quantiteDisponible) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "Vidéoprojecteur");
            stmt.setInt(2, 5);
            stmt.setInt(3, 5); // Tout disponible initialement
            stmt.executeUpdate();

            stmt.setString(1, "PC Portable");
            stmt.setInt(2, 10);
            stmt.setInt(3, 10);
            stmt.executeUpdate();

            stmt.setString(1, "Tableau Interactif");
            stmt.setInt(2, 3);
            stmt.setInt(3, 3);
            stmt.executeUpdate();
        }
    }

    private static void insertUtilisateurs(Connection conn) throws SQLException {
        String sql = "INSERT INTO utilisateur (nom, password, role) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            // Ajout de l'administrateur
            stmt.setString(1, "Admin");
            stmt.setString(2, "admin"); // mot de passe provisoire
            stmt.setString(3, "admin");
            stmt.executeUpdate();

            // Ajout de demandeurs
            stmt.setString(1, "Alice Martine");
            stmt.setString(2, "demandeur");
            stmt.setString(3, "demandeur");
            stmt.executeUpdate();

            stmt.setString(1, "Bob Dupont");
            stmt.setString(2, "demandeur");
            stmt.setString(3, "demandeur");
            stmt.executeUpdate();

            // Ajout du responsable
            stmt.setString(1, "Charlie Petit");
            stmt.setString(2, "responsable");
            stmt.setString(3, "responsable");
            stmt.executeUpdate();
        }
    }

    private static void insertReservations(Connection conn) throws SQLException {
        String sql = "INSERT INTO reservation (id_utilisateur, id_salle, date, heure_debut, heure_fin, etat) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Réservations validées par des demandeurs
            stmt.setInt(1, 2); // Alice Martine (demandeur)
            stmt.setInt(2, 1); // Salle ID
            stmt.setDate(3, Date.valueOf("2025-02-10")); // Date
            stmt.setTime(4, Time.valueOf("10:00:00")); // Heure début
            stmt.setTime(5, Time.valueOf("12:00:00")); // Heure fin
            stmt.setString(6, "validee");
            stmt.executeUpdate();

            // Réservations validées par des demandeurs
            stmt.setInt(1, 2); // Alice Martine (demandeur)
            stmt.setInt(2, 1); // Salle ID
            stmt.setDate(3, Date.valueOf("2025-02-10")); // Date
            stmt.setTime(4, Time.valueOf("08:00:00")); // Heure début
            stmt.setTime(5, Time.valueOf("10:00:00")); // Heure fin
            stmt.setString(6, "validee");
            stmt.executeUpdate();

            // Réservations validées par des demandeurs
            stmt.setInt(1, 2); // Alice Martine (demandeur)
            stmt.setInt(2, 1); // Salle ID
            stmt.setDate(3, Date.valueOf("2025-02-10")); // Date
            stmt.setTime(4, Time.valueOf("12:00:00")); // Heure début
            stmt.setTime(5, Time.valueOf("14:00:00")); // Heure fin
            stmt.setString(6, "validee");
            stmt.executeUpdate();

            stmt.setInt(1, 2); // Alice Martine
            stmt.setInt(2, 1); // Salle ID
            stmt.setDate(3, Date.valueOf("2025-07-01")); // Date
            stmt.setTime(4, Time.valueOf("09:00:00")); // Heure début
            stmt.setTime(5, Time.valueOf("11:00:00")); // Heure fin
            stmt.setString(6, "validee");
            stmt.executeUpdate();

            stmt.setInt(1, 3);
            stmt.setInt(2, 2); // Salle ID
            stmt.setDate(3, Date.valueOf("2025-07-01")); // Date
            stmt.setTime(4, Time.valueOf("12:00:00")); // Heure début
            stmt.setTime(5, Time.valueOf("14:00:00")); // Heure fin
            stmt.setString(6, "validee");
            stmt.executeUpdate();

            stmt.setInt(1, 3); // Bob Dupont
            stmt.setInt(2, 2); // Salle ID
            stmt.setDate(3, Date.valueOf("2025-07-11"));
            stmt.setTime(4, Time.valueOf("14:00:00"));
            stmt.setTime(5, Time.valueOf("16:00:00"));
            stmt.setString(6, "validee");
            stmt.executeUpdate();

            // Réservations en attente
            stmt.setInt(1, 3); // Bob Dupont
            stmt.setInt(2, 1); // Salle ID
            stmt.setDate(3, Date.valueOf("2025-07-12"));
            stmt.setTime(4, Time.valueOf("09:00:00"));
            stmt.setTime(5, Time.valueOf("11:00:00"));
            stmt.setString(6, "en attente");
            stmt.executeUpdate();

            // Plus de réservations par demandeurs
            stmt.setInt(1, 2); // Alice Martine
            stmt.setInt(2, 2); // Salle ID
            stmt.setDate(3, Date.valueOf("2025-07-13"));
            stmt.setTime(4, Time.valueOf("13:00:00"));
            stmt.setTime(5, Time.valueOf("15:00:00"));
            stmt.setString(6, "validee");
            stmt.executeUpdate();

            stmt.setInt(1, 3); // Bob Dupont
            stmt.setInt(2, 3); // Salle ID
            stmt.setDate(3, Date.valueOf("2025-07-14"));
            stmt.setTime(4, Time.valueOf("10:00:00"));
            stmt.setTime(5, Time.valueOf("12:00:00"));
            stmt.setString(6, "validee");
            stmt.executeUpdate();
        }
    }

    private static void insertReservationRessources(Connection conn) throws SQLException {
        String sql = "INSERT INTO reservation_ressource (id_reservation, id_ressource, quantite) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            // Insérez des ressources pour la réservation ID 1 (réservation d'Alice)
            stmt.setInt(1, 1); // ID de réservation
            stmt.setInt(2, 1); // ID de ressource (Vidéoprojecteur)
            stmt.setInt(3, 1); // Quantité
            stmt.executeUpdate();

            stmt.setInt(1, 1);
            stmt.setInt(2, 2); // ID de ressource (PC Portable)
            stmt.setInt(3, 2);
            stmt.executeUpdate();

            // Insérez des ressources pour la réservation ID 2 (réservation d'Alice)
            stmt.setInt(1, 2); // ID de réservation
            stmt.setInt(2, 3); // ID de ressource (Tableau Interactif)
            stmt.setInt(3, 1);
            stmt.executeUpdate();

            // Insérez des ressources pour la réservation ID 3 (réservation de Bob)
            stmt.setInt(1, 3); // ID de réservation
            stmt.setInt(2, 1); // ID de ressource (Vidéoprojecteur)
            stmt.setInt(3, 1); // Quantité
            stmt.executeUpdate();

            // Insérez des ressources pour la réservation ID 3 (réservation de Bob)
            stmt.setInt(1, 3); // ID de réservation
            stmt.setInt(2, 2); // ID de ressource (Vidéoprojecteur)
            stmt.setInt(3, 1); // Quantité
            stmt.executeUpdate();

            // Ajoutez d'autres ressources si nécessaire
            stmt.setInt(1, 4); // ID de réservation (en attente de Bob)
            stmt.setInt(2, 2); // ID de ressource (PC Portable)
            stmt.setInt(3, 1);
            stmt.executeUpdate();
        }
    }
}