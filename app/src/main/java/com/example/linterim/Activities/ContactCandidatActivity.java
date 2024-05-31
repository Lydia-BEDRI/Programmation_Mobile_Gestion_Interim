package com.example.linterim.Activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.linterim.Models.Candidat;
import com.example.linterim.Models.Candidature;
import com.example.linterim.Models.Employeur;
import com.example.linterim.Models.Offre;
import com.example.linterim.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ContactCandidatActivity extends AppCompatActivity {

    private DatabaseReference databaseReference;
    private String candidatureId;
    private Candidat candidat;
    private Employeur employeur;
    private Offre offre;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contacter_candidat);

        ImageView backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Retourner à l'activité précédente
            }
        });

        candidatureId = getIntent().getStringExtra("candidature_id");
        if (candidatureId == null) {
            Toast.makeText(this, "ID de candidature manquant", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        databaseReference = FirebaseDatabase.getInstance().getReference();
        getCandidatInfo(candidatureId);

        findViewById(R.id.contactByMail).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (candidat != null && employeur != null && offre != null) {
                    String subject = "Réponse à l'offre " + offre.getTitre();
                    String message = "Expéditeur: " + employeur.getEmail() + "\n" +
                            "Destinataire: " + candidat.getEmail() + "\n" +
                            "Objet: " + subject;

                    new AlertDialog.Builder(ContactCandidatActivity.this)
                            .setTitle("Confirmer l'envoi de l'email")
                            .setMessage(message)
                            .setPositiveButton("Envoyer", (dialog, which) -> {
                                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", candidat.getEmail(), null));
                                emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
                                emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{employeur.getEmail()});
                                startActivity(Intent.createChooser(emailIntent, "Envoyer un email..."));
                            })
                            .setNegativeButton("Annuler", null)
                            .show();
                } else {
                    Toast.makeText(ContactCandidatActivity.this, "Les informations du candidat, de l'employeur ou de l'offre ne sont pas disponibles", Toast.LENGTH_LONG).show();
                }
            }
        });

        findViewById(R.id.contactBySMS).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (candidat != null) {
                    if (candidat.getTelephone() != null && !candidat.getTelephone().isEmpty()) {
                        String message = "Numéro du destinataire: " + candidat.getTelephone();

                        new AlertDialog.Builder(ContactCandidatActivity.this)
                                .setTitle("Confirmer l'envoi du SMS")
                                .setMessage(message)
                                .setPositiveButton("Envoyer", (dialog, which) -> {
                                    Intent smsIntent = new Intent(Intent.ACTION_VIEW, Uri.fromParts("sms", candidat.getTelephone(), null));
                                    startActivity(smsIntent);
                                })
                                .setNegativeButton("Annuler", null)
                                .show();
                    } else {
                        Toast.makeText(ContactCandidatActivity.this, "Le candidat n'a pas de numéro de téléphone", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(ContactCandidatActivity.this, "Les informations du candidat ne sont pas disponibles", Toast.LENGTH_LONG).show();
                }
            }
        });

        findViewById(R.id.contactByTelephone).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (candidat != null) {
                    if (candidat.getTelephone() != null && !candidat.getTelephone().isEmpty()) {
                        String message = "Numéro à appeler: " + candidat.getTelephone();

                        new AlertDialog.Builder(ContactCandidatActivity.this)
                                .setTitle("Confirmer l'appel téléphonique")
                                .setMessage(message)
                                .setPositiveButton("Appeler", (dialog, which) -> {
                                    Intent callIntent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", candidat.getTelephone(), null));
                                    startActivity(callIntent);
                                })
                                .setNegativeButton("Annuler", null)
                                .show();
                    } else {
                        Toast.makeText(ContactCandidatActivity.this, "Le candidat n'a pas de numéro de téléphone", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(ContactCandidatActivity.this, "Les informations du candidat ne sont pas disponibles", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void getCandidatInfo(String candidatureId) {
        databaseReference.child("Candidatures").child(candidatureId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Candidature candidature = snapshot.getValue(Candidature.class);
                if (candidature != null) {
                    String candidatId = candidature.getCandidat_id();
                    String offreId = candidature.getOffre_id();

                    // Récupérer les informations du candidat
                    databaseReference.child("Candidats").child(candidatId).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            candidat = snapshot.getValue(Candidat.class);
                            if (candidat == null) {
                                Toast.makeText(ContactCandidatActivity.this, "Erreur lors de la récupération des informations du candidat", Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(ContactCandidatActivity.this, "Erreur de la base de données: " + error.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });

                    // Récupérer les informations de l'offre
                    databaseReference.child("Offres").child(offreId).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            offre = snapshot.getValue(Offre.class);
                            if (offre != null) {
                                String employeurId = offre.getEmployeur_id();

                                // Récupérer les informations de l'employeur
                                databaseReference.child("Employeurs").child(employeurId).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        employeur = snapshot.getValue(Employeur.class);
                                        if (employeur == null) {
                                            Toast.makeText(ContactCandidatActivity.this, "Erreur lors de la récupération des informations de l'employeur", Toast.LENGTH_LONG).show();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        Toast.makeText(ContactCandidatActivity.this, "Erreur de la base de données: " + error.getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                });
                            } else {
                                Toast.makeText(ContactCandidatActivity.this, "Erreur lors de la récupération de l'offre", Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(ContactCandidatActivity.this, "Erreur de la base de données: " + error.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                } else {
                    Toast.makeText(ContactCandidatActivity.this, "Erreur lors de la récupération de la candidature", Toast.LENGTH_LONG).show();
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ContactCandidatActivity.this, "Erreur de la base de données: " + error.getMessage(), Toast.LENGTH_LONG).show();
                finish();
            }
        });
    }
}
