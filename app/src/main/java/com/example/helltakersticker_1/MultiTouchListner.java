//이건 폐기. margin으로 이동하는거여서 끝에 갈수록 크기 작아지는 문제 등등이 생겼음. 이 코드를 MyService에서 직접 바꾸는걸로 수정함
//공부용으로 남김


//package com.example.helltakersticker_1;
//
//import android.annotation.SuppressLint;
//import android.content.Intent;
//import android.os.Handler;
//import android.util.Log;
//import android.view.MotionEvent;
//import android.view.View;
//import android.view.View.OnTouchListener;
//import android.view.ViewGroup;
//import android.view.ViewGroup.MarginLayoutParams;
//import android.widget.LinearLayout;
//import android.widget.RelativeLayout;
//import android.widget.TableLayout;
//
//import java.util.logging.LogRecord;
//
//public class MultiTouchListner implements View.OnTouchListener {
//
//    public static int LONG_PRESS_TIME = 4000;//miliseconds
//
//    final android.os.Handler _handler = new Handler();
//    Runnable _longPressed = new Runnable() {
//        @Override
//        public void run() {
//            Log.i("info", "it work");
//            myService.stopSelf();
//        }
//    };
//
//
//    private float mPrevX, mPrevY;
//
//    public MyService myService;
//
//    public MultiTouchListner(MyService myService1) {
//        myService = myService1;
//    }
//
//    @Override
//    public boolean onTouch(View view, MotionEvent event) {
//        float currX, currY;
//        int action = event.getAction();
//        switch (action) {
//            case MotionEvent.ACTION_DOWN: {
//                mPrevX = event.getX();
//                mPrevY = event.getY();
//                _handler.postDelayed(_longPressed, LONG_PRESS_TIME);
//                break;
//            }
//            case MotionEvent.ACTION_MOVE:
//            {
//                currX = event.getRawX();
//                currY = event.getRawY();
//
//
//                //문제점 1 - Layout으로 움직이는거여서 Layout 내부도 동시에 움직이는거 같지는 않음
//                //문제점 2 - 지금 layout 구분?이 되어있는지 화면 밖으로 가면 점점 작아지면서 사라지는데, 그러면 껐다 켜야함. 종료 안눌림
//                // 전체적인 문제는 다중 layout이라 생기는 문제인거 같은데, 그것만 해결하면 될듯
//                MarginLayoutParams marginParams = new MarginLayoutParams(view.getLayoutParams());
//                marginParams.setMargins((int)(currX - mPrevX), (int)(currY - mPrevY),0, 0);
//                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(marginParams);
//                view.setLayoutParams(layoutParams);
//
//
//                //폰으로 직접만지면 움직임이 생겨서 여기도 추가(삭제)
//                _handler.removeCallbacks(_longPressed);
//                break;
//            }
//            case MotionEvent.ACTION_CANCEL:
//                _handler.removeCallbacks(_longPressed);
//                break;
//            case MotionEvent.ACTION_UP:
//                _handler.removeCallbacks(_longPressed);
//                break;
//        }
//        return true;
//    }
//}