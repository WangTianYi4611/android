package com.example.tianyi.sensenote.httpservice;

import com.example.tianyi.sensenote.application.SenseNoteApplication;
import com.example.tianyi.sensenote.bean.UserBean;
import com.example.tianyi.sensenote.util.NetworkUtil;
import com.example.tianyi.sensenote.util.StringUtil;

import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 每次请求需要重建builder
 */
public class RetrofitClient {

    private static final String API_BASE_URL = "http://192.168.1.5:8888/";

    private static final int DEFAULT_TIMEOUT = 5;

    private static OkHttpClient httpClient = initOkHttpClient();

    private static Retrofit retrofit = initRetrofit();

    private static Retrofit initRetrofit(){
        return new Retrofit.Builder()
                .baseUrl(API_BASE_URL).client(httpClient)
                .addConverterFactory(GsonConverterFactory.create()).build();
    }

    private static OkHttpClient initOkHttpClient() {
        return  new OkHttpClient.Builder().addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Interceptor.Chain chain) throws IOException {
                return addSomethingIntoHeader(chain);
            }
        }).connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS).build();
    }

    /**
     * 构建service
     *
     * @param serviceClass
     * @param <S>
     * @return
     */
    public static <S> S createService(Class<S> serviceClass) {
        //创建Retrofit客户端
        return retrofit.create(serviceClass);
    }


    private static Response addSomethingIntoHeader(Interceptor.Chain chain) throws IOException{
        UserBean user = SenseNoteApplication.getInstance().getUserBean();
        Request.Builder requestBuilder = chain.request()
                .newBuilder()
                .addHeader("Content-Type", "application/json;charset=UTF-8");
        if(user != null) requestBuilder.addHeader("Authorization",user.getToken());
        return chain.proceed(requestBuilder.build());
    }

    public static RequestBody generateRequestBody(JSONObject param) {
        return RequestBody.create(MediaType.parse("application/json"),param.toString());
    }

    public static synchronized void rebuild() {
        httpClient = initOkHttpClient();
        retrofit = initRetrofit();
    }
}
