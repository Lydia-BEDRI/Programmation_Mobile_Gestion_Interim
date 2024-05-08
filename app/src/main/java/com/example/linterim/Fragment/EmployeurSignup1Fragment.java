package com.example.linterim.Fragment;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.linterim.Activity.SignupActivity;
import com.example.linterim.R;

public class EmployeurSignup1Fragment extends Fragment {

    private Button buttonSuivant;
    private EditText nomEntreprise, adresseMail, motDePasse, confirmation;

    public EmployeurSignup1Fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.signup_employeur_1, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ImageView backBtn = view.findViewById(R.id.backBtn);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Naviguer vers MainActivity
                Intent intent = new Intent(requireActivity(), SignupActivity.class);
                startActivity(intent);
                requireActivity().finish(); // Facultatif : fermer le fragment actuel après la navigation
            }
        });

        // Trouver les vues dans le layout
        buttonSuivant = view.findViewById(R.id.button_suivant);
        nomEntreprise = view.findViewById(R.id.editTextNomEntreprise);
        adresseMail = view.findViewById(R.id.editTextAdresseMail);
        motDePasse = view.findViewById(R.id.editTextMotDePasse);
        confirmation = view.findViewById(R.id.editTextConfirmation);

        // Définir un OnClickListener sur le bouton "Suivant"
        buttonSuivant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Récupérer les valeurs des champs
                String text_nomEntreprise = nomEntreprise.getText().toString();
                String text_adresseMail = adresseMail.getText().toString();
                String text_motDePasse = motDePasse.getText().toString();
                String text_confirmation = confirmation.getText().toString();

                // Vérifier les conditions
                if (text_nomEntreprise.length() < 3) {
                    showToast("Le nom de l'entreprise doit comporter au moins 3 caractères");
                } else if (!text_adresseMail.contains("@")) {
                    showToast("L'adresse e-mail doit contenir @");
                } else if (text_motDePasse.length() < 6) {
                    showToast("Le mot de passe doit comporter au moins 4 caractères");
                } else if (!text_confirmation.equals(text_motDePasse)) {
                    showToast("La confirmation du mot de passe ne correspond pas");
                } else {
                    // Si toutes les conditions sont remplies, naviguer vers EmployeurSignup2Fragment
                    navigateToEmployeurSignup2Fragment();
                }
            }
        });
    }


        private void navigateToEmployeurSignup2Fragment() {
            // Créer un Bundle pour transmettre les données au fragment suivant
            Bundle bundle = new Bundle();
            bundle.putString("nomEntreprise", nomEntreprise.getText().toString());
            bundle.putString("adresseMail", adresseMail.getText().toString());
            bundle.putString("motDePasse", motDePasse.getText().toString());

            // Créer une instance de EmployeurSignup2Fragment et lui passer le Bundle
            EmployeurSignup2Fragment fragment = new EmployeurSignup2Fragment();
            fragment.setArguments(bundle);

            // Remplacer le fragment actuel par EmployeurSignup2Fragment
            if (getActivity() != null) {
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainer, fragment)
                        .addToBackStack(null)  // Permet de revenir en arrière avec le bouton de retour
                        .commit();
            }
        }



    private void showToast(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
    }
}
