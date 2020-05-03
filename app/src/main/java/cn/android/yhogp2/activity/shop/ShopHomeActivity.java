package cn.android.yhogp2.activity.shop;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.reflect.TypeToken;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.android.yhogp2.R;
import cn.android.yhogp2.activity.MainActivity;
import cn.android.yhogp2.application.MainApplication;
import cn.android.yhogp2.intentservice.RequestNewService;
import cn.android.yhogp2.javabean.Goods;
import cn.android.yhogp2.javabean.Order;
import cn.android.yhogp2.javabean.Shop;
import cn.android.yhogp2.uitils.OkHttpUtil;
import cn.android.yhogp2.uitils.RequestHandler;
import cn.android.yhogp2.uitils.TextUtilTools;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class ShopHomeActivity extends AppCompatActivity {
    @BindView(R.id.tv_shopName)
    TextView tvShopName;
    @BindView(R.id.iv_shopSetting)
    ImageView ivShopSetting;
    @BindView(R.id.tv_shopOrdersAll)
    TextView tvShopOrdersAll;
    @BindView(R.id.tv_shopType)
    TextView tvShopType;
    @BindView(R.id.tv_shopIntroduction)
    TextView tvShopIntroduction;
    @BindView(R.id.tv_shopGoodsSelectAll)
    TextView tvShopGoodsSelectAll;
    @BindView(R.id.tv_shopGoodsSelectCancel)
    TextView tvShopGoodsSelectCancel;
    @BindView(R.id.iv_addGood)
    ImageView ivAddGood;
    @BindView(R.id.iv_subtractGood)
    ImageView ivSubtractGood;
    @BindView(R.id.rcv_shopGoods)
    RecyclerView rcvShopGoods;
    @BindView(R.id.btn_shopNewOrders)
    Button btnShopNewOrders;
    @BindView(R.id.btn_shopOrdersHistory)
    Button btnShopOrdersHistory;
    private Shop mShop;
    private List<Goods> goodsList;
    public static Handler mHandler;
    public static Handler orderHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_home);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        initShopInformation();
        getRequestHandler();
        requestGoods();
        initOrderHandler();
        this.startService(new Intent(this, RequestNewService.class));
    }

    private void initOrderHandler() {
        orderHandler = new Handler() {
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case -1:
                        TextUtilTools.myToast(getApplicationContext(), "网络异常获取不到新订单", 1);
                        break;
                    case 1:
                        List<Order> ordersList = (List<Order>) msg.obj;
                        TextUtilTools.myToast(getApplicationContext(), "接到" + ordersList.size() + "个新订单啦", 1);
                        break;
                }
            }
        };
    }

    private void initRcvAdapter() {
        rcvShopGoods.setLayoutManager(new LinearLayoutManager(this));
        Log.i("ssssa", "" + goodsList.get(0).getName());
        rcvShopGoods.setAdapter(new GoodsRcvAdapter(goodsList, this));
    }

    private void getRequestHandler() {
        mHandler = new RequestHandler() {
            @Override
            public void doRequestSuccess(Message msg) {
                String responseStr = String.valueOf(msg.obj);
                if (responseStr.equals("还未添加商品")) {
                } else {
                    upDataGoodsList(responseStr);
                    initRcvAdapter();
                }
            }

            @Override
            public void setTYPE() {
                REQUEST_TYPE = RequestHandler.REQUEST_LIST;
            }
        }.getRequestHandler(this);
    }

    private void upDataGoodsList(String json) {
        goodsList = TextUtilTools.fromToJson(json, new TypeToken<List<Goods>>() {
        }.getType());
        //rcv adapter
    }

    private void requestGoods() {

        OkHttpUtil.requestShopGoodsList(mShop.getShopId(), new Callback() {
            Message msg = mHandler.obtainMessage();

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                msg.what = OkHttpUtil.REQUEST_FAIL_NET;
                mHandler.sendMessage(msg);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String responseStr = response.body().string();
                if (!responseStr.equals("") && !responseStr.equals("null") && responseStr != null) {
                    msg.what = OkHttpUtil.REQUEST_SUCCESS;
                    msg.obj = responseStr;
                } else {
                    msg.obj = "还未添加商品";
                    msg.what = OkHttpUtil.REQUEST_FAIL_SERVER;
                }
                mHandler.sendMessage(msg);
            }
        });

    }

    private void initShopInformation() {
        mShop = MainApplication.loginShop;
        tvShopName.setText(mShop.getShopName());
        tvShopIntroduction.setText("介绍：" + mShop.getIntroduction());
        tvShopOrdersAll.setText("已完成订单：" + mShop.getFinishedOrders());
        tvShopType.setText("类型：" + mShop.getType());
    }


    @OnClick({R.id.iv_shopSetting, R.id.tv_shopGoodsSelectAll, R.id.tv_shopGoodsSelectCancel, R.id.iv_addGood, R.id.iv_subtractGood, R.id.btn_shopNewOrders, R.id.btn_shopOrdersHistory})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_shopSetting:
                showLoginOutDialog();
                break;
            case R.id.tv_shopGoodsSelectAll:
                break;
            case R.id.tv_shopGoodsSelectCancel:
                break;
            case R.id.iv_addGood:
                startActivity(new Intent(this, GoodsSettingActivity.class));
                break;
            case R.id.iv_subtractGood:
                break;
            case R.id.btn_shopNewOrders:
                startActivity(new Intent(this, NewOrderActivity.class));
                break;
            case R.id.btn_shopOrdersHistory:
                startActivity(new Intent(this, HistoryOrderActivity.class));
                break;
        }
    }

    private void showLoginOutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("要登出不");
        builder.setPositiveButton("是", (dialogInterface, i) -> {
            MainActivity.loginOut(this);
        });
        builder.setNegativeButton("不", (dialogInterface, i) -> {
        });
        builder.show();
    }
}
