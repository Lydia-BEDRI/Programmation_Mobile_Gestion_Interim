package com.example.linterim.Activities;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.linterim.Models.Offre;
import com.example.linterim.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CreerOffreActivity extends AppCompatActivity {

    private EditText editTextTitreOffre, editTextLieu, editTextRemuneration, editTextPeriode, editTextDescription, editTextMissionsPrincipales, editTextProfilRech, editTextTypeContrat;
    private FirebaseUser currentUser; // Utilisateur actuellement connecté

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_offre);

        // Initialisation des EditText
        editTextTitreOffre = findViewById(R.id.editTextTitreOffre);
        editTextLieu = findViewById(R.id.editTextLieu);
        editTextRemuneration = findViewById(R.id.editTextRemuneration);
        editTextPeriode = findViewById(R.id.editTextPeriode);
        editTextDescription = findViewById(R.id.editTextDescription);
        editTextMissionsPrincipales = findViewById(R.id.editTextMissionsPrincipales);
        editTextProfilRech = findViewById(R.id.editTextProfilRech);
        editTextTypeContrat = findViewById(R.id.editTextTypeContrat);

        ImageView backBtn = findViewById(R.id.backBtn);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Action à effectuer lorsque l'image est cliquée
                onBackPressed(); // Simuler le retour en arrière
            }
        });


        Button buttonDeposerOffre = findViewById(R.id.button_deposer_offre);
        buttonDeposerOffre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Récupération des valeurs saisies dans les champs
                String titre = editTextTitreOffre.getText().toString();
                String lieu = editTextLieu.getText().toString();
                String remuneration = editTextRemuneration.getText().toString();
                String periode = editTextPeriode.getText().toString();
                String description = editTextDescription.getText().toString();
                String missionsPrincipales = editTextMissionsPrincipales.getText().toString();
                String profilRecherche = editTextProfilRech.getText().toString();
                String typeContrat = editTextTypeContrat.getText().toString();
                String datePublication = getCurrentDate(); // À implémenter

                // Vérifier si tous les champs obligatoires sont remplis
                if (TextUtils.isEmpty(titre) || TextUtils.isEmpty(lieu) || TextUtils.isEmpty(remuneration) || TextUtils.isEmpty(periode) || TextUtils.isEmpty(description) || TextUtils.isEmpty(missionsPrincipales) || TextUtils.isEmpty(profilRecherche) || TextUtils.isEmpty(typeContrat)) {
                    Toast.makeText(CreerOffreActivity.this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
                } else {
                    // Récupérer l'utilisateur actuellement connecté
                    currentUser = FirebaseAuth.getInstance().getCurrentUser();
                    if (currentUser != null) {
                        String employeurId = currentUser.getUid();

                        // Créer une nouvelle instance d'Offre avec les valeurs saisies
                        Offre nouvelleOffre = new Offre(null, datePublication, description, employeurId, lieu, missionsPrincipales, periode, profilRecherche, remuneration, titre, typeContrat);

// Enregistrer l'offre dans la base de données Firebase avec une clé générée automatiquement par Firebase
                        DatabaseReference offreRef = FirebaseDatabase.getInstance().getReference("Offres").push();
                        String annonceId = offreRef.getKey(); // Récupérer la clé générée par Firebase

// Assigner la clé à l'annonce_id de l'offre
                        nouvelleOffre.setAnnonce_id(annonceId);

// Envoyer l'offre à Firebase
                        offreRef.setValue(nouvelleOffre)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(CreerOffreActivity.this, "L'offre a été publiée avec succès", Toast.LENGTH_SHORT).show();
                                            finish(); // Retour à l'activité précédente après avoir publié l'offre
                                        } else {
                                            Toast.makeText(CreerOffreActivity.this, "Erreur lors de la publication de l'offre", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });

                    } else {
                        Toast.makeText(CreerOffreActivity.this, "Utilisateur non connecté", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    // Méthode pour générer une date actuelle (à adapter selon vos besoins)
    private String getCurrentDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        return dateFormat.format(new Date());
    }

    // Méthode pour générer un ID unique pour l'annonce (à adapter selon vos besoins)
}

