package com.example.linterim.Helpers;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;

import com.example.linterim.Activity.DashboardEmployeurActivity;
import com.example.linterim.Activity.GestionCandidaturesEmployeurActivity;
import com.example.linterim.Activity.GestionOffresEmployeurActivity;
import com.example.linterim.Activity.ProfilEmployeurActivity;
import com.example.linterim.R;

public class MenuEmployeurManager {

    public static void setupMenuItems(View rootView, final Context context) {
        LinearLayout menuAcceuilEmp = rootView.findViewById(R.id.MenuAcceuilEmp);
        LinearLayout menuOffresEmp = rootView.findViewById(R.id.MenuOffresEmp);
        LinearLayout menuCandidaturesEmp = rootView.findViewById(R.id.MenuCandidaturesEmp);
        LinearLayout menuProfilEmp = rootView.findViewById(R.id.MenuProfilEmp);

        setupMenuItemClickListener(menuAcceuilEmp, context, DashboardEmployeurActivity.class);
        setupMenuItemClickListener(menuOffresEmp, context, GestionOffresEmployeurActivity.class);
        setupMenuItemClickListener(menuCandidaturesEmp, context, GestionCandidaturesEmployeurActivity.class);
        setupMenuItemClickListener(menuProfilEmp, context, ProfilEmployeurActivity.class);
    }

    private static void setupMenuItemClickListener(LinearLayout menuItem, final Context context, final Class<?> targetActivity) {
        menuItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Démarrer l'activité correspondante lorsque l'élément de menu est cliqué
                Intent intent = new Intent(context, targetActivity);
                context.startActivity(intent);
            }
        });
    }
}

