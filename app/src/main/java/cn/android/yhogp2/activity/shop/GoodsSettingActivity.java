package cn.android.yhogp2.activity.shop;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.android.yhogp2.R;
import cn.android.yhogp2.javabean.Goods;

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

    private Goods mGoods;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_setting);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        mGoods = (Goods) getIntent().getSerializableExtra("goods");
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

                break;
        }
    }
}

