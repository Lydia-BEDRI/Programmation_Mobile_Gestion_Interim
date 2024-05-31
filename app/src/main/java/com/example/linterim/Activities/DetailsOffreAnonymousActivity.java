package com.example.linterim.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.linterim.Models.Offre;
import com.example.linterim.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DetailsOffreAnonymousActivity extends AppCompatActivity {
    private TextView textViewDescriptionOffreD;
    private TextView textViewLieuOffreD;
    private TextView textViewRemunerationOffreD;
    private TextView textViewTypeContratOffreD;
    private TextView textViewPeriodeOffreD;
    private TextView textViewMissionsPOffreD;
    private TextView textViewProfilRechOffreD;
    private TextView textViewConditionsTravailOffreD;
    private TextView textViewTitreOffreD;

    private TextView textViewDatePublication;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_offre_anonyme);

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
        textViewDatePublication = findViewById(R.id.textViewDatePublication);


        if (getIntent() != null && getIntent().hasExtra("offreId")) {
            String offreId = getIntent().getStringExtra("offreId");
            Log.d("----Details------",offreId);

            // Utiliser l'ID pour afficher les détails de l'offre correspondante
            afficherDetailsOffre(offreId);
        }



        Button postulerMaintenantBtn = findViewById(R.id.button_postuler_maintenant);
        postulerMaintenantBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    // Afficher un toast demandant à l'utilisateur de s'inscrire ou de s'authentifier
                    Toast.makeText(DetailsOffreAnonymousActivity.this, "Veuillez vous connecter ou vous inscrire pour postuler.", Toast.LENGTH_SHORT).show();

                    // Redirection vers MainActivity pour s'authentifier ou s'inscrire
                    Intent intent = new Intent(DetailsOffreAnonymousActivity.this, MainActivity.class);
                    startActivity(intent);

            }
        });

    }
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
                    textViewDatePublication.setText("Publiée le " + offre.getDate_publication());
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
                Toast.makeText(DetailsOffreAnonymousActivity.this, "Une erreur s'est produite lors de la récupération des données ! ", Toast.LENGTH_LONG).show();
            }
        });
    }
}
