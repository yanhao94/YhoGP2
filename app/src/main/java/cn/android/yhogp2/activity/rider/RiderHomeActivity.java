package cn.android.yhogp2.activity.rider;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.android.yhogp2.R;
import cn.android.yhogp2.activity.MainActivity;
import cn.android.yhogp2.application.MainApplication;
import cn.android.yhogp2.intentservice.UpdateRiderLocationService;
import cn.android.yhogp2.javabean.Order;
import cn.android.yhogp2.javabean.OrdersGroup;
import cn.android.yhogp2.javabean.RiderNewOrders;
import cn.android.yhogp2.uitils.OkHttpUtil;
import cn.android.yhogp2.uitils.TextUtilTools;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class RiderHomeActivity extends AppCompatActivity {


    @BindView(R.id.tv_riderLoginOut)
    TextView tvRiderLoginOut;
    @BindView(R.id.tv_getOrderDefault)
    TextView tvGetOrderDefault;
    @BindView(R.id.tv_getOrderEx)
    TextView tvGetOrderEx;
    @BindView(R.id.tv_reGetOrders)
    TextView tvReGetOrders;
    @BindView(R.id.rcv_newRiderOrder)
    RecyclerView rcvNewRiderOrder;
    @BindView(R.id.btn_ridersHistory)
    Button btnRidersHistory;
    @BindView(R.id.tv_routePlan)
    TextView tvRoutePlan;

    private List<OrdersGroup> listOrdersDefault;
    private List<Order> listOrdersEx;
    public static Handler handler;
    private NewOrderRcvAdapter adapter;
    private int adapterType = 0;
    private String permissionInfo;

    public static final int GET_ORDER_SUCCESS = 4;
    public static final int GET_ORDER_FAIL_BY_UNFINISHED = 5;
    public static final int GET_ORDER_FAIL_BY_ORDER_STATE_HAVE_CHANGE = 6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rider_home);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        getPersimmions();
        initSendLocationService();
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        initHandler();
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) && !locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            TextUtilTools.myToast(getApplicationContext(), "请打开定位，否则骑手无法上传当前位置信息", 0);
        }
        getListResource();
        initRcv();
    }

    private void initSendLocationService() {
        this.startService(new Intent(this, UpdateRiderLocationService.class));
    }

    @SuppressLint("HandlerLeak")
    private void initHandler() {
        handler = new Handler() {
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case OkHttpUtil.REQUEST_FAIL_NET:
                        TextUtilTools.myToast(getApplicationContext(), "无法连接服务器，获取列表失败", 1);
                        break;
                    case OkHttpUtil.REQUEST_FAIL_SERVER:
                        TextUtilTools.myToast(getApplicationContext(), "服务器故障，获取列表失败", 1);
                        break;
                    case OkHttpUtil.REQUEST_SUCCESS:
                        TextUtilTools.myToast(getApplicationContext(), "获取列表成功", 1);
                        //set adapter
                        RiderNewOrders rno = new Gson().fromJson((String) msg.obj, RiderNewOrders.class);
                        Log.i("cesss", (String) msg.obj + !rno.getListOrdersGroupJson().equals("null"));
                        if (!rno.getListOrdersGroupJson().equals("null"))
                            listOrdersDefault = TextUtilTools.fromToJson(rno.getListOrdersGroupJson(), new TypeToken<List<OrdersGroup>>() {
                            }.getType());
                        if (!rno.getListOrderExJson().equals("null"))
                            listOrdersEx = TextUtilTools.fromToJson(rno.getListOrderExJson(), new TypeToken<List<Order>>() {
                            }.getType());
                        if (listOrdersDefault != null && adapterType == 0) {
                            rcvNewRiderOrder.setAdapter(new NewOrderRcvAdapter(listOrdersDefault));
                            tvGetOrderDefault.setTextColor(Color.RED);
                            tvGetOrderEx.setTextColor(Color.BLUE);
                        } else {
                            tvGetOrderDefault.setTextColor(Color.BLUE);
                            tvGetOrderEx.setTextColor(Color.RED);
                            rcvNewRiderOrder.setAdapter(new NewOrderRcvAdapter(listOrdersEx, 1));
                        }
                        break;
                    case GET_ORDER_SUCCESS:
                        if (adapterType == 0) {
                            listOrdersDefault.remove(msg.arg1);
                            rcvNewRiderOrder.setAdapter(new NewOrderRcvAdapter(listOrdersDefault));
                        } else {
                            listOrdersEx.remove(msg.arg1);
                            rcvNewRiderOrder.setAdapter(new NewOrderRcvAdapter(listOrdersEx, 1));
                        }
                        break;
                    case GET_ORDER_FAIL_BY_UNFINISHED:
                        TextUtilTools.myToast(getApplicationContext(), "还有未完成的订单，请先完成再来", 0);
                        break;
                    case GET_ORDER_FAIL_BY_ORDER_STATE_HAVE_CHANGE:
                        TextUtilTools.myToast(getApplicationContext(), "该订单状态已改变，点击刷新重新获取订单列表", 0);
                        break;
                }
            }
        };
    }

    private void getListResource() {
        OkHttpUtil.riderRequestOrdersList(MainApplication.loginRider.getRiderId(), OkHttpUtil.TYPE_ORDER_NEW, new Callback() {
            Message msg = handler.obtainMessage();

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                msg.what = OkHttpUtil.REQUEST_FAIL_NET;
                handler.sendMessage(msg);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String responseStr = response.body().string();
                if (!responseStr.equals("null") && responseStr.charAt(0) != '<') {
                    msg.what = OkHttpUtil.REQUEST_SUCCESS;
                    msg.obj = responseStr;
                } else {
                    msg.what = OkHttpUtil.REQUEST_FAIL_SERVER;
                }
                handler.sendMessage(msg);
            }
        });
    }

    private void initRcv() {
        if (listOrdersDefault != null)
            adapter = new NewOrderRcvAdapter(listOrdersDefault);
        rcvNewRiderOrder.setAdapter(adapter);
        rcvNewRiderOrder.setLayoutManager(new LinearLayoutManager(this));
    }

    @OnClick({R.id.tv_riderLoginOut, R.id.tv_getOrderDefault, R.id.tv_getOrderEx, R.id.tv_reGetOrders, R.id.btn_ridersHistory, R.id.tv_routePlan})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_riderLoginOut:
                MainActivity.showLoginOutDialog(this, this);
                break;
            case R.id.tv_getOrderDefault:
                if (adapterType == adapter.TYPE_EX)
                    rcvNewRiderOrder.setAdapter(new NewOrderRcvAdapter(listOrdersDefault == null ? new ArrayList<>() : listOrdersDefault));
                adapterType = 0;
                tvGetOrderDefault.setTextColor(Color.RED);
                tvGetOrderEx.setTextColor(Color.BLUE);
                if (listOrdersDefault == null)
                    TextUtilTools.myToast(getApplicationContext(), "当前无拼单可接", 0);
                break;
            case R.id.tv_getOrderEx:
                if (adapterType == adapter.TYPE_GROUP)
                    rcvNewRiderOrder.setAdapter(new NewOrderRcvAdapter(listOrdersEx == null ? new ArrayList<>() : listOrdersEx, 1));
                adapterType = 1;
                tvGetOrderDefault.setTextColor(Color.BLUE);
                tvGetOrderEx.setTextColor(Color.RED);
                if (listOrdersEx == null)
                    TextUtilTools.myToast(getApplicationContext(), "当前无专属单可接", 0);
                break;
            case R.id.tv_reGetOrders:
                getListResource();
                break;
            case R.id.btn_ridersHistory:
                startActivity(new Intent(this, RiderHistoryActivity.class));
                break;
            case R.id.tv_routePlan:
                startActivity(new Intent(this, RoutePlanningActivity.class));
                break;
        }
    }

    @TargetApi(23)
    private void getPersimmions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ArrayList<String> permissions = new ArrayList<String>();
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
            }
            if (checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);
            }
            if (addPermission(permissions, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                permissionInfo += "Manifest.permission.WRITE_EXTERNAL_STORAGE Deny \n";
            }
            if (permissions.size() > 0) {
                requestPermissions(permissions.toArray(new String[permissions.size()]), 127);
            }
        }
    }

    @TargetApi(23)
    private boolean addPermission(ArrayList<String> permissionsList, String permission) {
        if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
            if (shouldShowRequestPermissionRationale(permission)) {
                return true;
            } else {
                permissionsList.add(permission);
                return false;
            }
        } else {
            return true;
        }
    }

    @TargetApi(23)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        // TODO Auto-generated method stub
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
