package cn.android.yhogp2.uitils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;

import android.util.Log;


import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;


import cn.android.yhogp2.activity.MainActivity;
import cn.android.yhogp2.application.MainApplication;
import cn.android.yhogp2.javabean.Rider;
import cn.android.yhogp2.javabean.Shop;
import okhttp3.Call;
import okhttp3.Response;

public class LoginUtil {
    private static Context context;
    private static LaunchDialog launchDialog;
    private static boolean isAuto;


    public static void initLoginUtil(Context mcontext,boolean misAuto) {
        context = mcontext;
        isAuto=misAuto;
    }

    public static void loginWithOkHttp(final String account, final String password, final Handler handler) {
        if (context != null&&!isAuto)
            launchDialog = new LaunchDialog(context);
        OkHttpUtil.loginWithOkHttp(account, password, new okhttp3.Callback() {
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (context != null&&!isAuto)
                    launchDialog.shutDown();
                final String responseData = response.body().string();
                Message msg = handler.obtainMessage();
                if (!responseData.equals("false")) {
                    msg.what = MainApplication.LOGIN_SUCCESS;
                    MainApplication.haveLogined = true;
                    saveUserRecord(account, password, OkHttpUtil.CLIENT_TYPE);
                    if (OkHttpUtil.CLIENT_TYPE.equals("1"))
                        MainApplication.loginShop = new Gson().fromJson(responseData, Shop.class);
                    else
                        MainApplication.loginRider = new Gson().fromJson(responseData, Rider.class);
                    MainApplication.haveLogined = true;
                    handler.sendMessage(msg);

                } else {
                    msg.what = MainApplication.AUTO_LOGIN_FAIL;
                    handler.sendMessage(msg);
                    Log.i("loginText", "login fail by account");
                }
                MainApplication.TYPE = OkHttpUtil.CLIENT_TYPE;
            }

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.i("loginText", "login  fail by net");
                if (context != null&&!isAuto)
                    launchDialog.shutDown();
                Message msg = handler.obtainMessage();
                msg.what = MainApplication.LOGIN_FAIL_NET;
                handler.sendMessage(msg);
            }
        });
    }


    public static void saveUserRecord(String account, String password, String type) {
        Log.i("loginnn", "do saveUserRecord");
        SharedPreferences.Editor editor = context.getSharedPreferences("client", Context.MODE_PRIVATE).edit();
        editor.putBoolean("isLogin", true);
        editor.putString("account", account);
        editor.putString("password", password);
        editor.putString("type", type);
        editor.apply();
    }


    public static void loginOut(Context context) {
        MainApplication.haveLogined = false;
        MainApplication.loginShop = null;
        MainApplication.loginRider = null;
        SharedPreferences sp_user = context.getSharedPreferences("client", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp_user.edit();
        editor.clear();
        editor.apply();
        context.startActivity(new Intent(context, MainActivity.class));
    }
}
