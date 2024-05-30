package com.example.linterim.Activity;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.linterim.Fragment.GestionCandEmpEnCoursFragment;
import com.example.linterim.Fragment.GestionCandUserEnCoursFragment;
import com.example.linterim.Helpers.MenuCandidatManager;
import com.example.linterim.R;

public class GestionCandidaturesCandidatActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_container); // Utilisez votre layout principal

        // Chargez le fragment par défaut
        if (savedInstanceState == null) {
            replaceFragment(new GestionCandUserEnCoursFragment());
        }
    }
    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}
