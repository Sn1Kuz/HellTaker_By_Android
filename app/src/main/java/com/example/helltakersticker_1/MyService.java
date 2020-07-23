package com.example.helltakersticker_1;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.graphics.drawable.AnimationDrawable;
import android.media.Image;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.IBinder;
import android.text.Layout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

public class MyService extends Service {

    MediaPlayer mp;

    WindowManager windowManager;
    View mView;

//    MultiTouchListner multiTouchListner = new MultiTouchListner(this);

    WindowManager.LayoutParams params;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        LayoutInflater inflate = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //<BGM>
        mp = MediaPlayer.create(this, R.raw.heltaker);
        mp.setLooping(true);
        //</BGM>

        windowManager =(WindowManager) getSystemService(WINDOW_SERVICE);

        params = new WindowManager.LayoutParams(
                /*ViewGroup.LayoutParams.MATCH_PARENT*/
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY, //Android O버전 부터 TYPE_SYSTEM_ALERT이 Deprecated되서  TYPE_APPLICATION_OVERLAY로 변경
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                        |WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                        |WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,
                PixelFormat.TRANSLUCENT);

        params.gravity = Gravity.LEFT | Gravity.TOP;
        mView = inflate.inflate(R.layout.view_in_service, null);
//        final TextView textView = (TextView) mView.findViewById(R.id.textView); //필요 없어서 삭제

        final ImageView button = (ImageView) mView.findViewById(R.id.imageView);
//        button.setOnTouchListener(multiTouchListner);
        button.setOnTouchListener(mViewTouchListener);

        //animation put in(now lucy)
        button.setImageResource(R.drawable.lucy_animation);
        AnimationDrawable lucyAnimation = (AnimationDrawable)button.getDrawable();
        lucyAnimation.start();

        windowManager.addView(mView, params);
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        // 서비스가 호출될 때마다 실행
        mp.start(); // 노래 시작
        return super.onStartCommand(intent, flags, startId);
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        if(windowManager != null) {
            if (mView != null) {
                windowManager.removeView(mView);
                mView = null;
            }
            windowManager = null;
        }
        mp.stop();
    }


    //TouchListner 관련 코드
    //중요!!

    public MyService myService = this;

    public static int LONG_PRESS_TIME = 4000;//miliseconds

    final android.os.Handler _handler = new Handler();
    Runnable _longPressed = new Runnable() {
        @Override
        public void run() {
            Log.i("info", "it work");
            myService.stopSelf();
        }
    };
    private float mTouchX, mTouchY;
    private int mViewX, mViewY;

    private View.OnTouchListener mViewTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:

                    mTouchX = event.getRawX();
                    mTouchY = event.getRawY();
                    mViewX = params.x;
                    mViewY = params.y;

                    _handler.postDelayed(_longPressed, LONG_PRESS_TIME);
                    break;


                case MotionEvent.ACTION_MOVE:
                    int x = (int) (event.getRawX() - mTouchX);
                    int y = (int) (event.getRawY() - mTouchY);

                    params.x = mViewX + x;
                    params.y = mViewY + y;

                    windowManager.updateViewLayout(mView, params);

                    _handler.postDelayed(_longPressed, LONG_PRESS_TIME);
                    break;

                case MotionEvent.ACTION_UP:
                    _handler.removeCallbacks(_longPressed);
                    break;
            }

            return true;
        }
    };

}
