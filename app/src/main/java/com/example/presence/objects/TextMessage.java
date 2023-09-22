package com.example.presence.objects;

import android.content.Context;

import com.google.firebase.firestore.DocumentSnapshot;

/**Concrete class for a {@link Message} that contains text.*/
public class TextMessage extends Message {
    /**The text a user associated with this message.*/
    private final String text;

    /**Creates a new TextMessage from a user with a given text and location.
     *
     * @param creatorPhoneNumber
     * @param location
     * @param text */
    public TextMessage(String creatorPhoneNumber, Coords location, String text, String id,
                       int upvotes, int downvotes, String date, String locationName) {
        super(creatorPhoneNumber, location, id, upvotes, downvotes, date, locationName);
        this.text = text;
    }

    public TextMessage(DocumentSnapshot messageSnapShot, Context currentContext) {
        super(messageSnapShot, currentContext);
        this.text = messageSnapShot.getString("messageContent");
    }

    /**Returns the text associated with this message.*/
    public String getText() {
        return text;
    }
}