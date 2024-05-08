package com.example.linterim.Fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import com.example.linterim.Activity.DashboardCandidatActivity;
import com.example.linterim.Activity.DashboardEmployeurActivity;
import com.example.linterim.Helper.Candidat;
import com.example.linterim.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class CandidatSignup3Fragment extends Fragment {

    private FirebaseAuth auth;
    private EditText cv, commentaire;
    private AppCompatButton btn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.signup_candidat_3, container, false);

        auth = FirebaseAuth.getInstance();  // Initialisation de FirebaseAuth

        ImageView backBtn = view.findViewById(R.id.backBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getFragmentManager() != null) {
                    getFragmentManager().popBackStack();
                }
            }
        });

        cv = view.findViewById(R.id.editTextCVCandidat);
        commentaire = view.findViewById(R.id.editTextCommentaireCandidat);
        btn = view.findViewById(R.id.button_finaliserInscCandidat);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Récupérer toutes les infos saisies
                Bundle args = getArguments();
                if (args != null) {
                    String nom = args.getString("nom", "");
                    String prenom = args.getString("prenom", "");
                    String adresseMail = args.getString("adresseMail", "");
                    String motDePasse = args.getString("motDePasse", "");
                    String nationalite = args.getString("nationalite", "");
                    String dateDeNaissance = args.getString("dateDeNaissance", "");
                    String numTel = args.getString("numTel", "");
                    String ville = args.getString("ville", "");

                    String cv_text = cv.getText().toString();
                    String comment_text = commentaire.getText().toString();
                    // Vérifier les champs obligatoires
                    if (!TextUtils.isEmpty(nom) && !TextUtils.isEmpty(prenom) && !TextUtils.isEmpty(adresseMail) && !TextUtils.isEmpty(motDePasse)) {
                        // Créer un objet Candidat avec les informations obligatoires
                        Candidat candidat = new Candidat(nom, prenom, adresseMail, motDePasse);
                        if (!TextUtils.isEmpty(nationalite) ){
                            candidat.setNationalite(nationalite);
                        }
                        if (!TextUtils.isEmpty(dateDeNaissance) ){
                            candidat.setDateNaissance(dateDeNaissance);
                        }
                        if (!TextUtils.isEmpty(numTel) ){
                            candidat.setTelephone(numTel);
                        }
                        if (!TextUtils.isEmpty(ville) ){
                            candidat.setVille(ville);
                        }
                        if (!TextUtils.isEmpty(cv_text) ){
                            candidat.setCvUrl(cv_text);
                        }
                        if (!TextUtils.isEmpty(comment_text) ){
                            candidat.setCommentaires(comment_text);
                        }
                        // Enregistrer le candidat dans la base de données
                        registerCandidat(candidat);
                        Intent intent = new Intent(getActivity(), DashboardCandidatActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(getActivity(), "Veuillez remplir tous les champs obligatoires", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        return view;
    }

    private void registerCandidat(Candidat candidat) {
        auth.createUserWithEmailAndPassword(candidat.getEmail(), candidat.getMotDePasse())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Récupérer l'ID de l'utilisateur authentifié
                            String candidatId = FirebaseAuth.getInstance().getCurrentUser().getUid();

                            // Enregistrer les informations dans la base de données
                            FirebaseDatabase.getInstance().getReference().child("Candidats")
                                    .child(candidatId).setValue(candidat)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(getActivity(), "Inscription sur la plateforme avec succès!", Toast.LENGTH_LONG).show();

                                                Log.d("CandidatSignup3Fragment", "Inscription à Realtime Database réussie !");
                                                // Naviguer vers l'écran suivant ou afficher un message de succès
                                            } else {
                                                Toast.makeText(getActivity(), "Erreur lors de l'inscription: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                                Log.e("CandidatSignup3Fragment", "Inscription échouée à Realtime Database", task.getException());
                                            }
                                        }
                                    });
                        } else {
                            Toast.makeText(getActivity(), "Erreur lors de l'inscription: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            Log.e("CandidatSignup3Fragment", "Inscription échouée à FirebaseAuth", task.getException());
                        }
                    }
                });
    }
}
