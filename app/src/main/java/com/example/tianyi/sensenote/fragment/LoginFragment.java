package com.example.tianyi.sensenote.fragment;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


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
    private SharedPreferenceUtil sharedPreferenceUtil;
    private static final String LOGIN_PREFERENCE_NAME = "login";
    public static final int LOGIN_SUCCESS = 1;
    public static final int LOGIN_FAIL = 2;
    private Handler loginHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case LOGIN_SUCCESS:
                    onLoginSuccessCallback((UserBean) msg.obj);
                    break;
                case LOGIN_FAIL:
                    Toast.makeText(getContext(),msg.obj.toString(),Toast.LENGTH_LONG).show();
                    break;
                case NetworkUtil.NETWORK_UNAVAILABLE:
                    Toast.makeText(getContext(),R.string.network_unavailable_string,Toast.LENGTH_LONG).show();
                    break;
            }
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((LoginActivity) getActivity()).getSupportActionBar().hide();
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

        BitMapUtil.setEditTextDrawableLeft(getContext(),R.drawable.user_name_icon,userNameEdtTxt);
        BitMapUtil.setEditTextDrawableLeft(getContext(),R.drawable.password_icon,passWordEdtTxt);
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

        loginInterface.userCheckIn(loginHandler,userName,passWord);
    }

    private void recoverSharedPreference() {
        if(sharedPreferenceUtil.contain("userName")){
            userNameEdtTxt.setText(sharedPreferenceUtil.getSharedPreference("userName","").toString());
        }
        if(sharedPreferenceUtil.contain("password")){
            passWordEdtTxt.setText(sharedPreferenceUtil.getSharedPreference("password","").toString());
        }
    }


    private void onLoginSuccessCallback(UserBean user) {
        SenseNoteApplication.getInstance().setUserBean(user);
        SaveToLocalUtil.saveObject(getActivity().getFilesDir().getPath().toString()+SaveToLocalUtil.USER_TOKEN_FILE,user); //用户信息存储到文件中
        //启动activity
        startActivity(MainActivity.newIntent(getActivity()));
        Toast.makeText(getContext(),"登录成功",Toast.LENGTH_LONG).show();
        getActivity().finish();
    }
}
