package com.imengli.appletServer.daomain;

public class BannerEntity {

    private int id;

    private String picUrlPre;

    private String picName;

    private String picUrl;

    private String status;

    private int orderType;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPicUrlPre() {
        return picUrlPre;
    }

    public void setPicUrlPre(String picUrlPre) {
        this.picUrlPre = picUrlPre;
    }

    public String getPicName() {
        return picName;
    }

    public void setPicName(String picName) {
        this.picName = picName;
    }

    public int getOrderType() {
        return orderType;
    }

    public void setOrderType(int orderType) {
        this.orderType = orderType;
    }
}
