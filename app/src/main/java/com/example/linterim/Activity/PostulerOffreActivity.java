package com.example.linterim.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.linterim.Helper.Candidat;
import com.example.linterim.Helper.MenuCandidatManager;
import com.example.linterim.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PostulerOffreActivity extends AppCompatActivity {
    private EditText nom, prenom, dateNaissace, nationalite;
    LinearLayout cv, lettreMotivation;
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
}
