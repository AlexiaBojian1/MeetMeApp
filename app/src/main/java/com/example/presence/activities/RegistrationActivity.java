package com.example.presence.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.presence.helpers.LabelGetter;
import com.example.presence.helpers.NicknameAdapter;
import com.example.presence.R;

public class RegistrationActivity extends AppCompatActivity {

    private Button continueButton; // create vars
    private EditText nameText;
    private String phoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        continueButton = findViewById(R.id.continueButton);
        nameText = findViewById(R.id.nameText);
        phoneNumber = getIntent().getStringExtra(LabelGetter.phoneNumberLbl);

        continueButton.setOnClickListener(new RegistrationStartHandler());
    }

    private class RegistrationStartHandler implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            String nickName = nameText.getText().toString();
            // check if given name has valid format
            if (nameHasValidFormat(nickName)) {
                // we check whether the name is unique:
                NicknameAdapter nicknameAdapter = new NicknameAdapter(RegistrationActivity.this);
                nicknameAdapter.countNameOccurrences(nickName).addOnCompleteListener(task -> {
                    if (nicknameAdapter.getNameCount() > 0) {
                        // if the nickname is not unique, show a toast
                        Toast.makeText(RegistrationActivity.this, "Name is already taken.",
                                Toast.LENGTH_LONG).show();
                    } else {
                        // if the nickname is unique, register the new user
                        nicknameAdapter.registerUser(nickName, phoneNumber);
                    }
                });
            }
        }

        /**
         * Checks if the given name is valid and makes descriptive toasts otherwise.
         * @param name the name to check
         * @return true if the name is valid, false if not.
         */
        private boolean nameHasValidFormat(String name) {
            if (name.trim().isEmpty()) {
                Toast.makeText(RegistrationActivity.this, "Enter a valid name.",
                        Toast.LENGTH_LONG).show(); // show toast if num empty
                return false;
            }
            if (name.length() > 15) {
                Toast.makeText(RegistrationActivity.this, "Entered name is too long.",
                        Toast.LENGTH_LONG).show(); // show toast if num empty
                return false;
            }
            if (name.length() < 3) {
                Toast.makeText(RegistrationActivity.this, "Entered name is too short.",
                        Toast.LENGTH_LONG).show(); // show toast if num empty
                return false;
            }

            return true;
        }
    }
}