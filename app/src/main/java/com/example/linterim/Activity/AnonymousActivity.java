package com.example.linterim.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.linterim.R;

public class AnonymousActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        ImageView backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Gérer le clic sur le bouton de retour (backBtn)
                onBackPressed(); // Retour en arrière
            }
        });

        // Gérer le clic sur "Me localiser"
        ConstraintLayout meLocaliserLayout = findViewById(R.id.meLocaliserLayout);
        meLocaliserLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Gérer le clic sur le bouton "Me localiser"
                // Mettez ici le code pour lancer une action de localisation
                // par exemple, démarrer un service de localisation
                Toast.makeText(AnonymousActivity.this, "GPS ON!", Toast.LENGTH_SHORT).show();
            }
        });

        // Gérer le clic sur "Accepter"
        TextView accepterTextView = findViewById(R.id.textViewAccepter);
        accepterTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Gérer le clic sur "Accepter"
                // Mettez ici le code pour autoriser l'accès à la localisation
                Toast.makeText(AnonymousActivity.this, "Accepter!", Toast.LENGTH_SHORT).show();

            }
        });

        // Gérer le clic sur "Refuser"
        TextView refuserTextView = findViewById(R.id.textViewRefuser);
        refuserTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Gérer le clic sur "Refuser"
                // Mettez ici le code pour refuser l'accès à la localisation
                Toast.makeText(AnonymousActivity.this, "Refuser", Toast.LENGTH_SHORT).show();

            }
        });
    }
    }

