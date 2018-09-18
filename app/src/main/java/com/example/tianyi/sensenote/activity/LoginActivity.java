package com.example.tianyi.sensenote.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Window;

import com.example.tianyi.sensenote.R;
import com.example.tianyi.sensenote.fragment.LoginFragment;
import com.example.tianyi.sensenote.fragment.QuestionFragment;
import com.example.tianyi.sensenote.fragment.RegisterFragment;

public class LoginActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return LoginFragment.newInstance();
    }

    public static Intent newIntent(Context packageContext){
        Intent intent = new Intent(packageContext,LoginActivity.class);
        return intent;
    }

    public void setRegisterFragment() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.fragment_container,RegisterFragment.newInstance());
        //transaction.addToBackStack(null);
        transaction.commit();
    }


    public void setLoginFragment() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.fragment_container,LoginFragment.newInstance());
        //transaction.addToBackStack(null);
        transaction.commit();
    }
}
