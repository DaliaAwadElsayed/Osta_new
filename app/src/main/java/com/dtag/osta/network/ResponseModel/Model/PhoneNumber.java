package com.dtag.osta.network.ResponseModel.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PhoneNumber {
    @SerializedName("email")
    @Expose
    private String phone;
    @SerializedName("code")
    @Expose
    private String code;

    public PhoneNumber() {
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
