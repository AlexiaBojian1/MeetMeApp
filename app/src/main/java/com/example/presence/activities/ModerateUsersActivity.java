package com.example.presence.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.presence.helpers.LabelGetter;
import com.example.presence.R;
import com.example.presence.objects.RegularUser;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.firebase.ui.firestore.SnapshotParser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class ModerateUsersActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private UsersAdapter adapter;
    private Query query;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moderate_users);

        query = FirebaseFirestore.getInstance().collection(LabelGetter.dbUserLbl).orderBy(LabelGetter.nicknameLbl);
        recyclerView = findViewById(R.id.recycler1);
        recyclerView.setLayoutManager((new LinearLayoutManager(this)));


        FirestoreRecyclerOptions<RegularUser> options = new FirestoreRecyclerOptions.Builder<RegularUser>()
                .setQuery(query, new SnapshotParser<RegularUser>() {
                    @NonNull
                    @Override
                    public RegularUser parseSnapshot(DocumentSnapshot snapshot) {
                        return new RegularUser(snapshot);
                    }
                }).build();


        adapter = new UsersAdapter(options);
        recyclerView.setItemAnimator(null);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new UsersAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                RegularUser userToPass = new RegularUser(documentSnapshot);
                Intent intent = new Intent(ModerateUsersActivity.this, ModeratorViewAllUserMessagesActivity.class);
                intent.putExtra(LabelGetter.appUserLbl, userToPass);
                startActivity(intent);
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