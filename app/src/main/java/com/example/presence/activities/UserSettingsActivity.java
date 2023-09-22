package com.example.presence.activities;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.presence.objects.AppUser;
import com.example.presence.helpers.LabelGetter;
import com.example.presence.helpers.NicknameAdapter;
import com.example.presence.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.concurrent.atomic.AtomicBoolean;

public class UserSettingsActivity extends AppCompatActivity {

    EditText nickname;
    Button signOutButton;
    Button deleteAccountButton;
    AlertDialog.Builder builder;
    /** Instance of the Firebase authentication service we use.*/
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    /** AppUser that opened this activity.*/
    AppUser appUser;
    /** NicknameAdapter used by this activity.*/
    NicknameAdapter nicknameAdapter = new NicknameAdapter(UserSettingsActivity.this);

    @Override
    public void onBackPressed() {
        changeNickname();
        // add a delay to allow the nickname to be updated in the database
        SystemClock.sleep(50);
        super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_settings);

        // assign UI elements to their variables
        nickname = findViewById(R.id.changeNicknameEditText);
        signOutButton = findViewById(R.id.signOutButton);
        deleteAccountButton = findViewById(R.id.deleteAccountButton);
        appUser = getIntent().getParcelableExtra(LabelGetter.appUserLbl);

        // Set listeners for buttons
        signOutButton.setOnClickListener(new signOutHandler());
        deleteAccountButton.setOnClickListener(new deleteAccountHandler());
        appUser.updateNickname().addOnCompleteListener(task -> {nickname.setHint(appUser.getNickname() + " (tap to edit)"); });
    }

    private class signOutHandler implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            builder = new AlertDialog.Builder(UserSettingsActivity.this);
            builder.setMessage(R.string.confirm)
                    .setCancelable(true)
                    .setPositiveButton(R.string.confirm_sign_out, (dialog, which) -> logout())
                    .setNegativeButton(R.string.cancel_sign_out, null);
            builder.create().show();


        }
    }

    private class deleteAccountHandler implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            builder = new AlertDialog.Builder(UserSettingsActivity.this);
            builder.setTitle(R.string.delete_account)
                    .setMessage(R.string.delete_account_warning)
                    .setCancelable(true)
                    .setPositiveButton(R.string.confirm_delete_account, (dialog, which) -> {
                        deleteAccount();
                        finish();
                        startActivity(new Intent(UserSettingsActivity.this, LoginActivity.class));
                    })
                    .setNegativeButton(R.string.cancel_delete_account, null);
            builder.create().show();
        }
    }

    private void logout() {
        mAuth.signOut();
        finish();
        startActivity(new Intent(UserSettingsActivity.this, LoginActivity.class));
    }

    private void deleteAccount() {
        //delete the user from the authentication database
        mAuth.getCurrentUser().delete().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(UserSettingsActivity.this, "Account deleted", Toast.LENGTH_SHORT).show();
                NicknameAdapter nicknameAdapter = new NicknameAdapter(UserSettingsActivity.this);
                nicknameAdapter.deleteAccountData(appUser.getPhoneNumber());
            } else {
                Toast.makeText(UserSettingsActivity.this, "Please authenticate and delete your account again", Toast.LENGTH_SHORT).show();
                logout();
            }
        });
    }

    private void changeNickname() {
        String newNickname = nickname.getText().toString();
        if (!nameIsValid(newNickname)) {
            return;
        }
        NicknameAdapter nicknameAdapter = new NicknameAdapter(UserSettingsActivity.this);
        nicknameAdapter.updateNickname(appUser.getPhoneNumber(), newNickname);
    }

    /**
     * Checks if the given name is valid and makes descriptive toasts otherwise.
     * @param name the name to check
     * @return true if the name is valid, false if not.
     */
    private boolean nameIsValid(String name) {
        if (name.trim().isEmpty()) {
            Toast.makeText(UserSettingsActivity.this, "Enter a valid name.",
                    Toast.LENGTH_LONG).show(); // show toast if num empty
            return false;
        }
        if (name.length() > 15) {
            Toast.makeText(UserSettingsActivity.this, "Entered name is too long.",
                    Toast.LENGTH_LONG).show(); // show toast if num empty
            return false;
        }
        if (name.length() < 3) {
            Toast.makeText(UserSettingsActivity.this, "Entered name is too short.",
                    Toast.LENGTH_LONG).show(); // show toast if num empty
            return false;
        }
        AtomicBoolean returnVal = new AtomicBoolean(true);
        nicknameAdapter.countNameOccurrences(name).addOnCompleteListener(task -> {
            if (nicknameAdapter.getNameCount() > 0) {
                Toast.makeText(UserSettingsActivity.this, "Name is already taken.",
                        Toast.LENGTH_LONG).show(); // show toast if num empty
                returnVal.set(false);
            }
        });
        return returnVal.get();

    }
}
