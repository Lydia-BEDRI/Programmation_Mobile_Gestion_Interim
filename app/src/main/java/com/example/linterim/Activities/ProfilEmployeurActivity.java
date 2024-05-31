package com.example.linterim.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.linterim.Models.Employeur;
import com.example.linterim.Helpers.MenuEmployeurManager;
import com.example.linterim.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfilEmployeurActivity extends AppCompatActivity {
    private EditText editTextNomEntreprise;
    private EditText editTextAdresseMailEntreprise;
    private EditText editTextAdresseEntreprise;
    private EditText editTextNumTelEntreprise;
    private EditText editTextSiteWebEntreprise;
    private EditText editTextLinkedInEntreprise;

    private ConstraintLayout logOut,Msg;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profil_employeur);
        View rootView = findViewById(android.R.id.content);
        MenuEmployeurManager.setupMenuItems(rootView,this);

        // Récupérer les références des EditText à partir de la mise en page XML
        editTextNomEntreprise = findViewById(R.id.editTextNomEntreprise);
        editTextAdresseMailEntreprise = findViewById(R.id.editTextAdresseMailEntreprise);
        editTextAdresseEntreprise = findViewById(R.id.editTextAdresseEntreprise);
        editTextNumTelEntreprise = findViewById(R.id.editTextNumTelEntreprise);
        editTextSiteWebEntreprise = findViewById(R.id.editTextSiteWebEntreprise);
        editTextLinkedInEntreprise = findViewById(R.id.editTextLinkedInEntreprise);

        Msg = findViewById(R.id.Msg);
        Msg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

                String userId = currentUser.getUid();

                Intent intent = new Intent(ProfilEmployeurActivity.this, MessagesEmpActivity.class);
                intent.putExtra("userId", userId);
                startActivity(intent);
                finish();
            }
        });

        logOut = findViewById(R.id.buttonLogout);
        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(ProfilEmployeurActivity.this, "Déconnexion avec succès", Toast.LENGTH_SHORT).show();

                // Utiliser un Handler pour introduire un délai avant de passer à l'activité suivante
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(ProfilEmployeurActivity.this, LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
                        // Ajouter les flags pour effacer la tâche précédente et démarrer une nouvelle tâche
                        finish(); // Optionnel : fermer l'activité actuelle
                    }
                }, 3000); // Délai en millisecondes (ici, 3000 ms = 3 secondes)
            }
        });

        // Récupérer l'utilisateur actuellement connecté
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            // Récupérer l'ID de l'utilisateur
            String userId = currentUser.getUid();

            // Référence à la table "Employeurs" dans la base de données Firebase
            DatabaseReference candidatRef = FirebaseDatabase.getInstance().getReference().child("Employeurs").child(userId);
            candidatRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        // Récupérer les données de l'employeur
                        Employeur employeur = dataSnapshot.getValue(Employeur.class);
                        if (employeur != null) {
                            // Mettre à jour les champs de l'interface utilisateur avec les informations de l'employeur
                            editTextNomEntreprise.setText(employeur.getNom_entreprise());
                            editTextAdresseEntreprise.setText(employeur.getAdresse());
                            editTextAdresseMailEntreprise.setText(employeur.getEmail());
                            editTextNumTelEntreprise.setText(employeur.getTelephone());
                            editTextSiteWebEntreprise.setText(employeur.getSite_web());
                            editTextLinkedInEntreprise.setText(employeur.getLinkedin_url());

                        }
                    } else {
                        // Aucune donnée trouvée pour ce candidat
                        Toast.makeText(ProfilEmployeurActivity.this, "Aucun employeur trouvé", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Gestion des erreurs de base de données
                    Toast.makeText(ProfilEmployeurActivity.this, "Erreur de chargement du profil", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
