package com.example.presence.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.presence.objects.AppUser;
import com.example.presence.helpers.LabelGetter;
import com.example.presence.fragments.MapFragment;
import com.example.presence.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapsSdkInitializedCallback;

public class HomeActivity extends AppCompatActivity implements OnMapsSdkInitializedCallback {

    ImageButton addMessageButton, yourMessagesButton;
    ImageButton userSettingsButton;
    TextView displayedUsername;
    AppUser appUser;
    FusedLocationProviderClient fusedLocationClient;

    @Override
    public void onBackPressed() { } // prevents you from going back to launch screen via home

    @Override
    protected void onResume() {
        super.onResume();
        // On coming back to home activity, it will update the displayed username, this may slow down the app, but im not too sure
        appUser.updateNickname().addOnCompleteListener(task -> {displayedUsername.setText(appUser.getNickname());});
    }

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //No call for super(). Bug on API Level > 11.
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        MapsInitializer.initialize(getApplicationContext(), MapsInitializer.Renderer.LATEST, this);

        addMessageButton = findViewById(R.id.addButton); // get a handle on the add button.
        userSettingsButton = findViewById(R.id.userSettingsButton);
        yourMessagesButton = findViewById(R.id.messagesButton);
        displayedUsername = findViewById(R.id.homeScreenUsername);
        appUser = getIntent().getParcelableExtra(LabelGetter.appUserLbl);

        addMessageButton.setOnClickListener(new AddMessageHandler());
        yourMessagesButton.setOnClickListener(new YourMessagesHandler());
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        userSettingsButton.setOnClickListener(new UserSettingsHandler());

        // Displays the username of the user on the home screen
        appUser.updateNickname().addOnCompleteListener(task -> {
            displayedUsername.setText(appUser.getNickname());
        });

        // Set up a bundle for the fragment
        Bundle appUserBundle = new Bundle();
        appUserBundle.putParcelable(LabelGetter.appUserLbl, appUser);
        Fragment fragment = new MapFragment();
        fragment.setArguments(appUserBundle);

        // Open fragment
        getSupportFragmentManager()
                .beginTransaction().replace(R.id.frame_layout, fragment)
                .commit();
        getSupportFragmentManager().
                beginTransaction().hide(fragment).commit();
    }

    @Override
    public void onMapsSdkInitialized(@NonNull MapsInitializer.Renderer renderer) {
        switch (renderer) {
            case LATEST:
                Log.d("MapsDebug", "The latest version of the renderer is used.");
                break;
            case LEGACY:
                Log.d("MapsDebug", "The legacy version of the renderer is used.");
                break;
        }
    }

    private class AddMessageHandler implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            // create new intent
            Intent nextIntent = new Intent(getApplicationContext(), AddMessageActivity.class);
            nextIntent.putExtra(LabelGetter.appUserLbl, appUser);
            startActivity(nextIntent);
        }
    }

    private class UserSettingsHandler implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Intent nextIntent = new Intent(getApplicationContext(), UserSettingsActivity.class);
            nextIntent.putExtra(LabelGetter.appUserLbl, appUser);
            startActivity(nextIntent);
        }
    }

    private class YourMessagesHandler implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Intent nextIntent = new Intent(getApplicationContext(), YourOwnMessagesActivity.class);
            nextIntent.putExtra(LabelGetter.appUserLbl, appUser);
            startActivity(nextIntent);
        }
    }
}