package com.example.linterim.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.linterim.Helpers.MessageAdapterCandidat;
import com.example.linterim.Helpers.MenuCandidatManager;
import com.example.linterim.Models.Message;
import com.example.linterim.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MessagesCandidatActivity extends AppCompatActivity {
    private ListView listViewMsgCandidat;
    private MessageAdapterCandidat adapter;
    private ArrayList<Message> messagesList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.messages_candidat);
        View rootView = findViewById(android.R.id.content);
        MenuCandidatManager.setupMenuItems(rootView, this);

        listViewMsgCandidat = findViewById(R.id.ListViewMsgCandidat);
        messagesList = new ArrayList<>();
        adapter = new MessageAdapterCandidat(this, R.layout.list_msg_item, messagesList);
        listViewMsgCandidat.setAdapter(adapter);

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String currentUserId = currentUser.getUid();
            fetchMessages(currentUserId);
        }
    }

    private void fetchMessages(String userId) {
        DatabaseReference messagesRef = FirebaseDatabase.getInstance().getReference("Messages");

        messagesRef.orderByChild("recipientId").equalTo(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                messagesList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Message message = snapshot.getValue(Message.class);
                    if (message != null) {
                        messagesList.add(message);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle possible errors.
            }
        });
    }
}
