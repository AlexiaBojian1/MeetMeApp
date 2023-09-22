package com.example.presence.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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

public class ModeratorViewAllUserMessagesActivity extends AppCompatActivity {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference messagesRef = db.collection(LabelGetter.dbMessageLbl);
    private MessageRecyclerAdapter adapter;
    RegularUser user;
    TextView nicknameTextView, phoneNumberTextView;
    Button deactivateUserButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moderator_view_all_user_messages);
        nicknameTextView = findViewById(R.id.AllMessagesUserNickname);
        phoneNumberTextView = findViewById(R.id.allMessagesPhoneNumberTextView);
        deactivateUserButton = findViewById(R.id.deactivateUserButton);


        user = getIntent().getParcelableExtra(LabelGetter.appUserLbl);
        if (user == null) {
            user = initTestUser();
        }

        nicknameTextView.setText(user.getNickname());
        phoneNumberTextView.setText(user.getPhoneNumber());
        if (user.isBlocked()) {
            deactivateUserButton.setText("reactivate user");
        } else {
            deactivateUserButton.setText("block user");
        }

        setUpRecyclerView();

        deactivateUserButton.setOnClickListener(v -> {
            user.block();
            if (!user.isBlocked()) {
                Toast.makeText(ModeratorViewAllUserMessagesActivity.this, "User reactivated", Toast.LENGTH_SHORT).show();
                deactivateUserButton.setText("block user");
            } else {
                Toast.makeText(ModeratorViewAllUserMessagesActivity.this, "User deactivated.", Toast.LENGTH_SHORT).show();
                deactivateUserButton.setText("reactivate user");
            }
        });
    }

    private void setUpRecyclerView() {
        Query query = messagesRef.whereEqualTo(LabelGetter.phoneNumberLbl, user.getPhoneNumber()).orderBy(LabelGetter.dbDateLbl, Query.Direction.ASCENDING);
        FirestoreRecyclerOptions<TextMessage> options = new FirestoreRecyclerOptions.Builder<TextMessage>()
                .setQuery(query, new SnapshotParser<TextMessage>() {
                    @NonNull
                    @Override
                    public TextMessage parseSnapshot(@NonNull DocumentSnapshot snapshot) {
                        return new TextMessage(snapshot, getApplicationContext());
                        }}).build();

        adapter = new MessageRecyclerAdapter(options);
        RecyclerView recyclerView = findViewById(R.id.allMessagesRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(null);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new MessageRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                TextMessage messageToPass = new TextMessage(documentSnapshot, getApplicationContext());
                Intent intent = new Intent(ModeratorViewAllUserMessagesActivity.this, ModeratorViewMessageActivity.class);
                intent = CustomMessagePasser.passMessage(intent, messageToPass);
                intent.putExtra(LabelGetter.appUserLbl, user);
                intent.putExtra(LabelGetter.previousLbl, "ModeratorViewAllUserMessagesActivity");
                startActivity(intent);
            }
        });

    }

    private RegularUser initTestUser() {
        RegularUser user = new RegularUser("+2345678", "Error");
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