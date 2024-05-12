package com.example.linterim.Helpers;

        import android.content.Context;
        import android.content.Intent;
        import android.view.View;
        import android.widget.LinearLayout;

        import com.example.linterim.Activity.DashboardCandidatActivity;
        import com.example.linterim.Activity.GestionCandidaturesCandidatActivity;
        import com.example.linterim.Activity.ProfilCandidatActivity;
        import com.example.linterim.R;

public class MenuCandidatManager {

    public static void setupMenuItems(View rootView, final Context context) {
        LinearLayout menuAExplorerC = rootView.findViewById(R.id.MenuExplorerCandidat);
        LinearLayout menuCandidaturesC = rootView.findViewById(R.id.MenuCandidatureCandidat);
        LinearLayout menuProfilC = rootView.findViewById(R.id.MenuProfilCandidat);

        setupMenuItemClickListener(menuAExplorerC, context, DashboardCandidatActivity.class);
        setupMenuItemClickListener(menuCandidaturesC, context, GestionCandidaturesCandidatActivity.class);
        setupMenuItemClickListener(menuProfilC, context, ProfilCandidatActivity.class);
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

