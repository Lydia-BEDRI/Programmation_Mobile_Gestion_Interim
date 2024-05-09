package com.example.linterim.Helper;
import java.util.Date;

public class Offre {
    private String annonceId;
    private String titre;
    private String description;
    private String metier;
    private String lieu;
    private String periode;
    private double remuneration;
    private String employeurId;
    private Date datePublication;

    // Constructeur par défaut
    public Offre() {
        // Nécessaire pour Firebase (si vous utilisez Firebase pour la base de données)
    }

    // Constructeur avec paramètres
    public Offre(String annonceId, String titre, String description, String metier, String lieu,
                 String periode, double remuneration, String employeurId, Date datePublication) {
        this.annonceId = annonceId;
        this.titre = titre;
        this.description = description;
        this.metier = metier;
        this.lieu = lieu;
        this.periode = periode;
        this.remuneration = remuneration;
        this.employeurId = employeurId;
        this.datePublication = datePublication;
    }

    // Getters et setters pour chaque champ
    public String getAnnonceId() {
        return annonceId;
    }

    public void setAnnonceId(String annonceId) {
        this.annonceId = annonceId;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMetier() {
        return metier;
    }

    public void setMetier(String metier) {
        this.metier = metier;
    }

    public String getLieu() {
        return lieu;
    }

    public void setLieu(String lieu) {
        this.lieu = lieu;
    }

    public String getPeriode() {
        return periode;
    }

    public void setPeriode(String periode) {
        this.periode = periode;
    }

    public double getRemuneration() {
        return remuneration;
    }

    public void setRemuneration(double remuneration) {
        this.remuneration = remuneration;
    }

    public String getEmployeurId() {
        return employeurId;
    }

    public void setEmployeurId(String employeurId) {
        this.employeurId = employeurId;
    }

    public Date getDatePublication() {
        return datePublication;
    }

    public void setDatePublication(Date datePublication) {
        this.datePublication = datePublication;
    }
}
