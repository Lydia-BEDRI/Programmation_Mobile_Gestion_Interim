package com.example.linterim.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.linterim.Helpers.MenuCandidatManager;
import com.example.linterim.R;

public class VoirCandidatureUserActivity extends AppCompatActivity {
    private TextView textViewDescriptionOffreD;
    private TextView textViewLieuOffreD;
    private TextView textViewRemunerationOffreD;
    private TextView textViewTypeContratOffreD;
    private TextView textViewPeriodeOffreD;
    private TextView textViewMissionsPOffreD;
    private TextView textViewProfilRechOffreD;
    private TextView textViewConditionsTravailOffreD;
    private TextView textViewTitreOffreD;
    private TextView textViewDateCandidature;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_offre_candidat);

        View rootView = findViewById(android.R.id.content);
        MenuCandidatManager.setupMenuItems(rootView, this);

        // Handle the back button
        ImageView backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Close the current activity to return to the previous activity
                finish();
            }
        });

        // Retrieve the views
        textViewTitreOffreD = findViewById(R.id.textViewTitreOffreD);
        textViewDescriptionOffreD = findViewById(R.id.textViewDescriptionOffreD);
        textViewLieuOffreD = findViewById(R.id.textViewLieuOffreD);
        textViewRemunerationOffreD = findViewById(R.id.textViewRemunerationOffreD);
        textViewTypeContratOffreD = findViewById(R.id.textViewTypeContratOffreD);
        textViewPeriodeOffreD = findViewById(R.id.textViewPeriodeOffreD);
        textViewMissionsPOffreD = findViewById(R.id.textViewMissionsPOffreD);
        textViewProfilRechOffreD = findViewById(R.id.textViewProfilRechOffreD);
        textViewConditionsTravailOffreD = findViewById(R.id.textViewConditionsTravailOffreD);
        textViewDateCandidature = findViewById(R.id.textViewDateCandidature);

        // Retrieve data from intent
        Intent intent = getIntent();
        if (intent != null) {
            textViewTitreOffreD.setText(intent.getStringExtra("offre_titre"));
            textViewDescriptionOffreD.setText(intent.getStringExtra("offre_description"));
            textViewLieuOffreD.setText(intent.getStringExtra("offre_lieu"));
            textViewRemunerationOffreD.setText(intent.getStringExtra("offre_remuneration"));
            textViewTypeContratOffreD.setText(intent.getStringExtra("offre_type_contrat"));
            textViewPeriodeOffreD.setText(intent.getStringExtra("offre_periode"));
            textViewMissionsPOffreD.setText(intent.getStringExtra("offre_missions"));
            textViewProfilRechOffreD.setText(intent.getStringExtra("offre_profil_recherche"));
            textViewDateCandidature.setText("Candidature envoyée le" + intent.getStringExtra("date_candidature"));

            // Construct conditions de travail
            String conditionsTravail = "Type de contract : " + intent.getStringExtra("offre_type_contrat") +
                    "\nLieu de travail : " + intent.getStringExtra("offre_lieu") +
                    "\nSalaire : " + intent.getStringExtra("offre_remuneration");
            textViewConditionsTravailOffreD.setText(conditionsTravail);
        }
    }
}
