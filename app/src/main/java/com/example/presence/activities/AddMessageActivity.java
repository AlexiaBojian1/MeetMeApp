package com.example.presence.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.presence.objects.AppUser;
import com.example.presence.objects.Coords;
import com.example.presence.helpers.LabelGetter;
import com.example.presence.helpers.LocationHelper;
import com.example.presence.helpers.MessageAdapter;
import com.example.presence.R;
import com.example.presence.objects.TextMessage;
import com.example.presence.exceptions.NullLocationException;
import com.example.presence.interfaces.LocationCallback;

public class AddMessageActivity extends AppCompatActivity {

    Button postButton;
    EditText messageText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_message);

        postButton = findViewById(R.id.postButton);
        messageText = findViewById(R.id.messageText);

        postButton.setOnClickListener(new PostMessageHandler());
    }

    private class PostMessageHandler implements View.OnClickListener, LocationCallback {
        String messageContent;
        AppUser appUser;

        @Override
        public void onClick(View view) {
            messageContent = messageText.getText().toString();
            appUser = getIntent().getParcelableExtra(LabelGetter.appUserLbl);


            if (!messageIsValid(messageContent)) {
                Toast.makeText(getApplicationContext(),
                        "Please enter a valid message.",
                        Toast.LENGTH_LONG).show(); // tell user to enter valid message
                return;
            }

            if (appUser.isBlocked()) {
                Toast.makeText(getApplicationContext(),
                        "You have been blocked by a moderator.",
                        Toast.LENGTH_LONG).show(); // tell user they are blocked
                return;
            }

            // get current location:
            LocationHelper locationGetter = new LocationHelper(AddMessageActivity.this);
            try {
                locationGetter.startLocationCallback(this);
            } catch (NullLocationException e) {
                System.out.println(e);
                return; // in case location can't be retrieved, we prematurely exit.
            }

        }

        private boolean messageIsValid(String message) {
            return !(message.trim().isEmpty());
        }

        @Override
        public void onLocation(Location location) {
            double longitude = location.getLongitude();
            double latitude = location.getLatitude();
            Coords coords = new Coords(latitude, longitude);
            TextMessage message = new TextMessage(appUser.getPhoneNumber(), coords, messageContent, null, 0, 0, null, null);
            MessageAdapter messageAdapter = new MessageAdapter(AddMessageActivity.this);
            messageAdapter.addMessage(message);
        }
    }
}