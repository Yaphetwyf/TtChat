package com.example.yaphet.ttchat.controller.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.yaphet.ttchat.R;
import com.example.yaphet.ttchat.model.Model;
import com.example.yaphet.ttchat.model.bean.UserInfo;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;
import com.hyphenate.util.Utils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends Activity {

    @Bind(R.id.username)
    EditText username;
    @Bind(R.id.password)
    EditText password;
    @Bind(R.id.register)
    Button register;
    @Bind(R.id.login)
    Button login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.em_activity_login);
        ButterKnife.bind(this);
        username.addTextChangedListener(new mChangerListener());//设置username改变的监听
    }
    @OnClick({R.id.register, R.id.login})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.register:
                //注册功能
                register();
                break;
            case R.id.login:
                //登录按钮
                login();
                break;
        }
    }
    private void login() {
        final String Musername = username.getText().toString();
        final String Mpassword = password.getText().toString();
        //联网登录到环信服务器
        if(TextUtils.isEmpty(Musername)||TextUtils.isEmpty(Mpassword)){//如果驶入为空
            Toast.makeText(LoginActivity.this, "输入的账号或者密码为空", Toast.LENGTH_SHORT).show();
            return;//跳出这个大方法
        }
        Model.getInstance().getCachedThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                EMClient.getInstance().login(Musername, Mpassword, new EMCallBack() {
                    @Override
                    public void onSuccess() {
                    //跳转到主页面
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                                startMainActivity();
                            }
                        });
                    }
                    @Override
                    public void onError(int i, final String s) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(LoginActivity.this, "登录失败"+s, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onProgress(int i, String s) {

                    }
                });
            }
        });
    }
    private void register() {
        //注册EMClient.getInstance().createAccount(username, pwd);//同步方法
        //1.首先获取注册的账户和密码
        final String Musername = username.getText().toString();
        final String Mpassword = password.getText().toString();
        //判断空处理
        if(TextUtils.isEmpty(Musername)||TextUtils.isEmpty(Mpassword)){//如果驶入为空
            Toast.makeText(LoginActivity.this, "输入的账号或者密码为空", Toast.LENGTH_SHORT).show();
            return;
        }
        //开启线程注册
        Model.getInstance().getCachedThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                //注册
                try {
                    EMClient.getInstance().createAccount(Musername,Mpassword);
                    //注册成功后，需要将用户账号信息保存到数据库
                    Model.getInstance().createdb();
                    Model.getInstance().addDb(new UserInfo(Musername,Musername,Musername,Musername));

                    runOnUiThread(new Runnable() {//跳到主线程，更新UI，注册成功后，跳转到主页面
                        @Override
                        public void run() {
                            Toast.makeText(LoginActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                            //startMainActivity();
                        }
                    });
                } catch (HyphenateException e) {
                    e.printStackTrace();
                    final String s = e.toString();
                    runOnUiThread(new Runnable() {//跳到主线程，更新UI，注册成功后，跳转到主页面
                        @Override
                        public void run() {
                            Toast.makeText(LoginActivity.this, s, Toast.LENGTH_SHORT).show();
                            //startMainActivity();
                        }
                    });
                }
            }
        });
    }
    private void startMainActivity() {
        Intent intent=new Intent(LoginActivity.this,MainActivity.class);
        startActivity(intent);
        finish();
    }

    private class mChangerListener implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            password.setText(null);
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }
}
