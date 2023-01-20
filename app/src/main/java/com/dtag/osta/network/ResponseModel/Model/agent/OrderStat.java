package com.dtag.osta.network.ResponseModel.Model.agent;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OrderStat {
    @SerializedName("date_now")
    @Expose
    private String dateNow;
    @SerializedName("total")
    @Expose
    private Integer total;

    public OrderStat() {
    }

    public String getDateNow() {
        return dateNow;
    }

    public void setDateNow(String dateNow) {
        this.dateNow = dateNow;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }
}
