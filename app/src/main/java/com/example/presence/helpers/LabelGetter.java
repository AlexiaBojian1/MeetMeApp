package com.example.presence.helpers;

import com.example.presence.objects.AppUser;
import com.example.presence.objects.Message;

public class LabelGetter {

    public static final String appUserLbl = "appUser";
    public static final String messageLbl = "message";
    public static final String reportLbl = "report";
    public static final String phoneNumberLbl = "phoneNumber";
    public static final String nicknameLbl = "nickName";
    public static final String latLbl = "latitude";
    public static final String longLbl = "longitude";
    public static final String idLbl = "id";
    public static final String upvotesLbl = "countU";
    public static final String downvotesLbl = "countD";
    public static final String blockedLbl = "blocked";
    public static final String locationLbl = "location";
    public static final String geoHashLbl = "geoHash";
    public static final String reporterNrLbl = "reporterPhoneNumber";
    public static final String reportedNrLbl = "reportedPhoneNumber";
    public static final String reportContentLbl = "reportContent";
    public static final String messageIdLbl = "messageId";
    public static final String previousLbl = "previousActivity";
    public static final String verificationIdLbl = "verificationId";
    public static final String coordinatesLbl = "coordinates";
    public static final String[] collectionPaths = {"users", "messages", "reports", "moderators"};
    public static final String dbUserLbl = collectionPaths[0];
    public static final String dbMessageLbl = collectionPaths[1];
    public static final String dbReportLbl = collectionPaths[2];
    public static final String dbModeratorLbl = collectionPaths[3];
    public static final String dbContentLbl = "messageContent";
    public static final String dbDateLbl = "date";

    public static String getLabel(Object o) {
        if (o instanceof AppUser) {
            return appUserLbl;
        } else if (o instanceof Message) {
            return messageLbl;
        } else {
            throw new IllegalArgumentException();
        }
    }
}
