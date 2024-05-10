package com.example.linterim.Activity;

import android.os.Bundle;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.linterim.Helper.Offre;
import com.example.linterim.Helper.OffreAdapter;
import com.example.linterim.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class OffresActivity extends AppCompatActivity {

    private ListView listView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.offres);

        listView = findViewById(R.id.ListViewOffresLocalisation);

        ArrayList<Offre> arrayList = new ArrayList<>();

        arrayList.add(new Offre("TitrePoste1", "Description du poste","Lieu","Temps plein","XXX€/mois","CDI"));
        arrayList.add(new Offre("TitrePoste2", "Description du poste","Lieu","Temps plein","XXX€/mois","CDI"));
        arrayList.add(new Offre("TitrePoste3", "Description du poste","Lieu","Temps plein","XXX€/mois","CDI"));
        arrayList.add(new Offre("TitrePoste4", "Description du poste","Lieu","Temps plein","XXX€/mois","CDI"));


        OffreAdapter offreAdapter = new OffreAdapter(this,R.layout.list_offre_item,arrayList);
        listView.setAdapter(offreAdapter);


//        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Offres");
    }
}
