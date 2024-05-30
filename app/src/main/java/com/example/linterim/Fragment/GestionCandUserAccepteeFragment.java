package com.example.linterim.Fragment;

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

import com.example.linterim.Helpers.CandAcceptUserAdapter;
import com.example.linterim.Helpers.CandEnCoursUserAdapter;
import com.example.linterim.Helpers.MenuCandidatManager;
import com.example.linterim.Models.Candidature;
import com.example.linterim.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class GestionCandUserAccepteeFragment extends Fragment {


    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.gestion_candidature_acceptee_user, container, false);

        MenuCandidatManager.setupMenuItems(rootView, getContext());
        ImageView backBtn = rootView.findViewById(R.id.backBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the back button click
                if (getFragmentManager() != null) {
                    getFragmentManager().popBackStack();
                }
            }
        });

        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) ListView listView = rootView.findViewById(R.id.ListViewCandidaturesAcceptUser);
        ArrayList<Candidature> arrayList = new ArrayList<>();
        CandAcceptUserAdapter candEnCoursAdapter = new CandAcceptUserAdapter(getContext(), R.layout.list_cand_accept_user_item, arrayList);
        listView.setAdapter(candEnCoursAdapter);

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String currentCandidatId = currentUser.getUid();
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Candidatures");

            Query query = databaseReference.orderByChild("candidat_id").equalTo(currentCandidatId);
            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    arrayList.clear(); // Clear the old data
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        // Retrieve each candidature from the database
                        Candidature candidature = snapshot.getValue(Candidature.class);
                        if (candidature != null && "acceptée".equals(candidature.getStatut())) {
                            // Add the pending candidature to the list
                            arrayList.add(candidature);
                        }
                    }
                    // Notify the adapter of the changes
                    candEnCoursAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle data retrieval errors
                    Toast.makeText(requireContext(), "Une erreur s'est produite lors de la récupération des données !", Toast.LENGTH_LONG).show();
                }
            });
        }


    LinearLayout LLcandEnCours = rootView.findViewById(R.id.LLcandEnCoursUser);
    LinearLayout LLcandAcceptee = rootView.findViewById(R.id.LLcandEnAccepteeUser);

        LLcandAcceptee.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // Stay on the same fragment, so no action here
        }
    });

        LLcandEnCours.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // Launch GestionCandEmpAccepteeFragment
            GestionCandUserEnCoursFragment accepteeFragment = new GestionCandUserEnCoursFragment();
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, accepteeFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }
    });

        return rootView;
}}
