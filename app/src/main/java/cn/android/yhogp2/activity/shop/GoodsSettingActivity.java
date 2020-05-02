package cn.android.yhogp2.activity.shop;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Text;

import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.android.yhogp2.R;
import cn.android.yhogp2.application.MainApplication;
import cn.android.yhogp2.javabean.Goods;
import cn.android.yhogp2.uitils.OkHttpUtil;
import cn.android.yhogp2.uitils.RequestHandler;
import cn.android.yhogp2.uitils.TextUtilTools;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class GoodsSettingActivity extends AppCompatActivity {
    @BindView(R.id.tiet_settingName)
    TextInputEditText tietSettingName;
    @BindView(R.id.tiet_settingIntroduction)
    TextInputEditText tietSettingIntroduction;
    @BindView(R.id.tiet_settingPrice)
    TextInputEditText tietSettingPrice;
    @BindView(R.id.cb_Setting1)
    CheckBox cbSetting1;
    @BindView(R.id.cb_Setting2)
    CheckBox cbSetting2;
    @BindView(R.id.cb_Setting3)
    CheckBox cbSetting3;
    @BindView(R.id.cb_Setting4)
    CheckBox cbSetting4;
    @BindView(R.id.cb_Setting5)
    CheckBox cbSetting5;
    @BindView(R.id.cb_Setting6)
    CheckBox cbSetting6;
    @BindView(R.id.cb_Setting7)
    CheckBox cbSetting7;
    @BindView(R.id.cb_Setting8)
    CheckBox cbSetting8;
    @BindView(R.id.cb_Setting9)
    CheckBox cbSetting9;
    @BindView(R.id.cb_Setting10)
    CheckBox cbSetting10;
    @BindView(R.id.cb_Setting11)
    CheckBox cbSetting11;
    @BindView(R.id.cb_Setting12)
    CheckBox cbSetting12;
    @BindView(R.id.tv_settingSalesAll)
    TextView tvSettingSalesAll;
    @BindView(R.id.tv_settingSalesMonth)
    TextView tvSettingSalesMonth;
    @BindView(R.id.tv_settingPrTimes)
    TextView tvSettingPrTimes;
    @BindView(R.id.tv_settingNrTimes)
    TextView tvSettingNrTimes;
    @BindView(R.id.btn_goodsEdit)
    Button btnGoodsEdit;
    @BindView(R.id.btn_goodsSettingSummit)
    Button btnGoodsSettingSummit;

    @BindViews({R.id.tiet_settingName, R.id.tiet_settingIntroduction, R.id.tiet_settingPrice,
            R.id.cb_Setting12, R.id.cb_Setting11, R.id.cb_Setting10, R.id.cb_Setting9,
            R.id.cb_Setting8, R.id.cb_Setting7, R.id.cb_Setting6, R.id.cb_Setting5,
            R.id.cb_Setting4, R.id.cb_Setting3, R.id.cb_Setting2, R.id.cb_Setting1})
    List<View> viewsGroup;
    @BindViews({R.id.cb_Setting1, R.id.cb_Setting2, R.id.cb_Setting3, R.id.cb_Setting4,
            R.id.cb_Setting5, R.id.cb_Setting6, R.id.cb_Setting7, R.id.cb_Setting8,
            R.id.cb_Setting9, R.id.cb_Setting10, R.id.cb_Setting11, R.id.cb_Setting12})
    List<CheckBox> checkGroup;

    private Goods mGoods;
    private int type;
    private Handler handler;
    private final int TYPE_ADD = 0;
    private final int TYPE_CHANGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_setting);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        mGoods = (Goods) getIntent().getSerializableExtra("goods");
        if (mGoods != null) {
            this.type = TYPE_CHANGE;
            initChangeContent();
        } else {
            this.type = TYPE_ADD;
            mGoods = new Goods(MainApplication.loginShop.getShopId());
        }
        initHandler();
    }

    private void initHandler() {
        handler = new RequestHandler() {
            @Override
            public void doRequestSuccess(Message msg) {
                TextUtilTools.myToast(getApplicationContext(), "请求成功", 1);
                Message msg1 = ShopHomeActivity.mHandler.obtainMessage();
                msg1.obj = "have change ";
                msg1.what = OkHttpUtil.REQUEST_SUCCESS;
                ShopHomeActivity.mHandler.sendMessage(msg1);
                finish();
            }

            @Override
            public void setTYPE() {
                REQUEST_TYPE = REQUSET;
            }
        }.getRequestHandler(this);
    }

    private void initChangeContent() {
        tietSettingName.setText(mGoods.getName());
        tietSettingIntroduction.setText(mGoods.getIntroduction());
        tietSettingPrice.setText(String.valueOf(mGoods.getPrice()));
        tvSettingSalesAll.setText("总售：" + mGoods.getSalesAll());
        tvSettingSalesMonth.setText("月售：" + mGoods.getSalesMonth());
        tvSettingPrTimes.setText("好评次数：" + mGoods.getPrTimes());
        tvSettingNrTimes.setText("差评次数：" + mGoods.getNrTimes());
    }

    @OnClick({R.id.btn_goodsEdit, R.id.btn_goodsSettingSummit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_goodsEdit:
                if (tietSettingPrice.isEnabled()) {
                    for (int i = 0; i < viewsGroup.size(); i++) {
                        viewsGroup.get(i).setEnabled(false);
                    }
                } else {
                    for (int i = 0; i < viewsGroup.size(); i++) {
                        viewsGroup.get(i).setEnabled(true);
                    }
                }
                break;
            case R.id.btn_goodsSettingSummit:
                mGoods.setIntroduction(tietSettingIntroduction.getText().toString());
                mGoods.setName(tietSettingName.getText().toString());
                mGoods.setPrice(Double.parseDouble(tietSettingPrice.getText().toString()));
                mGoods.setType(getType());
                if (type == TYPE_CHANGE) {
                    changeGoods();
                } else {
                    addGoods();
                }
                break;
        }
    }

    private int getType() {
        StringBuilder sb = new StringBuilder(15);
        for (CheckBox checkBox : checkGroup) {
            sb.append(checkBox.isChecked() ? 1 : 0);
        }
        return Integer.parseInt(sb.toString());
    }

    private void addGoods() {
        OkHttpUtil.addGoods(new Gson().toJson(mGoods), new Callback() {
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
                else
                    msg.what = OkHttpUtil.REQUEST_FAIL_SERVER;
                handler.sendMessage(msg);
            }
        });
    }

    private void changeGoods() {
        OkHttpUtil.changeGoods(new Gson().toJson(mGoods), new Callback() {
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
                else
                    msg.what = OkHttpUtil.REQUEST_FAIL_SERVER;
                handler.sendMessage(msg);
            }
        });
    }
}

