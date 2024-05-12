package com.example.linterim.Fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.example.linterim.Activity.DetailsOffreActivity;
import com.example.linterim.Models.Offre;
import com.example.linterim.Helpers.OffreAdapter;
import com.example.linterim.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class OffresGeoFragment extends Fragment {

    private static final int REQUEST_CODE_LOCATION_PERMISSION = 1001;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private ListView listView;

    private ArrayList<Offre> arrayList;
    private OffreAdapter offreAdapter;
    private DatabaseReference databaseReference;

    private boolean isFirstLocationUpdate = true;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.offres_emploi_anonyme_with_location, container, false);

        locationManager = (LocationManager) requireContext().getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                if (isFirstLocationUpdate) {
                    // Obtain city name from location
                    String cityName = getCityNameFromLocation(location);
                    if (cityName != null) {
                        fetchOffersByCity(cityName);
                    }
                    isFirstLocationUpdate = false;
                }
            }

            @Override
            public void onProviderEnabled(@NonNull String provider) {
                Log.d("Location", "GPS provider enabled");
            }

            @Override
            public void onProviderDisabled(@NonNull String provider) {
                Log.d("Location", "GPS provider disabled");
                Toast.makeText(requireContext(), "Erreur : GPS désactivé", Toast.LENGTH_LONG).show();
            }
        };

        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestLocationPermissions();
        } else {
            startLocationUpdates();
        }

        listView = view.findViewById(R.id.ListViewOffresLocalisation);
        arrayList = new ArrayList<>();
        offreAdapter = new OffreAdapter(getActivity(), R.layout.list_offre_item, arrayList);
        listView.setAdapter(offreAdapter);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Offres");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                arrayList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Offre offre = snapshot.getValue(Offre.class);
                    if (offre != null) {
                        arrayList.add(offre);
                    }
                }
                offreAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(requireContext(), "Une erreur s'est produite lors de la récupération des données ! ", Toast.LENGTH_LONG).show();
            }
        });

        // Ajouter un listener de clic sur les éléments de la liste
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Récupérer l'ID de l'offre sélectionnée
                String offreId = arrayList.get(position).getAnnonce_id();

                // Créer une intention pour ouvrir DetailsOffreActivity
                Intent intent = new Intent(getActivity(), DetailsOffreActivity.class);
                intent.putExtra("offreId", offreId);
                intent.putExtra("Anonyme", true);

                startActivity(intent);
            }
        });

        return view;
    }

    private String getCityNameFromLocation(Location location) {
        // Use geocoding or reverse geocoding to obtain city name from location
        // Example: Using Geocoder
        Geocoder geocoder = new Geocoder(requireContext(), Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            if (!addresses.isEmpty()) {
                return addresses.get(0).getLocality(); // Get city name
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void fetchOffersByCity(String cityName) {
        // Filter offers by city name
        ArrayList<Offre> filteredList = new ArrayList<>();
        for (Offre offre : arrayList) {
            if (offre.getLieu().equalsIgnoreCase(cityName)) {
                filteredList.add(offre);
            }
        }
        // Update adapter with filtered list
        offreAdapter.clear();
        offreAdapter.addAll(filteredList);
        offreAdapter.notifyDataSetChanged();
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
                startLocationUpdates();
            } else {
                Toast.makeText(requireContext(), "Permission de localisation refusée", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (locationManager != null && locationListener != null) {
            locationManager.removeUpdates(locationListener);
        }
    }
}

