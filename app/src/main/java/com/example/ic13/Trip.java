package com.example.ic13;

import java.io.Serializable;

public class Trip implements Serializable {

    public String tripName, city, placeId;

    @Override
    public String toString() {
        return "Trip{" +
                "tripName='" + tripName + '\'' +
                ", city='" + city + '\'' +
                '}';
    }
}
