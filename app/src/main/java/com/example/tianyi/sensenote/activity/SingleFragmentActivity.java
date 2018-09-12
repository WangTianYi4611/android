package com.example.tianyi.sensenote.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.example.tianyi.sensenote.R;

public abstract class SingleFragmentActivity extends AppCompatActivity {

    protected abstract Fragment createFragment();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager fm =getSupportFragmentManager();
        Fragment fragement = fm.findFragmentById(R.id.fragment_container);

        if(fragement == null){
            fragement = createFragment();
            fm.beginTransaction().add(R.id.fragment_container,fragement).commit();
        }

    }



}
