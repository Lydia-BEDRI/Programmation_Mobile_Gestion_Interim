package com.example.linterim.Models;

public class Candidature {

    private String candidature_id;
    private String offre_id;
    private String candidat_id;
    private String lettre_motivation;
    private String statut;
    private String date_candidature;

    // Constructeur par défaut (nécessaire pour Firebase)
    public Candidature() {
    }

    // Constructeur paramétré
    public Candidature(String candidature_id, String offre_id, String candidat_id, String lettre_motivation, String statut, String date_candidature) {
        this.candidature_id = candidature_id;
        this.offre_id = offre_id;
        this.candidat_id = candidat_id;
        this.lettre_motivation = lettre_motivation;
        this.statut = statut;
        this.date_candidature = date_candidature;
    }

    // Getters et Setters
    public String getCandidature_id() {
        return candidature_id;
    }

    public void setCandidature_id(String candidature_id) {
        this.candidature_id = candidature_id;
    }

    public String getOffre_id() {
        return offre_id;
    }

    public void setOffre_id(String offre_id) {
        this.offre_id = offre_id;
    }

    public String getCandidat_id() {
        return candidat_id;
    }

    public void setCandidat_id(String candidat_id) {
        this.candidat_id = candidat_id;
    }

    public String getLettre_motivation() {
        return lettre_motivation;
    }

    public void setLettre_motivation(String lettre_motivation) {
        this.lettre_motivation = lettre_motivation;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public String getDate_candidature() {
        return date_candidature;
    }

    public void setDate_candidature(String date_candidature) {
        this.date_candidature = date_candidature;
    }
}

