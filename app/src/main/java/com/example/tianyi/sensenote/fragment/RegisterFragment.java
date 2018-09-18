package com.example.tianyi.sensenote.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.tianyi.sensenote.InterfaceImpl.LoginInterfaceImpl;
import com.example.tianyi.sensenote.R;
import com.example.tianyi.sensenote.activity.LoginActivity;
import com.example.tianyi.sensenote.bean.UserBean;
import com.example.tianyi.sensenote.exception.NetworkException;
import com.example.tianyi.sensenote.interfaces.LoginInterface;
import com.example.tianyi.sensenote.po.BasicResult;
import com.example.tianyi.sensenote.util.BitMapUtil;
import com.example.tianyi.sensenote.util.NetworkUtil;
import com.example.tianyi.sensenote.util.ToastUtil;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterFragment extends Fragment {


    private EditText registerUserNameEdt;
    private EditText registerPasswordEdt;
    private EditText registerEmailEdt;
    private Button registerBtn;
    public static final int REGISTER_SUCCESS = 1;
    public static final int REGISTER_FAIL = 2;

    private Handler registerHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case REGISTER_SUCCESS:
                    Toast.makeText(getContext(),R.string.register_success,Toast.LENGTH_LONG).show();
                    backToLogin();
                    break;
                case REGISTER_FAIL:
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
        setActionBar();
        setHasOptionsMenu(true);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:   //返回键的id
                backToLogin();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void backToLogin() {
        ((LoginActivity) getActivity()).setLoginFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_register,container,false);
        initView(v);
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRegisterBtnClick();
            }
        });
        return v;
    }

    private void onRegisterBtnClick() {
        String username = registerUserNameEdt.getText().toString();
        if(!ToastUtil.checkNullThenToast(getContext().getApplicationContext(),username,"用户名不能为空")) return;
        String password = registerPasswordEdt.getText().toString();
        if(!ToastUtil.checkNullThenToast(getContext(),password,"密码不能为空")) return;
        String email = registerEmailEdt.getText().toString();
        if(!ToastUtil.checkNullThenToast(getContext(),email,"邮箱不能为空")) return;
        LoginInterface loginInterface = new LoginInterfaceImpl();
        Log.i("network","current thread id"+Thread.currentThread().getId());
        loginInterface.registerNewUser(registerHandler,new UserBean(username,password,email));
    }

    private void initView(View v) {
        registerUserNameEdt = v.findViewById(R.id.edtTxt_register_username);
        registerPasswordEdt = v.findViewById(R.id.edtTxt_register_password);
        registerEmailEdt = v.findViewById(R.id.edtTxt_register_email);
        registerBtn = v.findViewById(R.id.btn_register_apply);

        BitMapUtil.setEditTextDrawableLeft(getContext(),R.drawable.ic_mobile_init,registerUserNameEdt);
        BitMapUtil.setEditTextDrawableLeft(getContext(),R.drawable.ic_passwd_init,registerPasswordEdt);
        BitMapUtil.setEditTextDrawableLeft(getContext(),R.drawable.ic_note_email,registerEmailEdt);
    }

    private void setActionBar() {
        ActionBar actionBar = ((LoginActivity) getActivity()).getSupportActionBar();
        // 显示返回按钮
        actionBar.setDisplayHomeAsUpEnabled(true);
        // 去掉logo图标
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setTitle("注册新用户");
        //actionBar.setIcon(R.drawable.ic_note_back);
        actionBar.show();
    }

    public static Fragment newInstance() {
        return new RegisterFragment();
    }
}
