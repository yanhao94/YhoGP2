package cn.android.yhogp2.uitils;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.math.BigDecimal;

public class TextUtilTools {
    public static boolean isNotEmpty(String text) {
        if (TextUtils.isEmpty(text))
            return false;
        return true;
    }
    public static <T> T fromToJson(String json, Type listType){
        Gson gson = new Gson();
        T t = null;
        t = gson.fromJson(json,listType);
        return t;
    }

    public static void myToast(Context context, String content, int duration) {
        Toast.makeText(context, content, duration == 1 ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT).show();
    }

    public static String getKm(double distance) {
        BigDecimal km = new BigDecimal(distance);
        return String.valueOf(km.setScale(1, BigDecimal.ROUND_UP))+"km";
    }
}
