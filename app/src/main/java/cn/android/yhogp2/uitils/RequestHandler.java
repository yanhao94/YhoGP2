package cn.android.yhogp2.uitils;

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

    private String failToastStr;

    public Handler getRequestHandler(final Context context) {
        switch (REQUEST_TYPE) {
            case REGISTER:
                failToastStr = "该账号已被注册";
                break;
            case LOGIN:
                failToastStr = "账号或密码不对，仔细核对";
                break;
            case REQUEST_LIST:
                failToastStr = "获取列表失败，稍后再试";
                break;
            case REQUSET:
                failToastStr = "请求失败，稍后再试";
                break;
        }
        return new Handler() {
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case OkHttpUtil.REQUEST_SUCCESS:
                        doRequestSuccess(msg);
                        break;
                    case OkHttpUtil.REQUEST_FAIL_NET:
                        TextUtilTools.myToast(context.getApplicationContext(), "网络不好使，请确认网络无误后再试", 0);
                        break;
                    case OkHttpUtil.REQUEST_FAIL_SERVER:
                        TextUtilTools.myToast(context.getApplicationContext(), failToastStr, 0);
                        break;
                }
            }
        };
    }

    public abstract void doRequestSuccess(Message msg);

    public abstract void setTYPE();

}
