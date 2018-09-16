package com.example.tianyi.sensenote.fragment;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.example.tianyi.sensenote.InterfaceImpl.LoginInterfaceImpl;
import com.example.tianyi.sensenote.R;
import com.example.tianyi.sensenote.activity.LoginActivity;
import com.example.tianyi.sensenote.activity.MainActivity;
import com.example.tianyi.sensenote.application.SenseNoteApplication;
import com.example.tianyi.sensenote.bean.UserBean;
import com.example.tianyi.sensenote.httpservice.RetrofitClient;
import com.example.tianyi.sensenote.httpservice.UserLoginHttpService;
import com.example.tianyi.sensenote.interfaces.LoginInterface;
import com.example.tianyi.sensenote.po.BasicResult;
import com.example.tianyi.sensenote.util.BitMapUtil;
import com.example.tianyi.sensenote.util.NetworkUtil;
import com.example.tianyi.sensenote.util.SaveToLocalUtil;
import com.example.tianyi.sensenote.util.SharedPreferenceUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginFragment extends Fragment {


    private Button loginButton;
    private Button registerButton;
    private EditText userNameEdtTxt;
    private EditText passWordEdtTxt;
    private TextView forgetPassWord;
    private RequestQueue queues;
    private SharedPreferenceUtil sharedPreferenceUtil;
    private static final String LOGIN_PREFERENCE_NAME = "login";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public static LoginFragment newInstance(){
        return new LoginFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_login,container,false);
        loginButton = v.findViewById(R.id.btn_login);
        registerButton = v.findViewById(R.id.btn_register);
        userNameEdtTxt = v.findViewById(R.id.edtTxt_login_username);
        passWordEdtTxt = v.findViewById(R.id.edtTxt_login_password);

        setEditTextDrawableLeft(R.drawable.user_name_icon,userNameEdtTxt);
        setEditTextDrawableLeft(R.drawable.password_icon,passWordEdtTxt);
        sharedPreferenceUtil = new SharedPreferenceUtil(getActivity(),LOGIN_PREFERENCE_NAME);
        recoverSharedPreference();

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLoginBtnClick();
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRegisterBtnClick();
            }
        });

        return v;
    }

    private void onRegisterBtnClick() {
        ((LoginActivity) getActivity()).setRegisterFragment();
    }


    public void onLoginBtnClick(){
        String userName = userNameEdtTxt.getText().toString();
        String passWord = passWordEdtTxt.getText().toString();
        //存储sharedPreference
        sharedPreferenceUtil.put("userName",userName);;
        sharedPreferenceUtil.put("password",passWord);
        LoginInterface loginInterface = new LoginInterfaceImpl();
        Callback<BasicResult<UserBean>> callback = new Callback<BasicResult<UserBean>>() {
            @Override
            public void onResponse(Call<BasicResult<UserBean>> call, Response<BasicResult<UserBean>> response) {
                Log.i("login",response.body().toString());
                BasicResult<UserBean> result = response.body();
                if(BasicResult.SUCCESS == result.getCode()){
                    UserBean user = result.getSingleResult();
                    SenseNoteApplication.getInstance().setUserBean(user);
                    SaveToLocalUtil.saveObject(getActivity().getFilesDir().getPath().toString()+SaveToLocalUtil.USER_TOKEN_FILE,user); //用户信息存储到文件中
                    //启动activity
                    startActivity(MainActivity.newIntent(getActivity()));
                    Toast.makeText(getContext(),"登录成功",Toast.LENGTH_LONG);
                    getActivity().finish();
                }else{
                    StringBuffer failMsg = new StringBuffer();
                    failMsg.append("登录失败:").append(result.getMsg());
                    Toast.makeText(getContext(),failMsg,Toast.LENGTH_LONG);
                }
            }
            @Override
            public void onFailure(Call<BasicResult<UserBean>> call, Throwable t) {
                Log.i("login",t.toString());
                Toast.makeText(getContext(),"未知错误",Toast.LENGTH_LONG);
            }
        };
        loginInterface.userCheckIn(callback,userName,passWord);
    }

    private void recoverSharedPreference() {
        if(sharedPreferenceUtil.contain("userName")){
            userNameEdtTxt.setText(sharedPreferenceUtil.getSharedPreference("userName","").toString());
        }
        if(sharedPreferenceUtil.contain("password")){
            passWordEdtTxt.setText(sharedPreferenceUtil.getSharedPreference("password","").toString());
        }
    }

    public void setEditTextDrawableLeft(Integer resourceId,EditText editText){
        Drawable drawable = BitMapUtil.zoomDrawable(getResources().getDrawable(resourceId),180,180);
        editText.setCompoundDrawablesWithIntrinsicBounds(drawable,null,null,null);
    }


}
