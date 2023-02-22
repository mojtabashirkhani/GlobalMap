package com.samiei.globalmap.Models.search;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SearchMapIrResponseModel {
    @SerializedName("odata.count")
    @Expose
    private Integer odataCount;

    @SerializedName("value")
    @Expose
    private List<Value> value = null;

    public Integer getOdataCount() {
        return odataCount;
    }

    public void setOdataCount(Integer odataCount) {
        this.odataCount = odataCount;
    }

    public List<Value> getValue() {
        return value;
    }

    public void setValue(List<Value> value) {
        this.value = value;
    }
}
