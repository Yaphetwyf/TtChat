package com.example.yaphet.ttchat.controller.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;
import android.view.MotionEvent;

import com.example.yaphet.ttchat.R;
import com.example.yaphet.ttchat.model.Model;
import com.hyphenate.chat.EMClient;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import scut.carson_ho.kawaii_loadingview.Kawaii_LoadingView;

public class HomeActivity extends Activity {
    private String Tag = HomeActivity.class.getSimpleName();
    private Handler handler;
    private Kawaii_LoadingView kawaii_loadingView;
    //private ExecutorService cachedThreadPool = Executors.newCachedThreadPool();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homectivity);
        kawaii_loadingView = findViewById(R.id.Kawaii_LoadingView);
        kawaii_loadingView.startMoving();
        handler = new Handler();
        handler.postDelayed(new Runnable() {//注意：只有当Runnable传入Thread的构造方法时才会在分线程执行
            @Override
            public void run() {
                //跳轉到主界面
                //注意;在主线程
                StartMainActivity();
            }
        }, 4000);
    }
    private void StartMainActivity() {
        kawaii_loadingView.stopMoving();
        Model.getInstance().getCachedThreadPool().execute(new Runnable() {//引入线程池概念
            @Override
            public void run() {
                if (EMClient.getInstance().isLoggedInBefore()) {//登陆过
                    //获取登录信息，传到主页面

                    Intent intentMain = new Intent(HomeActivity.this, MainActivity.class);
                    startActivity(intentMain);
                } else {//没有登陆过，跳转到登录界面
                    Intent intentLogin = new Intent(HomeActivity.this, LoginActivity.class);
                    startActivity(intentLogin);
                }
            }
        });
        finish();//开启主界面后欢迎界面不得在栈中
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {//当点击页面时直接跳转到主界面
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                handler.removeCallbacksAndMessages(null);
                StartMainActivity();
                break;
        }
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }
}
