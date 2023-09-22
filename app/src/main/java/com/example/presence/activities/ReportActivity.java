package com.example.presence.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.presence.objects.AppUser;
import com.example.presence.helpers.CustomMessagePasser;
import com.example.presence.helpers.LabelGetter;
import com.example.presence.R;
import com.example.presence.objects.Report;
import com.example.presence.helpers.ReportAdapter;
import com.example.presence.objects.TextMessage;

public class ReportActivity extends AppCompatActivity {

    AppUser appUser;
    TextMessage reportedMessage;
    EditText reportTextField;
    Button reportButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        reportedMessage = CustomMessagePasser.retrieveMessage(getIntent());
        appUser = getIntent().getParcelableExtra(LabelGetter.appUserLbl);

        reportTextField = findViewById(R.id.reportTextField);
        reportButton = findViewById(R.id.postReportButton);

        reportButton.setOnClickListener(new ReportHandler());
    }

    private class ReportHandler implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            String reportText = reportTextField.getText().toString();
            if (!reportTextIsValid(reportText)) {
                Toast.makeText(getApplicationContext(),
                        "Please enter a valid report.",
                        Toast.LENGTH_LONG);
                return;
            }
            // Create a new report
            Report report = new Report(null, reportedMessage.getId(), reportText,
                    reportedMessage.getCreatorPhoneNumber(), appUser.getPhoneNumber());
            ReportAdapter reportAdapter = new ReportAdapter(ReportActivity.this);
            reportAdapter.uploadReport(report, appUser);
        }

        private boolean reportTextIsValid(String reportText) {
            return !(reportText.trim().isEmpty()); // return if report text is not empty

        }
    }
}