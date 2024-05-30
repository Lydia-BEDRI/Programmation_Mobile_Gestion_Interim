package com.example.linterim.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.example.linterim.Helpers.MenuCandidatManager;
import com.example.linterim.Models.Message;
import com.example.linterim.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ContactEmpActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference mMessagesRef;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_employeur);
        View rootView = findViewById(android.R.id.content);
        MenuCandidatManager.setupMenuItems(rootView, this);
        ImageView backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the back button click
                if (getFragmentManager() != null) {
                    getFragmentManager().popBackStack();
                }
            }
        });

        mAuth = FirebaseAuth.getInstance();
        mMessagesRef = FirebaseDatabase.getInstance().getReference().child("Messages");

        EditText editTextNomEntreprise = findViewById(R.id.editTextNomEntreprise);
        EditText editTextMessage = findViewById(R.id.editTextMessage);
        AppCompatButton button_envoyer = findViewById(R.id.button_envoyer);

        // Récupérer le nom de l'entreprise depuis l'intent
        String nomEntreprise = getIntent().getStringExtra("nomEntreprise");
        // Placer le nom de l'entreprise dans le champ "Nom de l'entreprise"
        editTextNomEntreprise.setText(nomEntreprise);
        // Rendre le champ non modifiable
        editTextNomEntreprise.setEnabled(false);

        button_envoyer.setOnClickListener(v -> {
            String employeurId = getIntent().getStringExtra("employeurId");
            String offreId = getIntent().getStringExtra("offreId");

            String messageContent = editTextMessage.getText().toString();
            String candidatId = mAuth.getCurrentUser().getUid();
            String currentDate = getCurrentFormattedDate();

            // Confirmation de l'envoi du message
            new AlertDialog.Builder(this)
                    .setTitle("Confirmation")
                    .setMessage("Voulez-vous envoyer ce message à " + nomEntreprise + " ?")
                    .setPositiveButton("Envoyer", (dialog, which) -> {
                        // Envoi du message
                        sendMessage(employeurId, messageContent, candidatId, offreId, currentDate);

                        // Redirection vers DashboardCandidatActivity
                        Intent intent = new Intent(ContactEmpActivity.this, DashboardCandidatActivity.class);
                        startActivity(intent);
                        finish(); // Ferme l'activité actuelle
                    })
                    .setNegativeButton("Annuler", null)
                    .show();
        });
    }

    private void sendMessage(String employeurId, String content, String candidatId, String offreId, String date) {
        String messageId = mMessagesRef.push().getKey();
        if (messageId != null) {
            Message message = new Message("message", employeurId, content, candidatId, offreId, date);
            mMessagesRef.child(messageId).setValue(message);
        }
    }

    private String getCurrentFormattedDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        return sdf.format(new Date());
    }
}
