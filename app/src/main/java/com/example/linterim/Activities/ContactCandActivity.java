package com.example.linterim.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.example.linterim.Helpers.MenuEmployeurManager;
import com.example.linterim.Models.Message;
import com.example.linterim.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ContactCandActivity extends AppCompatActivity {
    private EditText editTextNomPrenomCandidat, editTextMessage;
    private AppCompatButton button_envoyer;
    private FirebaseAuth mAuth;
    private DatabaseReference mMessagesRef;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_candidat);

        View rootView = findViewById(android.R.id.content);
        MenuEmployeurManager.setupMenuItems(rootView, this);

        ImageView backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Close the current activity to return to the previous activity
                finish();
            }
        });

        editTextNomPrenomCandidat = findViewById(R.id.editTextNomPrenomCandidat);
        editTextMessage = findViewById(R.id.editTextMessage);
        button_envoyer = findViewById(R.id.button_envoyer);

        mAuth = FirebaseAuth.getInstance();
        mMessagesRef = FirebaseDatabase.getInstance().getReference().child("Messages");

        // Récupérer les données depuis l'intent
        Intent intent = getIntent();
        String candidatNomPrenom = intent.getStringExtra("candidat_nom_prenom");
        String candidatId = intent.getStringExtra("candidat_id");
        String offreId = intent.getStringExtra("offre_id");

        // Prérémplir le champ et le rendre non modifiable
        editTextNomPrenomCandidat.setText(candidatNomPrenom);
        editTextNomPrenomCandidat.setEnabled(false);

        button_envoyer.setOnClickListener(v -> {
            String messageContent = editTextMessage.getText().toString();
            String employeurId = mAuth.getCurrentUser().getUid();
            String currentDate = getCurrentFormattedDate();

            // Confirmation de l'envoi du message
            new AlertDialog.Builder(this)
                    .setTitle("Confirmation")
                    .setMessage("Voulez-vous envoyer ce message à " + candidatNomPrenom + " ?")
                    .setPositiveButton("Envoyer", (dialog, which) -> {
                        // Envoi du message
                        sendMessage(employeurId, candidatId, messageContent, offreId, currentDate);

                        // Redirection vers une autre activité ou un message de confirmation
                        Intent dashboardIntent = new Intent(ContactCandActivity.this, DashboardEmployeurActivity.class);
                        startActivity(dashboardIntent);
                        finish(); // Ferme l'activité actuelle
                    })
                    .setNegativeButton("Annuler", null)
                    .show();
        });
    }

    private void sendMessage(String senderId, String recipientId, String content, String offreId, String date) {
        String messageId = mMessagesRef.push().getKey();
        if (messageId != null) {
            Message message = new Message("message", senderId, recipientId, content, offreId, date);
            mMessagesRef.child(messageId).setValue(message);
        }
    }

    private String getCurrentFormattedDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        return sdf.format(new Date());
    }
}
