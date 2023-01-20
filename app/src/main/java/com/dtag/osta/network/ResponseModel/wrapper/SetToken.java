package com.dtag.osta.network.ResponseModel.wrapper;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SetToken {
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("token")
    @Expose
    private String device_token;

    public SetToken() {
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDevice_token() {
        return device_token;
    }

    public void setDevice_token(String device_token) {
        this.device_token = device_token;
    }
}
