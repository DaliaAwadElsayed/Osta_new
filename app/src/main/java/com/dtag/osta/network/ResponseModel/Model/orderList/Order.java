package com.dtag.osta.network.ResponseModel.Model.orderList;

import com.dtag.osta.network.ResponseModel.Model.agent.Agent;
import com.dtag.osta.network.ResponseModel.Model.user.Registeration;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Order {
    @SerializedName("construction_type_id")
    @Expose
    private Integer construction_type_id;
    @SerializedName("levels_count")
    @Expose
    private Integer levels_count;
    @SerializedName("units_count")
    @Expose
    private Integer units_count;
    @SerializedName("order_id")
    @Expose
    private Integer order_id;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("user_id")
    @Expose
    private Integer userId;
    @SerializedName("agent_id")
    @Expose
    private Integer agentId;
    @SerializedName("city_id")
    @Expose
    private Integer cityId;
    @SerializedName("category_id")
    @Expose
    private Integer categoryId;
    @SerializedName("offer_id")
    @Expose
    private Object offerId;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("latitude")
    @Expose
    private String latitude;
    @SerializedName("longitude")
    @Expose
    private String longitude;
    @SerializedName("qr_code")
    @Expose
    private String qrCode;

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("agent_description")
    @Expose
    private String agentDescription;
    @SerializedName("payment_method")
    @Expose
    private String paymentMethod;
    @SerializedName("visit_fees")
    @Expose
    private double visitFees;
    @SerializedName("discount")
    @Expose
    private Integer discount;
    @SerializedName("cost")
    @Expose
    private double cost;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("time")
    @Expose
    private String time;
    @SerializedName("rate")
    @Expose
    private float rate;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("cancel_reason")
    @Expose
    private String cancelReason;
    @SerializedName("city")
    @Expose
    private City city;
    @SerializedName("category")
    @Expose
    private Category category;
    @SerializedName("agent")
    @Expose
    private Agent agent;
    @SerializedName("coupon")
    @Expose
    private String coupon;
    @SerializedName("uploaded_images")
    @Expose
    private List<UploadedImage> uploadedImages = null;
    @SerializedName("user")
    @Expose
    private Registeration user;

    public Order() {
    }

    public Integer getOrder_id() {
        return order_id;
    }

    public void setOrder_id(Integer order_id) {
        this.order_id = order_id;
    }


    public Registeration getUser() {
        return user;
    }

    public void setUser(Registeration user) {
        this.user = user;
    }

    public List<UploadedImage> getUploadedImages() {
        return uploadedImages;
    }

    public void setUploadedImages(List<UploadedImage> uploadedImages) {
        this.uploadedImages = uploadedImages;
    }

    public Integer getConstruction_type_id() {
        return construction_type_id;
    }

    public void setConstruction_type_id(Integer construction_type_id) {
        this.construction_type_id = construction_type_id;
    }

    public Integer getLevels_count() {
        return levels_count;
    }

    public void setLevels_count(Integer levels_count) {
        this.levels_count = levels_count;
    }

    public Integer getUnits_count() {
        return units_count;
    }

    public void setUnits_count(Integer units_count) {
        this.units_count = units_count;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getAgentId() {
        return agentId;
    }

    public void setAgentId(Integer agentId) {
        this.agentId = agentId;
    }

    public Integer getCityId() {
        return cityId;
    }

    public void setCityId(Integer cityId) {
        this.cityId = cityId;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public Object getOfferId() {
        return offerId;
    }

    public void setOfferId(Object offerId) {
        this.offerId = offerId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAgentDescription() {
        return agentDescription;
    }

    public void setAgentDescription(String agentDescription) {
        this.agentDescription = agentDescription;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public double getVisitFees() {
        return visitFees;
    }

    public void setVisitFees(double visitFees) {
        this.visitFees = visitFees;
    }

    public Integer getDiscount() {
        return discount;
    }

    public void setDiscount(Integer discount) {
        this.discount = discount;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public float getRate() {
        return rate;
    }

    public void setRate(float rate) {
        this.rate = rate;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getCancelReason() {
        return cancelReason;
    }

    public void setCancelReason(String cancelReason) {
        this.cancelReason = cancelReason;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Agent getAgent() {
        return agent;
    }

    public void setAgent(Agent agent) {
        this.agent = agent;
    }

    public String getCoupon() {
        return coupon;
    }

    public void setCoupon(String coupon) {
        this.coupon = coupon;
    }


}
