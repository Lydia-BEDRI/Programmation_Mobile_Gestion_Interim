package com.example.linterim.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.linterim.R;

public class LocationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);


        ImageView backBtn = findViewById(R.id.backBtn);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Action à effectuer lorsque l'image est cliquée
                onBackPressed(); // Simuler le retour en arrière
            }
        });


        // Récupérer le ConstraintLayout
        ConstraintLayout meLocaliserLayout = findViewById(R.id.meLocaliserLayout);

        // Ajouter un OnClickListener au ConstraintLayout
        meLocaliserLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout ll = findViewById(R.id.linearLayoutMeLocaliser);
                ll.setVisibility(View.VISIBLE);

                TextView accepter = findViewById(R.id.textViewAccepterLocalisation);
                TextView refuser = findViewById(R.id.textViewRefuserLocalisation);

                accepter.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        launchAnonymousActivity(true); 
                    }
                });


                refuser.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        launchAnonymousActivity(false);
                    }
                });

        }
    });


}

    private void launchAnonymousActivity(boolean b) {
        Intent intent = new Intent(LocationActivity.this, AnonymousActivity.class);
        intent.putExtra("accepted", b);
        startActivity(intent);
    }
}