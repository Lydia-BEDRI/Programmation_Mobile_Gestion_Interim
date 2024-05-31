package com.example.linterim.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.linterim.Models.Offre;
import com.example.linterim.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ModifierOffreActivity extends AppCompatActivity {
    private TextView textViewOffreRef;
    private EditText editTextTitreOffre, editTextLieu, editTextRemuneration,
            editTextPeriode, editTextDescription, editTextMissionsPrincipales,
            editTextProfilRech, editTextTypeContrat, editTextDatePublication;
    private Button buttonOk;
    private ImageView backBtn;
    private Offre offre; // Déclarer la variable 'offre' ici pour qu'elle soit accessible globalement

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.modifier_offre_emp);

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
            DatabaseReference offreRef = FirebaseDatabase.getInstance().getReference().child("Offres").child(String.valueOf(offreId));

            offreRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        // Récupérer l'objet Offre depuis le DataSnapshot
                        offre = dataSnapshot.getValue(Offre.class);
                        if (offre == null) {
                            Log.d("Tag", "Aucune offre trouvée pour l'identifiant : " + offreId);
                        } else {
                            // Afficher les détails de l'offre dans les EditText
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
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.e("TAG", "Erreur lors du chargement de l'offre depuis Firebase : " + databaseError.getMessage());
                }
            });

            buttonOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Vérifier que 'offre' n'est pas null (pour éviter les NullPointerException)
                    if (offre != null) {
                        // Vérifier que tous les champs sont remplis
                        if (areAllFieldsFilled()) {
                            // Mettre à jour l'objet Offre avec les nouvelles valeurs
                            offre.setTitre(editTextTitreOffre.getText().toString());
                            offre.setLieu(editTextLieu.getText().toString());
                            offre.setRemuneration(editTextRemuneration.getText().toString());
                            offre.setPeriode(editTextPeriode.getText().toString());
                            offre.setDescription(editTextDescription.getText().toString());
                            offre.setMissions_principales(editTextMissionsPrincipales.getText().toString());
                            offre.setProfil_recherche(editTextProfilRech.getText().toString());
                            offre.setType_contract(editTextTypeContrat.getText().toString());
                            offre.setDate_publication(editTextDatePublication.getText().toString());

                            // Enregistrer les modifications dans Firebase
                            DatabaseReference offreRef = FirebaseDatabase.getInstance().getReference("Offres").child(offreId);
                            offreRef.setValue(offre)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(ModifierOffreActivity.this, "L'offre a été mise à jour avec succès", Toast.LENGTH_SHORT).show();
                                                finish(); // Retour à l'activité précédente après avoir mis à jour l'offre
                                            } else {
                                                Toast.makeText(ModifierOffreActivity.this, "Erreur lors de la mise à jour de l'offre", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                        } else {
                            // Afficher un message d'erreur si un champ est vide
                            Toast.makeText(ModifierOffreActivity.this, "Veuillez remplir tous les champs !", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });

    // Méthode pour vérifier que tous les champs sont remplis



    // Gérer le clic sur le bouton de retour
            backBtn.setOnClickListener(new View.OnClickListener()

    {
        @Override
        public void onClick (View v){
        // Retour à l'activité précédente (MainActivity ou autre)
        onBackPressed();
    }
    });
}}
    private boolean areAllFieldsFilled() {
        return !TextUtils.isEmpty(editTextTitreOffre.getText().toString())
                && !TextUtils.isEmpty(editTextLieu.getText().toString())
                && !TextUtils.isEmpty(editTextRemuneration.getText().toString())
                && !TextUtils.isEmpty(editTextPeriode.getText().toString())
                && !TextUtils.isEmpty(editTextDescription.getText().toString())
                && !TextUtils.isEmpty(editTextMissionsPrincipales.getText().toString())
                && !TextUtils.isEmpty(editTextProfilRech.getText().toString())
                && !TextUtils.isEmpty(editTextTypeContrat.getText().toString())
                && !TextUtils.isEmpty(editTextDatePublication.getText().toString());
    }
}
