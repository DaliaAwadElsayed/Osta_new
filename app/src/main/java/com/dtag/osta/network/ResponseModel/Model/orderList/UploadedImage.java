package com.dtag.osta.network.ResponseModel.Model.orderList;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UploadedImage {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("order_id")
    @Expose
    private Integer orderId;
    @SerializedName("src")
    @Expose
    private String src;

    public UploadedImage() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }
}
