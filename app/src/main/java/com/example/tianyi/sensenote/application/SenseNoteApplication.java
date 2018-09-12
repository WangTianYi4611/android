package com.example.tianyi.sensenote.application;

import android.app.Application;
import android.app.IntentService;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.tianyi.sensenote.service.InitializeService;

public class SenseNoteApplication extends Application {

    public static RequestQueue queues;

    @Override
    public void onCreate() {
        super.onCreate();
        queues = Volley.newRequestQueue(getApplicationContext());
        InitializeService.start(this);
    }

    public static RequestQueue getHttpQueues(){
        return queues;
    }

}
