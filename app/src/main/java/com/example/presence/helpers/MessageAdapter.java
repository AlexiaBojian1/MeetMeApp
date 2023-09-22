package com.example.presence.helpers;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Parcelable;
import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.presence.objects.Coords;
import com.example.presence.objects.RegularUser;
import com.example.presence.objects.TextMessage;
import com.example.presence.activities.HomeActivity;
import com.example.presence.activities.ModeratorViewAllReportsActivity;
import com.firebase.geofire.GeoFireUtils;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQueryBounds;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MessageAdapter {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private List<TextMessage> messageList= new ArrayList<>();
    /** The activity that uses this MessageAdapter.*/
    private Activity activity;

    public MessageAdapter(Activity activity) {
        this.activity = activity;
    }


    /**
     * Gets messages stored in the firebase and assigns them to the messageList object in the class.
     * @param location The location to search around
     * @param currentContext The context of the activity that calls this method
     */
    public Task<?> downloadMessagesInRange(Coords location, Context currentContext) {
        return downloadMessagesInRange(location, currentContext, 10000000);
    }

    /**
     * Gets messages stored in the firebase that have their location within 1km of a given location
     * and assigns them to the messageList object in the class.
     * @param location The location to search around
     * @param currentContext The context of the activity that calls this method
     * @param radius The radius to search around the location
     */
    public Task<?> downloadMessagesInRange(Coords location, Context currentContext, int radius) {

        /** Location details */
        final GeoLocation center = new GeoLocation(location.getLatitude(), location.getLongitude());
        List<GeoQueryBounds> bounds = GeoFireUtils.getGeoHashQueryBounds(center, radius);
        final List<Task<QuerySnapshot>> tasks = new ArrayList<>();

        for (GeoQueryBounds b : bounds) {
            Query q = db.collection(LabelGetter.dbMessageLbl)
                    .orderBy(LabelGetter.geoHashLbl)
                    .startAt(b.startHash)
                    .endAt(b.endHash);
            tasks.add(q.get());
        }
        return Tasks.whenAllComplete(tasks)
                .addOnCompleteListener(new OnCompleteListener<List<Task<?>>>() {
                    @Override
                    public void onComplete(@NonNull Task<List<Task<?>>> t) {
                        List<DocumentSnapshot> matchingDocs = new ArrayList<>();

                        for (Task<QuerySnapshot> task : tasks) {
                            QuerySnapshot snap = task.getResult();
                            for (DocumentSnapshot doc : snap.getDocuments()) {
                                GeoLocation docLocation = new GeoLocation(location.getLatitude(), location.getLongitude());
                                TextMessage message = new TextMessage(doc, currentContext);
                                messageList.add(message);
                            }
                        }
                    }
                });
    }

    /**
     * Uploads a given message to the database.
     *
     * @param message  The message to upload
     */
    @SuppressLint("NewApi")
    public void addMessage(TextMessage message) {
        // get a handle on the database
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        // prepare new message's record:
        String geoHash = GeoFireUtils.getGeoHashForLocation(new GeoLocation(
                message.getCoordinates().getLatitude(), message.getCoordinates().getLongitude()));
        Map<String, Object> newMessage = new HashMap<>(); // create a new message
        // set its properties:
        newMessage.put(LabelGetter.phoneNumberLbl, message.getCreatorPhoneNumber());
        newMessage.put(LabelGetter.dbContentLbl, message.getText());
        newMessage.put(LabelGetter.dbDateLbl, LocalDateTime.now());
        newMessage.put(LabelGetter.longLbl, message.getCoordinates().getLongitude());
        newMessage.put(LabelGetter.latLbl, message.getCoordinates().getLatitude());
        newMessage.put(LabelGetter.geoHashLbl, geoHash);
        newMessage.put(LabelGetter.upvotesLbl, 0);
        newMessage.put(LabelGetter.downvotesLbl, 0);
        DocumentReference ref = db.collection(LabelGetter.dbMessageLbl).document(); // get the message's id
        newMessage.put(LabelGetter.idLbl, ref.getId()); // put it in the message's document

        // add the new message to the database:
        ArrayList<Parcelable> dataToPass = new ArrayList() {{ add(new RegularUser(message.getCreatorPhoneNumber())); }};
        db.collection(LabelGetter.dbMessageLbl).document(ref.getId())
                .set(newMessage)
                .addOnCompleteListener(new UploadCompleteHandler(activity,
                        HomeActivity.class, dataToPass));
    }

    /**
     * Deletes a message from the database.
     * @param id  The ID of the message to be deleted
     * @param previousActivity  The activity previous to the current activity, for going back to the
     *                          right screen after deleting the message
     */
    public void deleteMessage(String id, String previousActivity) {
        db.collection(LabelGetter.dbMessageLbl).document(id).delete().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(activity, "Message deleted", Toast.LENGTH_SHORT).show();
                if (previousActivity.equals("ModeratorViewSingleReportActivity")) {
                    Intent intent = new Intent(activity, ModeratorViewAllReportsActivity.class);
                    activity.startActivity(intent);
                }
                activity.finish();
            } else {
                Toast.makeText(activity, "Message not deleted", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Updates the votes for a message with the given ID in the database.
     *
     * @param id  ID of the message to update
     * @param countU  New amount of upvotes
     * @param countD  New amount of downvotes
     * @param listener  OnCompleteListener that handles UI changes after the votes have been updated
     */
    public void updateVotes(String id, int countU, int countD, OnCompleteListener listener) {
        db.collection(LabelGetter.dbMessageLbl).document(id).update(LabelGetter.upvotesLbl, countU).
                addOnCompleteListener(listener);
        db.collection(LabelGetter.dbMessageLbl).document(id).update(LabelGetter.downvotesLbl, countD).
                addOnCompleteListener(listener);
    }

    /**
     * Returns a list of the messages found in downloadMessagesInRange.
     *
     * @return  A list of the messages found in downloadMessagesInRange
     */
    public List<TextMessage> getMessageList() {
        return messageList;
    }
}