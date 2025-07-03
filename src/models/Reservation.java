package models;

import java.time.LocalDate;
import java.time.LocalTime;

public class Reservation {
    private int id;
    private int id_Utilisateur;
    private int id_Salle;
    private LocalDate date; // Changé de String à LocalDate
    private LocalTime heureDebut;
    private LocalTime heureFin;
    private String Etat;
    private String nomRessource;

    public Reservation(int id, int id_Utilisateur, int id_Salle, LocalDate date, LocalTime heureDebut, LocalTime heureFin, String etat) {
        this.id = id;
        this.id_Utilisateur = id_Utilisateur;
        this.id_Salle = id_Salle;
        this.date = date;
        this.heureDebut = heureDebut;
        this.heureFin = heureFin;
        this.Etat = etat;
    }

    public String getNomRessource() {
        return nomRessource;
    }

    public void setNomRessource(String nomRessource) {
        this.nomRessource = nomRessource;
    }

    public int getId() {
        return id;
    }

    public int getId_Utilisateur() {
        return id_Utilisateur;
    }

    public int getId_Salle() {
        return id_Salle;
    }

    public LocalDate getDate() {
        return date;
    }

    public LocalTime getHeureDebut() {
        return heureDebut;
    }

    public LocalTime getHeureFin() {
        return heureFin;
    }

    public String getEtat() {
        return Etat;
    }

    public void setId_Utilisateur(int id_Utilisateur) {
        this.id_Utilisateur = id_Utilisateur;
    }

    public void setId_Salle(int id_Salle) {
        this.id_Salle = id_Salle;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setHeureDebut(LocalTime heureDebut) {
        this.heureDebut = heureDebut;
    }

    public void setHeureFin(LocalTime heureFin) {
        this.heureFin = heureFin;
    }

    public void setEtat(String etat) {
        this.Etat = etat;
    }
}