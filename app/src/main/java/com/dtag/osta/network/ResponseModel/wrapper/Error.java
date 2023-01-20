package com.dtag.osta.network.ResponseModel.wrapper;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Error {
    @SerializedName("agent_id")
    @Expose
    private List<String> agent_id = null;
    @SerializedName("phone")
    @Expose
    private List<String> phone = null;
    @SerializedName("code")
    @Expose
    private List<String> code = null;
    @SerializedName("name")
    @Expose
    private List<String> name = null;
    @SerializedName("email")
    @Expose
    private List<String> email = null;
    @SerializedName("password")
    @Expose
    private List<String> password = null;
    @SerializedName("city_id")
    @Expose
    private List<String> cityId = null;
    @SerializedName("address")
    @Expose
    private List<String> address = null;
    @SerializedName("account")
    @Expose
    private List<String> account = null;
    @SerializedName("status")
    @Expose
    private List<String> status = null;
    @SerializedName("birthday")
    @Expose
    private List<String> birthday = null;
    @SerializedName("coupon")
    @Expose
    private List<String> coupon = null;
    @SerializedName("documents_images")
    @Expose
    private List<String> documents_images = null;
    @SerializedName("documents_images[0]")
    @Expose
    private List<String> documents_images0 = null;
    @SerializedName("order")
    @Expose
    private String order = null;
    @SerializedName("un_active_account")
    @Expose
    private String un_active_account = null;
    @SerializedName("error")
    @Expose
    private String error = null;
    @SerializedName("national_id")
    private List<String> national_id;


    @SerializedName("national_id[0]")
    private List<String> national_id0;

    @SerializedName("national_id2")
    private List<String> national_id2;

    private List<String> image;
    private List<String> category_id;
    private List<String> latitude;
    private List<String> longitude;
    private List<String> date;
    private List<String> time;
    private List<String> description;
    private List<String> images;
    private List<String> payment_method;
    @SerializedName("images.0")
    @Expose
    private List<String> images_0;

    @SerializedName("images.1")
    @Expose
    private List<String> images_1;

    public List<String> getDocuments_images() {
        return documents_images;
    }

    public String getUn_active_account() {
        return un_active_account;
    }

    public List<String> getAgent_id() {
        return agent_id;
    }

    public void setAgent_id(List<String> agent_id) {
        this.agent_id = agent_id;
    }

    public void setUn_active_account(String un_active_account) {
        this.un_active_account = un_active_account;
    }

    public String getError() {
        return error;
    }

    public List<String> getDocuments_images0() {
        return documents_images0;
    }

    public void setDocuments_images0(List<String> documents_images0) {
        this.documents_images0 = documents_images0;
    }

    public List<String> getNational_id0() {
        return national_id0;
    }

    public void setNational_id0(List<String> national_id0) {
        this.national_id0 = national_id0;
    }

    public List<String> getNational_id2() {
        return national_id2;
    }

    public void setNational_id2(List<String> national_id2) {
        this.national_id2 = national_id2;
    }

    public void setError(String error) {
        this.error = error;
    }

    public void setDocuments_images(List<String> documents_images) {
        this.documents_images = documents_images;
    }

    public List<String> getCategory_id() {
        return category_id;
    }

    public void setCategory_id(List<String> category_id) {
        this.category_id = category_id;
    }

    public List<String> getBirthday() {
        return birthday;
    }

    public void setBirthday(List<String> birthday) {
        this.birthday = birthday;
    }

    public List<String> getLatitude() {
        return latitude;
    }

    public List<String> getCoupon() {
        return coupon;
    }

    public void setCoupon(List<String> coupon) {
        this.coupon = coupon;
    }

    public void setLatitude(List<String> latitude) {
        this.latitude = latitude;
    }

    public List<String> getLongitude() {
        return longitude;
    }

    public void setLongitude(List<String> longitude) {
        this.longitude = longitude;
    }

    public List<String> getDate() {
        return date;
    }

    public void setDate(List<String> date) {
        this.date = date;
    }

    public List<String> getTime() {
        return time;
    }

    public void setTime(List<String> time) {
        this.time = time;
    }

    public List<String> getDescription() {
        return description;
    }

    public void setDescription(List<String> description) {
        this.description = description;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public List<String> getPayment_method() {
        return payment_method;
    }

    public void setPayment_method(List<String> payment_method) {
        this.payment_method = payment_method;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public List<String> getAccount() {
        return account;
    }

    public void setAccount(List<String> account) {
        this.account = account;
    }

    public List<String> getName() {
        return name;
    }

    public void setName(List<String> name) {
        this.name = name;
    }

    public List<String> getEmail() {
        return email;
    }

    public void setEmail(List<String> email) {
        this.email = email;
    }

    public List<String> getPassword() {
        return password;
    }

    public void setPassword(List<String> password) {
        this.password = password;
    }

    public List<String> getCityId() {
        return cityId;
    }

    public void setCityId(List<String> cityId) {
        this.cityId = cityId;
    }

    public List<String> getAddress() {
        return address;
    }

    public void setAddress(List<String> address) {
        this.address = address;
    }

    public List<String> getCode() {
        return code;
    }

    public void setCode(List<String> code) {
        this.code = code;
    }

    public List<String> getPhone() {
        return phone;
    }

    public void setPhone(List<String> phone) {
        this.phone = phone;
    }

    public List<String> getNational_id() {
        return national_id;
    }

    public void setNational_id(List<String> national_id) {
        this.national_id = national_id;
    }

    public List<String> getImage() {
        return image;
    }

    public void setImage(List<String> image) {
        this.image = image;
    }

    public List<String> getStatus() {
        return status;
    }

    public void setStatus(List<String> status) {
        this.status = status;
    }

    public List<String> getImages_0() {
        return images_0;
    }

    public void setImages_0(List<String> images_0) {
        this.images_0 = images_0;
    }

    public List<String> getImages_1() {
        return images_1;
    }

    public void setImages_1(List<String> images_1) {
        this.images_1 = images_1;
    }

    @Override
    public String toString() {
        return "Error{" +
                "phone=" + phone +
                ", code=" + code +
                ", name=" + name +
                ", email=" + email +
                ", password=" + password +
                ", cityId=" + cityId +
                ", address=" + address +
                ", account=" + account +
                ", status=" + status +
                ", birthday=" + birthday +
                ", coupon=" + coupon +
                ", documents_images=" + documents_images +
                ", order='" + order + '\'' +
                ", error='" + error + '\'' +
                ", national_id=" + national_id +
                ", image=" + image +
                ", category_id=" + category_id +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", date=" + date +
                ", time=" + time +
                ", description=" + description +
                ", images=" + images +
                ", payment_method=" + payment_method +
                ", images_0=" + images_0 +
                ", images_1=" + images_1 +
                '}';
    }
}
