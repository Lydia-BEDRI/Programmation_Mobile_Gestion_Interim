package com.example.linterim.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.linterim.Helpers.MenuCandidatManager;
import com.example.linterim.Models.Offre;
import com.example.linterim.Helpers.OffreAdapter;
import com.example.linterim.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DashboardCandidatActivity extends AppCompatActivity {
    private ListView listView;

    private ArrayList<Offre> arrayList;
    private OffreAdapter offreAdapter;
    private DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.offres_emploi_user);
        View rootView = findViewById(android.R.id.content);
        MenuCandidatManager.setupMenuItems(rootView,this);

        listView = findViewById(R.id.ListViewOffresLocalisation);
        arrayList = new ArrayList<>();
        offreAdapter = new OffreAdapter(this, R.layout.list_offre_item, arrayList);
        listView.setAdapter(offreAdapter);

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
                // Notifier l'adaptateur des changements
                offreAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Gestion des erreurs de récupération de données
                Toast.makeText(DashboardCandidatActivity.this, "Une erreur s'est produite lors de la récupération des données !", Toast.LENGTH_LONG).show();
            }
        });

        // Ajouter un listener de clic sur les éléments de la liste
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Récupérer l'ID de l'offre sélectionnée
                String offreId = arrayList.get(position).getAnnonce_id();

                // Créer une intention pour ouvrir DetailsOffreActivity
                Intent intent = new Intent(DashboardCandidatActivity.this,DetailsOffreActivity.class);
                intent.putExtra("offreId", offreId);
                startActivity(intent);
            }
        });
    }
}
