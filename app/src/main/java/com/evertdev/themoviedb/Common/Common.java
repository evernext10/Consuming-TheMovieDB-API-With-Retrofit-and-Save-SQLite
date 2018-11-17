package com.evertdev.themoviedb.Common;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class Common {
    /**
     * The constant MOVIEDB_API_KEY.
     */
    public final static String MOVIEDB_API_KEY = "0d677b16a44d2b5a79edf325bc1ee9b7";

    /**
     * The constant MOVIEDB_API_URL.
     */
    public final static String MOVIEDB_API_URL = "http://api.themoviedb.org/3/";

    /**
     * The constant MOVIEDB_SMALL_POSTER_URL.
     */
    public final static String MOVIEDB_SMALL_POSTER_URL = "http://image.tmdb.org/t/p/w185";

    /**
     * The constant MOVIEDB_LARGE_POSTER_URL.
     */
    public final static String MOVIEDB_LARGE_POSTER_URL = "http://image.tmdb.org/t/p/w500";

    /**
     * The constant INTENT_MOVIE_DETAIL.
     */
    public final static String INTENT_MOVIE_DETAIL = "INTENT_MOVIE_DETAIL";

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }
}
