package com.example.linterim.Helper;

public class Offre {
    private String annonce_id;
    private String titre;
    private String description;
    private String lieu;
    private String periode;
    private String remuneration;
    private String employeur_id;
    private String datePublication; // Utilisation de String pour les dates au format normal
    private String profil_recherche;
    private String missions_principales;
    private String type_contract;

    // Constructeur par défaut requis pour Firebase
    public Offre() {
        // Constructeur vide nécessaire pour Firebase Realtime Database
    }

  /*  public Offre(String annonceId, String titre, String description, String lieu, String periode, String remuneration, String employeurId, String datePublication, String profilRecherche, String missions_principales, String typeContrat) {
        this.annonce_id = annonceId;
        this.titre = titre;
        this.description = description;
        this.lieu = lieu;
        this.periode = periode;
        this.remuneration = remuneration;
        this.employeur_id = employeurId;
        this.datePublication = datePublication;
        this.profil_recherche = profilRecherche;
        this.missions_principales = missions_principales;
        this.type_contrat = typeContrat;
    }*/

    public Offre(String titre, String description, String lieu, String periode, String remuneration, String typeContrat) {
        this.titre = titre;
        this.description = description;
        this.lieu = lieu;
        this.periode = periode;
        this.remuneration = remuneration;
        this.type_contract = typeContrat;
    }

    // Getters et Setters
    public String getAnnonce_id() {
        return annonce_id;
    }

    public void setAnnonce_id(String annonce_id) {
        this.annonce_id = annonce_id;
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

    public String getRemuneration() {
        return remuneration;
    }

    public void setRemuneration(String remuneration) {
        this.remuneration = remuneration;
    }

    public String getEmployeur_id() {
        return employeur_id;
    }

    public void setEmployeur_id(String employeur_id) {
        this.employeur_id = employeur_id;
    }

    public String getDatePublication() {
        return datePublication;
    }

    public void setDatePublication(String datePublication) {
        this.datePublication = datePublication;
    }

    public String getProfil_recherche() {
        return profil_recherche;
    }

    public void setProfil_recherche(String profil_recherche) {
        this.profil_recherche = profil_recherche;
    }

    public String getMissions_principales() {
        return missions_principales;
    }

    public void setMissions_principales(String missions_principales) {
        this.missions_principales = missions_principales;
    }

    public String getType_contract() {
        return type_contract;
    }

    public void setType_contract(String type_contract) {
        this.type_contract = type_contract;
    }
}
