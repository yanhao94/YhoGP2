package cn.android.yhogp2.activity.shop;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

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
import cn.android.yhogp2.application.MainApplication;
import cn.android.yhogp2.javabean.Goods;
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
                Log.i("ttaaa", responseStr);
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
                break;
            case R.id.tv_shopGoodsSelectAll:
                break;
            case R.id.tv_shopGoodsSelectCancel:
                break;
            case R.id.iv_addGood:
                break;
            case R.id.iv_subtractGood:
                break;
            case R.id.btn_shopNewOrders:
                break;
            case R.id.btn_shopOrdersHistory:
                break;
        }
    }
}
