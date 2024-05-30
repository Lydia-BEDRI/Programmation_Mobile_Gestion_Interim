package com.example.linterim.Activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.FileProvider;

import com.example.linterim.Helpers.MenuEmployeurManager;
import com.example.linterim.Models.Candidature;
import com.example.linterim.Models.Employeur;
import com.example.linterim.Models.Message;
import com.example.linterim.Models.Offre;
import com.example.linterim.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import firebase.com.protolitewrapper.BuildConfig;

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
        setContentView(com.example.linterim.R.layout.details_candidature_employeur);

        View rootView = findViewById(android.R.id.content);
        MenuEmployeurManager.setupMenuItems(rootView, this);

        ImageView backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

        CLaccepter = findViewById(R.id.CLaccepter);
        CLrefuser = findViewById(R.id.CLrefuser);
        CLrepondre = findViewById(R.id.CLrepondre);

        Intent intent = getIntent();
        if (intent != null) {
            textViewTitreOffre.setText("Offre : " + intent.getStringExtra("offre_titre"));
            textViewDateOffre.setText("Publiée le " + intent.getStringExtra("offre_date"));
            textViewNomPrenomCand.setText("Candidat : " + intent.getStringExtra("candidat_nom_prenom"));
            textViewDateCandidature.setText("Candidaté le " + intent.getStringExtra("date_candidature"));
            textViewDDNaissance.setText("Date de naissance : " + intent.getStringExtra("candidat_date_naissance"));
            textViewNationalite.setText("Nationalité : " + intent.getStringExtra("candidat_nationalite"));
            textViewVoirCV.setText("Voir le CV");
            textViewVoirLettreM.setText("Voir la Lettre de Motivation");

            String cvUrl = intent.getStringExtra("candidat_cv");
            String lettreMUrl = intent.getStringExtra("candidat_lettre_motivation");

            textViewVoirCV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    downloadAndOpenPDF(cvUrl);
                }
            });

            textViewVoirLettreM.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    downloadAndOpenPDF(lettreMUrl);
                }
            });
        }

        CLrepondre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                changeCandidatureStatus(getIntent().getStringExtra("candidature_id"), "acceptée");
            }
        });

        CLrefuser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeCandidatureStatus(getIntent().getStringExtra("candidature_id"), "refusée");
            }
        });
    }

    private void downloadAndOpenPDF(String gsUrl) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl(gsUrl);

        try {
            File localFile = File.createTempFile("tempfile", ".pdf");

            storageRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Uri path = FileProvider.getUriForFile(VoirCandidatureEmpActivity.this, BuildConfig.APPLICATION_ID + ".fileprovider", localFile);

                    Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
                    pdfIntent.setDataAndType(path, "application/pdf");
                    pdfIntent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    pdfIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                    try {
                        startActivity(pdfIntent);
                    } catch (ActivityNotFoundException e) {
                        Toast.makeText(VoirCandidatureEmpActivity.this, "No application to view PDF", Toast.LENGTH_SHORT).show();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    Toast.makeText(VoirCandidatureEmpActivity.this, "Failed to download file", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (IOException e) {
            Toast.makeText(this, "Failed to create temporary file", Toast.LENGTH_SHORT).show();
        }
    }


    private void changeCandidatureStatus(String candidatureId, String newStatus) {
        DatabaseReference candidatureRef = FirebaseDatabase.getInstance().getReference().child("Candidatures").child(candidatureId);
        candidatureRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Candidature candidature = dataSnapshot.getValue(Candidature.class);
                    if (candidature != null) {
                        DatabaseReference offreRef = FirebaseDatabase.getInstance().getReference().child("Offres").child(candidature.getOffre_id());
                        String employeurId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        offreRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    Offre offre = dataSnapshot.getValue(Offre.class);
                                    if (offre != null) {
                                        DatabaseReference employeurRef = FirebaseDatabase.getInstance().getReference().child("Employeurs").child(employeurId);
                                        employeurRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                if (dataSnapshot.exists()) {
                                                    Employeur employeur = dataSnapshot.getValue(Employeur.class);
                                                    if (employeur != null) {
                                                        new AlertDialog.Builder(VoirCandidatureEmpActivity.this)
                                                                .setTitle("Confirmation")
                                                                .setMessage("Voulez-vous changer le statut de cette candidature ?")
                                                                .setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                                                                    @Override
                                                                    public void onClick(DialogInterface dialog, int which) {
                                                                        candidature.setStatut(newStatus);
                                                                        candidatureRef.setValue(candidature);
                                                                        sendNotificationToCandidate(candidature.getCandidat_id(), employeur.getNom_entreprise(), offre.getTitre(), candidature.getOffre_id(), newStatus.equals("acceptée"));
                                                                        finish();
                                                                    }
                                                                })
                                                                .setNegativeButton("Non", null)
                                                                .show();
                                                    }
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {}
                                        });
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {}
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });
    }

    private void sendNotificationToCandidate(String candidatId, String nomEntreprise, String titrePoste, String offreId, boolean accepted) {
        String status = accepted ? "acceptée" : "refusée";
        String content = "L'employeur " + nomEntreprise + " a " + status + " votre candidature pour le poste " + titrePoste;
        String employeurId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String currentDate = getCurrentFormattedDate();
        Message message = new Message("notification", employeurId, candidatId, content, offreId, currentDate);
        DatabaseReference messagesRef = FirebaseDatabase.getInstance().getReference().child("Messages");
        String messageId = messagesRef.push().getKey();
        if (messageId != null) {
            messagesRef.child(messageId).setValue(message);
        }
    }

    private String getCurrentFormattedDate() {
        Date currentDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        return dateFormat.format(currentDate);
    }
}
