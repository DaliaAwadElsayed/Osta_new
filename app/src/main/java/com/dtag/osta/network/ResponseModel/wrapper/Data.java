package com.dtag.osta.network.ResponseModel.wrapper;

import com.dtag.osta.network.ResponseModel.Model.City;
import com.dtag.osta.network.ResponseModel.Model.Services;
import com.dtag.osta.network.ResponseModel.Model.agent.Agent;
import com.dtag.osta.network.ResponseModel.Model.agent.OrderStat;
import com.dtag.osta.network.ResponseModel.Model.orderList.Category;
import com.dtag.osta.network.ResponseModel.Model.orderList.Order;
import com.dtag.osta.network.ResponseModel.Model.setting.Setting;
import com.dtag.osta.network.ResponseModel.Model.user.Registeration;
import com.dtag.osta.network.ResponseModel.Model.user.sales.Sales;
import com.dtag.osta.network.ResponseModel.Model.user.sales.SalesDetails;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Data {
    @SerializedName("categories")
    @Expose
    private List<Services> servicesList = null;
    @SerializedName("orders")
    @Expose
    private List<Order> orders = null;
    @SerializedName("cities")
    @Expose
    private List<City> cities = null;
    @SerializedName("city")
    @Expose
    private City city;
    @SerializedName("user")
    @Expose
    private Registeration user;
    @SerializedName("agent")
    @Expose
    private Agent agent;
    @SerializedName("category")
    @Expose
    private Category category;
    @SerializedName("order")
    @Expose
    private Order order;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("content")
    @Expose
    private String content;
    @SerializedName("page_name")
    @Expose
    private String pageName;
    @SerializedName("content_en")
    @Expose
    private String contentEn;
    @SerializedName("content_ar")
    @Expose
    private String contentAr;
    @SerializedName("name_ar")
    @Expose
    private String nameAr;
    @SerializedName("name_en")
    @Expose
    private String nameEn;
    @SerializedName("text_ar")
    @Expose
    private String textAr;
    @SerializedName("text_en")
    @Expose
    private String textEn;
    @SerializedName("coupon")
    @Expose
    private String coupon;
    @SerializedName("discount")
    @Expose
    private String discount;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("start_date")
    @Expose
    private String startDate;
    @SerializedName("end_date")
    @Expose
    private String endDate;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("sales ")
    @Expose
    private List<Sales> sales = null;
    @SerializedName("sales_details")
    @Expose
    private SalesDetails salesDetails;
    @SerializedName("settings")
    @Expose
    private List<Setting> settings = null;
    @SerializedName("order_stat")
    @Expose
    private List<OrderStat> orderStat = null;

    public List<OrderStat> getOrderStat() {
        return orderStat;
    }

    public void setOrderStat(List<OrderStat> orderStat) {
        this.orderStat = orderStat;
    }
    public List<Setting> getSettings() {
        return settings;
    }

    public void setSettings(List<Setting> settings) {
        this.settings = settings;
    }
    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public SalesDetails getSalesDetails() {
        return salesDetails;
    }

    public void setSalesDetails(SalesDetails salesDetails) {
        this.salesDetails = salesDetails;
    }

    public List<Sales> getSales() {
        return sales;
    }

    public void setSales(List<Sales> sales) {
        this.sales = sales;
    }

    public String getContentEn() {
        return contentEn;
    }

    public void setContentEn(String contentEn) {
        this.contentEn = contentEn;
    }

    public String getContentAr() {
        return contentAr;
    }

    public void setContentAr(String contentAr) {
        this.contentAr = contentAr;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPageName() {
        return pageName;
    }

    public void setPageName(String pageName) {
        this.pageName = pageName;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Agent getAgent() {
        return agent;
    }

    public void setAgent(Agent agent) {
        this.agent = agent;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public Registeration getUser() {
        return user;
    }

    public void setUser(Registeration user) {
        this.user = user;
    }

    public List<City> getCities() {
        return cities;
    }

    public void setCities(List<City> cities) {
        this.cities = cities;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    public List<Services> getServicesList() {
        return servicesList;
    }

    public void setServicesList(List<Services> servicesList) {
        this.servicesList = servicesList;
    }
}
