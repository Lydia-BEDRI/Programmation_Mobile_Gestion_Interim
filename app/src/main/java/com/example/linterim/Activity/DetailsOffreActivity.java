package com.example.linterim.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.linterim.Helpers.MenuCandidatManager;
import com.example.linterim.Models.Offre;
import com.example.linterim.R;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DetailsOffreActivity extends AppCompatActivity {
    private TextView textViewDescriptionOffreD;
    private TextView textViewLieuOffreD;
    private TextView textViewRemunerationOffreD;
    private TextView textViewTypeContratOffreD;
    private TextView textViewPeriodeOffreD;
    private TextView textViewMissionsPOffreD;
    private TextView textViewProfilRechOffreD;
    private TextView textViewConditionsTravailOffreD;
   private TextView textViewTitreOffreD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_offre);

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

        // Récupérer les vues
        textViewTitreOffreD = findViewById(R.id.textViewTitreOffreD);
        textViewDescriptionOffreD = findViewById(R.id.textViewDescriptionOffreD);
        textViewLieuOffreD = findViewById(R.id.textViewLieuOffreD);
        textViewRemunerationOffreD = findViewById(R.id.textViewRemunerationOffreD);
        textViewTypeContratOffreD = findViewById(R.id.textViewTypeContratOffreD);
        textViewPeriodeOffreD = findViewById(R.id.textViewPeriodeOffreD);
        textViewMissionsPOffreD = findViewById(R.id.textViewMissionsPOffreD);
        textViewProfilRechOffreD = findViewById(R.id.textViewProfilRechOffreD);
        textViewConditionsTravailOffreD = findViewById(R.id.textViewConditionsTravailOffreD);

        // Extraire l'ID de l'offre de l'intent
        if (getIntent() != null && getIntent().hasExtra("offreId")) {
            String offreId = getIntent().getStringExtra("offreId");
            Log.d("----Details------",offreId);

            // Utiliser l'ID pour afficher les détails de l'offre correspondante
            afficherDetailsOffre(offreId);
        }
        if(getIntent() != null && getIntent().hasExtra("Anonyme")){
            BottomAppBar bottomAppBar = findViewById(R.id.bottomAppBar);
            bottomAppBar.setVisibility(View.GONE);
        }

        Button postulerMaintenantBtn = findViewById(R.id.button_postuler_maintenant);
        postulerMaintenantBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isUserAuthenticated()) {
                    // L'utilisateur est authentifié, lancer PostulerOffreActivity
                    Intent intent = new Intent(DetailsOffreActivity.this, PostulerOffreActivity.class);
                    String offreId = getIntent().getStringExtra("offreId");
                    intent.putExtra("offreId", offreId);
                    startActivity(intent);
                } else {
                    // Afficher un toast demandant à l'utilisateur de s'inscrire ou de s'authentifier
                    Toast.makeText(DetailsOffreActivity.this, "Veuillez vous connecter ou vous inscrire pour postuler.", Toast.LENGTH_SHORT).show();

                    // Redirection vers MainActivity pour s'authentifier ou s'inscrire
                    Intent intent = new Intent(DetailsOffreActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }
        });

    }

    // Méthode pour afficher les détails de l'offre correspondante
    private void afficherDetailsOffre(String offreId) {
        // Vous devez récupérer les détails de l'offre à partir de votre source de données (base de données, API, etc.)
        // et ensuite mettre à jour les TextView avec ces détails

        // Par exemple, vous pouvez utiliser Firebase Realtime Database pour récupérer les détails de l'offre
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Offres").child(offreId);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Récupérer l'objet Offre à partir du snapshot
                Offre offre = dataSnapshot.getValue(Offre.class);

                // Mettre à jour les TextView avec les détails de l'offre
                if (offre != null) {
                    textViewTitreOffreD.setText(offre.getTitre());
                    textViewDescriptionOffreD.setText(offre.getDescription());
                    textViewLieuOffreD.setText(offre.getLieu());
                    textViewRemunerationOffreD.setText(offre.getRemuneration());
                    textViewTypeContratOffreD.setText(offre.getType_contract());
                    textViewPeriodeOffreD.setText(offre.getPeriode());
                    textViewMissionsPOffreD.setText(offre.getMissions_principales());
                    textViewProfilRechOffreD.setText(offre.getProfil_recherche());
                    textViewConditionsTravailOffreD.setText("Type de contract : "+offre.getType_contract()+
                                                            "\nLieu de travail : "+offre.getLieu()+
                                                            "\nSalaire : "+offre.getRemuneration());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Gestion des erreurs de récupération de données
                Toast.makeText(DetailsOffreActivity.this, "Une erreur s'est produite lors de la récupération des données ! ", Toast.LENGTH_LONG).show();
            }
        });
    }


    // Méthode pour vérifier si l'utilisateur est authentifié avec Firebase Authentication
    private boolean isUserAuthenticated() {
        // Récupérer l'instance de FirebaseAuth
        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        // Récupérer l'utilisateur actuellement connecté
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            // Utilisateur actuellement connecté
            Log.d("Auth", "Utilisateur connecté: " + currentUser.getUid());
            return true;
        } else {
            // Aucun utilisateur connecté
            Log.d("Auth", "Aucun utilisateur connecté");
            return false;
        }
    }



}
