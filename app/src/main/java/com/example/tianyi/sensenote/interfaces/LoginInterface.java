package com.example.tianyi.sensenote.interfaces;

import android.os.Handler;

import com.example.tianyi.sensenote.bean.UserBean;

public interface LoginInterface {

    boolean checkTokenValid(UserBean user);

    void userCheckIn(Handler handler, String userName, String passWord);

    void registerNewUser(Handler user, UserBean callback);

}
