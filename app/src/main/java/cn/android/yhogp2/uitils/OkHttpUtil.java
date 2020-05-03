package cn.android.yhogp2.uitils;

import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class OkHttpUtil {
    public final static String ORIGINAL_URL = "http://192.168.0.108:9090/AndroidService";
    public final static String LOGIN_URL = ORIGINAL_URL + "/LoginServlet";
    public final static String REGISTER_URL = ORIGINAL_URL + "/RegisterServlet";
    public final static String REQUEST_URL = ORIGINAL_URL + "/RequestServlet";
    public final static String ORDER_URL = ORIGINAL_URL + "/OrderManagerServlet";

    public static final int TYPE_ORDER_NEW = 0;
    public static final int TYPE_ORDER_HISTORY = 1;

    public static String CLIENT_TYPE = "";


    public final static int ORDER_ACCOUNT_CANCEL = 0;
    public final static int ORDER_CHANGE_NAME = 1;
    public final static int ORDER_CHANGE_PASSWORD = 2;
    public final static int ORDER_REQUEST_SHOP_LIST = 3;
    public final static int ORDER_REQUEST_SHOP_GOODS = 4;
    public final static int ORDER_ADD_GOODS = 6;
    public final static int ORDER_CHANGE_GOODS = 7;


    public final static int ORDER_SHOP_REQUEST_NEW_ORDERSS = 1;
    public final static int ORDER_SHOP_CHANGE_ORDERS_STATE = 5;
    public final static int ORDER_SHOP_REQUEST_NEWORDERS_LIST = 6;
    public final static int ORDER_SHOP_REQUEST_HISTORY_ORDERS_LIST = 7;

    public final static int ORDER_RIDER_REQUEST_ORDERS_LIST = 9;
    public final static int ORDER_RIDER_UPDATE_LOCATION = 10;
    public final static int ORDER_RIDER_CHANGE_ORDER_STATE = 11;

    public static final int REQUEST_SUCCESS = 1;
    public static final int REQUEST_FAIL_NET = 2;
    public static final int REQUEST_FAIL_SERVER = 3;

    public static void loginWithOkHttp(String account, String password, okhttp3.Callback callback) {
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("loginAccount", account)
                .add("loginPassword", password)
                .add("loginType", CLIENT_TYPE)
                .build();
        Request request = new Request.Builder()
                .url(LOGIN_URL)
                .post(body)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void registerWithOkHttp(String account, String password, String name, String tel, String shopJson, okhttp3.Callback callback) {
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("registerAccount", account)
                .add("registerPassword", password)
                .add("registerName", name)
                .add("registerTel", tel)
                .add("registerType", CLIENT_TYPE)
                .add("shopJson", shopJson)
                .build();
        Request request = new Request.Builder()
                .url(REGISTER_URL)
                .post(body)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void changeNameWithOkHttp(String account, String name, okhttp3.Callback callback) {
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("order", String.valueOf(ORDER_CHANGE_NAME))
                .add("account", account)
                .add("newName", name)
                .add("type", CLIENT_TYPE)
                .build();
        Request request = new Request.Builder()
                .url(REQUEST_URL)
                .post(body)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void changePasswordWithOkHttp(String account, String newPassword, okhttp3.Callback callback) {
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("order", String.valueOf(ORDER_CHANGE_PASSWORD))
                .add("account", account)
                .add("newPassword", newPassword)
                .add("type", CLIENT_TYPE)
                .build();
        Request request = new Request.Builder()
                .url(REQUEST_URL)
                .post(body)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void loginOffAccountWithOkHttp(String account, okhttp3.Callback callback) {
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("order", String.valueOf(ORDER_ACCOUNT_CANCEL))
                .add("account", account)
                .add("type", CLIENT_TYPE)
                .build();
        Request request = new Request.Builder()
                .url(REQUEST_URL)
                .post(body)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void requestShopList(String cityCode, String longtitude, String latitude, okhttp3.Callback callback) {
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("order", String.valueOf(ORDER_REQUEST_SHOP_LIST))
                .add("cityCode", cityCode)
                .add("longtitude", longtitude)
                .add("latitude", latitude)
                .build();
        Request request = new Request.Builder()
                .url(REQUEST_URL)
                .post(body)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void requestShopGoodsList(int shopId, Callback callback) {
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("order", String.valueOf(ORDER_REQUEST_SHOP_GOODS))
                .add("shopId", String.valueOf(shopId))
                .build();
        Request request = new Request.Builder()
                .url(REQUEST_URL)
                .post(body)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void shopRequestNewOrder(int shopId, Callback callback) {
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("order", String.valueOf(ORDER_SHOP_REQUEST_NEW_ORDERSS))
                .add("shopId", String.valueOf(shopId))
                .build();
        Request request = new Request.Builder()
                .url(ORDER_URL)
                .post(body)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void changeOrderState(int orderId, double state, Callback callback) {
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("order", String.valueOf(ORDER_SHOP_CHANGE_ORDERS_STATE))
                .add("orderId", String.valueOf(orderId))
                .add("state", String.valueOf(state))
                .build();
        Request request = new Request.Builder()
                .url(ORDER_URL)
                .post(body)
                .build();
        client.newCall(request).enqueue(callback);
    }
    public static void riderChangeOrderState(int orderId, double state, int riderId,int orderType,Callback callback) {
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("order", String.valueOf(ORDER_RIDER_CHANGE_ORDER_STATE))
                .add("orderId", String.valueOf(orderId))
                .add("riderId", String.valueOf(riderId))
                .add("orderType", String.valueOf(orderType))
                .add("state", String.valueOf(state))
                .build();
        Request request = new Request.Builder()
                .url(ORDER_URL)
                .post(body)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void shopRequestOrdersList(int shopId, int type, Callback callback) {
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("shopId", String.valueOf(shopId))
                .add("order", String.valueOf(type == TYPE_ORDER_NEW ? ORDER_SHOP_REQUEST_NEWORDERS_LIST : ORDER_SHOP_REQUEST_HISTORY_ORDERS_LIST))
                .build();
        Request request = new Request.Builder()
                .url(ORDER_URL)
                .post(body)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void changeGoods(String goodsJson, Callback callback) {
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("goodsJson", goodsJson)
                .add("order", String.valueOf(ORDER_CHANGE_GOODS))
                .build();
        Request request = new Request.Builder()
                .url(REQUEST_URL)
                .post(body)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void addGoods(String goodsJson, Callback callback) {
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("goodsJson", goodsJson)
                .add("order", String.valueOf(ORDER_ADD_GOODS))
                .build();
        Request request = new Request.Builder()
                .url(REQUEST_URL)
                .post(body)
                .build();
        client.newCall(request).enqueue(callback);
    }


    public static void riderRequestOrdersList(int riderId, int type, Callback callback) {
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("riderId", String.valueOf(riderId))
                .add("type", String.valueOf(type))
                .add("order", String.valueOf(ORDER_RIDER_REQUEST_ORDERS_LIST))
                .build();
        Request request = new Request.Builder()
                .url(ORDER_URL)
                .post(body)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void riderUpdateLocation(int riderId, double latitude, double lontitude, Callback callback) {
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("riderId", String.valueOf(riderId))
                .add("latitude", String.valueOf(latitude))
                .add("lontitude", String.valueOf(lontitude))
                .add("order", String.valueOf(ORDER_RIDER_UPDATE_LOCATION))
                .build();
        Request request = new Request.Builder()
                .url(ORDER_URL)
                .post(body)
                .build();
        client.newCall(request).enqueue(callback);
    }
}
