package com.example.tianyi.sensenote.util;


import android.os.Handler;
import android.os.Message;



public class NetworkUtil {

    public static final String SERVER_HOST = "http://192.168.1.5:8888/";

    public static final int NETWORK_UNAVAILABLE = 500;

    public static void sendNetworkUnavailable(Handler handler){
        Message msg = handler.obtainMessage();
        msg.what = NetworkUtil.NETWORK_UNAVAILABLE;
        handler.sendMessage(msg);
    }

}
