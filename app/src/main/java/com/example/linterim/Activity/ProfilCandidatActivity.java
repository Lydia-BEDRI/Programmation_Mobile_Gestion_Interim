package com.example.linterim.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.linterim.R;
import com.google.firebase.auth.FirebaseAuth;

public class ProfilCandidatActivity extends AppCompatActivity {
    private ConstraintLayout logOut;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profil_candidat);

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
                        startActivity(new Intent(ProfilCandidatActivity.this, LoginActivity.class));
                        finish(); // Optionnel : fermer l'activité actuelle
                    }
                }, 3000); // Délai en millisecondes (ici, 3000 ms = 3 secondes)
            }
        });
    }
}
