package com.dimaspungkas.searchusergithub.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MainDataResponse {
    @SerializedName("items")
    private List<MainData> items;
    @SerializedName("total_count")
    @Expose
    private int totalCount;

    public List<MainData> getItems() {
        return items;
    }
    public int getTotalCount() {
        return totalCount;
    }
}
