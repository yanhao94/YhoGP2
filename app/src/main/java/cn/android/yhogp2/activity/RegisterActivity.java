package cn.android.yhogp2.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.RadioButton;

import com.google.android.material.textfield.TextInputEditText;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import cn.android.yhogp2.R;
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
    private RadioButton rb_shop;
    private RadioButton rb_rider;
    private Handler handler;
    private LaunchDialog launchDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        init();
    }

    private void init() {
        findViewById(R.id.btn_registerSubmit).setOnClickListener(this);
        tiet_registerName = findViewById(R.id.tiet_registerName);
        tiet_registerAccount = findViewById(R.id.tiet_registerAccount);
        tiet_registerPassword = findViewById(R.id.tiet_registerPassword);
        tiet_registerSurePassword = findViewById(R.id.tiet_registerSurePassword);
        rb_shop = findViewById(R.id.rb_Shop);
        rb_rider = findViewById(R.id.rb_rider);

        handler = new RequestHandler() {
            @Override
            public void doRequestSuccess() {

            }
        }.getRequestHander(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_registerSubmit:
                String account = tiet_registerAccount.getText().toString();
                String pwd = tiet_registerPassword.getText().toString();
                String surePwd = tiet_registerSurePassword.getText().toString();
                String name = tiet_registerName.getText().toString();

                if (isRightInput(account, pwd, surePwd, name)) {
                    //registerWithOkHttp(account, pwd, name);
                    OkHttpUtil.CLIENT_TYPE = rb_rider.isChecked() ? "2" : "1";
                    OkHttpUtil.registerWithOkHttp(account, pwd, name, new Callback() {
                        Message msg = handler.obtainMessage();

                        @Override
                        public void onFailure(@NotNull Call call, @NotNull IOException e) {
                            msg.what = OkHttpUtil.REQUEST_FAIL_NET;
                            handler.sendMessage(msg);
                        }

                        @Override
                        public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                            if (!response.body().string().equals("false")) {
                                msg.what = OkHttpUtil.REQUEST_SUCCESS;
                            } else {
                                msg.what = OkHttpUtil.REQUEST_FAIL_SERVER;
                            }
                            handler.sendMessage(msg);
                        }
                    });

                    launchDialog = new LaunchDialog(this);
                }
        }
    }

    private boolean isRightInput(String account, String pwd, String surePwd, String name) {

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
        if (!TextUtilTools.isNotEmpty(name)) {
            tiet_registerName.setError("昵称不能为空");
            return false;
        }
        if (!equals(surePwd)) {
            tiet_registerSurePassword.setError("确认密码不等密码");
            return false;
        }
        if (!rb_shop.isChecked() && !rb_rider.isChecked()) {
            TextUtilTools.myToast(this, "选择骑手或商家", 0);
            return false;
        }
        return true;
    }
}
