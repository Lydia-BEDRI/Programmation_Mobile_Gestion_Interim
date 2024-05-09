package com.example.linterim.Activity;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.linterim.Helper.MenuCandidatManager;
import com.example.linterim.Helper.MenuEmployeurManager;
import com.example.linterim.R;

public class DashboardCandidatActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.offres_emploi_user);
        View rootView = findViewById(android.R.id.content);
        MenuCandidatManager.setupMenuItems(rootView,this);
    }
}
