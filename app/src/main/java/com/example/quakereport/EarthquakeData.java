package com.example.quakereport;

import java.util.Date;

public class EarthquakeData {

    private double mMagnitude;
    private String mPlace;
    private Date mDate;
    private String url;


    public EarthquakeData(double mMagnitude, String mPlace, Date mDate, String url) {
        this.mMagnitude = mMagnitude;
        this.mPlace = mPlace;
        this.mDate = mDate;
        this.url = url;
    }

    public double getmMagnitude() {
        return mMagnitude;
    }

    public String getmPlace() {
        return mPlace;
    }

    public Date getmDate() {
        return mDate;
    }

    public String getUrl() {
        return url;
    }
}
