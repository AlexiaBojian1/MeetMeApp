package com.example.presence.objects;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.presence.helpers.LabelGetter;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.checkerframework.checker.nullness.qual.NonNull;

/**Abstract class that defines a user.*/
public abstract class AppUser implements Parcelable {
    /**The phone number used by this user.*/
    protected final String phoneNumber;
    /**The nickname used by this user.*/
    String nickname = "";
    /** Whether this user is allowed to post messages. */
    private boolean blocked;

    /**
     * Creates a new AppUser with a given phoneNumber and nickname.
     *
     * @param phoneNumber  The phoneNumber used by this AppUser
     * @param nickname  The nickname used by this AppUser
     * @throws IllegalArgumentException when phoneNumber is not a valid phone number
     */
    public AppUser(String phoneNumber, String nickname) throws IllegalArgumentException {
        this.phoneNumber = phoneNumber;
        this.nickname = nickname;
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Query userIsBlocked = db.collection(LabelGetter.dbUserLbl).whereEqualTo(
                LabelGetter.phoneNumberLbl, getPhoneNumber());
        userIsBlocked.get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            setBlocked(Boolean.TRUE.equals(
                                    document.getBoolean(LabelGetter.blockedLbl)));
                        }
                    } else {
                        throw new IllegalArgumentException("no blocked field in database for " +
                                phoneNumber);
                    }
                });
    }

    /**
     * Creates an AppUser object for an existing user, given the phoneNumber associated with it.
     *
     * @param phoneNumber  The phoneNumber associated with this AppUser
     * @throws IllegalArgumentException when phoneNumber is not a valid phone number
     */
    public AppUser(String phoneNumber) throws IllegalArgumentException {
        this.phoneNumber = phoneNumber;
        updateNickname();
    }

    /**
     * Update the nickname and blocked fields to the ones in the database.
     * @modifies {@code String nickname} the nickname of the user.
     * @modifies {@code boolean blocked} whether this user is allowed to post messages.
     * */
    public Task<QuerySnapshot> updateNickname() throws IllegalArgumentException {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Query userByPhone = db.collection(LabelGetter.dbUserLbl).whereEqualTo(
                LabelGetter.phoneNumberLbl, phoneNumber);
        return userByPhone.get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            nickname = document.getString(LabelGetter.nicknameLbl);
                            blocked = Boolean.TRUE.equals(document.getBoolean(
                                    LabelGetter.blockedLbl));
                        }
                    } else {
                        throw new IllegalArgumentException("no nickname in database for " +
                                phoneNumber);
                    }
                });
    }

    /**Creates an AppUser object from a given parcel.
     *
     * @param source  the Parcel that contains the data for this AppUser
     */
    protected AppUser(Parcel source) {
        phoneNumber = source.readString();
        nickname = source.readString();
        int blockedInt = source.readInt();
        blocked = blockedInt != 0;
    }

    /**
     * Returns the phone number used by this user.
     *
     * @return phoneNumber  This AppUser's phoneNumber
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * Returns the nickname used by this AppUser.
     *
     * @return nickname  This AppUser's nickname
     */
    public String getNickname() {
        return nickname;
    }


    /** Returns whether this user is prevented from posting messages.*/
    public boolean isBlocked() {
        return blocked;
    }

    /** Block or unblock this user: invert {@code blocked} variable. */
    public void block() {
        setBlocked(!blocked);
    }

    /** Set the blocked variable of this user
     *
     * @param blocked  Whether this user is blocked.
     */
    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(LabelGetter.dbUserLbl).document(phoneNumber).update(
                LabelGetter.blockedLbl, blocked);
    }

    /**
     * Describes the content of this parcel.
     *
     * @return 0
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Writes this AppUser's data into a {@link Parcel}
     *
     * @param parcel  The parcel to write into
     * @param i  Additional flags
     */
    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(phoneNumber);    // include the phoneNumber in the parcel
        parcel.writeString(nickname);       // include the nickname in the parcel
        // include blocked in the parcel
        // Since parcel.writeBoolean is not supported in API 22, we use writeInt as a workaround
        if (blocked) {
            parcel.writeInt(1);
        } else {
            parcel.writeInt(0);
        }
    }
}