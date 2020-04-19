package cn.android.yhogp2.uitils;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class OkHttpUtil {
    public final static String ORIGINAL_URL = "http://192.168.0.104:9090/AndroidService";
    public final static String LOGIN_URL = ORIGINAL_URL + "/LoginServlet";
    public final static String REGISTER_URL = ORIGINAL_URL + "/RegisterServlet";
    public final static String REQUEST_URL = ORIGINAL_URL + "/RequestServlet";

    public  static String CLIENT_TYPE = "";


    public final static int ORDER_ACCOUNT_CANCEL = 0;
    public final static int ORDER_CHANGE_NAME = 1;
    public final static int ORDER_CHANGE_PASSWORD = 2;
    public final static int ORDER_REQUEST_SHOP_LIST = 3;

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

    public static void registerWithOkHttp(String account, String password, String name, okhttp3.Callback callback) {
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("registerAccount", account)
                .add("registerPassword", password)
                .add("registerName", name)
                .add("registerType", CLIENT_TYPE)
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
}
