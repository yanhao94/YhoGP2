package cn.android.yhogp2.intentservice;

import android.annotation.SuppressLint;
import android.app.IntentService;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.google.gson.reflect.TypeToken;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;
import java.util.OptionalDouble;

import cn.android.yhogp2.BdLocationService.LocationService;
import cn.android.yhogp2.activity.rider.RiderHomeActivity;
import cn.android.yhogp2.activity.shop.ShopHomeActivity;
import cn.android.yhogp2.application.MainApplication;
import cn.android.yhogp2.javabean.Order;
import cn.android.yhogp2.uitils.OkHttpUtil;
import cn.android.yhogp2.uitils.RequestHandler;
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
public class UpdateRiderLocationService extends IntentService {
    private BDAbstractLocationListener mListener;
    private LocationService locationService;
    private Handler locationHandler;

    private static int updateTimes = 0;

    private final static int GET_NEW_ORDERS = 4;

    public UpdateRiderLocationService() {
        super("RequestNewService");
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        doUpdateRiderLocation();
    }

    private void doUpdateRiderLocation() {
        initHandler();
        initLocationService();
    }

    @SuppressLint("HandlerLeak")
    private void initHandler() {
        locationHandler = new Handler() {
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case OkHttpUtil.REQUEST_SUCCESS:
                        if (updateTimes == 0)
                            TextUtilTools.myToast(getApplicationContext(), "成功发送位置", 1);
                        updateTimes++;
                        OkHttpUtil.riderRequestNewOrder(MainApplication.loginRider.getRiderId(), new okhttp3.Callback() {
                            @Override
                            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                            }

                            @Override
                            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                                String responseStr = response.body().string();
                                if (!responseStr.equals("null")) {
                                    Message msg = locationHandler.obtainMessage();
                                    msg.what = GET_NEW_ORDERS;
                                    msg.obj=responseStr;
                                    locationHandler.sendMessage(msg);
                                }
                            }
                        });
                        break;
                    case OkHttpUtil.REQUEST_FAIL_NET:
                        TextUtilTools.myToast(getApplicationContext(), "无法连接服务器，发送位置失败", 1);
                        break;
                    case OkHttpUtil.REQUEST_FAIL_SERVER:
                        TextUtilTools.myToast(getApplicationContext(), "服务器异常，发送位置失败", 1);
                        break;
                    case GET_NEW_ORDERS:
                      List<Order> list=  TextUtilTools.fromToJson((String)msg.obj , new TypeToken<List<Order>>() {
                    }.getType());
                      TextUtilTools.myToast(getApplicationContext(),"接到可获得订单"+list.size()+"单",1);
                        break;
                }
            }
        };
    }

    private void initLocationService() {
        locationService = new LocationService(getApplicationContext());
        mListener = new BDAbstractLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation bdLocation) {
                if (null != bdLocation && bdLocation.getLocType() != BDLocation.TypeServerError) {
                    Message msg = locationHandler.obtainMessage();
                    if (bdLocation.getAddrStr() != null || !bdLocation.getAddrStr().equals("null") || !bdLocation.equals("")) {
                        OkHttpUtil.riderUpdateLocation(MainApplication.loginRider.getRiderId(), bdLocation.getLatitude(), bdLocation.getLongitude(), new Callback() {
                            @Override
                            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                                msg.what = OkHttpUtil.REQUEST_FAIL_NET;
                                locationHandler.sendMessage(msg);
                            }

                            @Override
                            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                                if (response.body().string().equals("true"))
                                    msg.what = OkHttpUtil.REQUEST_SUCCESS;
                                else
                                    msg.what = OkHttpUtil.REQUEST_FAIL_SERVER;
                                locationHandler.sendMessage(msg);
                            }
                        });
                    }
                }
            }
        };
        locationService.registerListener(mListener);
        locationService.setLocationOption(locationService.getDefaultLocationClientOption());
    }
}
