package com.example.linterim.Models;

public class Message {
    private String type;
    private String senderId;
    private String recipientId;
    private String content;
    private String offreId;
    private String date;

    // Constructeur
    public Message(String type, String senderId, String recipientId, String content, String offreId, String date) {
        this.type = type;
        this.senderId = senderId;
        this.recipientId = recipientId;
        this.content = content;
        this.offreId = offreId;
        this.date = date;
    }

    // Getters et Setters
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getSenderId() { return senderId; }
    public void setSenderId(String senderId) { this.senderId = senderId; }

    public String getRecipientId() { return recipientId; }
    public void setRecipientId(String recipientId) { this.recipientId = recipientId; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public String getOffreId() { return offreId; }
    public void setOffreId(String offreId) { this.offreId = offreId; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
}
