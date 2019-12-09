package com.example.ic13;

import java.io.Serializable;

public class Place implements Serializable {
    String name, latitude, longitude, icon;

    @Override
    public String toString() {
        return "Place{" +
                "name='" + name + '\'' +
                ", latitude='" + latitude + '\'' +
                ", longitude='" + longitude + '\'' +
                ", icon='" + icon + '\'' +
                '}';
    }
}
