package com.example.tianyi.sensenote.InterfaceImpl;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.example.tianyi.sensenote.activity.MainActivity;
import com.example.tianyi.sensenote.application.SenseNoteApplication;
import com.example.tianyi.sensenote.bean.UserBean;
import com.example.tianyi.sensenote.fragment.LoginFragment;
import com.example.tianyi.sensenote.fragment.RegisterFragment;
import com.example.tianyi.sensenote.httpservice.RetrofitClient;
import com.example.tianyi.sensenote.httpservice.UserLoginHttpService;
import com.example.tianyi.sensenote.interfaces.LoginInterface;
import com.example.tianyi.sensenote.po.BasicResult;
import com.example.tianyi.sensenote.util.NetworkUtil;
import com.example.tianyi.sensenote.util.SaveToLocalUtil;
import com.example.tianyi.sensenote.util.StringUtil;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginInterfaceImpl implements LoginInterface{

    private static UserLoginHttpService request = RetrofitClient.createService(UserLoginHttpService.class);

    @Override
    public boolean checkTokenValid(UserBean user) {
        //TODO:未来可以做一个请求一下token是否过期的功能
        if(user != null) return true;
        return false;
    }

    @Override
    public void userCheckIn(final Handler handler, String userName, String passWord) {

        //构建参数
        JSONObject loginParam = new JSONObject();
        try {
            loginParam.put("userName",userName);
            loginParam.put("userPassword",passWord);
        } catch (JSONException e) {
            Log.e("login",e.toString());
        }
        //发送网络请求
        Call<BasicResult<UserBean>> call = request.userLogin(RetrofitClient.generateRequestBody(loginParam));
        Callback<BasicResult<UserBean>> callback = new Callback<BasicResult<UserBean>>() {
            @Override
            public void onResponse(Call<BasicResult<UserBean>> call, Response<BasicResult<UserBean>> response) {
                Log.i("login",response.body().toString());
                BasicResult<UserBean> result = response.body();
                Message msg = handler.obtainMessage();
                if(StringUtil.isEmpty(result.getErrorMsg())){
                    msg.what = LoginFragment.LOGIN_SUCCESS;
                    msg.obj = result.getSingleResult();
                }else{
                    msg.what = LoginFragment.LOGIN_FAIL;
                    msg.obj = "登录失败:"+result.getErrorMsg();
                }
                handler.sendMessage(msg);
            }
            @Override
            public void onFailure(Call<BasicResult<UserBean>> call, Throwable t) {
                NetworkUtil.sendNetworkUnavailable(handler);
            }
        };
        call.enqueue(callback);
    }

    @Override
    public void registerNewUser(final Handler handler, UserBean user) {
        //构建参数
        JSONObject loginParam = new JSONObject();
        try {
            loginParam.put("userName",user.getUserName());
            loginParam.put("userPassword",user.getUserPassWord());
            loginParam.put("userEmail",user.getEmail());
        } catch (JSONException e) {
            Log.e("login",e.toString());
        }
        Call<BasicResult<String>> call = request.userRegister(RetrofitClient.generateRequestBody(loginParam));
        Callback<BasicResult<String>> callback = new Callback<BasicResult<String>>() {
            @Override
            public void onResponse(Call<BasicResult<String>> call, Response<BasicResult<String>> response) {
                BasicResult<String> result = response.body();
                Message msg = handler.obtainMessage();
                if(StringUtil.isEmpty(result.getErrorMsg())){
                    msg.what = RegisterFragment.REGISTER_SUCCESS;
                }else{
                    msg.what = RegisterFragment.REGISTER_FAIL;
                    msg.obj = result.getErrorMsg();
                }
                Log.i("network","current thread id"+Thread.currentThread().getId());
                handler.sendMessage(msg);
            }

            @Override
            public void onFailure(Call<BasicResult<String>> call, Throwable t) {
                NetworkUtil.sendNetworkUnavailable(handler);
            }
        };
        call.enqueue(callback);


    }



}
