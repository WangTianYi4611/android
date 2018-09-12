package com.example.tianyi.sensenote.activity;

import android.support.v4.app.Fragment;

import com.example.tianyi.sensenote.fragment.SplashFragment;

public class SplashActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return SplashFragment.newInstance();
    }


}
