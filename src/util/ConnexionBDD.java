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

        stmt.executeUpdate("CREATE TABLE `reservation` (\n" +
                "  `id` int(11) NOT NULL AUTO_INCREMENT,\n" +
                "  `id_utilisateur` int(11) DEFAULT NULL,\n" +
                "  `id_salle` int(11) DEFAULT NULL,\n" +
                "  `date` date DEFAULT NULL,\n" +
                "  `heure_debut` time DEFAULT NULL,\n" +
                "  `heure_fin` time DEFAULT NULL,\n" +
                "  `etat` enum('en attente','validee','rejetee') DEFAULT NULL,\n" +
                "  PRIMARY KEY (`id`),\n" +
                "  KEY `reservation_ibfk_1` (`id_utilisateur`),\n" +
                "  KEY `reservation_ibfk_2` (`id_salle`),\n" +
                "  CONSTRAINT `reservation_ibfk_1` FOREIGN KEY (`id_utilisateur`) REFERENCES `utilisateur` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,\n" +
                "  CONSTRAINT `reservation_ibfk_2` FOREIGN KEY (`id_salle`) REFERENCES `salle` (`id`) ON DELETE CASCADE ON UPDATE CASCADE\n" +
                ") ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci\n");

        stmt.executeUpdate("CREATE TABLE `reservation_ressource` (\n" +
                "  `id_reservation` int(11) DEFAULT NULL,\n" +
                "  `id_ressource` int(11) DEFAULT NULL,\n" +
                "  `quantite` int(11) DEFAULT NULL,\n" +
                "  KEY `reservation_ressource_ibfk_1` (`id_reservation`),\n" +
                "  KEY `reservation_ressource_ibfk_2` (`id_ressource`),\n" +
                "  CONSTRAINT `reservation_ressource_ibfk_1` FOREIGN KEY (`id_reservation`) REFERENCES `reservation` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,\n" +
                "  CONSTRAINT `reservation_ressource_ibfk_2` FOREIGN KEY (`id_ressource`) REFERENCES `ressource` (`id`) ON DELETE CASCADE ON UPDATE CASCADE\n" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci\n");
    }

    private static void populateDatabase(Connection conn) throws SQLException {
        conn.setCatalog(DB_NAME); // Sélectionner la base de données

        insertSalles(conn);
        insertRessources(conn);
        insertUtilisateurs(conn);
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
            stmt.setInt(2, 5);
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

            stmt.setString(1, "PC Desktop");
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
            stmt.setString(2, "admin123"); // mot de passe provisoire
            stmt.setString(3, "admin");
            stmt.executeUpdate();

            // Ajout du responsable
            stmt.setString(1, "Ngono Charlie");
            stmt.setString(2, "responsable");
            stmt.setString(3, "responsable");
            stmt.executeUpdate();

            // Ajout de demandeur
            stmt.setString(1, "Seben Martin");
            stmt.setString(2, "00000000");
            stmt.setString(3, "demandeur");
            stmt.executeUpdate();
        }
    }


}