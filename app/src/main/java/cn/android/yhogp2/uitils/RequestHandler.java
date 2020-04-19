package cn.android.yhogp2.uitils;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;

public abstract class RequestHandler {
    public Handler getRequestHander(final Context context) {
        return new Handler() {
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case OkHttpUtil.REQUEST_SUCCESS:
                        doRequestSuccess();
                        break;
                    case OkHttpUtil.REQUEST_FAIL_NET:
                        TextUtilTools.myToast(context.getApplicationContext(), "网络不好使，请确认网络无误后再试", 0);
                        break;
                    case OkHttpUtil.REQUEST_FAIL_SERVER:
                        TextUtilTools.myToast(context.getApplicationContext(), "服务器不好使，请稍后再试", 0);
                        break;
                }
            }
        };
    }

    public abstract void doRequestSuccess();

}
