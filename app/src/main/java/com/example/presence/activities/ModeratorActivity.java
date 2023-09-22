package com.example.presence.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.presence.helpers.LabelGetter;
import com.example.presence.R;
import com.google.firebase.auth.FirebaseAuth;

public class ModeratorActivity extends AppCompatActivity {

    Button moderateUsersButton;
    Button moderateMessagesButton;
    Button reportButton;
    ImageButton moderatorSignOutButton;
    AlertDialog.Builder builder;
    private String phoneNumber;

    @Override
    public void onBackPressed() {}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moderator);

        moderateMessagesButton = findViewById(R.id.moderateMessagesButton);
        moderateUsersButton = findViewById(R.id.moderateUsersButton);
        reportButton = findViewById(R.id.reportsButton);
        moderatorSignOutButton = findViewById(R.id.moderatorSignOutButton);
        phoneNumber = getIntent().getStringExtra(LabelGetter.phoneNumberLbl);

        moderateMessagesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // start moderate messages activity, with new intent
                Intent nextIntent = new Intent(getApplicationContext(), ModerateMessagesActivity.class);
                startActivity(nextIntent);
            }
        });

        moderateUsersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // start moderate users activity, with new intent.
                Intent nextIntent = new Intent(getApplicationContext(), ModerateUsersActivity.class);
                nextIntent.putExtra(LabelGetter.phoneNumberLbl, phoneNumber);
                startActivity(nextIntent);
            }
        });

        reportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start view reports activity, with new intent
                Intent viewReports = new Intent(getApplicationContext(), ModeratorViewAllReportsActivity.class);
                viewReports.putExtra(LabelGetter.phoneNumberLbl, phoneNumber);
                startActivity(viewReports);
            }
        });

        moderatorSignOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // build a dialog to ask user to confirm they want to sign out
                builder = new AlertDialog.Builder(ModeratorActivity.this);
                builder.setMessage(R.string.confirm)
                        .setCancelable(true)
                        .setPositiveButton(R.string.confirm_sign_out, (dialog, which) -> {
                            FirebaseAuth.getInstance().signOut();
                            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                            finish();
                        })
                        .setNegativeButton(R.string.cancel_sign_out, null);
                builder
                        .create()
                        .show();
            }
        });

    }
}