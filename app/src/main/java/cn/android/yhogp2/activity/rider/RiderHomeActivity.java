package cn.android.yhogp2.activity.rider;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.android.yhogp2.R;
import cn.android.yhogp2.application.MainApplication;
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

    private List<OrdersGroup> listOrdersDefault;
    private List<Order> listOrdersEx;
    private Handler handler;
    private NewOrderRcvAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rider_home);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        initHandler();
        getListResource();
        initRcv();
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
                        listOrdersDefault = TextUtilTools.fromToJson(rno.getListOrdersGroupJson(), new TypeToken<List<OrdersGroup>>() {
                        }.getType());
                        listOrdersEx = TextUtilTools.fromToJson(rno.getListOrderExJson(), new TypeToken<List<Order>>() {
                        }.getType());
                        if(listOrdersDefault!=null)
                            rcvNewRiderOrder.setAdapter(new NewOrderRcvAdapter(listOrdersDefault));
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
                if (!responseStr.equals("null")) {
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
        if(listOrdersDefault!=null)
        adapter=new NewOrderRcvAdapter(listOrdersDefault);
        rcvNewRiderOrder.setAdapter(adapter);
        rcvNewRiderOrder.setLayoutManager(new LinearLayoutManager(this));
    }

    @OnClick({R.id.tv_riderLoginOut, R.id.tv_getOrderDefault, R.id.tv_getOrderEx, R.id.tv_reGetOrders, R.id.btn_ridersHistory})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_riderLoginOut:
                break;
            case R.id.tv_getOrderDefault:
                if(adapter.type==adapter.TYPE_EX)
                    rcvNewRiderOrder.setAdapter(new NewOrderRcvAdapter(listOrdersDefault));
                break;
            case R.id.tv_getOrderEx:
                if(adapter.type==adapter.TYPE_GROUP)
                    rcvNewRiderOrder.setAdapter(new NewOrderRcvAdapter(listOrdersEx,1));
                break;
            case R.id.tv_reGetOrders:
                getListResource();
                break;
            case R.id.btn_ridersHistory:

                break;
        }
    }
}
