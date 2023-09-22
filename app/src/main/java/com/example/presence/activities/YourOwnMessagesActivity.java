package com.example.presence.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.presence.objects.AppUser;
import com.example.presence.helpers.CustomMessagePasser;
import com.example.presence.helpers.LabelGetter;
import com.example.presence.R;
import com.example.presence.objects.RegularUser;
import com.example.presence.objects.TextMessage;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.firebase.ui.firestore.SnapshotParser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class YourOwnMessagesActivity extends AppCompatActivity {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private AppUser appUser;
    private CollectionReference messagesRef = db.collection(LabelGetter.dbMessageLbl);
    private MessageRecyclerAdapter adapter;

    @Override
    public void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_own_messages);

        appUser = getIntent().getParcelableExtra(LabelGetter.appUserLbl);
        if (appUser == null) {
            appUser = initTestUser();
        }

        setUpRecyclerView();
    }

    private void setUpRecyclerView() {
        Query query = messagesRef
                .whereEqualTo(LabelGetter.phoneNumberLbl, appUser.getPhoneNumber())
                .orderBy(LabelGetter.dbDateLbl, Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<TextMessage> options = new FirestoreRecyclerOptions.Builder<TextMessage>()
                .setQuery(query, new SnapshotParser<TextMessage>() {
                    @NonNull
                    @Override
                    public TextMessage parseSnapshot(@NonNull DocumentSnapshot snapshot) {
                        return new TextMessage(snapshot, getApplicationContext());
                        }}).build();

        adapter = new MessageRecyclerAdapter(options);
        RecyclerView recyclerView = findViewById(R.id.yourOwnMessagesRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setItemAnimator(null);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new MessageRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                Intent intent = new Intent(YourOwnMessagesActivity.this, ModeratorViewMessageActivity.class);
                TextMessage messageToPass = new TextMessage(documentSnapshot, getApplicationContext());
                intent = CustomMessagePasser.passMessage(intent, messageToPass);
                intent.putExtra(LabelGetter.appUserLbl, appUser);
                intent.putExtra(LabelGetter.previousLbl, "YourOwnMessagesActivity");
                startActivity(intent);
            }
        });
    }

    private AppUser initTestUser() {
        AppUser user = new RegularUser("+11234567890", "ahhh");
        return user;
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

