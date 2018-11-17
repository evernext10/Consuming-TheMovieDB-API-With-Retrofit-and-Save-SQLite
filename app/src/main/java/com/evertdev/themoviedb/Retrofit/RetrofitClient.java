package com.evertdev.themoviedb.Retrofit;

import com.evertdev.themoviedb.Common.Common;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Evert Moreno on 16-11-2018.
 */
public class RetrofitClient {

    private static Retrofit retrofit = null;


    /**
     * Gets client.
     *
     * @return the client
     */
    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(Common.MOVIEDB_API_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}