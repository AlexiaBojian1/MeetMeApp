package com.example.presence.objects;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Coords implements Parcelable {

    private double latitude;
    private double longitude;

    public Coords(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    protected Coords(Parcel in) {
        latitude = in.readDouble();
        longitude = in.readDouble();
    }

    public static final Creator<Coords> CREATOR = new Creator<Coords>() {
        @Override
        public Coords createFromParcel(Parcel in) {
            return new Coords(in);
        }

        @Override
        public Coords[] newArray(int size) {
            return new Coords[size];
        }
    };

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
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
     * Writes this Coords's data into a {@link Parcel}
     *
     * @param parcel  The parcel to write into
     * @param i  Additional flags
     */
    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeDouble(latitude);
        parcel.writeDouble(longitude);
    }
}