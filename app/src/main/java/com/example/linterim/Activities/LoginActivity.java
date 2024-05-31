package com.example.linterim.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.example.linterim.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {
    private EditText editTextEmail;
    private EditText editTextPassword;
    private Button buttonConnexion;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Récupérer les références des EditText et du bouton depuis le layout XML
        editTextEmail = findViewById(R.id.editTextPersonName);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonConnexion = (AppCompatButton) findViewById(R.id.button_connexion);

        auth = FirebaseAuth.getInstance();
        buttonConnexion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Récupérer les valeurs saisies par l'utilisateur dans les EditText
                String email = editTextEmail.getText().toString();
                String password = editTextPassword.getText().toString();

                if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                    Toast.makeText(LoginActivity.this, "Veuillez remplir tous les champs", Toast.LENGTH_LONG).show();
                } else {
                    LoginUser(email, password);
                }
            }
        });
    }

    private void LoginUser(String email, String password) {
        auth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        // Connexion réussie
                        String userId = auth.getCurrentUser().getUid();

                        // Vérifier le type d'utilisateur dans la base de données
                        DatabaseReference employersRef = FirebaseDatabase.getInstance().getReference().child("Employeurs").child(userId);
                        employersRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    // L'utilisateur est un Employeur
                                    startActivity(new Intent(LoginActivity.this, DashboardEmployeurActivity.class));
                                    finish(); // Fermer l'activité de connexion
                                } else {
                                    // L'utilisateur n'est pas un Employeur, vérifier s'il est un Candidat
                                    DatabaseReference candidatsRef = FirebaseDatabase.getInstance().getReference().child("Candidats").child(userId);
                                    candidatsRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            if (dataSnapshot.exists()) {
                                                // L'utilisateur est un Candidat
                                                startActivity(new Intent(LoginActivity.this, DashboardCandidatActivity.class));
                                                finish(); // Fermer l'activité de connexion
                                            } else {
                                                // Utilisateur non trouvé dans Employeurs ni dans Candidats (gestion d'erreur)
                                                Toast.makeText(LoginActivity.this, "Type d'utilisateur inconnu", Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {
                                            // Gérer les erreurs de base de données si nécessaire
                                            Toast.makeText(LoginActivity.this, "Erreur de connexion", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                // Gérer les erreurs de base de données si nécessaire
                                Toast.makeText(LoginActivity.this, "Erreur de connexion", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Échec de la connexion, gérer les erreurs Firebase
                        if (e instanceof FirebaseAuthInvalidUserException) {
                            // L'adresse e-mail (nom d'utilisateur) n'existe pas
                            Toast.makeText(LoginActivity.this, "Adresse e-mail incorrecte ou compte inexistant", Toast.LENGTH_SHORT).show();
                        } else if (e instanceof FirebaseAuthInvalidCredentialsException) {
                            // Le mot de passe est incorrect
                            Toast.makeText(LoginActivity.this, "Mot de passe incorrect", Toast.LENGTH_SHORT).show();
                        } else {
                            // Autres erreurs Firebase
                            Toast.makeText(LoginActivity.this, "Échec de connexion: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
