package com.example.yaphet.ttchat.model;

/**
 * Created by WYF on 2018/3/14.
 */

import android.content.Context;

import com.example.yaphet.ttchat.model.bean.UserInfo;
import com.example.yaphet.ttchat.model.dao.CreateDb;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 当controller层需要和model层进行数据交互时必须走这个方法
 * 1.单例模式，全局只有一个变量
 * 2.为了后期方便维护，比如当model层需要改动时，不需要改动Controller层
 */
public class Model {
    private static volatile Model instance=null;
    private Context mContext;
    private ExecutorService cachedThreadPool = Executors.newCachedThreadPool();

    private Model() {
    }
    public static Model getInstance(){//双重锁式,获取单例对象
        if(instance==null){
            synchronized(Model .class){
                if(instance==null){
                    instance=new Model ();
                }
            }
        }
        return instance;
    }
    public void init(Context context){
        mContext=context;
    }
    public ExecutorService getCachedThreadPool(){
        return cachedThreadPool;
    }
    public void createdb(){
        CreateDb.Createdb(mContext);
    }
    public void addDb(UserInfo info){

        CreateDb.addDb(info.getHxId(),info.getName(),info.getNick(),info.getPhoto());
    }
    public UserInfo getUserByid(String HXid){
        UserInfo userInfo = CreateDb.searchDbByid(HXid);
        return userInfo;
    }
    public UserInfo getUserByName(String name){
        UserInfo userInfo = CreateDb.getAccountByname(name);
        return userInfo;
    }
}
