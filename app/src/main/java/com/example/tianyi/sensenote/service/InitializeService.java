package com.example.tianyi.sensenote.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;

public class InitializeService extends IntentService {

    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    public static final String ACTION_INIT_WHEN_APP_CREATE = "com.example.tianyi.service.action.app.create";
    public static final String EXTRA_PARAM = "com.example.tianyi.service.extra.PARAM";

    public InitializeService() {
        super("InitializeService");
    }

    /**
     * 启动调用
     * @param context
     */
    public static void start(Context context) {
        Intent intent = new Intent(context, InitializeService.class);
        intent.setAction(ACTION_INIT_WHEN_APP_CREATE);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_INIT_WHEN_APP_CREATE.equals(action)) {
                performInit(EXTRA_PARAM);
            }
        }
    }

    /**
     * 启动初始化操作
     */
    private void performInit(String param) {

    }
}
