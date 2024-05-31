package com.example.linterim.Activities;

import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.linterim.Fragments.OffresGeoFragment;
import com.example.linterim.Fragments.OffresRecentesFragment;
import com.example.linterim.R;

public class AnonymousActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.anonymous);

        // Récupération de la réponse de l'utilisateur depuis l'intent
        boolean accepted = getIntent().getBooleanExtra("accepted", false);

        // Choix du fragment à afficher en fonction de la réponse
        Fragment fragment;
        if (accepted) {
            // L'utilisateur a accepté, afficher le fragment des offres selon la zone géographique
            fragment = new OffresGeoFragment();

            // Vérifier si le GPS est activé, sinon rediriger vers les paramètres
            LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            if (locationManager != null && !locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        } else {
            // L'utilisateur a refusé, afficher le fragment des offres récentes
            fragment = new OffresRecentesFragment();
        }

        // Remplacement du contenu du conteneur par le fragment choisi
        replaceFragment(fragment);
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerAnonymous, fragment)
                .commit();
    }
}
