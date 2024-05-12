package com.example.linterim.Helpers;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.linterim.Activity.ModifierOffreActivity;
import com.example.linterim.Activity.VoirOffreEmpActivity;
import com.example.linterim.Models.Employeur;
import com.example.linterim.Models.Offre;
import com.example.linterim.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class OffreAdapterEmp extends ArrayAdapter<Offre> {
    private Context mContext;
    private int mResource;
    private DatabaseReference mEmployeurRef;

    public OffreAdapterEmp(@NonNull Context context, int resource, @NonNull ArrayList<Offre> objects) {
        super(context, resource, objects);
        this.mContext = context;
        this.mResource = resource;
        this.mEmployeurRef = FirebaseDatabase.getInstance().getReference().child("Employeurs");
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        convertView = layoutInflater.inflate(mResource, parent, false);

        TextView titreOffre = convertView.findViewById(R.id.textViewTitreOffre);
        TextView nomEntreprise = convertView.findViewById(R.id.textViewNomEntreprise);
        TextView lieuOffre = convertView.findViewById(R.id.textViewLieuOffre);
        TextView datePublication = convertView.findViewById(R.id.textViewDatePublicationOffreEmp);

        Button voirButton = convertView.findViewById(R.id.buttonVoir);
        Button modifierButton = convertView.findViewById(R.id.buttonModifier);
        Button supprimerButton = convertView.findViewById(R.id.buttonSuppr);

        Offre currentOffre = getItem(position);

        if (currentOffre != null) {
            titreOffre.setText(currentOffre.getTitre());
            lieuOffre.setText(currentOffre.getLieu());
            datePublication.setText("Offre publiée le "+currentOffre.getDate_publication());

            String employeurId = currentOffre.getEmployeur_id();
            DatabaseReference currentEmployeurRef = mEmployeurRef.child(employeurId);
            currentEmployeurRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        Employeur employeur = dataSnapshot.getValue(Employeur.class);
                        if (employeur != null) {
                            nomEntreprise.setText(employeur.getNom_entreprise());
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.e("OffreAdapterEmp", "Erreur d'accès à la base de données", databaseError.toException());
                }
            });

            // Gestion du clic sur le bouton "Voir"
            voirButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Ouvrir VoirOffreEmpActivity en passant l'ID de l'offre
                    Intent voirIntent = new Intent(mContext, VoirOffreEmpActivity.class);
                    voirIntent.putExtra("offreId", currentOffre.getAnnonce_id());
                    mContext.startActivity(voirIntent);
                }
            });

            // Gestion du clic sur le bouton "Modifier"
            modifierButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Ouvrir ModifierOffreActivity en passant l'ID de l'offre
                    Intent modifierIntent = new Intent(mContext, ModifierOffreActivity.class);
                    modifierIntent.putExtra("offreId", currentOffre.getAnnonce_id());
                    mContext.startActivity(modifierIntent);
                }
            });

            // Gestion du clic sur le bouton "Supprimer"
            supprimerButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String offreId = currentOffre.getAnnonce_id(); // Récupérer l'ID de l'offre à supprimer

                    // Référence à l'emplacement de l'offre dans la base de données Firebase
                    DatabaseReference offreRef = FirebaseDatabase.getInstance().getReference().child("Offres").child(offreId);

                    // Supprimer l'offre de la base de données
                    offreRef.removeValue()
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    // Succès : Offre supprimée avec succès
                                    Toast.makeText(mContext, "L'offre a été supprimée avec succès", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    // Échec : Gérer l'erreur
                                    Toast.makeText(mContext, "Erreur lors de la suppression de l'offre : " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            });



        }

        return convertView;
    }
}
