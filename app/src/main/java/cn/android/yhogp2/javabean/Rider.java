package cn.android.yhogp2.javabean;

public class Rider {
	private String account;
	private String pwd;
	private String name;
	private String insertTime;
	private int RiderId;
	private int ordersAll;
	private int ordersEx;
	private int ordersDefault;
	private double chargeAll;

	
	public int getRiderId() {
		return RiderId;
	}

	public void setRiderId(int riderId) {
		RiderId = riderId;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getInsertTime() {
		return insertTime;
	}

	public void setInsertTime(String insertTime) {
		this.insertTime = insertTime;
	}

	public int getOrdersAll() {
		return ordersAll;
	}

	public void setOrdersAll(int ordersAll) {
		this.ordersAll = ordersAll;
	}

	public int getOrdersEx() {
		return ordersEx;
	}

	public void setOrdersEx(int ordersEx) {
		this.ordersEx = ordersEx;
	}

	public int getOrdersDefault() {
		return ordersDefault;
	}

	public void setOrdersDefault(int ordersDefault) {
		this.ordersDefault = ordersDefault;
	}

	public double getChargeAll() {
		return chargeAll;
	}

	public void setChargeAll(double chargeAll) {
		this.chargeAll = chargeAll;
	}

}
