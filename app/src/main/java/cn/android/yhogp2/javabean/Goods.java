package cn.android.yhogp2.javabean;

import java.io.Serializable;

public class Goods implements Serializable {
    private int id;
    private int salesAll;
    private int salesMonth;
    private int prTimes;
    private int nrTimes;
    private int shopOwnerId;
    private int amount;
    private String type;
    private String name;
    private String introduction;
    private Double price;

    public final static String TYPE[]={"糕点","面食","米饭","烧烤","海鲜","火锅","炸鸡","奶茶","咖啡","汉堡","快餐","其他"};

    public Goods(int id, int shopOwnerId, String name, String type, String introduction, double price, int salesAll,
                 int salesMonth, int prTimes, int nrTimes) {
        this.id = id;
        this.shopOwnerId = shopOwnerId;
        this.salesAll = salesAll;
        this.salesMonth = salesMonth;
        this.prTimes = prTimes;
        this.nrTimes = nrTimes;
        this.name = name;
        this.type = type;
        this.introduction = introduction;
        this.price = price;

    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public Goods(int shopOwnerId) {
        this.shopOwnerId = shopOwnerId;
    }

    public Double getPrice() {
        return price;
    }


    public void setPrice(Double price) {
        this.price = price;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSalesAll() {
        return salesAll;
    }

    public void setSalesAll(int salesAll) {
        this.salesAll = salesAll;
    }

    public int getSalesMonth() {
        return salesMonth;
    }

    public void setSalesMonth(int salesMonth) {
        this.salesMonth = salesMonth;
    }

    public int getPrTimes() {
        return prTimes;
    }

    public void setPrTimes(int prTimes) {
        this.prTimes = prTimes;
    }

    public int getNrTimes() {
        return nrTimes;
    }

    public void setNrTimes(int nrTimes) {
        this.nrTimes = nrTimes;
    }

    public int getShopOwnerId() {
        return shopOwnerId;
    }

    public void setShopOwnerId(int shopOwnerId) {
        this.shopOwnerId = shopOwnerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getTypeString() {
        StringBuffer sb = new StringBuffer(128);
        for (int i = 0; i < 12; i++) {
            if (type.charAt(i) == '1')
                sb.append(TYPE[i]).append("\t");
        }
        return sb.toString();
    }
    public int getTypeId()
    {
        for (int i = 0; i < 12; i++) {
            if (type.charAt(i) == '1')
                return i;
        }
        return 11;
    }
}
