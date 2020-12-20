package com.dimaspungkas.searchusergithub.api;

import com.dimaspungkas.searchusergithub.model.MainDataResponse;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

public interface APIInterface {

    @GET("/search/users")
    Call<MainDataResponse> getMainDataList(@QueryMap(encoded = false) Map<String,String> filter );
}
