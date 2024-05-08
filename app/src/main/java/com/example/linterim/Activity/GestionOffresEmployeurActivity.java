package com.example.linterim.Activity;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.linterim.Helper.MenuEmployeurManager;
import com.example.linterim.R;

public class GestionOffresEmployeurActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gestion_offres_employeur);
        View rootView = findViewById(android.R.id.content);
        MenuEmployeurManager.setupMenuItems(rootView,this);
    }
}
