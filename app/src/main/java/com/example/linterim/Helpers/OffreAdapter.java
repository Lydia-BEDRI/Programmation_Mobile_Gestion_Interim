package com.example.linterim.Helpers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.linterim.Models.Offre;
import com.example.linterim.R;

import java.util.ArrayList;

public class OffreAdapter extends ArrayAdapter<Offre> {
    private Context mcContext;
    private int mResource;
    public OffreAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Offre> objects) {
        super(context, resource, objects);
        this.mcContext = context;
        this.mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(mcContext);
        convertView = layoutInflater.inflate(mResource,parent,false);

        TextView titreffre = convertView.findViewById(R.id.textViewTitreOffre);

        TextView desription = convertView.findViewById(R.id.textViewDescriptionOffre);

        TextView lieu = convertView.findViewById(R.id.textViewLieuOffre);

        TextView remuneration = convertView.findViewById(R.id.textViewRemunerationOffre);

        TextView typeContrat = convertView.findViewById(R.id.textViewTypeContratOffre);

        TextView periode = convertView.findViewById(R.id.textViewPeriodeOffre);

        titreffre.setText(getItem(position).getTitre());
        desription.setText(getItem(position).getDescription());
        lieu.setText(getItem(position).getLieu());
        remuneration.setText(getItem(position).getRemuneration());
        typeContrat.setText(getItem(position).getType_contract());
        periode.setText(getItem(position).getPeriode());


        return convertView;

    }
}
