package com.example.linterim.Models;

public class Candidat {
    private String uid;           // Identifiant unique de l'utilisateur
    private String email;         // Email de l'utilisateur
    private String nom;           // Nom du candidat
    private String prenom;        // Prénom du candidat
    private String nationalite;   // Nationalité du candidat

    private String motDePasse;
    private String dateNaissance; // Date de naissance du candidat (peut être représentée comme une chaîne de caractères ou un objet Date)
    private String telephone;     // Numéro de téléphone du candidat
    private String adresse;       // Adresse du candidat
    private String ville;         // Ville du candidat
    private String cvUrl;         // Lien vers le CV du candidat
    private String commentaires;  // Commentaires sur le candidat (expérience, compétences, etc.)

    // Constructeur par défaut
    public Candidat() {
        // Constructeur vide requis par Firebase pour la désérialisation
    }

    // Constructeur avec paramètres
    public Candidat(String uid, String email,String motDePasse, String nom, String prenom, String nationalite, String dateNaissance,
                    String telephone, String adresse, String ville, String cvUrl, String commentaires) {
        this.uid = uid;
        this.motDePasse = motDePasse;
        this.email = email;
        this.nom = nom;
        this.prenom = prenom;
        this.nationalite = nationalite;
        this.dateNaissance = dateNaissance;
        this.telephone = telephone;
        this.adresse = adresse;
        this.ville = ville;
        this.cvUrl = cvUrl;
        this.commentaires = commentaires;
    }

    public Candidat(String nom, String prenom, String adresseMail, String motDePasse) {
        this.motDePasse=motDePasse;
        this.nom =nom;
        this.prenom =prenom;
        this.email = adresseMail;
    }

    // Méthodes getters et setters pour accéder et définir les valeurs des champs
    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getNationalite() {
        return nationalite;
    }

    public void setNationalite(String nationalite) {
        this.nationalite = nationalite;
    }

    public String getDateNaissance() {
        return dateNaissance;
    }

    public void setDateNaissance(String dateNaissance) {
        this.dateNaissance = dateNaissance;
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

    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public String getCvUrl() {
        return cvUrl;
    }

    public void setCvUrl(String cvUrl) {
        this.cvUrl = cvUrl;
    }

    public String getCommentaires() {
        return commentaires;
    }

    public void setCommentaires(String commentaires) {
        this.commentaires = commentaires;
    }


    public String getMotDePasse() {
        return motDePasse;
    }

    public void setMotDePasse(String motDePasse) {
        this.motDePasse = motDePasse;
    }
}

