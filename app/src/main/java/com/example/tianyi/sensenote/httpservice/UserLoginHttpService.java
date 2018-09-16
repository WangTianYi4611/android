package com.example.tianyi.sensenote.httpservice;

import com.example.tianyi.sensenote.bean.UserBean;
import com.example.tianyi.sensenote.po.BasicResult;
import com.example.tianyi.sensenote.util.NetworkUtil;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface UserLoginHttpService {

    public static final String API_LOGIN_URI = "login/userCheckIn";

    @POST(API_LOGIN_URI)
    Call<BasicResult<UserBean>> userLogin(@Body RequestBody requestBody);

}
