package com.example.quakereport;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

import java.util.List;

public class EarthquakeLoader extends AsyncTaskLoader<List<EarthquakeData>> {

    private String mUrl;
    private final String LOG_TAG = EarthquakeLoader.class.getSimpleName();

    public EarthquakeLoader(@NonNull Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        Log.i(LOG_TAG, "onStartLoading: ");
        forceLoad();
    }

    @Nullable
    @Override
    public List<EarthquakeData> loadInBackground() {
        Log.i(LOG_TAG, "loadInBackground: ");
        if (mUrl == null) {
            return null;
        }
        return QueryUtils.fetchEarthquakeData(mUrl);
    }
}
