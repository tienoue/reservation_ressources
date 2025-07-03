package models;

import util.ConnexionBDD;

import java.sql.*;
import java.util.*;

public class Utilisateur {
    private int id;
    private String nom;
    private String password;
    private String role;

    public Utilisateur() {}

    public Utilisateur(int id, String nom, String password, String role) {
        this.id = id;
        this.nom = nom;
        this.password = password;
        this.role = role;
    }

    public int getId() { return id; }
    public String getNom() { return nom; }
    public String getPassword() { return password; }
    public String getRole() { return role; }

    public void setId(int id) { this.id = id; }
    public void setNom(String nom) { this.nom = nom; }
    public void setPassword(String password) { this.password = password; }
    public void setRole(String role) { this.role = role; }

    public static List<Utilisateur> getAll() throws SQLException {
        Connection conn = ConnexionBDD.getConnection();
        List<Utilisateur> utilisateurs = new ArrayList<>();
        //PreparedStatement ps = conn.prepareStatement("SELECT * FROM utilisateur");
        PreparedStatement ps = conn.prepareStatement("SELECT * FROM utilisateur where role='demandeur'");
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            Utilisateur u = new Utilisateur(
                    rs.getInt("id"),
                    rs.getString("nom"),
                    rs.getString("password"),
                    rs.getString("role")
            );
            utilisateurs.add(u);
        }
        return utilisateurs;
    }

    public void add() throws SQLException {
        Connection conn = ConnexionBDD.getConnection();
        PreparedStatement ps = conn.prepareStatement("INSERT INTO utilisateur (nom, password, role) VALUES (?, ?, ?)");
        ps.setString(1, nom);
        ps.setString(2, password);
        ps.setString(3, role);
        ps.executeUpdate();
    }

    public void update() throws SQLException {
        Connection conn = ConnexionBDD.getConnection();
        PreparedStatement ps = conn.prepareStatement("UPDATE utilisateur SET nom=?, password=?, role=? WHERE id=?");
        ps.setString(1, nom);
        ps.setString(2, password);
        ps.setString(3, role);
        ps.setInt(4, id);
        ps.executeUpdate();
    }

    public void delete() throws SQLException {
        Connection conn = ConnexionBDD.getConnection();
        PreparedStatement ps = conn.prepareStatement("DELETE FROM utilisateur WHERE id=?");
        ps.setInt(1, id);
        ps.executeUpdate();
    }
}
