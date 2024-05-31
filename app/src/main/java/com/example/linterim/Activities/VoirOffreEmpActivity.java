package com.example.linterim.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.linterim.Models.Offre;
import com.example.linterim.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class VoirOffreEmpActivity extends AppCompatActivity {

    private TextView textViewOffreRef;
    private EditText editTextTitreOffre, editTextLieu, editTextRemuneration,
            editTextPeriode, editTextDescription, editTextMissionsPrincipales,
            editTextProfilRech, editTextTypeContrat,editTextDatePublication;
    private Button buttonOk;
    private ImageView backBtn;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.voir_offre_emp);

        // Récupérer les vues
        textViewOffreRef = findViewById(R.id.textView1);
        editTextTitreOffre = findViewById(R.id.editTextTitreOffre);
        editTextLieu = findViewById(R.id.editTextLieu);
        editTextRemuneration = findViewById(R.id.editTextRemuneration);
        editTextPeriode = findViewById(R.id.editTextPeriode);
        editTextDescription = findViewById(R.id.editTextDescription);
        editTextMissionsPrincipales = findViewById(R.id.editTextMissionsPrincipales);
        editTextProfilRech = findViewById(R.id.editTextProfilRech);
        editTextTypeContrat = findViewById(R.id.editTextTypeContrat);
        editTextDatePublication = findViewById(R.id.editTextDatePublication);
        buttonOk = findViewById(R.id.button_ok);
        backBtn = findViewById(R.id.backBtn);

        // Récupérer l'ID de l'offre depuis l'intent
        Intent voirIntent = getIntent();
        if (voirIntent != null && voirIntent.hasExtra("offreId")) {
            String offreId = voirIntent.getStringExtra("offreId");

            // Chargez les détails de l'offre depuis la base de données en utilisant l'offreId
            // et obtenir l'objet Offre correspondant
            DatabaseReference offreRef = FirebaseDatabase.getInstance().getReference().child("Offres").child(String.valueOf(offreId));

            offreRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        Offre offre = dataSnapshot.getValue(Offre.class);
                        if (offre == null) {
                            // Aucune donnée trouvée pour cet identifiant d'offre
                            // Gérer le cas où l'offre n'existe pas
                            Log.d("Tag", "Aucune offre trouvée pour l'identifiant : " + offreId);
                        }else{
                            textViewOffreRef.setText("Offre réf. " + offreId);
                            editTextTitreOffre.setText(offre.getTitre());
                            editTextLieu.setText(offre.getLieu());
                            editTextRemuneration.setText(offre.getRemuneration());
                            editTextPeriode.setText(offre.getPeriode());
                            editTextDescription.setText(offre.getDescription());
                            editTextMissionsPrincipales.setText(offre.getMissions_principales());
                            editTextProfilRech.setText(offre.getProfil_recherche());
                            editTextTypeContrat.setText(offre.getType_contract());
                            editTextDatePublication.setText(offre.getDate_publication());
                        }
                    }

                }
                @Override
                public void onCancelled (@NonNull DatabaseError databaseError){
                    // En cas d'erreur lors de la récupération des données depuis Firebase
                    // Gérer l'erreur ici
                    Log.e("TAG", "Erreur lors du chargement de l'offre depuis Firebase : " + databaseError.getMessage());
                }
            });

        }

        // Gérer le clic sur le bouton OK
        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Action à effectuer lors du clic sur le bouton OK
                // Par exemple, enregistrer les modifications dans la base de données
                // ou effectuer toute autre action nécessaire
                // Vous pouvez ajouter votre logique ici
                finish(); // Fermer l'activité après traitement
            }
        });

        // Gérer le clic sur le bouton de retour
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Retour à l'activité précédente (MainActivity ou autre)
                onBackPressed();
            }
        });
    }

}

