package com.example.presence.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.presence.helpers.LabelGetter;
import com.example.presence.R;
import com.example.presence.objects.Report;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class ModeratorViewAllReportsActivity extends AppCompatActivity {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference reportsRef = db.collection(LabelGetter.dbReportLbl);
    private ReportRecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_own_messages);
        TextView title = findViewById(R.id.yourOwnMessagesTitle);
        title.setText("Filed reports:");

        setUpRecyclerView();
    }

    private void setUpRecyclerView() {
        Query query = reportsRef.orderBy(LabelGetter.idLbl, Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<Report> options = new FirestoreRecyclerOptions.Builder<Report>()
                .setQuery(query, Report.class).build();
        adapter = new ReportRecyclerAdapter(options);
        RecyclerView recyclerView = findViewById(R.id.yourOwnMessagesRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(null);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new ReportRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                Intent intent = new Intent(ModeratorViewAllReportsActivity.this, ModeratorViewSingleReportActivity.class);
                intent.putExtra(LabelGetter.reportLbl, documentSnapshot.toObject(Report.class));
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
