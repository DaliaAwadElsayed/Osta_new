package com.dtag.osta.network.ResponseModel.Model.common;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RateApp {
    @SerializedName("comment")
    @Expose
    private String comment;
    @SerializedName("rate")
    @Expose
    private float rate;

    public RateApp() {
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public float getRate() {
        return rate;
    }

    public void setRate(float rate) {
        this.rate = rate;
    }
}
