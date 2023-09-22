package com.example.presence.objects;

import android.os.Parcel;

/**A special type of AppUser with moderator permissions.*/
public class ModeratorUser extends AppUser {
    /**Creates a new ModeratorUser from a given phoneNumber and nickname.*/
    public ModeratorUser(String phoneNumber, String nickname) {
        super(phoneNumber, nickname);
    }

    /**Creates a new ModeratorUser from a given phoneNumber.*/
    public ModeratorUser(String phoneNumber) {
        super(phoneNumber);
    }

    /**
     * Creates a new ModeratorUser from a given parcel.
     *
     * @param source  The Parcel containing this AppUser's data
     */
    ModeratorUser(Parcel source) {
        super(source);
    }

    /**The {@link android.os.Parcelable.Creator} used to create {@link android.os.Parcel}s
     * from ModeratorUser instances.*/
    public static final Creator<AppUser> CREATOR = new Creator<AppUser>() {
        @Override
        public AppUser createFromParcel(Parcel source) {
            return new ModeratorUser(source);
        }

        @Override
        public AppUser[] newArray(int size) {
            return new AppUser[size];
        }
    };
}