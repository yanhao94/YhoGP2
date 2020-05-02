package cn.android.yhogp2.javabean;


import androidx.annotation.NonNull;

public class Order {
    private String shopName;
    private String riderName;
    private String userName;
    private double state;
    private String time;
    private String content;
    private int type;
    private String goodsListJson;
    private String addressJson;
    private int orderId;
    private int userId;
    private int shopId;
    private int riderId;
    private int imageId;
    private double charge;
    private double deliveryFee;

    public final static double SHOP_REJECT_ORDERS = -1;
    public final static double USER_SUMMIT_ORDERS = 0;// 用户已下单
    public final static double SHOP_CHECKED_ORDERS = 0.5;// 商家已经查看该订单
    public final static double SHOP_GET_ORDERS = 1;// 商家已接单
    public final static double RIDER_GET_ORDERS = 2;
    public final static double RIDER_DISTRUBUTION = 3;
    public final static double ORDER_FINISH = 4;

    public String getAddressJson() {
        return addressJson;
    }

    public void setAddressJson(String addressJson) {
        this.addressJson = addressJson;
    }

    public double getDeliveryFee() {
        return deliveryFee;
    }

    public void setDeliveryFee(double deliveryFee) {
        this.deliveryFee = deliveryFee;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getGoodsListJson() {
        return goodsListJson;
    }

    public void setGoodsListJson(String goodsListJson) {
        this.goodsListJson = goodsListJson;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getRiderName() {
        return riderName;
    }

    public void setRiderName(String riderName) {
        this.riderName = riderName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getShopId() {
        return shopId;
    }

    public void setShopId(int shopId) {
        this.shopId = shopId;
    }

    public int getRiderId() {
        return riderId;
    }

    public void setRiderId(int riderId) {
        this.riderId = riderId;
    }


    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public double getState() {
        return state;
    }

    public void setState(double state) {
        this.state = state;
    }


    public double getCharge() {
        return charge;
    }

    public void setCharge(double charge) {
        this.charge = charge;
    }

    @NonNull
    @Override
    public String toString() {
        return shopName +
                riderName + userName + state + time + content + type + goodsListJson + orderId + userId + shopId + riderId + charge;
    }
}
