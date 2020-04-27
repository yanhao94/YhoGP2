package cn.android.yhogp2.javabean;

public class User {
	private String account;
	private String password;
	private String name;
	private int userId;
	private int ordersAll = 0;
	private int ordersEx = 0;
	private int ordersDefault = 0;
	private double chargeAll = 0;
	private String inserTime;

	

	public User(String name, String account, String password) {
		this.name = name;
		this.account = account;
		this.password = password;
	}

	public User(String account, String password, String name, int ordersAll, int ordersEx, int ordersDefault,
			double chargeAll, String inserTime) {
		this.name = name;
		this.account = account;
		this.password = password;
		this.ordersAll = ordersAll;
		this.ordersEx = ordersEx;
		this.ordersDefault = ordersDefault;
		this.chargeAll = chargeAll;
		this.inserTime = inserTime;
	}

	
	
	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getInserTime() {
		return inserTime;
	}

	public void setInserTime(String inserTime) {
		this.inserTime = inserTime;
	}
	
	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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
