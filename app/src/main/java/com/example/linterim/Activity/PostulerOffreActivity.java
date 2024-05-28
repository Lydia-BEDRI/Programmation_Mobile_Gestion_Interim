package com.example.linterim.Activity;

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

import com.example.linterim.Models.Candidat;
import com.example.linterim.Helpers.MenuCandidatManager;
import com.example.linterim.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class PostulerOffreActivity extends AppCompatActivity {
    private EditText nom, prenom, dateNaissace, nationalite;
    private LinearLayout cv, lettreMotivation;
    private TextView infoCV, infoLettreMotiv;

    private StorageReference storageReferenceCV, storageReferenceLettreM;

    private Uri selectedCVURI,selectedLettreMURI;

    private ActivityResultLauncher<Intent> resultLauncher;
    private static final int PICK_PDF_REQUEST = 1;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.candidate);

        View rootView = findViewById(android.R.id.content);
        MenuCandidatManager.setupMenuItems(rootView,this);

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
        prenom= findViewById(R.id.editTextPrenomCandidature);
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
                            String urlTronquee = urlCv.substring(0, longueurMaxAffichee) + "...";
                            infoCV.setText(urlTronquee);

                            // gérer la modif du CV
                            cv.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                   storageReferenceCV =  uploadCV();
                                   candidat.setCvUrl(storageReferenceCV.toString());
                                    String urlCv = candidat.getCvUrl();
                                    String urlTronquee = urlCv.substring(0, longueurMaxAffichee) + "...";
                                   infoCV.setText(urlTronquee);
                                }
                            });



///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////:::
                            // gérer l'ajout de la lettre de motiv
                            lettreMotivation.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    storageReferenceLettreM =  uploadCV();

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

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==PICK_PDF_REQUEST && resultCode == RESULT_OK){
            selectedCVURI = data.getData();
            openFilePicker();
        }
    }

    private void openFilePicker() {
        Intent intent = new Intent();
        intent.setType("application/pdf");
      //  intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        resultLauncher.launch(Intent.createChooser(intent, "Select PDF"));    }
    private StorageReference uploadCV() {
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Uploading");
        progressDialog.show();

        if (selectedCVURI != null) {
            storageReferenceCV = FirebaseStorage.getInstance().getReference().child("CVs").child(String.valueOf(System.currentTimeMillis()) + ".pdf");

            // Mettre en œuvre le téléversement du fichier
            storageReferenceCV.putFile(selectedCVURI)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            // Si le téléversement est réussi, récupérer l'URL de téléchargement
                            storageReferenceCV.getDownloadUrl().addOnSuccessListener(uri -> {
                                String url = uri.toString();
                                Log.d("DownloadURL", url);
                                progressDialog.dismiss();
                                Toast.makeText(getApplicationContext(), "Fichier téléversé avec succès", Toast.LENGTH_SHORT).show();
                            }).addOnFailureListener(e -> {
                                // En cas d'échec de récupération de l'URL de téléchargement
                                progressDialog.dismiss();
                                Toast.makeText(getApplicationContext(), "Erreur lors de la récupération de l'URL de téléchargement", Toast.LENGTH_SHORT).show();
                            });
                        } else {
                            // En cas d'échec du téléversement du fichier
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "Erreur lors du téléversement du fichier", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            // Si aucun fichier n'a été sélectionné
            progressDialog.dismiss();
            Toast.makeText(getApplicationContext(), "Aucun fichier sélectionné", Toast.LENGTH_SHORT).show();
        }

        // Retourner la référence de stockage
        return storageReferenceCV;
    }

}


