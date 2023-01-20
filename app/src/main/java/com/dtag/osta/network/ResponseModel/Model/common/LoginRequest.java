package com.dtag.osta.network.ResponseModel.Model.common;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginRequest {
    @SerializedName("username")
    @Expose
    private String email;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("login_as")
    @Expose
    private String login_as;
    @SerializedName("device_token")
    @Expose
    private String device_token;

    public LoginRequest() {
    }

    public String getDevice_token() {
        return device_token;
    }

    public void setDevice_token(String device_token) {
        this.device_token = device_token;
    }

    public String getLogin_as() {
        return login_as;
    }

    public void setLogin_as(String login_as) {
        this.login_as = login_as;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
