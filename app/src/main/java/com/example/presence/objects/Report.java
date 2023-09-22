package com.example.presence.objects;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Report implements Parcelable {
    private String id;
    private String messageId;
    private String reportContent;
    private String reportedPhoneNumber;
    private String reporterPhoneNumber;

    public Report(String id, String messageId, String reportContent,
                  String reportedPhoneNumber, String reporterPhoneNumber) {
        this.id = id;
        this.messageId = messageId;
        this.reportContent = reportContent;
        this.reportedPhoneNumber = reportedPhoneNumber;
        this.reporterPhoneNumber = reporterPhoneNumber;
    }
    public Report() {

    }

    public Report(Parcel source) {
        id = source.readString();
        messageId = source.readString();
        reportContent = source.readString();
        reportedPhoneNumber = source.readString();
        reporterPhoneNumber = source.readString();
    }

    public String getId() { return id; }

    /**
     * Sets this report's id.
     *
     * @param id  The id to give to this report
     * @modifies this.id to the provided one
     */
    public void setId(String id) {
        this.id = id;
    }
    public String getMessageId() { return messageId; }
    public String getReportContent() { return reportContent; }
    public String getReportedPhoneNumber() { return reportedPhoneNumber; }
    public String getReporterPhoneNumber() { return reporterPhoneNumber; }

    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(messageId);
        dest.writeString(reportContent);
        dest.writeString(reportedPhoneNumber);
        dest.writeString(reporterPhoneNumber);
    }

    public static final Creator<Report> CREATOR = new Creator<Report>() {
        @Override
        public Report createFromParcel(Parcel in) {
            return new Report(in);
        }

        @Override
        public Report[] newArray(int size) {
            return new Report[size];
        }
    };

}
