package cn.android.yhogp2.javabean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class OrdersGroup {
    private int id;
    public List<Order> listOrders;
    public String ordersContent;
    private String time;
    private String addressJsonList;
    private Date startDate;
    private Double centerLontitude;
    private Double centerLatitude;
    private Double riderDistance;
    private Double state;

    public String getAddressJsonList() {
        return addressJsonList;
    }

    public void setAddressJsonList(String addressJsonList) {
        this.addressJsonList = addressJsonList;
    }

    public OrdersGroup() {
        this.listOrders = new ArrayList<>();
        this.startDate = new Date();
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Double getState() {
        return state;
    }


    public void setState(Double state) {
        this.state = state;
    }


    public Double getRiderDistance() {
        return riderDistance;
    }

    public void setRiderDistance(Double riderDistance) {
        this.riderDistance = riderDistance;
    }

    public Double getCenterLontitude() {
        return centerLontitude;
    }

    public void setCenterLontitude(Double centerLontitude) {
        this.centerLontitude = centerLontitude;
    }

    public Double getCenterLatitude() {
        return centerLatitude;
    }

    public void setCenterLatitude(Double centerLatitude) {
        this.centerLatitude = centerLatitude;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOrdersContent() {
        return ordersContent;
    }

    public void setOrdersContent(String ordersContent) {
        this.ordersContent = ordersContent;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public List<Order> getListOrders() {
        return listOrders;
    }

    public void setListOrders(List<Order> listOrders) {
        this.listOrders = listOrders;
    }


}
