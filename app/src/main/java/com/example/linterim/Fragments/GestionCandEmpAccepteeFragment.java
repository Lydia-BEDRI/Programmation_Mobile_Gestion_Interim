package com.example.linterim.Fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.linterim.Adapters.CandAcceptAdapter;
import com.example.linterim.Helpers.MenuEmployeurManager;
import com.example.linterim.Models.Candidature;
import com.example.linterim.Models.Offre;
import com.example.linterim.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class GestionCandEmpAccepteeFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.gestion_candidature_acceptee_emp, container, false);
        MenuEmployeurManager.setupMenuItems(rootView, getContext());

        ImageView backBtn = rootView.findViewById(R.id.backBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Gérer le clic sur le bouton de retour (backBtn)
                if (getFragmentManager() != null) {
                    getFragmentManager().popBackStack();
                }
            }
        });

        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) ListView listView = rootView.findViewById(R.id.ListViewCandidaturesAcceptees);
        ArrayList arrayList = new ArrayList();
        CandAcceptAdapter adapter = new CandAcceptAdapter(getContext(),R.layout.list_cand_acceptee_item,arrayList);
        listView.setAdapter(adapter);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Candidatures");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                arrayList.clear(); // Efface les anciennes données
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    // Récupérer chaque candidature de la base de données
                    Candidature candidature = snapshot.getValue(Candidature.class);
                    if (candidature != null) {

                        if (candidature.getStatut().equals("acceptée")) {
                            // Récupérer l'offre associée à la candidature
                            String offreId = candidature.getOffre_id();
                            DatabaseReference offreRef = FirebaseDatabase.getInstance().getReference().child("Offres").child(offreId);
                            offreRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    Offre offre = dataSnapshot.getValue(Offre.class);
                                    if (offre != null) {
                                        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                                        if (currentUser != null) {
                                            String currentEmployerId = currentUser.getUid();
                                            // Vérifier si l'offre appartient à l'employeur en cours
                                            if (offre.getEmployeur_id().equals(currentEmployerId)) {
                                                // Ajouter la candidature à la liste
                                                arrayList.add(candidature);
                                                // Notifier l'adaptateur des changements
                                                adapter.notifyDataSetChanged();
                                            }
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    Toast.makeText(requireContext(), "Une erreur s'est produite lors de la récupération des données ! ", Toast.LENGTH_LONG).show();

                                }

                            });
                        }}
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Gestion des erreurs de récupération de données
                Toast.makeText(requireContext(), "Une erreur s'est produite lors de la récupération des données ! ", Toast.LENGTH_LONG).show();
            }
        });

        LinearLayout LLcandEnCours = rootView.findViewById(R.id.LLcandEnCours);
        LinearLayout LLcandAcceptee = rootView.findViewById(R.id.LLcandEnAcceptee);

        LLcandAcceptee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Rester sur le même fragment, donc pas d'action ici
            }
        });

        LLcandEnCours.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lancer GestionCandEmpEnCoursFragment
                GestionCandEmpEnCoursFragment enCoursFragment = new GestionCandEmpEnCoursFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, enCoursFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        return rootView;
    }
}
