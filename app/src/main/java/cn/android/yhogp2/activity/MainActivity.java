package cn.android.yhogp2.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.RadioButton;

import com.google.android.material.textfield.TextInputEditText;


import cn.android.yhogp2.R;
import cn.android.yhogp2.activity.rider.RiderHomeActivity;
import cn.android.yhogp2.activity.shop.ShopHomeActivity;
import cn.android.yhogp2.application.MainApplication;
import cn.android.yhogp2.uitils.LaunchDialog;
import cn.android.yhogp2.uitils.LoginUtil;
import cn.android.yhogp2.uitils.OkHttpUtil;
import cn.android.yhogp2.uitils.RequestHandler;
import cn.android.yhogp2.uitils.TextUtilTools;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private TextInputEditText tiet_loginDialog_account;
    private TextInputEditText tiet_loginDialog_password;
    private RadioButton rb_loginShop;
    private RadioButton rb_loginRider;
    private Handler handler;
    private static SharedPreferences sp_user;
    private static LaunchDialog launchDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        isAutoLogin();
        init();
    }

    private void isAutoLogin() {
        if (MainApplication.haveLogined) {
            startActivity(new Intent(MainActivity.this, MainApplication.TYPE == "1" ? ShopHomeActivity.class : RiderHomeActivity.class));
        }
    }

    private void init() {
        tiet_loginDialog_account = findViewById(R.id.tiet_loginDialog_account);
        tiet_loginDialog_password = findViewById(R.id.tiet_loginDialog_password);
        rb_loginRider = findViewById(R.id.rb_loginRider);
        rb_loginShop = findViewById(R.id.rb_loginShop);
        findViewById(R.id.btn_loginDialog_login).setOnClickListener(this);
        findViewById(R.id.btn_loginDialog_register).setOnClickListener(this);
        LoginUtil.initLoginUtil(this);
        initLoginHandler();
    }


    private void initLoginHandler() {
        handler = new RequestHandler() {
            @Override
            public void doRequestSuccess(Message msg) {
                startActivity(new Intent(MainActivity.this, MainApplication.TYPE == "1" ? ShopHomeActivity.class : RiderHomeActivity.class));
            }

            @Override
            public void setTYPE() {
                REQUEST_TYPE = RequestHandler.LOGIN;
            }
        }.getRequestHandler(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_loginDialog_login:
                if (isRightInput()) {
                    String account = tiet_loginDialog_account.getText().toString();
                    String password = tiet_loginDialog_password.getText().toString();
                    OkHttpUtil.CLIENT_TYPE = rb_loginShop.isChecked() ? "1" : "2";
                    LoginUtil.loginWithOkHttp(account, password, handler, false);
                }
                break;
            case R.id.btn_loginDialog_register:
                startActivity(new Intent(this, RegisterActivity.class));
                break;
        }
    }

    Boolean isRightInput() {
        if (!TextUtils.isEmpty(tiet_loginDialog_account.getText()) && !TextUtils.isEmpty(tiet_loginDialog_password.getText()) && !(!rb_loginShop.isChecked() && !rb_loginRider.isChecked()))
            return true;
        if (TextUtils.isEmpty(tiet_loginDialog_account.getText()))
            tiet_loginDialog_account.setError("账号不可为空");
        if (TextUtils.isEmpty(tiet_loginDialog_password.getText()))
            tiet_loginDialog_password.setError("密码不可为空");
        if (!rb_loginShop.isChecked() && !rb_loginRider.isChecked())
            TextUtilTools.myToast(getApplicationContext(), "请选择登录类型", 0);
        return false;
    }

    public static void loginOut(Context context) {
        MainApplication.haveLogined = false;
        MainApplication.loginShop = null;
        MainApplication.loginRider = null;
        SharedPreferences sp_user = context.getSharedPreferences("client", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp_user.edit();
        editor.clear();
        editor.apply();
//        Message msg = MainActivity.handler.obtainMessage();
//        msg.what = MainApplication.LOGIN_OUT;
//        msg.arg1 = 0;
//        MainActivity.handler.sendMessage(msg);
        context.startActivity(new Intent(context, MainActivity.class));
    }
    public static  void showLoginOutDialog(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("要登出不");
        builder.setPositiveButton("是", (dialogInterface, i) -> {
            MainActivity.loginOut(context);
        });
        builder.setNegativeButton("不", (dialogInterface, i) -> {
        });
        builder.show();
    }
}
