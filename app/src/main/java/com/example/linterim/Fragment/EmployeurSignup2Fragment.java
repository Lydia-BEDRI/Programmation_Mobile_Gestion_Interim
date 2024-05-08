package com.example.linterim.Fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.linterim.Activity.DashboardEmployeurActivity;
import com.example.linterim.Activity.EmployerActivity;
import com.example.linterim.Helper.Employeur;
import com.example.linterim.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class EmployeurSignup2Fragment extends Fragment {

    private TextView textViewAdresseEntreprise;
    private TextView textViewNumTel;
    private TextView textViewLienLKIN,textViewSiteWeb;
    private Button validerInsc;

    private FirebaseAuth auth;
    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.signup_employeur_2, container, false);

        textViewAdresseEntreprise = view.findViewById(R.id.editTextAdresseEntreprise);
        textViewNumTel = view.findViewById(R.id.editTextTelEntreprise);
        textViewLienLKIN = view.findViewById(R.id.editTextLienLinkedIn);
        textViewSiteWeb = view.findViewById(R.id.editTextSiteWebEntreprise);
        validerInsc = view.findViewById(R.id.button_ValiderInscEmp);

        validerInsc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String AdresseEntreprise = textViewAdresseEntreprise.getText().toString();
                String NumTel = textViewNumTel.getText().toString();
                String LienLKIN = textViewLienLKIN.getText().toString();
                String SiteWeb = textViewSiteWeb.getText().toString();

                auth = FirebaseAuth.getInstance();

                // Récupérer les arguments (bundle) transmis depuis EmployeurSignup1Fragment
                Bundle args = getArguments();
                if (args != null) {
                    String nomEntreprise = args.getString("nomEntreprise", "");
                    String adresseMail = args.getString("adresseMail", "");
                    String motDePasse = args.getString("motDePasse", "");
                    Log.d("EmployeurSignup2Fragment",nomEntreprise);
                    Log.d("EmployeurSignup2Fragment",adresseMail);
                    Log.d("EmployeurSignup2Fragment",motDePasse);

                    if (!TextUtils.isEmpty(AdresseEntreprise) && !TextUtils.isEmpty(NumTel) &&
                            !TextUtils.isEmpty(LienLKIN) && !TextUtils.isEmpty(SiteWeb))
                    {
                        registerEmployeFacultativeInfos(nomEntreprise,adresseMail,motDePasse,AdresseEntreprise,NumTel,SiteWeb,LienLKIN);
                        Intent intent = new Intent(getActivity(), DashboardEmployeurActivity.class);
                        startActivity(intent);
                    }
                }
            }
        });



        return view;
    }

//    private void registerEmploye(String adresseMail, String motDePasse) {
//        auth.createUserWithEmailAndPassword(adresseMail, motDePasse).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//            @Override
//            public void onComplete(@NonNull Task<AuthResult> task) {
//                if (task.isSuccessful()) {
//                    // Accéder au contexte de l'activité parente pour afficher le Toast
//                    if (getActivity() instanceof EmployerActivity) {
//                        Toast.makeText(getActivity(), "Inscription à Authentication réussie!", Toast.LENGTH_SHORT).show();
//                    }
//                } else {
//                    Toast.makeText(getActivity(), "Erreur lors de l'inscription: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
//                    Log.e("EmployeurSignup2Fragment", "Inscription à Authentication échouée", task.getException());
//                }
//            }
//        });
//    }


        private void registerEmployeFacultativeInfos(String nomEntreprise, String adresseMail, String motDePasse,
                String adresseEntreprise, String numTel,String siteWeb, String lienLKIN) {

            auth.createUserWithEmailAndPassword(adresseMail, motDePasse)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Récupérer l'ID de l'utilisateur authentifié
                                String employeurId = FirebaseAuth.getInstance().getCurrentUser().getUid();

                                // Créer un objet Employeur avec les informations
                                Employeur employeur = new Employeur(employeurId, nomEntreprise, adresseMail, numTel,
                                        adresseEntreprise,siteWeb, lienLKIN);

                                // Enregistrer les informations dans la base de données
                                FirebaseDatabase.getInstance().getReference().child("Employeurs")
                                        .child(employeurId).setValue(employeur)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Log.e("EmployeurSignup2Fragment", "Inscription à Realtime réussie !", task.getException());
                                                } else {
                                                    Log.e("EmployeurSignup2Fragment", "Inscription à Realtime échouée", task.getException());
                                                }
                                            }
                                        });
                            } else {
                                Toast.makeText(getActivity(), "Erreur lors de l'inscription: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                Log.e("EmployeurSignup2Fragment", "Inscription échouée", task.getException());
                            }
                        }
                    });
        }


}

