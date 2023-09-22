package com.example.presence.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.presence.fragments.DisplayMessageFragment;
import com.example.presence.helpers.CustomMessagePasser;
import com.example.presence.helpers.LabelGetter;
import com.example.presence.helpers.NicknameAdapter;
import com.example.presence.R;
import com.example.presence.objects.RegularUser;
import com.example.presence.objects.Report;
import com.example.presence.helpers.ReportAdapter;
import com.example.presence.objects.TextMessage;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ModeratorViewSingleReportActivity extends AppCompatActivity {

    Button deleteMessage, viewReportedMessage;
    TextView nickname, phoneNumber;
    Report report;
    String posterNickname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moderator_view_single_report);

        // Create new DisplayMessageFragment
        Fragment fragment = new DisplayMessageFragment();

        deleteMessage = findViewById(R.id.postReportButton);
        nickname = findViewById(R.id.reporterNicknameTextView);
        phoneNumber = findViewById(R.id.reporterPhoneNumberTextView);
        viewReportedMessage = findViewById(R.id.viewReportedMessageButton);

        report = getIntent().getParcelableExtra(LabelGetter.reportLbl);

        // If something messes up, initlialize backup report that indicates an error
        if (report == null) {
            report = initTestReport();
        }

        Bundle bundle = new Bundle();
        bundle.putString(LabelGetter.messageLbl, report.getReportContent());

        // Pass message to fragment
        fragment.setArguments(bundle);


        // Open fragment
        getSupportFragmentManager()
                .beginTransaction().replace(R.id.report_fragment_container, fragment)
                .commit();

        // Sets report poster phone number // This works
        phoneNumber.setText(report.getReporterPhoneNumber());

        // Sets reporter nickname
        NicknameAdapter nicknameAdapter = new NicknameAdapter(ModeratorViewSingleReportActivity.this);
        nicknameAdapter.findNickname(report.getReporterPhoneNumber())
                .addOnCompleteListener(task -> {
                    nickname.setText(nicknameAdapter.getNickname());
                });

        // Retrieves message poster nickname
        nicknameAdapter.findNickname(report.getReportedPhoneNumber())
                .addOnCompleteListener(foundNickname -> {
                    posterNickname = nicknameAdapter.getNickname();
                });

        // Deletes report from database when button is clicked
        deleteMessage.setOnClickListener(v -> {
            ReportAdapter reportAdapter = new ReportAdapter(this);
            reportAdapter.deleteReport(report);
        });

        viewReportedMessage.setOnClickListener(new viewReportedMessageHandler());

    }

    private class viewReportedMessageHandler implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            FirebaseFirestore.getInstance().collection(LabelGetter.dbMessageLbl).document(report.getMessageId()).get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    System.out.println("Successfully retrieved message");
                    DocumentSnapshot document = task.getResult();
                    System.out.println(document);
                    if (document.exists()) {
                        TextMessage messageToPass = new TextMessage(document, ModeratorViewSingleReportActivity.this);
                        Intent intent = CustomMessagePasser.passMessage(new Intent(ModeratorViewSingleReportActivity.this,
                                ModeratorViewMessageActivity.class), messageToPass);
                        intent.putExtra(LabelGetter.appUserLbl, new RegularUser(report.getReportedPhoneNumber(), posterNickname));
                        intent.putExtra(LabelGetter.previousLbl, "ModeratorViewSingleReportActivity");
                        startActivity(intent);
                    } else {
                        Toast.makeText(ModeratorViewSingleReportActivity.this, "Message no longer exists.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(ModeratorViewSingleReportActivity.this, "Failed to retrieve message.", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private Report initTestReport() {
        return new Report("TFOfDmz3wYTbQBwiCiQy", "0I7DdIUENopMh4DMOAL7", "The message is reported because its just a test", "+11234567890", "+10987654321" );
    }
}