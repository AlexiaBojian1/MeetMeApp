package com.example.presence.helpers;

import android.content.Intent;

import com.example.presence.objects.Coords;
import com.example.presence.objects.TextMessage;

/**
 * A class that allows us to pass custom objects through intents.
 */
public class CustomMessagePasser {

    /**
     * Passes a TextMessage object through an intent.
     * @param intent the intent to pass the message through
     * @param message the message to pass through the intent
     * @return the intent with the message passed through it
     */
    public static Intent passMessage(Intent intent, TextMessage message) {
        intent.putExtra(LabelGetter.phoneNumberLbl, message.getCreatorPhoneNumber());
        intent.putExtra(LabelGetter.coordinatesLbl, message.getCoordinates());
        intent.putExtra(LabelGetter.locationLbl, message.getLocationName());
        intent.putExtra(LabelGetter.dbDateLbl, message.getDate());
        intent.putExtra(LabelGetter.dbContentLbl, message.getText());
        intent.putExtra(LabelGetter.idLbl, message.getId());
        intent.putExtra(LabelGetter.upvotesLbl, message.getUpvotes());
        intent.putExtra(LabelGetter.downvotesLbl, message.getDownvotes());
        return intent;
    }

    /**
     * Retrieves a TextMessage object from an intent.
     * @param intent the intent to retrieve the message from
     * @return the TextMessage object retrieved from the intent
     */
    public static TextMessage retrieveMessage(Intent intent) {
        String creatorPhoneNumber = intent.getStringExtra(LabelGetter.phoneNumberLbl);
        Coords coordinates = intent.getParcelableExtra(LabelGetter.coordinatesLbl);
        String locationName = intent.getStringExtra(LabelGetter.locationLbl);
        String date = intent.getStringExtra(LabelGetter.dbDateLbl);
        String message_text = intent.getStringExtra(LabelGetter.dbContentLbl);
        String message_id = intent.getStringExtra(LabelGetter.idLbl);
        int countU = intent.getIntExtra(LabelGetter.upvotesLbl, 0);
        int countD = intent.getIntExtra(LabelGetter.downvotesLbl, 0);
        return new TextMessage(creatorPhoneNumber, coordinates, message_text, message_id, countU,
                countD, date, locationName);
    }
}
