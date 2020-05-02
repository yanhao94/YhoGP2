package cn.android.yhogp2.intentservice;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.os.Message;
import android.util.Log;

import com.google.gson.reflect.TypeToken;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;

import cn.android.yhogp2.activity.MainActivity;
import cn.android.yhogp2.activity.shop.ShopHomeActivity;
import cn.android.yhogp2.application.MainApplication;
import cn.android.yhogp2.javabean.Goods;
import cn.android.yhogp2.javabean.Order;
import cn.android.yhogp2.uitils.OkHttpUtil;
import cn.android.yhogp2.uitils.TextUtilTools;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class RequestNewService extends IntentService {

    public RequestNewService() {
        super("RequestNewService");
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        doRequestNewOrder();
    }

    private void doRequestNewOrder() {
        OkHttpUtil.requestNewOrder(MainApplication.loginShop.getShopId(), new Callback() {
            Message msg = ShopHomeActivity.orderHandler.obtainMessage();

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                msg.what = -1;
                ShopHomeActivity.orderHandler.sendMessage(msg);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String responseStr = response.body().string();

                if (!responseStr.equals("notNow")) {
                    msg.what = 1;
                    List<Order> newOrderList = TextUtilTools.fromToJson(responseStr, new TypeToken<List<Order>>() {
                    }.getType());
                    msg.obj = newOrderList;
                    ShopHomeActivity.orderHandler.sendMessage(msg);
                }
            }
        });
        try {
            Thread.sleep(5000);
            doRequestNewOrder();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
