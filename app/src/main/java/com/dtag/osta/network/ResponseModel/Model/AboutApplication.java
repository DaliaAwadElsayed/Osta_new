package com.dtag.osta.network.ResponseModel.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AboutApplication {
    @SerializedName("name")
    @Expose
    private String name;

    public AboutApplication() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
