package com.example.tianyi.sensenote.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.tianyi.sensenote.InterfaceImpl.LoginInterfaceImpl;
import com.example.tianyi.sensenote.R;
import com.example.tianyi.sensenote.activity.LoginActivity;
import com.example.tianyi.sensenote.activity.MainActivity;
import com.example.tianyi.sensenote.application.SenseNoteApplication;
import com.example.tianyi.sensenote.bean.UserBean;
import com.example.tianyi.sensenote.interfaces.LoginInterface;
import com.example.tianyi.sensenote.po.BasicResult;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SplashFragment extends Fragment {


    private Button jumpSplashBtn = null;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * 此接口可以用于扩展 将Intent参数传递给fragment 避免了fragment和activity的托管关系
     * @return
     */
    public static Fragment newInstance(){
        SplashFragment splashFragment = new SplashFragment();
        return splashFragment;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_splash,container,false);

        jumpSplashBtn = v.findViewById(R.id.jump_splash);
        jumpSplashBtn.setText("跳过(5s)");

        final CountDownTimer countDownTimer = new CountDownTimer(5000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                jumpSplashBtn.setText("跳过"+millisUntilFinished/1000+"s");
            }

            @Override
            public void onFinish() {
                startLoginOrMainActivity();
            }
        }.start();

        jumpSplashBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                countDownTimer.cancel();
                startLoginOrMainActivity();
            }
        });

        return v;
    }


    private void startLoginOrMainActivity(){
        UserBean user = SenseNoteApplication.getInstance().getUserBean();
        LoginInterface loginInterface = new LoginInterfaceImpl();
        //boolean success = loginInterface.checkTokenValid(user);
        boolean success =false;
        if(success){
            Intent intent = MainActivity.newIntent(getActivity());
            startActivity(intent);
        }else{
            Intent intent = LoginActivity.newIntent(getActivity());
            startActivity(intent);
        }
        getActivity().finish();
    }



}
