package com.example.linterim.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.linterim.Helper.MenuCandidatManager;
import com.example.linterim.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.linterim.Helper.Candidat;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class ProfilCandidatActivity extends AppCompatActivity {
    private EditText nomEditText, prenomEditText, mailEditText,dateNaissaceEditText,villeEditText;
    private ConstraintLayout logOut;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profil_candidat);

        View rootView = findViewById(android.R.id.content);
        MenuCandidatManager.setupMenuItems(rootView,this);

        logOut = findViewById(R.id.buttonLogout);
        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(ProfilCandidatActivity.this, "Deconnexion avec succès", Toast.LENGTH_SHORT).show();

                // Utiliser un Handler pour introduire un délai avant de passer à l'activité suivante
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(ProfilCandidatActivity.this, LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
                        finish(); // Optionnel : fermer l'activité actuelle
                    }
                }, 3000); // Délai en millisecondes (ici, 3000 ms = 3 secondes)
            }
        });
        nomEditText = findViewById(R.id.editTextNomCandidat);
        prenomEditText = findViewById(R.id.editTextPrenomCandidat);
        mailEditText = findViewById(R.id.editTextMailCandidat);
        dateNaissaceEditText = findViewById(R.id.editTextDateNaissanceCandidat);
        villeEditText = findViewById(R.id.editTextVilleCandidat);

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
                            nomEditText.setText(candidat.getNom());
                            prenomEditText.setText(candidat.getPrenom());
                            mailEditText.setText(candidat.getEmail());
                            dateNaissaceEditText.setText(candidat.getDateNaissance());
                            villeEditText.setText(candidat.getVille());
                        }
                    } else {
                        // Aucune donnée trouvée pour ce candidat
                        Toast.makeText(ProfilCandidatActivity.this, "Aucun candidat trouvé", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Gestion des erreurs de base de données
                    Toast.makeText(ProfilCandidatActivity.this, "Erreur de chargement du profil", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
