package com.example.linterim.Fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.example.linterim.R;

public class OffresGeoFragment extends Fragment {

    private static final int REQUEST_CODE_LOCATION_PERMISSION = 1001;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private boolean isFirstLocationUpdate = true; // Variable pour contrôler l'affichage du Toast

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.offres_emploi_anonyme_with_location, container, false);

        // Initialize location manager and listener
        locationManager = (LocationManager) requireContext().getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                // Log location coordinates

                // Display location in toast only once
                if (isFirstLocationUpdate) {
                    Log.d("Location", "Latitude: " + location.getLatitude() + ", Longitude: " + location.getLongitude());

                    Toast.makeText(requireContext(), "Votre localisation : Latitude: " + location.getLatitude() + ", Longitude: " + location.getLongitude(), Toast.LENGTH_LONG).show();
                    isFirstLocationUpdate = false; // Ne plus afficher le Toast pour les mises à jour suivantes
                }
            }

            @Override
            public void onProviderEnabled(@NonNull String provider) {
                // GPS provider enabled
                Log.d("Location", "GPS provider enabled");
            }

            @Override
            public void onProviderDisabled(@NonNull String provider) {
                // GPS provider disabled
                Log.d("Location", "GPS provider disabled");
                Toast.makeText(requireContext(), "Erreur : GPS désactivé", Toast.LENGTH_LONG).show();
            }
        };

        // Check location permission
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Request location permissions if not granted
            requestLocationPermissions();
        } else {
            // Start listening for location updates
            startLocationUpdates();
        }

        return view;
    }

    private void requestLocationPermissions() {
        ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                REQUEST_CODE_LOCATION_PERMISSION);
    }

    @SuppressLint("MissingPermission")
    private void startLocationUpdates() {
        if (locationManager != null) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener, Looper.getMainLooper());
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_LOCATION_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, start location updates
                startLocationUpdates();
            } else {
                // Permission denied, show a message or handle accordingly
                Toast.makeText(requireContext(), "Permission de localisation refusée", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Stop listening for location updates to prevent memory leaks
        if (locationManager != null && locationListener != null) {
            locationManager.removeUpdates(locationListener);
        }
    }
}
