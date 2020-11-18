package com.monresto.acidlabs.monresto.UI.Address;


import java.io.Serializable;

public class MapLocation implements Serializable {
    Coordinates coordinates;


    public MapLocation(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    public double getLongtiude() {
        try {
            return coordinates.getLongitude();
        } catch (Exception e) {
            return 0;
        }
    }

    public double getLatitude() {
        try {
            return coordinates.getLatitude();
        } catch (Exception e) {
            return 0;
        }
    }

    public void setLatitude(double latitude) {
        try {
            coordinates.setLatitude(latitude);
        } catch (Exception ignored) {

        }
    }

    public void setLongitude(double longitude) {
        try {
            coordinates.setLongitude(longitude);
        } catch (Exception ignored) {

        }
    }
}
