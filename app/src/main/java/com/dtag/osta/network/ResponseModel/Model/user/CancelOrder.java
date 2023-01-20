package com.dtag.osta.network.ResponseModel.Model.user;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CancelOrder {
    @SerializedName("order_id")
    @Expose
    private Integer orderId;
    @SerializedName("cancel_reason")
    @Expose
    private String cancelReason;
    @SerializedName("status")
    @Expose
    private String status;

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public String getCancelReason() {
        return cancelReason;
    }

    public void setCancelReason(String cancelReason) {
        this.cancelReason = cancelReason;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
