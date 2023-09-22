package com.example.presence.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.presence.helpers.LabelGetter;
import com.example.presence.R;

public class TermsAndConditionsActivity extends AppCompatActivity {
    String phoneNumber;
    Button backButton;
    TextView termsAndConditions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_and_conditions);

        phoneNumber = getIntent().getStringExtra(LabelGetter.phoneNumberLbl);
        backButton = findViewById(R.id.backButton);
        termsAndConditions = findViewById(R.id.termsAndConditions);
        termsAndConditions.setMovementMethod(new ScrollingMovementMethod());

        backButton.setOnClickListener(new BackPressedHandler());
    }

    private class BackPressedHandler implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            // create new intent, we go to the terms screen:
            Intent nextIntent = new Intent(getApplicationContext(), RequestTermsAndConditionsActivity.class);
            // pass phone number over to next screen:
            nextIntent.putExtra(LabelGetter.phoneNumberLbl, phoneNumber);
            startActivity(nextIntent);
        }
    }
}