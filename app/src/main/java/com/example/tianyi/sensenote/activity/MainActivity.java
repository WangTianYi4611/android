package com.example.tianyi.sensenote.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;


import com.example.tianyi.sensenote.R;
import com.example.tianyi.sensenote.fragment.NoteBookFragment;
import com.example.tianyi.sensenote.fragment.QuestionFragment;
import com.example.tianyi.sensenote.util.BitMapUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity{


    private FragmentManager fragmentManager;
    private FragmentTransaction transaction;
    private QuestionFragment questionFragment;
    private NoteBookFragment noteBookFragment;

    private int lastSelectedPosition = 0;
    @BindView(R.id.txtView_main_note)
    public TextView noteFragmentTxtView;
    @BindView(R.id.txtView_main_search)
    public TextView searchFragmentTxtView;
    @BindView(R.id.txtView_main_news)
    public TextView newsFragmentTxtView;
    @BindView(R.id.txtView_main_control)
    public TextView personalFragmentTxtView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_with_navigation);
        init();
    }

    private void init() {
        ButterKnife.bind(this);
        setDefaultFragment();
    }




    private void setDefaultFragment() {
        fragmentManager = getSupportFragmentManager();
        transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.layFrame, noteBookFragment.newInstance());
        transaction.commit();
    }

    @OnClick({R.id.txtView_main_note,R.id.txtView_main_search,R.id.txtView_main_news,R.id.txtView_main_control})
    public void OnClickTextView(View view) {
        fragmentManager = getSupportFragmentManager();
        transaction = fragmentManager.beginTransaction();
        int viewId = view.getId();
        Log.i("click","out click");
        switch (viewId) {
            case R.id.txtView_main_note:
                if (noteBookFragment == null) {
                    noteBookFragment = NoteBookFragment.newInstance();
                }
                transaction.replace(R.id.layFrame, noteBookFragment);
                break;
            case R.id.txtView_main_search:
                if (questionFragment == null) {
                    questionFragment = QuestionFragment.newInstance();
                }
                transaction.replace(R.id.layFrame, questionFragment);
                break;
            case R.id.txtView_main_news:
                if (questionFragment == null) {
                    questionFragment = QuestionFragment.newInstance();
                }
                transaction.replace(R.id.layFrame, questionFragment);
                break;
            case R.id.txtView_main_control:
                if (questionFragment == null) {
                    questionFragment = QuestionFragment.newInstance();
                }
                transaction.replace(R.id.layFrame, questionFragment);
                break;
            default:
                break;
        }
        // 事务提交
        transaction.commit();
    }


    public static Intent newIntent(FragmentActivity activity) {
        return new Intent(activity,MainActivity.class);
    }
}
