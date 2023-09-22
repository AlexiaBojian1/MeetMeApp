package com.example.presence.helpers;

import android.app.Activity;
import android.content.Intent;
import android.os.Parcelable;
import android.widget.Toast;

import com.example.presence.objects.AppUser;
import com.example.presence.objects.Report;
import com.example.presence.activities.HomeActivity;
import com.example.presence.activities.ModeratorViewAllReportsActivity;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Handles report-related calls to the database.
 */
public class ReportAdapter {

    /** The activity that uses this ReportAdapter.*/
    private Activity activity;

    public ReportAdapter(Activity activity) {
        this.activity = activity;
    }

    /**
     * Uploads a given report to the database.
     * Starts the HomeActivity when the upload is complete.
     *
     * @param report  The report to add to the database
     * @param appUser  The appUser that called the method, for starting the HomeActivity
     */
    public void uploadReport(Report report, AppUser appUser) {
        FirebaseFirestore db = FirebaseFirestore.getInstance(); // get a handle on the database

        // prepare new report's document:
        // create a new report
        Map<String, Object> newReport = new HashMap<>();
        // set its properties
        newReport.put(LabelGetter.reporterNrLbl, report.getReporterPhoneNumber());
        newReport.put(LabelGetter.reportedNrLbl, report.getReportedPhoneNumber());
        newReport.put(LabelGetter.reportContentLbl, report.getReportContent());
        // add the message's id
        newReport.put(LabelGetter.messageIdLbl, report.getMessageId());
        // get the report's id
        DocumentReference ref = db.collection(LabelGetter.dbReportLbl).document();
        // put it in the report's document
        newReport.put(LabelGetter.idLbl, ref.getId());
        report.setId(ref.getId());

        // actually add the report to the database:
        ArrayList<Parcelable> dataToPass = new ArrayList() {{ add(appUser); }};
        db.collection(LabelGetter.dbReportLbl).document(ref.getId())
                .set(newReport)
                .addOnCompleteListener(new UploadCompleteHandler(activity,
                        HomeActivity.class, dataToPass));
    }

    /**
     * Deletes a given report from the database.
     *
     * @param report  Report to delete from the database
     */
    public void deleteReport(Report report) {
        FirebaseFirestore db = FirebaseFirestore.getInstance(); // get a handle on the database

        db.collection(LabelGetter.dbReportLbl).document(report.getId()).delete()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(activity, "Report deleted", Toast.LENGTH_SHORT).show();
                        activity.startActivity(
                                new Intent(activity, ModeratorViewAllReportsActivity.class));
                        activity.finish();
                    } else {
                        Toast.makeText(activity,
                                "Report could not be deleted", Toast.LENGTH_SHORT).show();
                    }
        });
    }
}
