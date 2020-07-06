package cn.android.yhogp2.javabean;

import java.io.Serializable;

public class MyAddress implements Serializable {
    private Double userLatitude;
    private Double userLongtitude;
    private Double shopLatitude;
    private Double shopLongtitude;

    private String userDescribeAdd;
    private String userAddr;
    private String shopAddr;

    public MyAddress() {
    }

    public MyAddress(String userAddr, String shopAddr, Double userLatitude, Double userLongtitude, Double shopLatitude, Double shopLongtitude) {
        this.userAddr = userAddr;
        this.shopAddr = shopAddr;
        this.userLatitude = userLatitude;
        this.userLongtitude = userLongtitude;
        this.shopLatitude = shopLatitude;
        this.shopLongtitude = shopLongtitude;
    }

    public String getUserDescribeAdd() {
        return userDescribeAdd;
    }

    public void setUserDescribeAdd(String userDescribeAdd) {
        this.userDescribeAdd = userDescribeAdd;
    }

    public Double getUserLatitude() {
        return userLatitude;
    }

    public void setUserLatitude(Double userLatitude) {
        this.userLatitude = userLatitude;
    }

    public Double getUserLongtitude() {
        return userLongtitude;
    }

    public void setUserLongtitude(Double userLongtitude) {
        this.userLongtitude = userLongtitude;
    }

    public Double getShopLatitude() {
        return shopLatitude;
    }

    public void setShopLatitude(Double shopLatitude) {
        this.shopLatitude = shopLatitude;
    }

    public Double getShopLongtitude() {
        return shopLongtitude;
    }

    public void setShopLongtitude(Double shopLongtitude) {
        this.shopLongtitude = shopLongtitude;
    }

    public String getUserAddr() {
        return userAddr;
    }

    public void setUserAddr(String userAddr) {
        this.userAddr = userAddr;
    }

    public String getShopAddr() {
        return shopAddr;
    }

    public void setShopAddr(String shopAddr) {
        this.shopAddr = shopAddr;
    }

}
