package com.example.presence.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.presence.helpers.LabelGetter;
import com.example.presence.R;

public class RequestTermsAndConditionsActivity extends AppCompatActivity {

    String phoneNumber;
    Button continueButton;
    TextView termsText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_terms_and_conditions);

        continueButton = findViewById(R.id.backButton);
        termsText = findViewById(R.id.termsAndConditions);
        phoneNumber = getIntent().getStringExtra(LabelGetter.phoneNumberLbl);

        continueButton.setOnClickListener(new ContinueHandler());
        termsText.setOnClickListener(new TermsPressedHandler());
    }

    private class TermsPressedHandler implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            // create new intent, we go to the terms screen:
            Intent nextIntent = new Intent(getApplicationContext(), TermsAndConditionsActivity.class);
            // pass phone number over to next screen:
            nextIntent.putExtra(LabelGetter.phoneNumberLbl, phoneNumber);
            startActivity(nextIntent);
        }
    }

    private class ContinueHandler implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            // create new intent, we go to registration screen:
            Intent nextIntent = new Intent(getApplicationContext(), RegistrationActivity.class);
            // pass phone number over to next screen:
            nextIntent.putExtra(LabelGetter.phoneNumberLbl, phoneNumber);
            startActivity(nextIntent);
        }
    }
}