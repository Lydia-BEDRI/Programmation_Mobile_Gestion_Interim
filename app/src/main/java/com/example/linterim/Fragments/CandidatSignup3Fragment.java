package com.example.linterim.Fragments;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
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

import com.example.linterim.Activities.DashboardCandidatActivity;
import com.example.linterim.Models.Candidat;
import com.example.linterim.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class CandidatSignup3Fragment extends Fragment {

    private FirebaseAuth auth;
    private EditText commentaire;
    private Button buttonUpload;
    private TextView textViewFilename;
    private Uri cvUri;
    private ProgressDialog progressDialog;
    private String cvUrl;
    private ActivityResultLauncher<Intent> resultLauncher;

    @SuppressLint("MissingInflatedId")
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

        // Initialiser les vues
        buttonUpload = view.findViewById(R.id.button_uploadCV);
        textViewFilename = view.findViewById(R.id.textview_filename);
        commentaire = view.findViewById(R.id.editTextCommentaireCandidat);
        AppCompatButton btn = view.findViewById(R.id.button_finaliserInscCandidat);

        buttonUpload.setEnabled(true);

        buttonUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectPDF();
            }
        });

        resultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == getActivity().RESULT_OK && result.getData() != null) {
                        Intent data = result.getData();
                        cvUri = data.getData();
                        textViewFilename.setText(data.getDataString().substring(data.getDataString().lastIndexOf("/") + 1));
                    }
                }
        );

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Collecter les informations du candidat
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

                    Candidat candidat = new Candidat(nom, prenom, adresseMail, motDePasse);
                    if (!TextUtils.isEmpty(nationalite)) {
                        candidat.setNationalite(nationalite);
                    }
                    if (!TextUtils.isEmpty(dateDeNaissance)) {
                        candidat.setDateNaissance(dateDeNaissance);
                    }
                    if (!TextUtils.isEmpty(numTel)) {
                        candidat.setTelephone(numTel);
                    }
                    if (!TextUtils.isEmpty(ville)) {
                        candidat.setVille(ville);
                    }

                    String comment_text = commentaire.getText().toString();
                    if (!TextUtils.isEmpty(comment_text)) {
                        candidat.setCommentaires(comment_text);
                    }

                    // Vérifier si un CV a été sélectionné
                    if (cvUri != null) {
                        // Télécharger le CV et enregistrer le candidat après
                        uploadPDFFileFirebaseAndRegister(candidat, cvUri);
                    } else {
                        // Enregistrer directement le candidat sans CV
                        registerCandidat(candidat);
                    }
                } else {
                    Toast.makeText(getActivity(), "Veuillez remplir tous les champs obligatoires", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    private void uploadPDFFileFirebaseAndRegister(Candidat candidat, Uri data) {
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Uploading");
        progressDialog.show();
        StorageReference reference = FirebaseStorage.getInstance().getReference().child("CVs").child(String.valueOf(System.currentTimeMillis()) + ".pdf");
        reference.putFile(data).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                uriTask.addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        cvUrl = uri.toString();
                        progressDialog.dismiss();
                        if (candidat != null) {
                            candidat.setCvUrl(cvUrl);
                            registerCandidat(candidat);
                        }
                    }
                });
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                double progress = (100.0 * snapshot.getBytesTransferred()) / snapshot.getTotalByteCount();
                progressDialog.setMessage("Fichier uploadé.." + (int) progress + "%");
            }
        });
    }

    private void registerCandidat(Candidat candidat) {
        auth.createUserWithEmailAndPassword(candidat.getEmail(), candidat.getMotDePasse())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            String candidatId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                            FirebaseDatabase.getInstance().getReference().child("Candidats")
                                    .child(candidatId).setValue(candidat)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(getActivity(), "Inscription sur la plateforme avec succès!", Toast.LENGTH_LONG).show();
                                                Intent intent = new Intent(getActivity(), DashboardCandidatActivity.class);
                                                startActivity(intent);
                                                Log.d("Fragment3", "Inscription à Realtime Database réussie !");
                                            } else {
                                                Toast.makeText(getActivity(), "Erreur lors de l'inscription: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                                Log.e("Fragment3", "Inscription échouée à Realtime Database", task.getException());
                                            }
                                        }
                                    });
                        } else {
                            Toast.makeText(getActivity(), "Erreur lors de l'inscription: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            Log.e("Fragment3", "Inscription échouée à FirebaseAuth", task.getException());
                        }
                    }
                });
    }

    private void selectPDF() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("application/pdf");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        resultLauncher.launch(Intent.createChooser(intent, "Select PDF"));
    }
}
