package com.example.presence.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.presence.helpers.LabelGetter;
import com.example.presence.R;
import com.example.presence.objects.RegularUser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

public class VerificationActivity extends AppCompatActivity {

    private EditText verificationCodeField; // create vars to store UI elements
    private Button checkCodeButton;
    private String verificationId; // create var to store verification id
    private String phoneNumber; // stores the phone number

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);

        verificationCodeField = findViewById(R.id.editTextVerification); // assign vars
        checkCodeButton = findViewById(R.id.checkCodeButton);
        verificationId = getIntent().getStringExtra(LabelGetter.verificationIdLbl);
        phoneNumber = getIntent().getStringExtra(LabelGetter.phoneNumberLbl);
        checkCodeButton.setOnClickListener(new VerificationStartHandler()); // assign listener
    }

    private class VerificationStartHandler implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            String verificationCode =
                    verificationCodeField.getText().toString(); // store verification code in string

            if (!verificationCodeIsValid(verificationCode)) {
                Toast.makeText(VerificationActivity.this, "Please enter a valid code.", Toast.LENGTH_LONG).show();
                return; // prematurely return in case verification code has invalid format
            }

            if (verificationId != null) {
                PhoneAuthCredential phoneAuthCredential =
                        PhoneAuthProvider.getCredential(
                                verificationId,
                                verificationCode
                        ); // create a new PhoneAuthCredential (id + code combination)
                FirebaseAuth.getInstance()
                        .signInWithCredential(phoneAuthCredential)
                        .addOnCompleteListener(new VerificationCompleteHandler()); // verify the credential
            }
        }

        private boolean verificationCodeIsValid(String verCode) {
            return !(verCode.trim().isEmpty()); // we should add more checks to see if code is valid
        }
    }

    private class VerificationCompleteHandler implements OnCompleteListener {

        private boolean loginAsMod = false;

        @Override
        public void onComplete(@NonNull Task task) {
            if(task.isSuccessful()) {
                try {
                    loginUser(phoneNumber); // we try to login
                } catch (IllegalStateException e) {
                    Toast.makeText(VerificationActivity.this,
                            "Could not establish connection to user database. Try again later.",
                            Toast.LENGTH_LONG).show(); // if illegal state exception is thrown show this message
                }
            } else {
                Toast.makeText(VerificationActivity.this, "Code entered was incorrect.", Toast.LENGTH_LONG).show();
                // show this message in case verification shows that code is incorrect
            }
        }

        private void loginUser(String phoneNumber)
            throws IllegalStateException {

            FirebaseFirestore db = FirebaseFirestore.getInstance(); // get reference to our database...
            CollectionReference userCollectionReference = db.collection(LabelGetter.dbUserLbl); // get ref to collection of users
            Query userQuery = userCollectionReference
                    .whereEqualTo(LabelGetter.phoneNumberLbl, phoneNumber); // query all documents with the specified phone number

            CollectionReference moderatorCollectionReference = db.collection(LabelGetter.dbModeratorLbl); // get ref to collection of mods
            Query modQuery = moderatorCollectionReference.whereEqualTo(LabelGetter.phoneNumberLbl, phoneNumber);

            modQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        if (!task.getResult().isEmpty()) {
                            loginAsMod = true;
                        }
                    } else {
                        throw new IllegalStateException("Couldn't reach the database");
                    }
                    userQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                if (task.getResult().getDocumentChanges().isEmpty()) { // in case we have new user...
                                    Intent nextIntent = new Intent(getApplicationContext(), RequestTermsAndConditionsActivity.class); // create new intent
                                    nextIntent.putExtra(LabelGetter.phoneNumberLbl, phoneNumber); // pass phone number to next activity.
                                    startActivity(nextIntent);
                                } else if (loginAsMod) {
                                    Intent nextIntent = new Intent(getApplicationContext(), ModeratorActivity.class); //Create new intent
                                    nextIntent.putExtra(LabelGetter.phoneNumberLbl, phoneNumber);
                                    startActivity(nextIntent);
                                } else { // in case we have existing user...
                                    Intent nextIntent = new Intent(getApplicationContext(), HomeActivity.class); // create new intent
                                    nextIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    nextIntent.putExtra(LabelGetter.appUserLbl, new RegularUser(phoneNumber)); // pass appUser to next activity.
                                    startActivity(nextIntent); // start next activity.
                                }
                            } else {
                                throw new IllegalStateException(); // throw exception in case we can't reach database
                            }
                        }
                    });
                }
            });

        }
    }


}