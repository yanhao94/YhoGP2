package cn.android.yhogp2.uitils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;


public abstract class RequestHandler {
    public int REQUEST_TYPE;
    public final static int REGISTER = 1;
    public final static int LOGIN = 2;
    public final static int REQUEST_LIST = 3;
    public final static int REQUSET = 4;


    @SuppressLint("HandlerLeak")
    public Handler getRequestHandler(final Context context) {

        return new Handler() {
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case OkHttpUtil.REQUEST_SUCCESS:
                        doRequestSuccess(msg);
                        break;
                    case OkHttpUtil.REQUEST_FAIL_NET:
                        TextUtilTools.myToast(context.getApplicationContext(), "连接失败，请确认网络无误后再试", 0);
                        break;
                    case OkHttpUtil.REQUEST_FAIL_SERVER:
                        TextUtilTools.myToast(context.getApplicationContext(), msg.obj.toString(), 0);
                        break;
                }
            }
        };
    }

    public abstract void doRequestSuccess(Message msg);

    public abstract void setTYPE();

}
