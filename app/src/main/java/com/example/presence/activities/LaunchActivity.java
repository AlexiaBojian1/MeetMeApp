package com.example.presence.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.presence.helpers.LocationHelper;
import com.example.presence.objects.ModeratorUser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import com.example.presence.helpers.LabelGetter;
import com.example.presence.R;
import com.example.presence.objects.RegularUser;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

public class LaunchActivity extends AppCompatActivity {

    private boolean isModUser = false;
    private boolean isRegularUser = false;

    LocationHelper locationHelper;
    Toast permissionToast;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        permissionToast = Toast.makeText(this.getApplicationContext(),
                "Please turn on location permissions in the settings.",
                Toast.LENGTH_LONG);
        if (!locationHelper.locationPermissionGranted()) {
            permissionToast.show();
        } else {
            permissionToast.cancel();
            onPermissionsGranted();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO); // disable night mode
        locationHelper = new LocationHelper(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // check and request location permissions:
        if (!locationHelper.locationPermissionGranted()) {
            locationHelper.requestPermissions();
        } else {
            onPermissionsGranted();
        }
    }

    private void onPermissionsGranted() {
        // checking if current user is signed in and a known user:
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser(); // get current user
        FirebaseFirestore db = FirebaseFirestore.getInstance(); // get handle on db

        if (user == null) {
            Intent nextIntent = new Intent(getApplicationContext(), LoginActivity.class); // create new intent
            startActivity(nextIntent); // start the next activity
        } else {
            checkModUser(db, user);
        }
    }

    private void checkModUser(FirebaseFirestore db, FirebaseUser user) {
        CollectionReference moderatorCollectionReference = db.collection(LabelGetter.dbModeratorLbl); // get ref to collection of mods
        Query modQuery = moderatorCollectionReference
                .whereEqualTo(LabelGetter.phoneNumberLbl, user.getPhoneNumber()); // query to check if number is moderator user
        modQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                isModUser = !task.getResult().getDocumentChanges().isEmpty();

                if (isModUser) {
                    Intent nextIntent = new Intent(getApplicationContext(), ModeratorActivity.class); //Create new intent
                    nextIntent.putExtra(LabelGetter.appUserLbl, new ModeratorUser(user.getPhoneNumber(), "MOD"));
                    startActivity(nextIntent);
                } else {
                    checkRegUser(db, user);
                }
            }
        });
    }

    private void checkRegUser(FirebaseFirestore db, FirebaseUser user) {
        CollectionReference regularUsersCollectionReference = db.collection(LabelGetter.dbUserLbl);
        Query regUserQuery = regularUsersCollectionReference
                .whereEqualTo(LabelGetter.phoneNumberLbl, user.getPhoneNumber()); // query to check if number is registered as regular user
        regUserQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                isRegularUser = !task.getResult().getDocumentChanges().isEmpty();

                if (isRegularUser) {
                    Intent nextIntent = new Intent(getApplicationContext(), HomeActivity.class); // create new intent
                    nextIntent.putExtra(LabelGetter.appUserLbl, new RegularUser(user.getPhoneNumber())); // pass phone number
                    startActivity(nextIntent); // start the next activity
                } else {
                    Intent nextIntent = new Intent(getApplicationContext(), LoginActivity.class); // create new intent
                    startActivity(nextIntent); // start the next activity
                }
            }
        });
    }
}
