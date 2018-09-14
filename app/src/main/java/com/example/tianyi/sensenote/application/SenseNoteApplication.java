package com.example.tianyi.sensenote.application;

import android.app.Application;
import android.app.IntentService;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.tianyi.sensenote.bean.UserBean;
import com.example.tianyi.sensenote.service.InitializeService;

public class SenseNoteApplication extends Application {

    private RequestQueue queues;

    private UserBean userBean;

    @Override
    public void onCreate() {
        super.onCreate();
        queues = Volley.newRequestQueue(getApplicationContext());
        InitializeService.start(this);
    }

    public RequestQueue getHttpQueues(){
        return queues;
    }

    public UserBean getUserBean() {
        return userBean;
    }
}
