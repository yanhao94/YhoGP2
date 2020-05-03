package cn.android.yhogp2.application;

import android.app.Application;
import android.app.Service;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.util.Log;

import com.baidu.mapapi.CoordType;
import com.baidu.mapapi.SDKInitializer;

import cn.android.yhogp2.BdLocationService.LocationService;
import cn.android.yhogp2.javabean.Rider;
import cn.android.yhogp2.javabean.Shop;
import cn.android.yhogp2.uitils.LoginUtil;
import cn.android.yhogp2.uitils.OkHttpUtil;
import cn.android.yhogp2.uitils.RequestHandler;

public class MainApplication extends Application {
    public static LocationService locationService;
    public Vibrator mVibrator;

    public static boolean haveLogined = false;
    public static Shop loginShop;
    public static Rider loginRider;
    public static String TYPE = "";
    private static Handler applicationHandler;

    public final static int AUTO_LOGIN_FAIL = -1;
    public final static int LOGIN_SUCCESS = 1;
    public final static int LOGIN_FAIL_NET = -2;
    public final static int LOGIN_OUT = 3;

    @Override
    public void onCreate() {
        super.onCreate();
        init();
    }

    private void init() {
        locationService = new LocationService(getApplicationContext());
        mVibrator = (Vibrator) getApplicationContext().getSystemService(Service.VIBRATOR_SERVICE);
        SDKInitializer.initialize(getApplicationContext());
        SDKInitializer.setCoordType(CoordType.BD09LL);
        getHandler();
        autoLogin();
    }

    private void getHandler() {
        applicationHandler = new RequestHandler() {
            @Override
            public void doRequestSuccess(Message msg) {
                //startActivity(new Intent(getApplicationContext(), MainApplication.TYPE=="1"? ShopHomeActivity.class: RiderHomeActivity.class));
            }

            @Override
            public void setTYPE() {
                REQUEST_TYPE = LOGIN;
            }
        }.getRequestHandler(this);
    }

    private void autoLogin() {
        LoginUtil.initLoginUtil(this);
        SharedPreferences sp_client = getSharedPreferences("client", MODE_PRIVATE);
        Log.i("sdsd", "isLogin" + sp_client.getBoolean("isLogin", false) + "type" + sp_client.getString("type", ""));
        if (sp_client.getBoolean("isLogin", false)) {
            if (sp_client.getString("type", "").equals("1"))
                OkHttpUtil.CLIENT_TYPE = "1";
            else
                OkHttpUtil.CLIENT_TYPE = "2";
            LoginUtil.loginWithOkHttp(sp_client.getString("account", ""),
                    sp_client.getString("password", ""), applicationHandler, true);
        }
    }
}
