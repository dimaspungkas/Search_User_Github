package com.dimaspungkas.searchusergithub.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIClient {

    public static final String BASE_URL = "https://api.github.com";

    private Retrofit retrofit;

    public APIClient() {
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public APIInterface getService() {

        return retrofit.create(APIInterface.class);
    }
}
