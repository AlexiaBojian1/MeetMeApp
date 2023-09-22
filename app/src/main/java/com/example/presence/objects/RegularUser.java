package com.example.presence.objects;

import android.os.Parcel;
import android.util.Log;

import com.example.presence.helpers.LabelGetter;
import com.google.firebase.firestore.DocumentSnapshot;

/**A regular user without moderator permissions.*/
public class RegularUser extends AppUser {
    /**Creates a new RegularUser by phoneNumber and nickname.*/
    public RegularUser(String phoneNumber, String nickname) {
        super(phoneNumber, nickname);
    }

    /**Creates a new RegularUser by phoneNumber.*/
    public RegularUser(String phoneNumber) {
        super(phoneNumber);
    }

    public RegularUser(DocumentSnapshot userSnapshot) {
        super(userSnapshot.getString(LabelGetter.phoneNumberLbl), userSnapshot.getString(LabelGetter.nicknameLbl));
    }

    /**Creates a new RegularUser from a given Parcel.*/
    public RegularUser(Parcel source) {
        super(source);
    }

    /**The {@link android.os.Parcelable.Creator} used to create RegularUser instances
     * from {@link android.os.Parcel}s.*/
    public static final Creator<AppUser> CREATOR = new Creator<AppUser>() {
        @Override
        public AppUser createFromParcel(Parcel source) {
            return new RegularUser(source);
        }

        @Override
        public AppUser[] newArray(int size) {
            return new RegularUser[size];
        }
    };

}