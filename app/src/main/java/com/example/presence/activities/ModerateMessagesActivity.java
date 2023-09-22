package com.example.presence.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.presence.helpers.CustomMessagePasser;
import com.example.presence.helpers.LabelGetter;
import com.example.presence.helpers.NicknameAdapter;
import com.example.presence.R;
import com.example.presence.objects.RegularUser;
import com.example.presence.objects.TextMessage;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.firebase.ui.firestore.SnapshotParser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class ModerateMessagesActivity extends AppCompatActivity {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference messagesRef = db.collection(LabelGetter.dbMessageLbl);
    private MessageRecyclerAdapter adapter;
    RegularUser user;
    TextView yourOwnMessagesTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_own_messages);
        yourOwnMessagesTitle = findViewById(R.id.yourOwnMessagesTitle);
        yourOwnMessagesTitle.setText("All messages");


        setUpRecyclerView();
    }

    private void setUpRecyclerView() {
        Query query = messagesRef.orderBy(LabelGetter.dbDateLbl, Query.Direction.ASCENDING);
        FirestoreRecyclerOptions<TextMessage> options = new FirestoreRecyclerOptions.Builder<TextMessage>()
                .setQuery(query, new SnapshotParser<TextMessage>() {
                    @NonNull
                    @Override
                    public TextMessage parseSnapshot(@NonNull DocumentSnapshot snapshot) {
                        return new TextMessage(snapshot, getApplicationContext());
                        }}).build();

        adapter = new MessageRecyclerAdapter(options);
        RecyclerView recyclerView = findViewById(R.id.yourOwnMessagesRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(null);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new MessageRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                TextMessage messageToPass = new TextMessage(documentSnapshot, getApplicationContext());
                NicknameAdapter nicknameAdapter = new NicknameAdapter(ModerateMessagesActivity.this);
                nicknameAdapter.findNickname(messageToPass.getCreatorPhoneNumber())
                        .addOnCompleteListener(task -> {
                            Intent intent = CustomMessagePasser.passMessage(new Intent(ModerateMessagesActivity.this, ModeratorViewMessageActivity.class), messageToPass);
                            user = new RegularUser(messageToPass.getCreatorPhoneNumber(), nicknameAdapter.getNickname());
                            intent.putExtra(LabelGetter.appUserLbl, user);
                            intent.putExtra(LabelGetter.previousLbl, "ModerateMessagesActivity");
                            startActivity(intent);
                        });
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

}