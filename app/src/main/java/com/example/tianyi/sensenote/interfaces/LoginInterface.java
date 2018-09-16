package com.example.tianyi.sensenote.interfaces;

import com.example.tianyi.sensenote.bean.UserBean;
import com.example.tianyi.sensenote.po.BasicResult;

import retrofit2.Callback;

public interface LoginInterface {

    boolean checkTokenValid(UserBean user);

    void userCheckIn(Callback<BasicResult<UserBean>> callback, String userName, String passWord);

}
