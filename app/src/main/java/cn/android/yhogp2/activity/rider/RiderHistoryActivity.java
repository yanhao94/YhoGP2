package cn.android.yhogp2.activity.rider;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

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
import cn.android.yhogp2.R;
import cn.android.yhogp2.activity.shop.OrderPageRcvAdapter;
import cn.android.yhogp2.application.MainApplication;
import cn.android.yhogp2.javabean.Order;
import cn.android.yhogp2.uitils.OkHttpUtil;
import cn.android.yhogp2.uitils.TextUtilTools;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class RiderHistoryActivity extends AppCompatActivity {

    @BindView(R.id.rcv_riderHistoryOrder)
    RecyclerView rcvRiderHistoryOrder;
    private static final int ORDER_RIDER_CHANGE_ORDER_STATE = 4;
    public static Handler handler;
    public static List<Order> ordersList;
    public static HistoryOrderRcvAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rider_history);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        ordersList = new ArrayList<>();
        initHandler();
        initRcv();
    }

    private void initRcv() {
        OkHttpUtil.riderRequestOrdersList(MainApplication.loginRider.getRiderId(), OkHttpUtil.TYPE_ORDER_HISTORY, new Callback() {
            Message msg = handler.obtainMessage();

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                msg.what = OkHttpUtil.REQUEST_FAIL_NET;
                handler.sendMessage(msg);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String responseStr = response.body().string();
              if(!responseStr.equals("false")&&responseStr.charAt(0)!='<')
              { msg.what = OkHttpUtil.REQUEST_SUCCESS;
                msg.obj = responseStr;
                handler.sendMessage(msg);}
            }
        });
        rcvRiderHistoryOrder.setLayoutManager(new LinearLayoutManager(this));
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
                            ordersList = TextUtilTools.fromToJson(responseStr, new TypeToken<List<Order>>() {
                            }.getType());
                            // adapter.notifyDataSetChanged();
                            adapter = new HistoryOrderRcvAdapter(ordersList);
                            rcvRiderHistoryOrder.setAdapter(adapter);

                        } else {
                            TextUtilTools.myToast(getApplicationContext(), "宁当前暂时没有订单", 1);
                        }
                        break;
                    case OkHttpUtil.REQUEST_FAIL_NET:
                        TextUtilTools.myToast(getApplicationContext(), "无法连接服务器", 1);
                        break;
                    case OkHttpUtil.REQUEST_FAIL_SERVER:
                        TextUtilTools.myToast(getApplicationContext(), "服务器故障稍后再试", 1);
                        break;
                    case OkHttpUtil.ORDER_RIDER_CHANGE_ORDER_STATE:
                        TextUtilTools.myToast(getApplicationContext(), "订单状态更新成功", 0);
                        initRcv();
                        break;
                }
            }
        };
    }
}
