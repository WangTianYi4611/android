package com.example.tianyi.sensenote.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.example.tianyi.sensenote.fragment.QuestionFragment;

public class LoginActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new QuestionFragment();
    }

    public static Intent newIntent(Context packageContext){
        Intent intent = new Intent(packageContext,LoginActivity.class);
        return intent;
    }

}
