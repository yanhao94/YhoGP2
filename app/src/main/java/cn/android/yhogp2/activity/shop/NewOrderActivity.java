package cn.android.yhogp2.activity.shop;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.reflect.TypeToken;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.android.yhogp2.R;
import cn.android.yhogp2.application.MainApplication;
import cn.android.yhogp2.javabean.Order;
import cn.android.yhogp2.uitils.OkHttpUtil;
import cn.android.yhogp2.uitils.TextUtilTools;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class NewOrderActivity extends AppCompatActivity {

    @BindView(R.id.rcv_newOrder)
    RecyclerView rcvNewOrder;
    @BindView(R.id.btn_getAll)
    Button btnGetAll;
    @BindView(R.id.btn_autoGet)
    Button btnAutoGet;
    @BindView(R.id.btn_rejectAll)
    Button btnRejectAll;

    public static List<Order> ordersList;
    public static Handler handler;
    public static OrderPageRcvAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_order);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        ordersList = new ArrayList<>();
        initHandler();
        initRcv();
    }

    @SuppressLint("HandlerLeak")
    private void initHandler() {
        handler = new Handler() {
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case OkHttpUtil.REQUEST_SUCCESS:
                        String responseStr = (String) msg.obj;
                        if (!responseStr.equals("null")) {
                            if (responseStr.equals("changeState")) {
                                adapter.notifyDataSetChanged();
                            } else {
                                ordersList = TextUtilTools.fromToJson(responseStr, new TypeToken<List<Order>>() {
                                }.getType());
                                adapter = new OrderPageRcvAdapter(ordersList, OrderPageRcvAdapter.TYPE_NEW);
                                rcvNewOrder.setAdapter(adapter);
                                //adapter.notifyDataSetChanged();
                            }
                        } else {
                            TextUtilTools.myToast(getApplicationContext(), "您当前暂时没有订单", 1);
                        }
                        break;
                    case OkHttpUtil.REQUEST_FAIL_NET:
                        TextUtilTools.myToast(getApplicationContext(), "无法连接服务器", 1);
                        break;
                    case OkHttpUtil.REQUEST_FAIL_SERVER:
                        TextUtilTools.myToast(getApplicationContext(), "服务器故障稍后再试", 1);
                        break;
                }
            }
        };
    }

    private void initRcv() {
        OkHttpUtil.shopRequestOrdersList(MainApplication.loginShop.getShopId(), OkHttpUtil.TYPE_ORDER_NEW, new Callback() {
            Message msg = handler.obtainMessage();

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                msg.what = OkHttpUtil.REQUEST_FAIL_NET;
                handler.sendMessage(msg);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String responseStr = response.body().string();
                if(responseStr!=null&&responseStr.charAt(0)!='<')
                msg.what = OkHttpUtil.REQUEST_SUCCESS;
                msg.obj = responseStr;
                ordersList = TextUtilTools.fromToJson(responseStr, new TypeToken<List<Order>>() {
                }.getType());
                handler.sendMessage(msg);
            }
        });
        adapter = new OrderPageRcvAdapter(ordersList, OrderPageRcvAdapter.TYPE_NEW);
        rcvNewOrder.setAdapter(adapter);
        rcvNewOrder.setLayoutManager(new LinearLayoutManager(this));
    }

    @OnClick({R.id.btn_getAll, R.id.btn_autoGet, R.id.btn_rejectAll})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_getAll:
                getAllOrders();
                break;
            case R.id.btn_autoGet:
                break;
            case R.id.btn_rejectAll:
                rejectAllOrders();
                break;
        }
    }

    private void rejectAllOrders() {
        for (int i = 0; i < adapter.list_resource.size(); i++) {
            changeOrderState(i, adapter.list_resource.get(i).getOrderId(), Order.SHOP_REJECT_ORDERS);
        }
    }

    private void getAllOrders() {
        for (int i = 0; i < adapter.list_resource.size(); i++) {
            changeOrderState(i, adapter.list_resource.get(i).getOrderId(), Order.SHOP_GET_ORDERS);
        }
    }


    private void changeOrderState(int position, int orderId, double state) {
        OkHttpUtil.changeOrderState(orderId, state, new Callback() {
            Message msg = NewOrderActivity.handler.obtainMessage();

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                msg.what = OkHttpUtil.REQUEST_FAIL_NET;
                msg.obj = "changeState";
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.body().string().equals("true")) {
                    msg.what = OkHttpUtil.REQUEST_SUCCESS;
                    adapter.list_resource.remove(position);
                } else {
                    msg.what = OkHttpUtil.REQUEST_FAIL_NET;
                }
                msg.obj = "changeState";
            }
        });
    }
}
