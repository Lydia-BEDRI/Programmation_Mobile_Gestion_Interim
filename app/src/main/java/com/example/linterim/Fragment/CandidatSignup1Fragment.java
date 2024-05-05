package com.example.linterim.Fragment;

import android.os.Bundle;
import android.text.TextUtils;
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

public class CandidatSignup1Fragment extends Fragment {
    private AppCompatButton btnValider;
    private EditText mail, motDePasse, confirmation, nom, prenom;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflater le layout pour ce fragment
        View view = inflater.inflate(R.layout.signup_candidat_1, container, false);
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
                }            }
        });

        // Trouver les vues dans le layout
        btnValider = view.findViewById(R.id.button_validation_insc_candidat);
        nom = view.findViewById(R.id.editTextFamilyNameCandidat);
        mail = view.findViewById(R.id.editTextAdresseMailCandidat);
        prenom = view.findViewById(R.id.editTextFirstNameCandidat);
        motDePasse = view.findViewById(R.id.editTextMotDePasseCandidat);
        confirmation = view.findViewById(R.id.editTextConfirmationMotDePasseCandidat);

        // Définir un OnClickListener sur le bouton "Suivant"
        btnValider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Récupérer les valeurs des champs
                String text_nom = nom.getText().toString();
                String text_adresseMail = mail.getText().toString();
                String text_prenom = prenom.getText().toString();
                String text_motDePasse = motDePasse.getText().toString();
                String text_confirmation = confirmation.getText().toString();

                // Vérifier les conditions
               if (TextUtils.isEmpty(text_prenom) ||TextUtils.isEmpty(text_nom)
               || TextUtils.isEmpty(text_motDePasse) || TextUtils.isEmpty(text_confirmation)){
                   showToast("Tous les champs sont obligatoires");
               } else if (!text_adresseMail.contains("@")) {
                    showToast("L'adresse e-mail doit contenir @");
                } else if (text_motDePasse.length() < 6) {
                    showToast("Le mot de passe doit comporter au moins 6 caractères");
                } else if (!text_confirmation.equals(text_motDePasse)) {
                    showToast("La confirmation du mot de passe ne correspond pas");
                } else {
                    // Si toutes les conditions sont remplies, naviguer vers EmployeurSignup2Fragment
                    navigateToCandidatSignup2Fragment();
                }
            }
        });
    }


    private void navigateToCandidatSignup2Fragment() {
        // Créer un Bundle pour transmettre les données au fragment suivant
        Bundle bundle = new Bundle();
        bundle.putString("nom", nom.getText().toString());
        bundle.putString("prenom", prenom.getText().toString());
        bundle.putString("adresseMail", mail.getText().toString());
        bundle.putString("motDePasse", motDePasse.getText().toString());

        // Créer une instance de EmployeurSignup2Fragment et lui passer le Bundle
       CandidatSignup2Fragment fragment = new CandidatSignup2Fragment();
        fragment.setArguments(bundle);

        // Remplacer le fragment actuel par EmployeurSignup2Fragment
        if (getActivity() != null) {
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainerJSeeker, fragment)
                    .addToBackStack(null)  // Permet de revenir en arrière avec le bouton de retour
                    .commit();
        }
    }



    private void showToast(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
    }
}
