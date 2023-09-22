package com.example.presence.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.presence.helpers.CustomMessagePasser;
import com.example.presence.fragments.DisplayMessageFragment;
import com.example.presence.helpers.LabelGetter;
import com.example.presence.helpers.MessageAdapter;
import com.example.presence.objects.AppUser;
import com.example.presence.R;
import com.example.presence.objects.TextMessage;

public class ModeratorViewMessageActivity extends AppCompatActivity {
    Button deleteMessage;
    ImageButton goToAllMessagesButton;
    TextView nicknameView, dateView, locationView, upvoteCount, downvoteCount;
    String flag;
    TextMessage message;
    AppUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moderator_view_message);

        // Create new DisplayMessageFragment
        Fragment fragment = new DisplayMessageFragment();

        deleteMessage = findViewById(R.id.deleteMessageButton);
        goToAllMessagesButton = findViewById(R.id.goToAllUserMessagesButton);
        nicknameView = findViewById(R.id.reporterNicknameTextView);
        dateView = findViewById(R.id.dateOfPosting);
        locationView = findViewById(R.id.location);
        upvoteCount = findViewById(R.id.upvoteCount);
        downvoteCount = findViewById(R.id.downvoteCount);

        // Get message from intent (click on a map marker or from list of messages activity)
        message = CustomMessagePasser.retrieveMessage(getIntent());
        user = getIntent().getParcelableExtra(LabelGetter.appUserLbl);
        flag = getIntent().getStringExtra(LabelGetter.previousLbl);

        if (!flag.equals("YourOwnMessagesActivity") && !flag.equals("HomeActivity")) {
            goToAllMessagesButton.setOnClickListener(view -> {
                Intent intent = new Intent(this, ModeratorViewAllUserMessagesActivity.class);
                intent.putExtra(LabelGetter.appUserLbl, user);
                startActivity(intent);
            });
        }

        // Test TextMessage object to see if it is displayed
        Bundle bundle = new Bundle();
        bundle.putString(LabelGetter.messageLbl, message.getText());

        // Pass message to fragment
        fragment.setArguments(bundle);

        // Open fragment
        getSupportFragmentManager()
                .beginTransaction().replace(R.id.report_fragment_container, fragment)
                .commit();

        // Sets message poster details
        // New implementation
        nicknameView.setText(user.getNickname());
        dateView.setText(message.getDate().replaceAll("[\r\n]+", ""));
        locationView.setText(message.getLocationName());
        upvoteCount.setText(String.valueOf(message.getUpvotes()));
        downvoteCount.setText(String.valueOf(message.getDownvotes()));

        // Deletes message from database when button is clicked
        deleteMessage.setOnClickListener(view -> {
                        MessageAdapter messageAdapter = new MessageAdapter(ModeratorViewMessageActivity.this);
                        messageAdapter.deleteMessage(message.getId(), flag);
                });
    }
}