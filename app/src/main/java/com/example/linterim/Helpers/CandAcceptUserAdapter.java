package com.example.linterim.Helpers;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.linterim.Activity.ContactEmpActivity;
import com.example.linterim.Models.Candidat;
import com.example.linterim.Models.Candidature;
import com.example.linterim.Models.Employeur;
import com.example.linterim.Models.Message;
import com.example.linterim.Models.Offre;
import com.example.linterim.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class CandAcceptUserAdapter extends ArrayAdapter<Candidature> {
    private Context mContext;
    private int mResource;
    private DatabaseReference mCandidatRef;
    private DatabaseReference mOffreRef;
    private DatabaseReference mEmployeurRef;
    private DatabaseReference mMessagesRef;

    public CandAcceptUserAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Candidature> arrayList) {
        super(context, resource, arrayList);
        this.mContext = context;
        this.mResource = resource;
        this.mCandidatRef = FirebaseDatabase.getInstance().getReference().child("Candidats");
        this.mOffreRef = FirebaseDatabase.getInstance().getReference().child("Offres");
        this.mEmployeurRef = FirebaseDatabase.getInstance().getReference().child("Employeurs");
        this.mMessagesRef = FirebaseDatabase.getInstance().getReference().child("Messages");
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

        Button buttonAccepter = convertView.findViewById(R.id.buttonAccepter);
        Button buttonRefuser = convertView.findViewById(R.id.buttonRefuser);
        Button buttonContact = convertView.findViewById(R.id.buttonContact);
        Button buttonItineraire = convertView.findViewById(R.id.buttonItineraire);

        Candidature currentCandidature = getItem(position);

        if (currentCandidature != null) {
            String offre_Id = currentCandidature.getOffre_id();
            String candidat_Id = currentCandidature.getCandidat_id();

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

                                    // Déplacement de la logique de onClick pour le bouton de contact ici
                                    buttonContact.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent intent = new Intent(mContext, ContactEmpActivity.class);
                                            intent.putExtra("employeurId", employeurId);
                                            intent.putExtra("offreId", offre_Id);
                                            intent.putExtra("nomEntreprise", employeur.getNom_entreprise());
                                            mContext.startActivity(intent);
                                        }
                                    });

                                    // Gestion du bouton d'itinéraire
                                    buttonItineraire.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            new AlertDialog.Builder(mContext)
                                                    .setTitle("Voir Itinéraire")
                                                    .setMessage("Voulez-vous voir l'itinéraire vers " + employeur.getNom_entreprise() + " situé à l'adresse suivante : " + employeur.getAdresse() + " ?")                                                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            // Récupérer l'adresse de l'employeur
                                                            String adresseEmployeur = employeur.getAdresse();
                                                            if (adresseEmployeur != null && !adresseEmployeur.isEmpty()) {
                                                                // Utiliser l'adresse de l'employeur pour afficher l'itinéraire
                                                                Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + Uri.encode(adresseEmployeur));
                                                                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                                                                mapIntent.setPackage("com.google.android.apps.maps");
                                                                if (mapIntent.resolveActivity(mContext.getPackageManager()) != null) {
                                                                    mContext.startActivity(mapIntent);
                                                                } else {
                                                                    Toast.makeText(mContext, "Google Maps n'est pas installé ou n'est pas disponible.", Toast.LENGTH_SHORT).show();
                                                                }
                                                            } else {
                                                                Toast.makeText(mContext, "L'adresse de l'entreprise n'est pas disponible.", Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                    })
                                                    .setNegativeButton(android.R.string.no, null)
                                                    .show();
                                        }
                                    });

                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                // Handle potential errors here
                            }
                        });


                        // Fetch the candidate details
                        mCandidatRef.child(candidat_Id).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot candidatSnapshot) {
                                Candidat candidat = candidatSnapshot.getValue(Candidat.class);
                                if (candidat != null) {
                                    String candidatName = candidat.getNom();
                                    String candidatSurname = candidat.getPrenom();

                                    // Update button listeners to include candidate name and offer title
                                    buttonAccepter.setOnClickListener(v -> new AlertDialog.Builder(mContext)
                                            .setTitle("Confirmer Acceptation")
                                            .setMessage("Êtes-vous sûr de vouloir accepter cette offre ?")
                                            .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                                                // Remove the candidature
                                                removeCandidature(currentCandidature);

                                                // Send a message to the employer
                                                sendMessageToEmployeur("accepté", employeurId, candidat_Id, offre_Id, candidatName, candidatSurname, offre.getTitre());

                                                // Show a dialog to congratulate the candidate
                                                new AlertDialog.Builder(mContext)
                                                        .setTitle("Félicitations")
                                                        .setMessage("Vous avez accepté l'offre. Vous recevrez bientôt un email avec plus de détails.")
                                                        .setPositiveButton(android.R.string.ok, null)
                                                        .show();
                                            })
                                            .setNegativeButton(android.R.string.no, null)
                                            .show());

                                    buttonRefuser.setOnClickListener(v -> new AlertDialog.Builder(mContext)
                                            .setTitle("Confirmer Refus")
                                            .setMessage("Êtes-vous sûr de vouloir refuser cette offre ?")
                                            .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                                                // Remove the candidature
                                                removeCandidature(currentCandidature);

                                                // Send a message to the employer
                                                sendMessageToEmployeur("refusé", employeurId, candidat_Id, offre_Id, candidatName, candidatSurname, offre.getTitre());
                                            })
                                            .setNegativeButton(android.R.string.no, null)
                                            .show());
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

        return convertView;
    }

    private void sendMessageToEmployeur(String action, String employeurId, String candidatId, String offreId, String candidatName, String offreTitle) {
        String message = "Le candidat " + candidatName + " a " + action + " l'offre '" + offreTitle + "'.";
        String messageId = mMessagesRef.push().getKey();

        if (messageId != null) {
            mMessagesRef.child(messageId).setValue(new Message("notification", employeurId, message, candidatId, offreId, getCurrentFormattedDate()));
        }
    }
    private void removeCandidature(Candidature candidature) {
        DatabaseReference candidaturesRef = FirebaseDatabase.getInstance().getReference().child("Candidatures");
        candidaturesRef.child(candidature.getCandidature_id()).removeValue();
    }

    private void sendMessageToEmployeur(String action, String employeurId, String candidatId, String offreId, String candidatName, String candidatSurname, String offreTitle) {
        String message = "Le candidat " + candidatName + " " + candidatSurname + " a " + action + " l'offre '" + offreTitle + "'.";
        String messageId = mMessagesRef.push().getKey();

        if (messageId != null) {
            mMessagesRef.child(messageId).setValue(new Message("notification", employeurId, message, candidatId, offreId, getCurrentFormattedDate()));
        }
    }


    private String getCurrentFormattedDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        return sdf.format(new Date());
    }


}
