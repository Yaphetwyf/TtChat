package com.example.yaphet.ttchat.controller.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.yaphet.ttchat.R;
import com.example.yaphet.ttchat.controller.fragment.BaseFrament;
import com.example.yaphet.ttchat.controller.fragment.ContactFragment;
import com.example.yaphet.ttchat.controller.fragment.ConverSationFragment;
import com.example.yaphet.ttchat.controller.fragment.SettingFragment;
import com.hyphenate.easeui.widget.EaseTitleBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends FragmentActivity {

    @Bind(R.id.Fl_main_FrameLayout)
    FrameLayout FlMainFrameLayout;
    @Bind(R.id.conversation)
    RadioButton conversation;
    @Bind(R.id.contasts)
    RadioButton contasts;
    @Bind(R.id.setting)
    RadioButton setting;
    @Bind(R.id.BR_main_Group)
    RadioGroup BRMainGroup;

    private List<Fragment> Fragments;
    private int Position;
    private Fragment mcontext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initView();//初始化Fragment页面
        initListener();
    }
    private void initView() {
        Fragments = new ArrayList<>();
        Fragments.add(new ConverSationFragment());
        Fragments.add(new ContactFragment());
        Fragments.add(new SettingFragment());
    }
    private void initListener() {
        BRMainGroup.setOnCheckedChangeListener(new MyRgChangeListener());
        BRMainGroup.check(R.id.conversation);//默认选中第一页
    }
    private class MyRgChangeListener implements RadioGroup.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
            switch (checkedId) {
                case R.id.conversation:
                    Position = 0;
                    break;
                case R.id.contasts:
                    Position = 1;
                    break;
                case R.id.setting:
                    Position = 2;
                    break;
                default:
                    Position = 0;
                    break;
            }
            Fragment frament = getFrament();
            replaceFrament(mcontext,frament);
        }
    }
    private Fragment getFrament(){
        if(Fragments!=null) {
            Fragment baseFrag = Fragments.get(Position);
            return baseFrag;
        }
        return null;
    }
    private void replaceFrament(Fragment from,Fragment to){
        if(from!=to) {
            mcontext=to;
            FragmentTransaction fragmentTransaction =getSupportFragmentManager().beginTransaction();
            if(!to.isAdded()) {//没有添加就添加
                if(from!=null) {
                    fragmentTransaction.hide(from);
                }
                if(to!=null) {
                    fragmentTransaction.add(R.id.Fl_main_FrameLayout,to).commit();
                }
            }else {//已添加就要直接显示
                if(from!=null) {
                    fragmentTransaction.hide(from);
                }
                if(to!=null) {
                    fragmentTransaction.show(to).commit();
                }
            }
        }
    }
}
