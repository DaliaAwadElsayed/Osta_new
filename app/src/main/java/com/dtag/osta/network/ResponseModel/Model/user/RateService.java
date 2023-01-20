package com.dtag.osta.network.ResponseModel.Model.user;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RateService {
    @SerializedName("agent_id")
    @Expose
    private Integer userId;
    @SerializedName("order_id")
    @Expose
    private Integer id;
    @SerializedName("rate")
    @Expose
    private float rate;
    @SerializedName("rate_comment")
    @Expose
    private String rate_comment;

    public RateService() {
    }

    public String getRate_comment() {
        return rate_comment;
    }

    public void setRate_comment(String rate_comment) {
        this.rate_comment = rate_comment;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public float getRate() {
        return rate;
    }

    public void setRate(float rate) {
        this.rate = rate;
    }
}
