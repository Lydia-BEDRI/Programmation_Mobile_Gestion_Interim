package com.example.linterim.Fragment;

import android.content.Intent;
import android.os.Bundle;
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
import androidx.fragment.app.Fragment;

import com.example.linterim.Activity.DetailsOffreActivity;
import com.example.linterim.Helpers.OffreAdapter;
import com.example.linterim.Models.Offre;
import com.example.linterim.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;

public class OffresRecentesFragment extends Fragment {

    private ListView listView;
    private EditText editTextMetierRech, editTextVilleRech;
    private ImageView Rechercher;

    private ArrayList<Offre> arrayList;
    private ArrayList<Offre> filteredList;
    private OffreAdapter offreAdapter;
    private DatabaseReference databaseReference;
    private SimpleDateFormat dateFormat;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.offres_emploi_anonyme, container, false);

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
                Intent intent = new Intent(getActivity(), DetailsOffreActivity.class);
                intent.putExtra("offreId", offreId);
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

        // Notifier l'adaptateur des changements
        offreAdapter.notifyDataSetChanged();

        if (filteredList.isEmpty()) {
            Toast.makeText(getActivity(), "Aucun résultat trouvé", Toast.LENGTH_SHORT).show();
        }
    }

    // Méthode pour convertir une chaîne de caractères en date
    private Date convertStringToDate(String dateString) {
        try {
            return dateFormat.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
            return new Date(); // Retourne la date actuelle en cas d'erreur
        }
    }
}
