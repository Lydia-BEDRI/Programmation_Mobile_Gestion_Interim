package com.example.linterim.Models;

public class Message {
    private String type;
    private String recipientId;
    private String content;
    private String candidatId;
    private String offreId;
    private String date;

    // Constructeur
    public Message(String type, String recipientId, String content, String candidatId, String offreId, String date) {
        this.type = type;
        this.recipientId = recipientId;
        this.content = content;
        this.candidatId = candidatId;
        this.offreId = offreId;
        this.date = date;
    }

    // Getters et Setters
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getRecipientId() { return recipientId; }
    public void setRecipientId(String recipientId) { this.recipientId = recipientId; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getCandidatId() { return candidatId; }
    public void setCandidatId(String candidatId) { this.candidatId = candidatId; }

    public String getOffreId() { return offreId; }
    public void setOffreId(String offreId) { this.offreId = offreId; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
}
