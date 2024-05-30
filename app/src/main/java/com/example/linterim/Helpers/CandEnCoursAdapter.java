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

import com.example.linterim.Models.Candidature;
import com.example.linterim.Models.Candidat;
import com.example.linterim.Models.Offre;
import com.example.linterim.R;
import com.example.linterim.Activity.VoirCandidatureEmpActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CandEnCoursAdapter extends ArrayAdapter<Candidature> {
    private Context mContext;
    private int mResource;
    private DatabaseReference mCandidatRef;
    private DatabaseReference mOffreRef;

    public CandEnCoursAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Candidature> arrayList) {
        super(context, resource, arrayList);
        this.mContext = context;
        this.mResource = resource;
        this.mCandidatRef = FirebaseDatabase.getInstance().getReference().child("Candidats");
        this.mOffreRef = FirebaseDatabase.getInstance().getReference().child("Offres");
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        convertView = layoutInflater.inflate(mResource, parent, false);

        TextView nomPrenom = convertView.findViewById(R.id.nomPrenom);
        TextView dateNaissance = convertView.findViewById(R.id.dateNaissance);
        TextView titreOffreCandEnCours = convertView.findViewById(R.id.titreOffreCandEnCours);

        Button buttonVoirCandidature = convertView.findViewById(R.id.buttonVoirCandidature);

        Candidature currentCandidature = getItem(position);

        if (currentCandidature != null) {
            String candidat_Id = currentCandidature.getCandidat_id();
            String offre_Id = currentCandidature.getOffre_id();

            // Récupérer les informations du candidat
            mCandidatRef.child(candidat_Id).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Candidat candidat = snapshot.getValue(Candidat.class);
                    if (candidat != null) {
                        nomPrenom.setText(candidat.getNom() + " " + candidat.getPrenom());
                        dateNaissance.setText(candidat.getDateNaissance() != null ? candidat.getDateNaissance() : "Non Disponible");

                        // Récupérer les informations de l'offre
                        mOffreRef.child(offre_Id).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                Offre offre = snapshot.getValue(Offre.class);
                                if (offre != null) {
                                    titreOffreCandEnCours.setText(offre.getTitre());

                                    // Transmettre les informations via l'intent
                                    buttonVoirCandidature.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent intent = new Intent(mContext, VoirCandidatureEmpActivity.class);
                                            intent.putExtra("candidature_id", currentCandidature.getCandidature_id());
                                            intent.putExtra("offre_titre", offre.getTitre());
                                            intent.putExtra("offre_date", offre.getDate_publication());
                                            intent.putExtra("candidat_nom_prenom", candidat.getNom() + " " + candidat.getPrenom());
                                            intent.putExtra("date_candidature", currentCandidature.getDate_candidature());
                                            intent.putExtra("candidat_date_naissance", candidat.getDateNaissance() != null ? candidat.getDateNaissance() : "Non Disponible");
                                            intent.putExtra("candidat_nationalite", candidat.getNationalite() != null ? candidat.getNationalite() : "Non Disponible");
                                            intent.putExtra("candidat_cv", candidat.getCvUrl());
                                            intent.putExtra("candidat_lettre_motivation", currentCandidature.getLettre_motivation());

                                            mContext.startActivity(intent);
                                        }
                                    });
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                // Handle possible errors.
                            }
                        });
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Handle possible errors.
                }
            });
        }

        return convertView;
    }
}
