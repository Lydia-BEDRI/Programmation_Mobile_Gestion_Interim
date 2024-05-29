package com.example.linterim.Fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import com.example.linterim.Activity.DashboardCandidatActivity;
import com.example.linterim.Models.Candidat;
import com.example.linterim.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class CandidatSignup3Fragment extends Fragment {

    private FirebaseAuth auth;
    private EditText commentaire;
    private AppCompatButton btn;
    private StorageReference storageReference;
    private Uri selectedPdf;
    private ActivityResultLauncher<Intent> resultLauncher;
    private static final int PICK_PDF_REQUEST = 1;
    private Button buttonUpload;
    private TextView textViewFilename;

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

       // cv = view.findViewById(R.id.editTextCVCandidat);

        // traiter l'ajout d'un CV en PDF

        buttonUpload = view.findViewById(R.id.button_uploadCV);
        textViewFilename = view.findViewById(R.id.textview_filename);



        buttonUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFilePicker();
            }
        });

        resultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == getActivity().RESULT_OK && result.getData() != null) {
                        selectedPdf = result.getData().getData();
                        String filePath = selectedPdf.getPath();
                        textViewFilename.setText(filePath);

                        ProgressDialog progressDialog = new ProgressDialog(getActivity());
                        progressDialog.setMessage("Uploading");
                        progressDialog.show();
                        storageReference = FirebaseStorage.getInstance().getReference().child("CVs").child(String.valueOf(System.currentTimeMillis()) + ".pdf");
                        storageReference.putFile(selectedPdf).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        String url = selectedPdf.toString();
                                        Log.d("DownloadURL",url);
                                        progressDialog.dismiss();
                                        Toast.makeText(getActivity(), "fichier uploadé avec succès ", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        });
                    }
                }
        );

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

                   // String cv_text = cv.getText().toString();
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
                        if (selectedPdf != null ){
                            candidat.setCvUrl(storageReference.toString());
                        }
                        if (!TextUtils.isEmpty(comment_text) ){
                            candidat.setCommentaires(comment_text);
                        }
                        // Enregistrer le candidat dans la base de données
                        registerCandidat(candidat);
                    } else {
                        Toast.makeText(getActivity(), "Veuillez remplir tous les champs obligatoires", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        return view;
    }

    private void openFilePicker() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("application/pdf");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        resultLauncher.launch(Intent.createChooser(intent, "Select PDF"));
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
                                                Intent intent = new Intent(getActivity(), DashboardCandidatActivity.class);
                                                startActivity(intent);
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
