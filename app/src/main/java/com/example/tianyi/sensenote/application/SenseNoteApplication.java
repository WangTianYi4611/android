package com.example.tianyi.sensenote.application;

import android.app.Application;
import android.app.IntentService;


import com.example.tianyi.sensenote.bean.UserBean;
import com.example.tianyi.sensenote.httpservice.RetrofitClient;
import com.example.tianyi.sensenote.service.InitializeService;
import com.example.tianyi.sensenote.util.SaveToLocalUtil;

public class SenseNoteApplication extends Application {

    private static SenseNoteApplication instance;

    private UserBean userBean;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        InitializeService.start(this);
    }

    public UserBean getUserBean() {
        if(userBean == null){
            userBean = (UserBean) SaveToLocalUtil.restoreObject(this.getFilesDir().getPath().toString()+SaveToLocalUtil.USER_TOKEN_FILE);
        }
        return userBean;
    }

    public void setUserBean(UserBean userBean){
        this.userBean = userBean;
        RetrofitClient.rebuild();
    }

    public static SenseNoteApplication getInstance(){
        return instance;
    }

}
