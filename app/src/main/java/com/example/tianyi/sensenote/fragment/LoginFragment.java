package com.example.tianyi.sensenote.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.tianyi.sensenote.R;

public class LoginFragment extends Fragment {

    private Button loginButton;
    private Button registerButton;
    private EditText userName;
    private EditText passWord;
    private TextView forgetPassWord;


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
        userName = v.findViewById(R.id.edtTxt_login_username);
        passWord = v.findViewById(R.id.edtTxt_login_password);

        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerButton.getText();
            }
        });


        return v;
    }
}
