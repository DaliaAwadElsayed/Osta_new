package com.dtag.osta.network.ResponseModel.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Services {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("name_en")
    @Expose
    private String nameEn;
    @SerializedName("icon")
    @Expose
    private String icon;
    @SerializedName("visit_fees")
    @Expose
    private Integer visitFees;

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

    public String getNameEn() {
        return nameEn;
    }

    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Integer getVisitFees() {
        return visitFees;
    }

    public void setVisitFees(Integer visitFees) {
        this.visitFees = visitFees;
    }
}
