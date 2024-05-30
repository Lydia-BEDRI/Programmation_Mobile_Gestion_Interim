package com.example.linterim.Helpers;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.linterim.Activity.VoirCandidatureUserActivity;
import com.example.linterim.Models.Candidature;
import com.example.linterim.Models.Offre;
import com.example.linterim.Models.Employeur;
import com.example.linterim.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CandEnCoursUserAdapter extends ArrayAdapter<Candidature> {
    private Context mContext;
    private int mResource;
    private DatabaseReference mCandidatRef;
    private DatabaseReference mOffreRef;
    private DatabaseReference mEmployeurRef;

    public CandEnCoursUserAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Candidature> arrayList) {
        super(context, resource, arrayList);
        this.mContext = context;
        this.mResource = resource;
        this.mCandidatRef = FirebaseDatabase.getInstance().getReference().child("Candidats");
        this.mOffreRef = FirebaseDatabase.getInstance().getReference().child("Offres");
        this.mEmployeurRef = FirebaseDatabase.getInstance().getReference().child("Employeurs");
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        convertView = layoutInflater.inflate(mResource, parent, false);

        TextView titreOffre = convertView.findViewById(R.id.titreMetier);
        TextView nomEntreprise = convertView.findViewById(R.id.nomEntreprise);
        TextView adresseEntreprise = convertView.findViewById(R.id.adresseEntreprise);
        TextView remuneration = convertView.findViewById(R.id.remuneration);
        TextView typeContrat = convertView.findViewById(R.id.typeContrat);
        TextView duree = convertView.findViewById(R.id.duree);
        TextView dateCandidature = convertView.findViewById(R.id.dateCandidature);

        Button buttonVoir = convertView.findViewById(R.id.buttonVoir);

        Candidature currentCandidature = getItem(position);

        if (currentCandidature != null) {
            String offre_Id = currentCandidature.getOffre_id();

            mOffreRef.child(offre_Id).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Offre offre = snapshot.getValue(Offre.class);
                    if (offre != null) {
                        titreOffre.setText(offre.getTitre());
                        adresseEntreprise.setText(offre.getLieu());
                        remuneration.setText(offre.getRemuneration());
                        typeContrat.setText(offre.getType_contract());
                        duree.setText(offre.getPeriode());
                        dateCandidature.setText("Candidature envoyée le " + currentCandidature.getDate_candidature());

                        String employeurId = offre.getEmployeur_id();

                        // Fetch the employeur details
                        mEmployeurRef.child(employeurId).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot employeurSnapshot) {
                                Employeur employeur = employeurSnapshot.getValue(Employeur.class);
                                if (employeur != null) {
                                    nomEntreprise.setText(employeur.getNom_entreprise());
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                // Handle potential errors here
                            }
                        });
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Handle potential errors here
                }
            });
        }

        buttonVoir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, VoirCandidatureUserActivity.class);
                intent.putExtra("candidature_id", currentCandidature.getCandidature_id());
                mContext.startActivity(intent);
            }
        });

        return convertView;
    }
}
