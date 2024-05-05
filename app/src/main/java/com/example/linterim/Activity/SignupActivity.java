package com.example.linterim.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.linterim.R;

public class SignupActivity extends AppCompatActivity {

    private RadioGroup radioGroupOptions;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.type_compte);

        // Initialiser la RadioGroup et écouter les clics sur le bouton "Suivant"
        radioGroupOptions = findViewById(R.id.radioGroupOptions);
        findViewById(R.id.buttonNext).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Vérifier quel bouton radio est sélectionné
                int selectedRadioButtonId = radioGroupOptions.getCheckedRadioButtonId();

                if (selectedRadioButtonId != -1) {
                    RadioButton selectedRadioButton = findViewById(selectedRadioButtonId);

                    // Vérifier le texte du bouton radio sélectionné
                    if (selectedRadioButton.getText().equals("Chercheur d'emploi")) {
                        // Lancer l'activité pour le chercheur d'emploi
                        startActivity(new Intent(SignupActivity.this, JobSeekerActivity.class));
                    } else if (selectedRadioButton.getText().equals("Employeur")) {
                        // Lancer l'activité pour l'employeur
                        startActivity(new Intent(SignupActivity.this, EmployerActivity.class));
                    }
                }
            }
        });
    }
}

