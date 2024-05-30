package com.example.linterim.Activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.linterim.Helpers.MenuEmployeurManager;
import com.example.linterim.Models.Candidature;
import com.example.linterim.Models.Employeur;
import com.example.linterim.Models.Message;
import com.example.linterim.Models.Offre;
import com.example.linterim.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class VoirCandidatureEmpActivity extends AppCompatActivity {
    private TextView textViewTitreOffre;
    private TextView textViewDateOffre;
    private TextView textViewNomPrenomCand;
    private TextView textViewDateCandidature;
    private TextView textViewDDNaissance;
    private TextView textViewNationalite;
    private TextView textViewVoirCV;
    private TextView textViewVoirLettreM;
    private ConstraintLayout CLaccepter, CLrefuser, CLrepondre;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.details_candidature_employeur);

        View rootView = findViewById(android.R.id.content);
        MenuEmployeurManager.setupMenuItems(rootView, this);

        // Handle the back button
        ImageView backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Close the current activity to return to the previous activity
                finish();
            }
        });

        textViewTitreOffre = findViewById(R.id.textViewTitreOffre);
        textViewDateOffre = findViewById(R.id.textViewDateOffre);
        textViewNomPrenomCand = findViewById(R.id.textViewNomPrenomCand);
        textViewDateCandidature = findViewById(R.id.textViewDateCandidature);
        textViewDDNaissance = findViewById(R.id.textViewDDNaissance);
        textViewNationalite = findViewById(R.id.textViewNationalite);
        textViewVoirCV = findViewById(R.id.textViewVoirCV);
        textViewVoirLettreM = findViewById(R.id.textViewVoirLettreM);

        CLaccepter = findViewById(R.id.CLaccepter); // changer le status de la candidature a acceptée
        CLrefuser = findViewById(R.id.CLrefuser); // changer le status de la candidature a refusée
        CLrepondre = findViewById(R.id.CLrepondre); // lancer ContactCandActivity en lui passant Nom et Prenom cand, plus les infos necessaires au msg

        // Retrieve data from intent
        Intent intent = getIntent();
        if (intent != null) {
            textViewTitreOffre.setText("Offre : " + intent.getStringExtra("offre_titre"));
            textViewDateOffre.setText("Publiée le " + intent.getStringExtra("offre_date"));
            textViewNomPrenomCand.setText("Candidat : " + intent.getStringExtra("candidat_nom_prenom"));
            textViewDateCandidature.setText("Candidaté le " + intent.getStringExtra("date_candidature"));
            textViewDDNaissance.setText("Date de naissance : " + intent.getStringExtra("candidat_date_naissance"));
            textViewNationalite.setText("Nationalité : " + intent.getStringExtra("candidat_nationalite"));
            textViewVoirCV.setText("Voir le CV " + intent.getStringExtra("candidat_cv"));
            textViewVoirLettreM.setText("Voir la Lettre de Motivation " + intent.getStringExtra("candidat_lettre_motivation"));
        }

        // Set click listeners for the ConstraintLayouts




        CLrepondre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lancer ContactCandActivity en lui passant Nom et Prenom cand, plus les infos nécessaires au msg
                Intent contactIntent = new Intent(VoirCandidatureEmpActivity.this, ContactCandActivity.class);
                contactIntent.putExtra("candidat_nom_prenom", intent.getStringExtra("candidat_nom_prenom"));
                contactIntent.putExtra("candidat_id", intent.getStringExtra("candidat_id"));
                contactIntent.putExtra("offre_id", intent.getStringExtra("offre_id"));
                startActivity(contactIntent);
            }
        });


    CLaccepter.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // Changer le statut de la candidature à acceptée
            changeCandidatureStatus(getIntent().getStringExtra("candidature_id"), "acceptée");
        }
    });

CLrefuser.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // Changer le statut de la candidature à refusée
            changeCandidatureStatus(getIntent().getStringExtra("candidature_id"), "refusée");
        }
    });
}
    private void changeCandidatureStatus(String candidatureId, String newStatus) {
        // Référence à la candidature dans la base de données
        DatabaseReference candidatureRef = FirebaseDatabase.getInstance().getReference().child("Candidatures").child(candidatureId);

        // Récupérer la candidature existante
        candidatureRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Récupérer la candidature
                    Candidature candidature = dataSnapshot.getValue(Candidature.class);
                    if (candidature != null) {
                        // Récupérer les informations sur l'offre et l'employeur
                        DatabaseReference offreRef = FirebaseDatabase.getInstance().getReference().child("Offres").child(candidature.getOffre_id());
                        String employeurId = FirebaseAuth.getInstance().getCurrentUser().getUid();                        offreRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    Offre offre = dataSnapshot.getValue(Offre.class);
                                    if (offre != null) {
                                        // Récupérer les informations sur l'offre et l'employeur
                                        DatabaseReference offreRef = FirebaseDatabase.getInstance().getReference().child("Offres").child(candidature.getOffre_id());
                                        String employeurId = FirebaseAuth.getInstance().getCurrentUser().getUid();
// Récupérer les informations sur l'employeur
                                        DatabaseReference employeurRef = FirebaseDatabase.getInstance().getReference().child("Employeurs").child(employeurId);

                                        // Récupérer les informations sur l'employeur
                                        employeurRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                if (dataSnapshot.exists()) {
                                                    Employeur employeur = dataSnapshot.getValue(Employeur.class);
                                                    if (employeur != null) {
                                                        // Afficher une boîte de dialogue de confirmation
                                                        new AlertDialog.Builder(VoirCandidatureEmpActivity.this)
                                                                .setTitle("Confirmation")
                                                                .setMessage("Voulez-vous changer le statut de cette candidature ?")
                                                                .setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                                                                    @Override
                                                                    public void onClick(DialogInterface dialog, int which) {
                                                                        // Mettre à jour le statut de la candidature
                                                                        candidature.setStatut(newStatus);

                                                                        // Enregistrer les modifications dans la base de données
                                                                        candidatureRef.setValue(candidature);

                                                                        // Envoyer une notification au candidat
                                                                        sendNotificationToCandidate(candidature.getCandidat_id(), employeur.getNom_entreprise(), offre.getTitre(), candidature.getOffre_id(), newStatus.equals("acceptée"));

                                                                        // Revenir à l'activité précédente
                                                                        finish();
                                                                    }
                                                                })
                                                                .setNegativeButton("Non", null)
                                                                .show();
                                                    }
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                                // Gérer les erreurs
                                            }
                                        });
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                // Gérer les erreurs
                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Gérer les erreurs
            }
        });
    }




    private void sendNotificationToCandidate(String candidatId, String nomEntreprise, String titrePoste, String offreId, boolean accepted) {
        // Construction du contenu du message en fonction du statut
        String status = accepted ? "acceptée" : "refusée";
        String content = "L'employeur " + nomEntreprise + " a " + status + " votre candidature pour le poste " + titrePoste;

        // Obtenez l'ID de l'employeur actuel (vous pouvez le récupérer de l'authentification Firebase ou de toute autre source)
        String employeurId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // Obtenez la date actuelle
        String currentDate = getCurrentFormattedDate();

        // Créez un nouvel objet Message
        Message message = new Message("notification", employeurId, candidatId, content, offreId, currentDate);

        // Enregistrez le message dans la base de données Firebase
        DatabaseReference messagesRef = FirebaseDatabase.getInstance().getReference().child("Messages");
        String messageId = messagesRef.push().getKey();
        if (messageId != null) {
            messagesRef.child(messageId).setValue(message);
        }
    }





    private String getCurrentFormattedDate() {
        // Obtenir la date actuelle
        Date currentDate = new Date();

        // Formater la date
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        return dateFormat.format(currentDate);
    }



}
