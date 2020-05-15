package cn.android.yhogp2.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClientOption;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import cn.android.yhogp2.BdLocationService.LocationService;
import cn.android.yhogp2.R;
import cn.android.yhogp2.javabean.Shop;
import cn.android.yhogp2.uitils.RequestHandler;
import cn.android.yhogp2.uitils.LaunchDialog;
import cn.android.yhogp2.uitils.OkHttpUtil;
import cn.android.yhogp2.uitils.TextUtilTools;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    private TextInputEditText tiet_registerName;
    private TextInputEditText tiet_registerAccount;
    private TextInputEditText tiet_registerPassword;
    private TextInputEditText tiet_registerSurePassword;
    private TextInputEditText tiet_registerAddr;
    private TextInputEditText tiet_registerDeliveryDistance;
    private TextInputEditText tiet_registerTel;
    private TextView registerLocation;
    private RadioButton rb_shop;
    private RadioButton rb_rider;
    private Handler handler;
    private LaunchDialog launchDialog;
    private Handler locationHandler;

    private Shop registerShop;
    private BDAbstractLocationListener mListener;
    private LocationService locationService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        init();
    }

    private void init() {
        registerShop = new Shop();
        findViewById(R.id.btn_registerSubmit).setOnClickListener(this);
        findViewById(R.id.registerGetLocation).setOnClickListener(this);
        tiet_registerName = findViewById(R.id.tiet_registerName);
        tiet_registerAccount = findViewById(R.id.tiet_registerAccount);
        tiet_registerPassword = findViewById(R.id.tiet_registerPassword);
        tiet_registerSurePassword = findViewById(R.id.tiet_registerSurePassword);
        tiet_registerAddr = findViewById(R.id.tiet_registerAddr);
        tiet_registerDeliveryDistance = findViewById(R.id.tiet_registerDeliveryDistance);
        tiet_registerTel = findViewById(R.id.tiet_registerTel);
        registerLocation = findViewById(R.id.registerLocation);
        rb_shop = findViewById(R.id.rb_Shop);
        rb_rider = findViewById(R.id.rb_rider);
        rb_shop.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                tiet_registerAddr.setEnabled(true);
                tiet_registerDeliveryDistance.setEnabled(true);
            } else {
                tiet_registerAddr.setEnabled(false);
                tiet_registerDeliveryDistance.setEnabled(false);
            }
        });
        initLocationService();
        handler = new RequestHandler() {
            @Override
            public void doRequestSuccess(Message msg) {
                TextUtilTools.myToast(getApplicationContext(), "registerSuccess", 0);
                finish();
            }

            @Override
            public void setTYPE() {
                REQUEST_TYPE = RequestHandler.REGISTER;
            }
        }.getRequestHandler(this);
        locationHandler = new Handler() {
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                TextUtilTools.myToast(getApplicationContext(), "获取位置成功", 0);
                registerLocation.setText(msg.obj.toString());
            }
        };
    }

    private void initLocationService() {
        locationService = new LocationService(getApplicationContext());
        LocationClientOption clientOption=locationService.getDefaultLocationClientOption();
        clientOption.setLocationNotify(true);
        locationService.setLocationOption(clientOption);
        mListener = new BDAbstractLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation bdLocation) {
                if (null != bdLocation && bdLocation.getLocType() != BDLocation.TypeServerError) {
                    Message msg = locationHandler.obtainMessage();
                    String addrrr = bdLocation.getAddrStr();
                    registerShop.setCityCode(Integer.parseInt(bdLocation.getCityCode()));
                    if (addrrr!= null || !addrrr.equals("null")) {
                        registerShop.setAddr(addrrr);
                    } else {
                        registerShop.setAddr(bdLocation.getStreet());
                        addrrr = bdLocation.getStreet();
                    }
                    registerShop.setLatitude(bdLocation.getLatitude());
                    registerShop.setLongtitude(bdLocation.getLongitude());
                    msg.obj = addrrr;
                    locationHandler.sendMessage(msg);
                    locationService.stop();
                }
            }
        };
        locationService.registerListener(mListener);
        locationService.setLocationOption(locationService.getDefaultLocationClientOption());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_registerSubmit:
                String account = tiet_registerAccount.getText().toString();
                String pwd = tiet_registerPassword.getText().toString();
                String surePwd = tiet_registerSurePassword.getText().toString();
                String name = tiet_registerName.getText().toString();
                String tel = tiet_registerTel.getText().toString();

                if (isRightInput(tiet_registerAccount, tiet_registerPassword, tiet_registerSurePassword, tiet_registerName, tiet_registerTel)) {
                    if (rb_shop.isChecked()) {
                        registerShop.setDeliveryDistance(Double.parseDouble(tiet_registerDeliveryDistance.getText().toString()));
                        registerShop.setFullAddr(tiet_registerAddr.getText().toString());
                        registerWithOkHttp(account, pwd, name, tel, new Gson().toJson(registerShop));
                    } else {
                        registerWithOkHttp(account, pwd, name, tel, "");
                    }
                }
                break;
            case R.id.registerGetLocation:
                locationService.start();
                registerLocation.setText("定位中");
                break;
        }

    }


    private void registerWithOkHttp(final String account, final String pwd, String name, String tel, String jsonShop) {
        launchDialog = new LaunchDialog(this);
        OkHttpUtil.CLIENT_TYPE = rb_rider.isChecked() ? "2" : "1";
        OkHttpUtil.registerWithOkHttp(account, pwd, name, tel, jsonShop, new Callback() {
            Message msg = handler.obtainMessage();

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                msg.what = OkHttpUtil.REQUEST_FAIL_NET;
                handler.sendMessage(msg);
                launchDialog.shutDown();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.body().string().equals("true")) {
                    msg.what = OkHttpUtil.REQUEST_SUCCESS;
                } else {
                    msg.what = OkHttpUtil.REQUEST_FAIL_SERVER;
                }
                handler.sendMessage(msg);
                launchDialog.shutDown();
            }
        });


    }

    private boolean isRightInput(TextInputEditText account, TextInputEditText pwd, TextInputEditText surePwd, TextInputEditText name, TextInputEditText tel) {

        if (!TextUtilTools.isNotEmpty(name)) {
            tiet_registerName.setError("昵称不能为空");
            return false;
        }
        if (!TextUtilTools.isNotEmpty(account)) {
            tiet_registerAccount.setError("账号不能为空");
            return false;
        }
        if (!TextUtilTools.isNotEmpty(pwd)) {
            tiet_registerPassword.setError("密码不能为空");
            return false;
        }
        if (!TextUtilTools.isNotEmpty(surePwd)) {
            tiet_registerSurePassword.setError("确认密码不能为空");
            return false;
        }
        if (!TextUtilTools.isNotEmpty(tel)) {
            tiet_registerSurePassword.setError("电话不能为空");
            return false;
        }
        if (!tiet_registerPassword.getText().toString().equals(tiet_registerSurePassword.getText().toString())) {
            tiet_registerSurePassword.setError("确认密码不等密码");
            return false;
        }
        if (!rb_shop.isChecked() && !rb_rider.isChecked()) {
            TextUtilTools.myToast(this, "选择骑手或商家", 0);
            return false;
        }
        if (rb_shop.isChecked()) {
            if (!TextUtilTools.isNotEmpty(tiet_registerAddr)) {
                tiet_registerAddr.setError("此处不可为空");
                return false;
            }
            if (!TextUtilTools.isNotEmpty(tiet_registerDeliveryDistance)) {
                tiet_registerDeliveryDistance.setError("此处不可为空");
                return false;
            }
            String location = registerLocation.getText().toString();
            if (location.equals("") || location.equals("null") || location == null) {
                TextUtilTools.myToast(getApplicationContext(), "需获取当前位置，可打开定位后点击 “获取位置”", 0);
                return false;
            }
        }
        return true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (locationService != null) {
            locationService.stop();
            locationService.unregisterListener(mListener);
        }
    }
}
