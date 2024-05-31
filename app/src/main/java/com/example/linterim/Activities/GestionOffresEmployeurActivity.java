package com.example.linterim.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.linterim.Models.Offre;
import com.example.linterim.Adapters.OffreAdapterEmp;
import com.example.linterim.Helpers.MenuEmployeurManager;
import com.example.linterim.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class GestionOffresEmployeurActivity extends AppCompatActivity {
    private ArrayList<Offre> arrayList;
    private ListView listView;
    private OffreAdapterEmp offreAdapter;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gestion_offres_employeur);

        View rootView = findViewById(android.R.id.content);
        MenuEmployeurManager.setupMenuItems(rootView, this);


        listView = findViewById(R.id.ListViewOffresEmp);
        arrayList = new ArrayList<>();
        offreAdapter = new OffreAdapterEmp(GestionOffresEmployeurActivity.this, R.layout.list_offre_item_emp, arrayList);
        listView.setAdapter(offreAdapter);

        // Trouver le LinearLayout pour créer une nouvelle offre
        LinearLayout linearLayoutNewOffer = findViewById(R.id.linearLayoutNewOfferEmp);

        // Ajouter un OnClickListener au LinearLayout
        linearLayoutNewOffer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lancer CreerOffreActivity lorsque le LinearLayout est cliqué
                Intent intent = new Intent(GestionOffresEmployeurActivity.this, CreerOffreActivity.class);
                startActivity(intent);
            }
        });

        // Référence à la base de données Firebase Realtime
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Offres");

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();

            // Écouteur pour récupérer les offres de l'utilisateur connecté depuis Firebase
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    arrayList.clear(); // Efface les anciennes données
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Offre offre = snapshot.getValue(Offre.class);
                        if (offre != null && offre.getEmployeur_id().equals(userId)) {
                            // Ajoute uniquement les offres de l'employeur connecté à la liste
                            arrayList.add(offre);
                        }
                    }
                    // Notifie l'adaptateur des changements
                    offreAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Gestion des erreurs de récupération de données
                    Toast.makeText(GestionOffresEmployeurActivity.this, "Erreur de récupération des offres", Toast.LENGTH_SHORT).show();
                }
            });

            // Écouteur pour gérer le clic sur un élément de la liste
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    // Récupère l'ID de l'offre sélectionnée
                    String offreId = arrayList.get(position).getAnnonce_id();
                    Log.d("GestionOffresActivity", "ID de l'offre sélectionnée: " + offreId);

                    // Crée une intention pour ouvrir DetailsOffreActivity
                    Intent intent = new Intent(GestionOffresEmployeurActivity.this, DetailsOffreActivity.class);
                    intent.putExtra("offreId", offreId);
                    startActivity(intent);
                }
            });
        } else {
            Toast.makeText(GestionOffresEmployeurActivity.this, "Erreur d'authentification de l'employeur", Toast.LENGTH_SHORT).show();
        }
    }
}
