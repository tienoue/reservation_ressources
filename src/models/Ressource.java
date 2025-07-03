package models;

import util.ConnexionBDD;

import java.sql.*;
import java.util.*;

public class Ressource {
    private int id;
    private String nom;
    private int quantiteTotale;
    private int quantiteDisponible;

    public Ressource() {}

    public Ressource(int id, String nom, int quantiteTotale, int quantiteDisponible) {
        this.id = id;
        this.nom = nom;
        this.quantiteTotale = quantiteTotale;
        this.quantiteDisponible = quantiteDisponible;
    }

    public int getId() { return id; }
    public String getNom() { return nom; }
    public int getQuantiteTotale() { return quantiteTotale; }
    public int getQuantiteDisponible() { return quantiteDisponible; }

    public void setId(int id) { this.id = id; }
    public void setNom(String nom) { this.nom = nom; }
    public void setQuantiteTotale(int quantiteTotale) { this.quantiteTotale = quantiteTotale; }
    public void setQuantiteDisponible(int quantiteDisponible) { this.quantiteDisponible = quantiteDisponible; }

    public static List<Ressource> getAll() throws SQLException {
        Connection conn = ConnexionBDD.getConnection();
        List<Ressource> ressources = new ArrayList<>();
        PreparedStatement ps = conn.prepareStatement("SELECT * FROM ressource");
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            Ressource r = new Ressource(
                    rs.getInt("id"),
                    rs.getString("nom"),
                    rs.getInt("quantiteTotale"),
                    rs.getInt("quantiteDisponible")
            );
            ressources.add(r);
        }
        return ressources;
    }

    public void add() throws SQLException {
        Connection conn = ConnexionBDD.getConnection();
        PreparedStatement ps = conn.prepareStatement("INSERT INTO ressource (nom, quantiteTotale, quantiteDisponible) VALUES (?, ?, ?)");
        ps.setString(1, nom);
        ps.setInt(2, quantiteTotale);
        ps.setInt(3, quantiteDisponible);
        ps.executeUpdate();
    }

    public void update() throws SQLException {
        Connection conn = ConnexionBDD.getConnection();
        PreparedStatement ps = conn.prepareStatement("UPDATE ressource SET nom=?, quantiteTotale=?, quantiteDisponible=? WHERE id=?");
        ps.setString(1, nom);
        ps.setInt(2, quantiteTotale);
        ps.setInt(3, quantiteDisponible);
        ps.setInt(4, id);
        ps.executeUpdate();
    }

    public void delete() throws SQLException {
        Connection conn = ConnexionBDD.getConnection();
        PreparedStatement ps = conn.prepareStatement("DELETE FROM ressource WHERE id=?");
        ps.setInt(1, id);
        ps.executeUpdate();
    }
}
