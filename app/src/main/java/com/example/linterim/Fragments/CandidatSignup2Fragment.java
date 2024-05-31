package com.example.linterim.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import com.example.linterim.R;

public class CandidatSignup2Fragment extends Fragment {
    private AppCompatButton button_suivantInscCand2;
    private EditText nationalite, dateDeNaissance, numTel,ville;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflater le layout pour ce fragment
        View view = inflater.inflate(R.layout.signup_candidat_2, container, false);
        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ImageView backBtn = view.findViewById(R.id.backBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Gérer le clic sur le bouton de retour (backBtn)
                if (getFragmentManager() != null) {
                    getFragmentManager().popBackStack();
                }
            }
        });

        // Trouver les vues dans le layout
        button_suivantInscCand2 = view.findViewById(R.id.button_suivantInscCand2);
        nationalite = view.findViewById(R.id.editTextNatiobaliteCandidat);
        dateDeNaissance = view.findViewById(R.id.editTextDateNaisCandidat);
        numTel = view.findViewById(R.id.editTextNumTelCandidat);
        ville = view.findViewById(R.id.editTextVilleCandidat);

        // Définir un OnClickListener sur le bouton "Suivant"
        button_suivantInscCand2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Récupérer les valeurs des champs
                String text_nationalite = nationalite.getText().toString();
                String text_dateDeNaissance = dateDeNaissance.getText().toString();
                String text_numTel = numTel.getText().toString();
                String text_ville = ville.getText().toString();

                // Vérifier les conditions
//                if (!text_adresseMail.contains("@")) {
//                    showToast("L'adresse e-mail doit contenir @");
//                } else if (text_motDePasse.length() < 6) {
//                    showToast("Le mot de passe doit comporter au moins 6 caractères");
//                } else if (!text_confirmation.equals(text_motDePasse)) {
//                    showToast("La confirmation du mot de passe ne correspond pas");
//                } else {
                // Si toutes les conditions sont remplies, naviguer vers EmployeurSignup2Fragment
                // Toutes les conditions sont remplies,
                // préparer les données pour le fragment suivant

                Bundle args = getArguments();
                if (args != null){
                Bundle bundle = new Bundle();
                bundle.putString("nom",  args.getString("nom",""));
                bundle.putString("prenom",  args.getString("prenom", ""));
                bundle.putString("adresseMail", args.getString("adresseMail", ""));
                bundle.putString("motDePasse",  args.getString("motDePasse", ""));
                bundle.putString("nationalite", text_nationalite);
                bundle.putString("dateDeNaissance", text_dateDeNaissance);
                bundle.putString("numTel", text_numTel);
                bundle.putString("ville", text_ville);



                // Afficher les informations du Bundle dans les logs
                    Log.d("BundleInfo", "Contenu du Bundle :");
                    Log.d("BundleInfo", "Nom : " + bundle.getString("nom"));
                    Log.d("BundleInfo", "Prénom : " + bundle.getString("prenom"));
                    Log.d("BundleInfo", "Adresse e-mail : " + bundle.getString("adresseMail"));
                    Log.d("BundleInfo", "Mot de passe : " + bundle.getString("motDePasse"));
                    Log.d("BundleInfo", "Nationalité : " + bundle.getString("nationalite"));
                    Log.d("BundleInfo", "Date de naissance : " + bundle.getString("dateDeNaissance"));
                    Log.d("BundleInfo", "Numéro de téléphone : " + bundle.getString("numTel"));
                    Log.d("BundleInfo", "Ville : " + bundle.getString("ville"));

                // Créer une instance de EmployeurSignup2Fragment et lui passer le Bundle
                CandidatSignup3Fragment fragment = new CandidatSignup3Fragment ();
                fragment.setArguments(bundle);

                // Remplacer le fragment actuel par EmployeurSignup2Fragment
                if (getActivity() != null) {
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragmentContainerJSeeker, fragment)
                            .addToBackStack(null)  // Permet de revenir en arrière avec le bouton de retour
                            .commit();
                }
            }
            }
        });
    }


    private void showToast(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
    }
}
