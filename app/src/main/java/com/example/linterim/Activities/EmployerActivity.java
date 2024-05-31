package com.example.linterim.Activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.linterim.Fragments.EmployeurSignup1Fragment;
import com.example.linterim.R;

public class EmployerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employer);

        // Chargez le premier fragment par défaut
        loadFragment(new EmployeurSignup1Fragment());

        // Vous pouvez ensuite naviguer vers le deuxième fragment en fonction des actions de l'utilisateur
        // Par exemple, vous pouvez appeler loadFragment(new SignupEmployerFragment2()); lorsque l'utilisateur clique sur un bouton.
    }

    // Méthode pour charger un fragment spécifique
    private void loadFragment(androidx.fragment.app.Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainer, fragment);
        fragmentTransaction.addToBackStack(null); // Permet de revenir en arrière avec le bouton de retour
        fragmentTransaction.commit();
    }
}
