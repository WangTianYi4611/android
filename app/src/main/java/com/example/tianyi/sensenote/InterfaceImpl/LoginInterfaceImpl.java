package com.example.tianyi.sensenote.InterfaceImpl;

import android.util.Log;

import com.example.tianyi.sensenote.bean.UserBean;
import com.example.tianyi.sensenote.httpservice.RetrofitClient;
import com.example.tianyi.sensenote.httpservice.UserLoginHttpService;
import com.example.tianyi.sensenote.interfaces.LoginInterface;
import com.example.tianyi.sensenote.po.BasicResult;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;

public class LoginInterfaceImpl implements LoginInterface{

    @Override
    public boolean checkTokenValid(UserBean user) {
        //TODO:未来可以做一个请求一下token是否过期的功能
        if(user != null) return true;
        return false;
    }

    @Override
    public void userCheckIn(Callback<BasicResult<UserBean>> callback, String userName, String passWord) {

        //构建参数
        JSONObject loginParam = new JSONObject();
        try {
            loginParam.put("userName",userName);
            loginParam.put("userPassword",passWord);
        } catch (JSONException e) {
            Log.e("login",e.toString());
        }
        //发送网络请求
        UserLoginHttpService request = RetrofitClient.createService(UserLoginHttpService.class);
        Call<BasicResult<UserBean>> call = request.userLogin(RetrofitClient.generateRequestBody(loginParam));
        call.enqueue(callback);
    }
}
