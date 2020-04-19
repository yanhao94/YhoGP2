package cn.android.yhogp2.uitils;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AlertDialog;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cn.android.yhogp2.R;


public class LaunchDialog {
    private static Context context;
    private AlertDialog lunchDialog;
    private RelativeLayout rl_lunch;
    private Handler handler;
    private ExecutorService cachedThreadPool;

    public final static int DOT_COUNT = 8;

    public LaunchDialog(Context contextp) {
        context = contextp;
        init();
    }

    private void init() {
        View view = LayoutInflater.from(context).inflate(R.layout.lunch_dialog, null, false);
        lunchDialog = new AlertDialog.Builder(context).setView(view).create();
        rl_lunch = view.findViewById(R.id.rl_lunch);
        final List<ImageView> ivDots = initProgressDots();
        lunchDialog.setCancelable(false);
        lunchDialog.show();
        WindowManager.LayoutParams params = lunchDialog.getWindow().getAttributes();
        params.width = 400;
        params.height = 400;
        lunchDialog.getWindow().setAttributes(params);
        cachedThreadPool = Executors.newCachedThreadPool();
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what > 0)
                    startAllAnimation(ivDots, handler);
            }
        };
        startAllAnimation(ivDots, handler);

    }

    private void startAllAnimation(final List<ImageView> ivDots, final Handler handler) {

        for (int i = 0; i < DOT_COUNT; i++) {
            final int finalI = i;
            Runnable rab = new Runnable() {
                @Override
                public void run() {
                    Animation animationM = new RotateAnimation(0, 360, Animation.ABSOLUTE,
                            0,
                            Animation.ABSOLUTE,
                            150);
                    animationM.setRepeatCount(0);
                    animationM.setDuration(3000 + finalI * 400);
                    ivDots.get(finalI).startAnimation(animationM);
                    if (finalI == DOT_COUNT - 1) {
                        animationM.setAnimationListener(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {

                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {
                                startAllAnimation(ivDots, handler);
                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {

                            }
                        });
                    }
                }
            };
            cachedThreadPool.execute(rab);
        }
    }

    private List<ImageView> initProgressDots() {
        List<ImageView> ivDots = new ArrayList<>();
        for (int i = 0; i < DOT_COUNT; i++) {
            ImageView iv = new ImageView(context);
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(30, 30);
            layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
            iv.setBackgroundResource(R.drawable.shape_dot_white);
            rl_lunch.addView(iv, layoutParams);
            ivDots.add(iv);
        }
        return ivDots;
    }


    public void shutDown() {
        Log.i("lunchDdd","shutDown"+lunchDialog);
            cachedThreadPool.shutdown();
            lunchDialog.dismiss();
    }
}
