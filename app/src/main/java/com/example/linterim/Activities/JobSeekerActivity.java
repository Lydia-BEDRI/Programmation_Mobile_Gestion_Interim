package com.example.linterim.Activities;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.linterim.Fragments.CandidatSignup1Fragment;
import com.example.linterim.R;

public class JobSeekerActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.job_seeker);
        // Chargez le premier fragment par défaut
        loadFragment(new CandidatSignup1Fragment());
    }

    // Méthode pour charger un fragment spécifique
    private void loadFragment(androidx.fragment.app.Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainerJSeeker, fragment);
        fragmentTransaction.addToBackStack(null); // Permet de revenir en arrière avec le bouton de retour
        fragmentTransaction.commit();
    }
}
