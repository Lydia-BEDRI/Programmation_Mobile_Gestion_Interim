package com.example.linterim.Activities;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.linterim.Helpers.MenuEmployeurManager;
import com.example.linterim.R;

public class DashboardEmployeurActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard_employeur);
        View rootView = findViewById(android.R.id.content);
        MenuEmployeurManager.setupMenuItems(rootView,this);
    }
}
