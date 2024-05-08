package com.example.linterim.Helper;

import com.google.firebase.database.DataSnapshot;

public class Employeur {
    private String employeur_id;
    private String nom_entreprise;
    private String email;
    private String telephone;
    private String adresse;
    private String site_web;
    private String linkedin_url;

    // Constructeur par défaut requis pour Firebase
    public Employeur() {
    }

    public Employeur(String employeur_id, String nom_entreprise, String email, String telephone,
                     String adresse,String siteWeb, String linkedin_url) {
        this.employeur_id = employeur_id;
        this.nom_entreprise = nom_entreprise;
        this.email = email;
        this.telephone = telephone;
        this.adresse = adresse;
        this.site_web = siteWeb;
        this.linkedin_url = linkedin_url;
    }


    public String getEmployeur_id() {
        return employeur_id;
    }

    public void setEmployeur_id(String employeur_id) {
        this.employeur_id = employeur_id;
    }

    public String getNom_entreprise() {
        return nom_entreprise;
    }

    public void setNom_entreprise(String nom_entreprise) {
        this.nom_entreprise = nom_entreprise;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getSite_web() {
        return site_web;
    }

    public void setSite_web(String site_web) {
        this.site_web = site_web;
    }

    public String getLinkedin_url() {
        return linkedin_url;
    }

    public void setLinkedin_url(String linkedin_url) {
        this.linkedin_url = linkedin_url;
    }

    // Exemple de méthode pour obtenir un objet Employeur à partir de Firebase
     public static Employeur fromSnapshot(DataSnapshot snapshot) {
        return snapshot.getValue(Employeur.class);
    }
}
