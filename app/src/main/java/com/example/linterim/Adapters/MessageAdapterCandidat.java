package com.example.linterim.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.linterim.Models.Message;
import com.example.linterim.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MessageAdapterCandidat extends ArrayAdapter<Message> {
    private Context mcContext;
    private int mResource;
    private DatabaseReference employeursRef;

    public MessageAdapterCandidat(@NonNull Context context, int resource, @NonNull ArrayList<Message> objects) {
        super(context, resource, objects);
        this.mcContext = context;
        this.mResource = resource;
        this.employeursRef = FirebaseDatabase.getInstance().getReference("Employeurs");
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(mcContext);
            convertView = layoutInflater.inflate(mResource, parent, false);
        }

        TextView type = convertView.findViewById(R.id.type);
        TextView sender = convertView.findViewById(R.id.sender);
        TextView date = convertView.findViewById(R.id.date);
        TextView contentMsg = convertView.findViewById(R.id.contentMsg);

        Message message = getItem(position);

        if (message != null) {
            type.setText(message.getType());
            date.setText(message.getDate());
            contentMsg.setText(message.getContent());

            String senderId = message.getSenderId();
            if (senderId != null) {
                // Fetching the sender's name from Firebase
                employeursRef.orderByChild("employeur_id").equalTo(senderId).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            for (DataSnapshot employeurSnapshot : snapshot.getChildren()) {
                                String employeurName = employeurSnapshot.child("nom_entreprise").getValue(String.class);
                                sender.setText(employeurName);
                                break;
                            }
                        } else {
                            sender.setText("Unknown");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        sender.setText("Error");
                    }
                });
            } else {
                sender.setText("Unknown");
            }
        }

        return convertView;
    }
}
