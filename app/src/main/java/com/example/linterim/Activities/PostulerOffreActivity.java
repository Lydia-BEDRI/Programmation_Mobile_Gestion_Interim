package com.example.linterim.Activities;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.example.linterim.Models.Candidat;
import com.example.linterim.Helpers.MenuCandidatManager;
import com.example.linterim.Models.Candidature;
import com.example.linterim.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class PostulerOffreActivity extends AppCompatActivity {
    private EditText nom, prenom, dateNaissace, nationalite;
    private LinearLayout cv, lettreMotivation;
    private TextView infoCV, infoLettreMotiv;

    private StorageReference storageReferenceCV, storageReferenceLettreM;

    private Uri selectedCVURI, selectedLettreMURI;
    private String lettreMotivationUrl;
    private String cvUrl;

    private ActivityResultLauncher<Intent> resultLauncherCV;
    private ActivityResultLauncher<Intent> resultLauncherLettreM;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.candidate);

        View rootView = findViewById(android.R.id.content);
        MenuCandidatManager.setupMenuItems(rootView, this);

        // gerer le back button
        ImageView backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Fermer l'activité actuelle pour retourner à l'activité précédente
                finish();
            }
        });

        nom = findViewById(R.id.editTextNomCandidature);
        prenom = findViewById(R.id.editTextPrenomCandidature);
        dateNaissace = findViewById(R.id.editTextDDNCandidature);
        nationalite = findViewById(R.id.editTextNationaliteCandidature);
        cv = findViewById(R.id.CVCandidature);
        lettreMotivation = findViewById(R.id.LettreMotCandidature);
        infoCV = findViewById(R.id.textview_infoCV);
        infoLettreMotiv = findViewById(R.id.textview_infoLettreMotiv);

        // Récupérer l'utilisateur actuellement connecté
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            // Récupérer l'ID de l'utilisateur
            String userId = currentUser.getUid();

            // Référence à la table "Candidats" dans la base de données Firebase
            DatabaseReference candidatRef = FirebaseDatabase.getInstance().getReference().child("Candidats").child(userId);
            candidatRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        // Récupérer les données du candidat
                        Candidat candidat = dataSnapshot.getValue(Candidat.class);
                        if (candidat != null) {
                            // Mettre à jour les champs de l'interface utilisateur avec les informations du candidat
                            nom.setText(candidat.getNom());
                            prenom.setText(candidat.getPrenom());
                            dateNaissace.setText(candidat.getDateNaissance());
                            nationalite.setText(candidat.getNationalite());
                            String urlCv = candidat.getCvUrl();
                            int longueurMaxAffichee = 10; // par exemple, vous pouvez spécifier 10 caractères max à afficher
                            String urlTronquee = urlCv != null ? urlCv.substring(0, Math.min(urlCv.length(), longueurMaxAffichee)) + "..." : "";
                            infoCV.setText(urlTronquee);

                            // gérer la modif du CV
                            cv.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    openFilePicker(resultLauncherCV);
                                }
                            });

                            // gérer l'ajout de la lettre de motiv
                            lettreMotivation.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    openFilePicker(resultLauncherLettreM);
                                }
                            });
                        }
                    } else {
                        // Aucune donnée trouvée pour ce candidat
                        Toast.makeText(PostulerOffreActivity.this, "Aucun candidat trouvé", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Gestion des erreurs de base de données
                    Toast.makeText(PostulerOffreActivity.this, "Erreur de chargement du profil", Toast.LENGTH_SHORT).show();
                }
            });
        }

        resultLauncherCV = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        selectedCVURI = result.getData().getData();
                        uploadFile(selectedCVURI, "CVs", infoCV);
                    }
                }
        );

        resultLauncherLettreM = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        selectedLettreMURI = result.getData().getData();
                        uploadFile(selectedLettreMURI, "Letters", infoLettreMotiv);
                    }
                }
        );

        AppCompatButton candidaterMaintenantButton = findViewById(R.id.button_candidaterMaintenant);
        candidaterMaintenantButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Récupérer les valeurs des champs de saisie
                String updatedNom = nom.getText().toString().trim();
                String updatedPrenom = prenom.getText().toString().trim();
                String updatedDateNaissance = dateNaissace.getText().toString().trim();
                String updatedNationalite = nationalite.getText().toString().trim();

                // Mettre à jour les informations du candidat dans Firebase
                updateCandidatInfo(updatedNom, updatedPrenom, updatedDateNaissance, updatedNationalite);
            }
        });
    }

    private void openFilePicker(ActivityResultLauncher<Intent> resultLauncher) {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("application/pdf");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        resultLauncher.launch(Intent.createChooser(intent, "Select PDF"));
    }



    private void uploadFile(Uri fileUri, String folder, TextView infoTextView) {
        if (fileUri != null) {
            ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Uploading");
            progressDialog.show();

            StorageReference storageReference = FirebaseStorage.getInstance().getReference().child(folder).child(System.currentTimeMillis() + ".pdf");
            storageReference.putFile(fileUri)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            storageReference.getDownloadUrl().addOnSuccessListener(uri -> {
                                String url = uri.toString();
                                Log.d("DownloadURL", url);
                                progressDialog.dismiss();
                                Toast.makeText(getApplicationContext(), "Fichier téléversé avec succès", Toast.LENGTH_SHORT).show();
                                String urlTronquee = url.substring(0, Math.min(url.length(), 10)) + "...";
                                infoTextView.setText(urlTronquee);

                                // Enregistrer l'URL Firebase Storage du CV
                                if (folder.equals("CVs")) {
                                    cvUrl = url;
                                } else if (folder.equals("Letters")) {
                                    lettreMotivationUrl = url;
                                }
                            }).addOnFailureListener(e -> {
                                progressDialog.dismiss();
                                Toast.makeText(getApplicationContext(), "Erreur lors de la récupération de l'URL de téléchargement", Toast.LENGTH_SHORT).show();
                            });
                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "Erreur lors du téléversement du fichier", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Toast.makeText(getApplicationContext(), "Aucun fichier sélectionné", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateCandidatInfo(String nom, String prenom, String dateNaissance, String nationalite) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            DatabaseReference candidatRef = FirebaseDatabase.getInstance().getReference().child("Candidats").child(userId);

            candidatRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        Candidat candidat = dataSnapshot.getValue(Candidat.class);
                        if (candidat != null) {
                            candidat.setNom(nom);
                            candidat.setPrenom(prenom);

                            // Formater la date de naissance au format jj/mm/aaaa
                            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                            try {
                                Date formattedDate = dateFormat.parse(dateNaissance);
                                candidat.setDateNaissance(dateFormat.format(formattedDate));
                            } catch (ParseException e) {
                                e.printStackTrace();
                                // En cas d'erreur de formatage, utiliser la date d'origine
                                candidat.setDateNaissance(dateNaissance);
                            }

                            candidat.setNationalite(nationalite);

                            // Ajouter l'URL du CV s'il existe
                            if (cvUrl != null) {
                                candidat.setCvUrl(cvUrl);
                            }

                            candidatRef.setValue(candidat).addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    // Les informations du candidat ont été mises à jour avec succès
                                    Toast.makeText(PostulerOffreActivity.this, "Informations personnelles du profil sont mises à jour !", Toast.LENGTH_SHORT).show();
                                    // Créer une nouvelle candidature
                                    createCandidature(userId);
                                } else {
                                    // Une erreur s'est produite lors de la mise à jour des informations
                                    Toast.makeText(PostulerOffreActivity.this, "Erreur de mise à jour des informations", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    } else {
                        Toast.makeText(PostulerOffreActivity.this, "Erreur lors de la récupération des informations du candidat", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(PostulerOffreActivity.this, "Erreur de mise à jour des informations", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }


    private void createCandidature(String candidatId) {
        String offreId = getIntent().getStringExtra("offreId");
        if (offreId == null) {
            Toast.makeText(this, "ID de l'offre manquant", Toast.LENGTH_LONG).show();
            return;
        }

        DatabaseReference candidaturesRef = FirebaseDatabase.getInstance().getReference().child("Candidatures");
        String candidatureId = candidaturesRef.push().getKey();

        // Formater la date actuelle au format jj/mm/aaaa
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String currentDate = dateFormat.format(new Date(System.currentTimeMillis()));

        Candidature nouvelleCandidature = new Candidature();
        nouvelleCandidature.setCandidature_id(candidatureId);
        nouvelleCandidature.setOffre_id(offreId);
        nouvelleCandidature.setCandidat_id(candidatId);
        nouvelleCandidature.setLettre_motivation(lettreMotivationUrl != null ? lettreMotivationUrl : "");
        nouvelleCandidature.setStatut("en attente");
        nouvelleCandidature.setDate_candidature(currentDate);

        candidaturesRef.child(candidatureId).setValue(nouvelleCandidature).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // La candidature a été créée avec succès
                Toast.makeText(PostulerOffreActivity.this, "Candidature envoyée avec succès", Toast.LENGTH_SHORT).show();
                // Redirection vers GestionCandidaturesCandidatActivity
                Intent intent = new Intent(PostulerOffreActivity.this, GestionCandidaturesCandidatActivity.class);
                startActivity(intent);
                finish(); // Optionnel : fermez cette activité pour empêcher l'utilisateur de revenir en arrière
            } else {
                // Une erreur s'est produite lors de la création de la candidature
                Toast.makeText(PostulerOffreActivity.this, "Erreur lors de l'envoi de la candidature", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
