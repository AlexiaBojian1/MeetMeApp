package com.example.presence.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.presence.helpers.LabelGetter;
import com.example.presence.R;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;


public class LoginActivity extends AppCompatActivity {

    EditText phoneNumber; // text field for number
    Button sendButton; // button to send number
    String trimmedPhoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        phoneNumber = findViewById(R.id.editTextPhone); // assign UI elements to their variables
        sendButton = findViewById(R.id.sendVerificationButton);

        sendButton.setOnClickListener(new LoginHandler()); // register number handler as listener
    }

    // If you press back while youre in this screen, it closes the app
    public void onBackPressed() {moveTaskToBack(true);}

    private class LoginHandler implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            trimmedPhoneNumber = phoneNumber.getText().toString().replaceAll("\\s", ""); // trim whitespace
            if (!phoneNumberIsValid(trimmedPhoneNumber)) {
                return;
            }

            PhoneAuthProvider.getInstance().verifyPhoneNumber(
                    trimmedPhoneNumber,
                    120, TimeUnit.SECONDS,
                    LoginActivity.this,
                    new CodeSentHandler());
        }

        private boolean phoneNumberIsValid(String number) {
            if (number.isEmpty()) {
                Toast.makeText(LoginActivity.this, "Phone number is empty.",
                        Toast.LENGTH_LONG).show();
                return false;
            }
            if (number.charAt(0) != '+') {
                Toast.makeText(LoginActivity.this, "Phone number must start with a country code.",
                        Toast.LENGTH_LONG).show();
                return false;
            }
            if (!PhoneNumberUtils.isWellFormedSmsAddress(number)) {
                Toast.makeText(LoginActivity.this, "Phone number is not valid.",
                        Toast.LENGTH_LONG).show();
                return false;
            }
            return true;
        }
    }

    private class CodeSentHandler extends PhoneAuthProvider.OnVerificationStateChangedCallbacks {
        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            // show that authentication failed:
            Toast.makeText(LoginActivity.this,
                    e.getMessage(), Toast.LENGTH_LONG).show();
        }

        @Override
        public void onCodeSent(@NonNull String verificationId,
                               @NonNull PhoneAuthProvider.ForceResendingToken token) {
            Intent nextIntent = new Intent(getApplicationContext(), VerificationActivity.class); // create new intent...
            nextIntent.putExtra(LabelGetter.phoneNumberLbl, trimmedPhoneNumber); // pass required data into intent...
            nextIntent.putExtra(LabelGetter.verificationIdLbl, verificationId);
            startActivity(nextIntent); // start verification activity, with new intent.
        }
    }
}