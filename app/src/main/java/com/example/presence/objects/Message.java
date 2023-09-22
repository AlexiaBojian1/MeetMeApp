package com.example.presence.objects;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import com.example.presence.helpers.LabelGetter;
import com.firebase.geofire.GeoFireUtils;
import com.firebase.geofire.GeoLocation;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.List;
import java.util.Locale;
import java.util.Map;

/**Abstract class of a message for a certain location.*/
public abstract class Message {
    /**The phone number of the user that created this message.*/
    protected final String creatorPhoneNumber;
    /**The coordinates of this message.*/
    protected final Coords coordinates;
    /**The name of the location of this message.*/
    protected final String locationName;
    /**The id of this message. */
    protected final String id;
    /**The date of this message. */
    protected final String date;
    /**The number of upvotes on this message.*/
    protected int upvotes;
    /**The number of downvotes on this message.*/
    protected int downvotes;

    /**Creates a new Message from a user for a given location.
     *
     * @param creatorPhoneNumber  The phone number of the user that created the message
     * @param coordinates  The location of the message
     * @param id  The ID of the message
     * @param upvotes  The amount of upvotes this message has
     * @param downvotes  The amount of downvotes this message has
     * @param date  The date this message was posted
     * @param locationName  The name of the location this message was posted at
     */
    Message(String creatorPhoneNumber, Coords coordinates, String id, int upvotes, int downvotes,
            String date, String locationName) {
        this.creatorPhoneNumber = creatorPhoneNumber;
        this.coordinates = coordinates;
        this.id = id;
        this.upvotes = upvotes;
        this.downvotes = downvotes;

        this.date = date;
        this.locationName = locationName;
    }

    Message(DocumentSnapshot messageSnapshot, Context currentContext) {
        this.creatorPhoneNumber = messageSnapshot.getString("phoneNumber");
        this.coordinates = new Coords(messageSnapshot.getDouble("latitude"),
                messageSnapshot.getDouble("longitude"));
        this.upvotes = Integer.parseInt(messageSnapshot.get(LabelGetter.upvotesLbl).toString());
        this.downvotes = Integer.parseInt(messageSnapshot.get(LabelGetter.downvotesLbl).toString());
        this.id = messageSnapshot.getId();
        this.date = parseDate(messageSnapshot);
        this.locationName = parseLocation(messageSnapshot, currentContext);
    }

    /**Returns the location of this message.*/
    public Coords getCoordinates() {
        return coordinates;
    }

    /**Returns the number of votes on this message.*/
    public int getVotes() {
        return upvotes - downvotes;
    }

    /**Returns the number of upvotes on this message.*/
    public int getUpvotes() {
        return upvotes;
    }

    /**Returns the number of downvotes on this message.*/
    public int getDownvotes() {
        return downvotes;
    }

    /**Returns the id of this message.*/
    public String getId() { return id; }

    /**Returns phone number of creator.*/
    public String getCreatorPhoneNumber() { return creatorPhoneNumber; }

    /**Returns the date this message was posted.*/
    public String getDate() { return date; }

    /**Returns the name of the location this message was posted at.*/
    public String getLocationName() { return locationName; }

    /**Returns the distance of the location of this message to given coordinates.
     *
     * @param coords  The coordinates to check the distance to
     * @return The distance of this message to the given coordinates
     */
    public double getDistanceToCoordinates(Coords coords) {
        return GeoFireUtils.getDistanceBetween(new GeoLocation(coords.getLatitude(),
                coords.getLongitude()), new GeoLocation(coordinates.getLatitude(),
                coordinates.getLongitude()));
    }

    /**Adds an upvote to this message.*/
    public void upvote() {
        upvotes++;
    }

    /**Removes an upvote of this message.*/
    public void unUpvote() {
        upvotes--;
    }

    /**Adds a downvote to this message.*/
    public void downvote() {
        downvotes++;
    }

    /**Removes a downvote of this message.*/
    public void unDownvote() {
        downvotes--;
    }

    private String parseDate(DocumentSnapshot documentSnapshot) {
        Map<String, Object> myMap = (Map<String, Object>) documentSnapshot.get("date");
        String date = myMap.get("month") + " " + myMap.get("dayOfMonth") + ", \n" +
                myMap.get("year") + ", " + myMap.get("hour") + ":" + myMap.get("minute");
        return date;
    }

    private String parseLocation(DocumentSnapshot snapshot, Context currentContext) {
        Geocoder gcd = new Geocoder(currentContext, Locale.getDefault());
        try {
            List<Address> addresses = gcd.getFromLocation(snapshot.getDouble("latitude"),
                    snapshot.getDouble("longitude"), 1);
            String countryCity = addresses.get(0).getCountryName() + ", \n" +
                    addresses.get(0).getLocality();
            return countryCity;
        } catch (Exception e) {
            return "Unknown Location";
        }
    }
}