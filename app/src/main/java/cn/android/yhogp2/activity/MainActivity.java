package cn.android.yhogp2.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.RadioButton;

import com.google.android.material.textfield.TextInputEditText;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import cn.android.yhogp2.R;
import cn.android.yhogp2.uitils.LaunchDialog;
import cn.android.yhogp2.uitils.OkHttpUtil;
import cn.android.yhogp2.uitils.RequestHandler;
import cn.android.yhogp2.uitils.TextUtilTools;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

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
        init();
    }

    private void init() {
        tiet_loginDialog_account = findViewById(R.id.tiet_loginDialog_account);
        tiet_loginDialog_password = findViewById(R.id.tiet_loginDialog_password);
        rb_loginRider = findViewById(R.id.rb_loginRider);
        rb_loginShop = findViewById(R.id.rb_loginShop);
        findViewById(R.id.btn_loginDialog_login).setOnClickListener(this);
        findViewById(R.id.btn_loginDialog_register).setOnClickListener(this);

        initLoginHandler();
    }

    private void initLoginHandler() {
        handler = new RequestHandler() {
            @Override
            public void doRequestSuccess() {
                startActivity(new Intent(MainActivity.this,HomeActivity.class));
            }
        }.getRequestHander(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_loginDialog_login:
                if (isRightInput()) {
                    String account = tiet_loginDialog_account.getText().toString();
                    String password = tiet_loginDialog_password.getText().toString();
                    String type = rb_loginShop.isChecked() ? "1" : "2";
                    loginWithOkHttp(account, password, type, handler);
                }
                break;
            case R.id.btn_loginDialog_register:
                startActivity(new Intent(this, RegisterActivity.class));
                break;
        }
    }

    private void loginWithOkHttp(String account, String password, String type, final Handler handler) {
        OkHttpUtil.CLIENT_TYPE=type;
        OkHttpUtil.loginWithOkHttp(account, password, new Callback() {
            Message msg = handler.obtainMessage();
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {

            }
        });
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
}
