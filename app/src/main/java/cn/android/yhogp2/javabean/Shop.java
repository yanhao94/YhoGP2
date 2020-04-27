package cn.android.yhogp2.javabean;

public class Shop {
	private String shopName;
	private String type;
	private String addr;
	private String fullAddr;
	private String introduction;
	private double score;
	private double favorableRate;
	private double distance;//toUser
	private double deliverPay;//toUser
	private double longtitude;
	private double latitude;
	private double deliveryDistance;
	
	private int ShopId;
	private int CityCode;
	private int imageId;
	private int finishedOrders;
	private int salesAll;
	private int salesMonthy;
	
	public Shop() {

	}

	public Shop(String shopName, double score, double distance, int imageId, int finishedOrders, int minPay, int deliverPay) {
		this.shopName = shopName;
		this.score = score;
		this.distance = distance;
		this.imageId = imageId;
		this.finishedOrders = finishedOrders;
		this.minPay = minPay;
		this.deliverPay = deliverPay;
	}
	
	
	public int getShopId() {
		return ShopId;
	}

	public void setShopId(int shopId) {
		ShopId = shopId;
	}

	public String getFullAddr() {
		return fullAddr;
	}

	public void setFullAddr(String fullAddr) {
		this.fullAddr = fullAddr;
	}

	public int getCityCode() {
		return CityCode;
	}

	public void setCityCode(int cityCode) {
		CityCode = cityCode;
	}

	public double getLongtitude() {
		return longtitude;
	}

	public void setLongtitude(double longtitude) {
		this.longtitude = longtitude;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getDeliveryDistance() {
		return deliveryDistance;
	}

	public void setDeliveryDistance(double deliveryDistance) {
		this.deliveryDistance = deliveryDistance;
	}

	public double getFavorableRate() {
		return favorableRate;
	}

	public void setFavorableRate(double favorableRate) {
		this.favorableRate = favorableRate;
	}

	
	public String getIntroduction() {
		return introduction;
	}

	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}

	public int getSalesAll() {
		return salesAll;
	}

	public void setSalesAll(int salesAll) {
		this.salesAll = salesAll;
	}

	public int getSalesMonthy() {
		return salesMonthy;
	}

	public void setSalesMonthy(int salesMonthy) {
		this.salesMonthy = salesMonthy;
	}

	private double minPay;
	

	
	
	public String getAddr() {
		return addr;
	}

	public void setAddr(String addr) {
		this.addr = addr;
	}

	public int getFinishedOrders() {
		return finishedOrders;
	}

	public void setFinishedOrders(int finishedOrders) {
		this.finishedOrders = finishedOrders;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	public int getImageId() {
		return imageId;
	}

	public void setImageId(int imageId) {
		this.imageId = imageId;
	}

	public double getScore() {
		return score;
	}

	public void setScore(double score) {
		this.score = score;
	}

	public double getDistance() {
		return distance;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}


	public double getMinPay() {
		return minPay;
	}

	public void setMinPay(double minPay) {
		this.minPay = minPay;
	}

	public double getDeliverPay() {
		return deliverPay;
	}

	public void setDeliverPay(double deliverPay) {
		this.deliverPay = deliverPay;
	}

}
