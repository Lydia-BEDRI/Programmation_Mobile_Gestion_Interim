package com.example.linterim.Fragments;

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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.example.linterim.Activities.DetailsOffreAnonymousActivity;
import com.example.linterim.Adapters.OffreAdapter;
import com.example.linterim.Models.Offre;
import com.example.linterim.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class OffresGeoFragment extends Fragment {

    private static final int REQUEST_CODE_LOCATION_PERMISSION = 1001;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private ListView listView;
    private EditText editTextMetierRech, editTextVilleRech;
    private ImageView Rechercher;

    private ArrayList<Offre> arrayList;
    private ArrayList<Offre> filteredList;
    private OffreAdapter offreAdapter;
    private DatabaseReference databaseReference;
    private SimpleDateFormat dateFormat;

    private boolean isFirstLocationUpdate = true;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.offres_emploi_anonyme_with_location, container, false);

        // Initialize views
        listView = view.findViewById(R.id.ListViewOffresLocalisation);
        editTextMetierRech = view.findViewById(R.id.editTextMetierRech);
        editTextVilleRech = view.findViewById(R.id.editTextVilleRech);
        Rechercher = view.findViewById(R.id.Rechercher);

        arrayList = new ArrayList<>();
        filteredList = new ArrayList<>();
        offreAdapter = new OffreAdapter(getActivity(), R.layout.list_offre_item, filteredList);
        listView.setAdapter(offreAdapter);

        dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

        // Référence à la base de données Firebase Realtime
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Offres");

        // Écouteur pour récupérer les données depuis Firebase
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                arrayList.clear(); // Efface les anciennes données
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    // Récupérer chaque offre de la base de données
                    Offre offre = snapshot.getValue(Offre.class);
                    if (offre != null) {
                        // Ajouter l'offre à la liste
                        arrayList.add(offre);
                    }
                }
                // Trier les offres par date de publication (plus récentes en premier)
                Collections.sort(arrayList, new Comparator<Offre>() {
                    @Override
                    public int compare(Offre o1, Offre o2) {
                        Date date1 = convertStringToDate(o1.getDate_publication());
                        Date date2 = convertStringToDate(o2.getDate_publication());
                        return date2.compareTo(date1); // Trier par ordre décroissant
                    }
                });
                // Afficher toutes les offres initialement
                filteredList.clear();
                filteredList.addAll(arrayList);
                // Notifier l'adaptateur des changements
                offreAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Gestion des erreurs de récupération de données
                Toast.makeText(requireContext(), "Une erreur s'est produite lors de la récupération des données !", Toast.LENGTH_LONG).show();
            }
        });

        // Ajouter un listener de clic sur les éléments de la liste
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Récupérer l'ID de l'offre sélectionnée
                String offreId = filteredList.get(position).getAnnonce_id();

                // Créer une intention pour ouvrir DetailsOffreActivity
                Intent intent = new Intent(getActivity(), DetailsOffreAnonymousActivity.class);
                intent.putExtra("offreId", offreId);
                intent.putExtra("Anonyme", true);

                startActivity(intent);
            }
        });

        // Ajouter un listener de clic pour l'image Rechercher
        Rechercher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performSearch();
            }
        });

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

        return view;
    }

    // Méthode pour effectuer la recherche
    private void performSearch() {
        String metier = editTextMetierRech.getText().toString().trim();
        String ville = editTextVilleRech.getText().toString().trim();

        filteredList.clear();
        for (Offre offre : arrayList) {
            boolean matchesMetier = metier.isEmpty() || offre.getTitre().toLowerCase().contains(metier.toLowerCase());
            boolean matchesVille = ville.isEmpty() || offre.getLieu().toLowerCase().contains(ville.toLowerCase());
            if (matchesMetier && matchesVille) {
                filteredList.add(offre);
            }
        }

        // Trier les résultats de la recherche par date de publication (plus récentes en premier)
        Collections.sort(filteredList, new Comparator<Offre>() {
            @Override
            public int compare(Offre o1, Offre o2) {
                Date date1 = convertStringToDate(o1.getDate_publication());
                Date date2 = convertStringToDate(o2.getDate_publication());
                return date2.compareTo(date1); // Trier par ordre décroissant
            }
        });

        offreAdapter.notifyDataSetChanged();
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
        performSearch(); // Perform the search first to filter by title and user-specified city
        ArrayList<Offre> locationFilteredList = new ArrayList<>();
        for (Offre offre : filteredList) {
            if (offre.getLieu().equalsIgnoreCase(cityName)) {
                locationFilteredList.add(offre);
            }
        }
        filteredList.clear();
        filteredList.addAll(locationFilteredList);
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

    private Date convertStringToDate(String dateString) {
        try {
            return dateFormat.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new Date();
    }
}
