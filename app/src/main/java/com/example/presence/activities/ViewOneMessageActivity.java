package com.example.presence.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.presence.objects.AppUser;
import com.example.presence.helpers.CustomMessagePasser;
import com.example.presence.fragments.DisplayMessageFragment;
import com.example.presence.helpers.LabelGetter;
import com.example.presence.helpers.MessageAdapter;
import com.example.presence.helpers.NicknameAdapter;
import com.example.presence.R;
import com.example.presence.objects.TextMessage;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

public class ViewOneMessageActivity extends AppCompatActivity implements OnCompleteListener {
    Button upvoteButton, downvoteButton;
    ImageButton reportFlag;
    TextView username, likeCount, dislikeCount;
    TextMessage message;
    AppUser appUser;

    private boolean upvoteClicked = false;
    private boolean downvoteClicked = false;
    private int countU = 0;
    private int countD = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_one_message);

        // Create new DisplayMessageFragment
        Fragment fragment = new DisplayMessageFragment();

        upvoteButton = findViewById(R.id.upvoteButton);
        downvoteButton = findViewById(R.id.downvoteButton);
        username = findViewById(R.id.posterNicknameTextView);
        likeCount = findViewById(R.id.likeCount);
        dislikeCount = findViewById(R.id.dislikeCount);
        reportFlag = findViewById(R.id.reportFlagButton);

        // Get message from intent (click on a map marker)
        message = CustomMessagePasser.retrieveMessage(getIntent());
        appUser = getIntent().getParcelableExtra(LabelGetter.appUserLbl);

        // Set up a messageAdapter for use in this activity
        MessageAdapter messageAdapter = new MessageAdapter(ViewOneMessageActivity.this);

        countU = message.getUpvotes();
        countD = message.getDownvotes();
        likeCount.setText(String.valueOf(countU));
        dislikeCount.setText(String.valueOf(countD));

        upvoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Increase the counter and update the TextView
                if (!upvoteClicked) {
                    if (downvoteClicked) {
                        countD--;
                        downvoteClicked = false;
                    }
                    countU++;
                    upvoteClicked = true;
                }
                else {
                    countU--;
                    upvoteClicked = false;
                }
                messageAdapter.updateVotes(message.getId(), countU, countD, ViewOneMessageActivity.this);
            }
        });

        downvoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Increase the counter and update the TextView
                if (!downvoteClicked) {
                    if (upvoteClicked) {
                        countU--;
                        upvoteClicked = false;
                    }
                    countD++;
                    downvoteClicked = true;
                }
                else {
                    countD--;
                    downvoteClicked = false;
                }
                messageAdapter.updateVotes(message.getId(), countU, countD,
                        ViewOneMessageActivity.this);
            }
        });

        reportFlag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ViewOneMessageActivity.this, ReportActivity.class);
                intent = CustomMessagePasser.passMessage(intent, message);
                intent.putExtra(LabelGetter.appUserLbl, appUser);
                startActivity(intent);
            }
        });

        // Test TextMessage object to see if it is displayed
        Bundle bundle = new Bundle();
        bundle.putString(LabelGetter.messageLbl, message.getText());

        // Pass message to fragment
        fragment.setArguments(bundle);

        // Open fragment
        getSupportFragmentManager()
                .beginTransaction().replace(R.id.message_fragment_container, fragment)
                .commit();

        // Sets message poster nickname
        NicknameAdapter nicknameAdapter = new NicknameAdapter(ViewOneMessageActivity.this);
        nicknameAdapter.findNickname(message.getCreatorPhoneNumber())
                .addOnCompleteListener(task -> {
                    String posterNickname = nicknameAdapter.getNickname();
                    // if nickname is null for some reason, just show the last three
                    // characters of the phone number
                    if (posterNickname == null) {
                        posterNickname = "..." + message.getCreatorPhoneNumber().substring(
                                message.getCreatorPhoneNumber().length() - 3);
                    }
                    username.setText(posterNickname);
                });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ViewOneMessageActivity.this, HomeActivity.class);
        intent.putExtra(LabelGetter.appUserLbl, appUser);
        startActivity(intent);
        finish();
    }

    @Override
    public void onComplete(@NonNull Task task) {
        if (task.isSuccessful()) {
            likeCount.setText(String.valueOf(countU));
            dislikeCount.setText(String.valueOf(countD));
        }
    }
}
