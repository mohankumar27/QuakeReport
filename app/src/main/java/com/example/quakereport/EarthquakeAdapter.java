package com.example.quakereport;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class EarthquakeAdapter extends ArrayAdapter<EarthquakeData> {

    private static final String LOCATION_SEPARATOR = " of ";


    public EarthquakeAdapter(@NonNull Context context, ArrayList<EarthquakeData> data) {
        super(context, 0,data);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View listItemView = convertView;
        if(listItemView == null){
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.earthquake_list, parent,false);
        }
        final EarthquakeData earthquakeData = getItem(position);

        TextView magnitudeTextView = listItemView.findViewById(R.id.magnitude);
        double magnitude = earthquakeData.getmMagnitude();
        int color = getMagnitudeColor((int) Math.round(magnitude));
        magnitudeTextView.setText(formatMagnitude(magnitude));
        GradientDrawable magnitudeCircle = (GradientDrawable) magnitudeTextView.getBackground();
        magnitudeCircle.setColor(color);

        String place = earthquakeData.getmPlace();
        String placeOffset = getContext().getString(R.string.near_the);
        String placePrimary;
        if(place.contains(LOCATION_SEPARATOR)){ // check if place value got from json contains " of "
            placeOffset = place.split(LOCATION_SEPARATOR,2)[0] + LOCATION_SEPARATOR;
            placePrimary = place.split(LOCATION_SEPARATOR,2)[1];
        }else{
            placePrimary = place;
        }
        TextView offsetplaceTextView = listItemView.findViewById(R.id.place_offset);
        offsetplaceTextView.setText(placeOffset.trim());
        TextView primaryplaceTextView = listItemView.findViewById(R.id.place_primary);
        primaryplaceTextView.setText(placePrimary.trim());

        SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
        String date = formatter.format(earthquakeData.getmDate());
        TextView dateTextView = listItemView.findViewById(R.id.date);
        dateTextView.setText(date);
        formatter = new SimpleDateFormat("h:mm a", Locale.getDefault());
        String time = formatter.format(earthquakeData.getmDate());
        TextView timeTextView = listItemView.findViewById(R.id.time);
        timeTextView.setText(time);

        return listItemView;

    }

    private int getMagnitudeColor(int magnitude) {
        int magnitudeColorResourceId;
        switch (magnitude) {
            case 0:
            case 1:
                magnitudeColorResourceId = R.color.magnitude1;
                break;
            case 2:
                magnitudeColorResourceId = R.color.magnitude2;
                break;
            case 3:
                magnitudeColorResourceId = R.color.magnitude3;
                break;
            case 4:
                magnitudeColorResourceId = R.color.magnitude4;
                break;
            case 5:
                magnitudeColorResourceId = R.color.magnitude5;
                break;
            case 6:
                magnitudeColorResourceId = R.color.magnitude6;
                break;
            case 7:
                magnitudeColorResourceId = R.color.magnitude7;
                break;
            case 8:
                magnitudeColorResourceId = R.color.magnitude8;
                break;
            case 9:
                magnitudeColorResourceId = R.color.magnitude9;
                break;
            default:
                magnitudeColorResourceId = R.color.magnitude10plus;
                break;
        }
        return ContextCompat.getColor(getContext(), magnitudeColorResourceId);

    }

    private String formatMagnitude(double magnitude){
        DecimalFormat formatter = new DecimalFormat("0.0");
        return formatter.format(magnitude);
    }
}
