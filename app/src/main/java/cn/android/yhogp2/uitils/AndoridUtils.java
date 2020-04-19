package cn.android.yhogp2.uitils;

import android.content.Context;
import android.content.Intent;

public class AndoridUtils {
    public static void jumpActvity(Context context, Class<?> targetActivity) {
        context.startActivity(new Intent(context, targetActivity));
    }
}
