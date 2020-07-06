package cn.android.yhogp2.activity.shop;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.android.yhogp2.R;
import cn.android.yhogp2.application.MainApplication;
import cn.android.yhogp2.javabean.Shop;
import cn.android.yhogp2.uitils.OkHttpUtil;
import cn.android.yhogp2.uitils.RequestHandler;
import cn.android.yhogp2.uitils.TextUtilTools;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class EditShopActivity extends AppCompatActivity {

    @BindView(R.id.tiet_changeName)
    TextInputEditText tietChangeName;
    @BindView(R.id.tiet_changeTel)
    TextInputEditText tietChangeTel;
    @BindView(R.id.tiet_changeAddr)
    TextInputEditText tietChangeAddr;
    @BindView(R.id.tiet_registerDeliveryDistance)
    TextInputEditText tietRegisterDeliveryDistance;
    @BindView(R.id.tiet_changeType)
    TextInputEditText tietChangeType;
    @BindView(R.id.tiet_changeIntroduction)
    TextInputEditText tietChangeIntroduction;
    @BindView(R.id.btnChangeShopSummit)
    Button btnChangeShopSummit;
    @BindView(R.id.tiet_changeMinPay)
    TextInputEditText tietChangeMinPay;
    private Shop shop = MainApplication.loginShop;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_shop);
        ButterKnife.bind(this);
        initHandler();
        initOrdinaryInformation();
    }

    private void initHandler() {
        handler = new RequestHandler() {
            @Override
            public void doRequestSuccess(Message msg) {
                TextUtilTools.myToast(getApplicationContext(), "修改成功", 0);
                Message msg2 = ShopHomeActivity.orderHandler.obtainMessage();
                msg2.what = ShopHomeActivity.CHANGE_SHOP_SUCCESS;
                ShopHomeActivity.orderHandler.sendMessage(msg2);
                finish();
            }

            @Override
            public void setTYPE() {
                REQUEST_TYPE = REQUSET;
            }
        }.getRequestHandler(this);
    }

    private void initOrdinaryInformation() {
        tietChangeName.setText(shop.getShopName());
        tietChangeTel.setText(shop.getTel());
        tietChangeAddr.setText(shop.getAddr());
        tietRegisterDeliveryDistance.setText(String.valueOf(shop.getDeliveryDistance()));
        tietChangeType.setText(shop.getType());
        tietChangeIntroduction.setText(shop.getIntroduction());
        tietChangeMinPay.setText(String.valueOf(shop.getMinPay()));
    }

    @OnClick(R.id.btnChangeShopSummit)
    public void onViewClicked() {
        if (isRightInput()) {
            OkHttpUtil.changeShop(getAChangeShopJson(), new Callback() {
                Message msg = handler.obtainMessage();

                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    msg.what = OkHttpUtil.REQUEST_FAIL_NET;
                    handler.sendMessage(msg);
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    if (response.body().string().equals("true"))
                        msg.what = OkHttpUtil.REQUEST_SUCCESS;
                    else {
                        msg.what = OkHttpUtil.REQUEST_FAIL_SERVER;
                        msg.obj="编辑失败稍后重试";
                    }
                    handler.sendMessage(msg);
                }
            });
        }
    }

    private String getAChangeShopJson() {
        shop.setShopName(tietChangeName.getText().toString());
        shop.setTel(tietChangeTel.getText().toString());
        shop.setAddr(tietChangeAddr.getText().toString());
        shop.setDeliveryDistance(Double.parseDouble(tietRegisterDeliveryDistance.getText().toString()));
        shop.setType(tietChangeType.getText().toString());
        shop.setIntroduction(tietChangeIntroduction.getText().toString());
        shop.setMinPay(Double.parseDouble(tietChangeMinPay.getText().toString()));
        return new Gson().toJson(shop);
    }

    private boolean isRightInput() {
        if (!TextUtilTools.isNotEmpty(tietChangeName)) {
            tietChangeName.setError("此处不能为空");
            return false;
        }
        if (!TextUtilTools.isNotEmpty(tietChangeTel)) {
            tietChangeTel.setError("此处不能为空");
            return false;
        }
        if (!TextUtilTools.isNotEmpty(tietChangeAddr)) {
            tietChangeAddr.setError("此处不能为空");
            return false;
        }
        if (!TextUtilTools.isNotEmpty(tietRegisterDeliveryDistance)) {
            tietRegisterDeliveryDistance.setError("此处不能为空");
            return false;
        }
        if (!TextUtilTools.isNotEmpty(tietChangeType)) {
            tietChangeType.setError("此处不能为空");
            return false;
        }
        if (!TextUtilTools.isNotEmpty(tietChangeIntroduction)) {
            tietChangeIntroduction.setError("此处不能为空");
            return false;
        }
        if (!TextUtilTools.isNotEmpty(tietChangeMinPay)) {
            tietChangeMinPay.setError("此处不能为空");
            return false;
        }
        return true;
    }
}
