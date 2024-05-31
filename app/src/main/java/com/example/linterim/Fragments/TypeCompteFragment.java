package com.example.linterim.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.fragment.app.Fragment;

import com.example.linterim.R;

public class TypeCompteFragment extends Fragment {

    private RadioGroup radioGroupOptions;
    private RadioButton radioButtonJobSeeker;
    private RadioButton radioButtonEmployer;
    private Button buttonNext;

    public TypeCompteFragment() {
        // Constructeur vide requis
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflater le layout pour ce fragment
        View view = inflater.inflate(R.layout.type_compte, container, false);

        // Références aux vues du layout fragment_type_compte.xml
        radioGroupOptions = view.findViewById(R.id.radioGroupOptions);
        radioButtonJobSeeker = view.findViewById(R.id.radioButtonJobSeeker);
        radioButtonEmployer = view.findViewById(R.id.radioButtonEmployer);
        buttonNext = view.findViewById(R.id.buttonNext);

        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedId = radioGroupOptions.getCheckedRadioButtonId();
                Log.d("TypeCompteFragment", "Selected RadioButton ID: " + selectedId);
                if (selectedId == radioButtonJobSeeker.getId()) {
                    Log.d("TypeCompteFragment", "Launching CandidatSignup1Fragment");
                } else if (selectedId == radioButtonEmployer.getId()) {
                    Log.d("TypeCompteFragment", "Launching EmployeurSignup1Fragment");
                }
            }
        });


        return view;
    }


}


