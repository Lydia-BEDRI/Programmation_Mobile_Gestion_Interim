package com.example.linterim.Activity;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.linterim.Helpers.MenuCandidatManager;
import com.example.linterim.R;

public class GestionCandidaturesCandidatActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gestion_candidature_en_cours_user);
        View rootView = findViewById(android.R.id.content);
        MenuCandidatManager.setupMenuItems(rootView,this);


    }
}
