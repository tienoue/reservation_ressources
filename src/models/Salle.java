package models;

import util.ConnexionBDD;

import java.sql.*;
import java.util.*;

public class Salle {
    private int id;
    private String nom;
    private int capacite;
    private String type;

    public Salle() {}

    public Salle(int id, String nom, int capacite, String type) {
        this.id = id;
        this.nom = nom;
        this.capacite = capacite;
        this.type = type;
    }

    public int getId() { return id; }
    public String getNom() { return nom; }
    public int getCapacite() { return capacite; }
    public String getType() { return type; }

    public void setId(int id) { this.id = id; }
    public void setNom(String nom) { this.nom = nom; }
    public void setCapacite(int capacite) { this.capacite = capacite; }
    public void setType(String type) { this.type = type; }

    public static List<Salle> getAll() throws SQLException {
        Connection conn = ConnexionBDD.getConnection();
        List<Salle> salles = new ArrayList<>();
        PreparedStatement ps = conn.prepareStatement("SELECT * FROM salle");
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            Salle s = new Salle(
                    rs.getInt("id"),
                    rs.getString("nom"),
                    rs.getInt("capacite"),
                    rs.getString("type")
            );
            salles.add(s);
        }
        return salles;
    }

    public void add() throws SQLException {
        Connection conn = ConnexionBDD.getConnection();
        PreparedStatement ps = conn.prepareStatement("INSERT INTO salle (nom, capacite, type) VALUES (?, ?, ?)");
        ps.setString(1, nom);
        ps.setInt(2, capacite);
        ps.setString(3, type);
        ps.executeUpdate();
    }

    public void update() throws SQLException {
        Connection conn = ConnexionBDD.getConnection();
        PreparedStatement ps = conn.prepareStatement("UPDATE salle SET nom=?, capacite=?, type=? WHERE id=?");
        ps.setString(1, nom);
        ps.setInt(2, capacite);
        ps.setString(3, type);
        ps.setInt(4, id);
        ps.executeUpdate();
    }

    public void delete() throws SQLException {
        Connection conn = ConnexionBDD.getConnection();
        PreparedStatement ps = conn.prepareStatement("DELETE FROM salle WHERE id=?");
        ps.setInt(1, id);
        ps.executeUpdate();
    }
}
