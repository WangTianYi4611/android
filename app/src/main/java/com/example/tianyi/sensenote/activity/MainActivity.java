package com.example.tianyi.sensenote.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;


import com.example.tianyi.sensenote.R;
import com.example.tianyi.sensenote.fragment.NoteBookFragment;
import com.example.tianyi.sensenote.fragment.NoteDetailFragment;
import com.example.tianyi.sensenote.fragment.QuestionFragment;
import com.example.tianyi.sensenote.fragment.SearchNoteDetailFragment;
import com.example.tianyi.sensenote.util.BitMapUtil;
import com.example.tianyi.sensenote.util.DensityUtils;
import com.example.tianyi.sensenote.util.ToastUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.view.WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;

public class MainActivity extends AppCompatActivity{


    private FragmentManager fragmentManager;
    private FragmentTransaction transaction;
    private QuestionFragment questionFragment;
    private NoteBookFragment noteBookFragment;
    private SearchNoteDetailFragment mSearchNoteDetailFragment;
    private int lastSelectedPosition = 0;
    @BindView(R.id.txtView_main_note)
    public TextView noteFragmentTxtView;
    @BindView(R.id.txtView_main_search)
    public TextView searchFragmentTxtView;
    @BindView(R.id.txtView_main_news)
    public TextView newsFragmentTxtView;
    @BindView(R.id.txtView_main_control)
    public TextView personalFragmentTxtView;

    private TextView addNewNoteTextView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_with_navigation);
        init();
    }

    private void init() {
        ButterKnife.bind(this);
        setDefaultFragment();
        initFixedWindowButton();
    }

    private void initFixedWindowButton() {
        addNewNoteTextView = new TextView(this);
        addNewNoteTextView.setText("新");
        //int padding = DensityUtils.dip2px(this,5);
        //addNewNoteTextView.setPadding(padding,padding,padding,padding);
        addNewNoteTextView.setTextSize(DensityUtils.sp2px(this,9));
        addNewNoteTextView.setGravity(Gravity.CENTER);
        addNewNoteTextView.setBackground(getResources().getDrawable(R.drawable.circle_red_drawable));
        WindowManager.LayoutParams mLayoutParams = new WindowManager.LayoutParams(WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,WindowManager.LayoutParams.TYPE_APPLICATION ,0, PixelFormat.RGBA_8888);
        mLayoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | FLAG_NOT_FOCUSABLE;
        mLayoutParams.gravity = Gravity.RIGHT | Gravity.BOTTOM;
        mLayoutParams.x = 50;
        mLayoutParams.y = 300;
        getWindowManager().addView(addNewNoteTextView,mLayoutParams);
        addNewNoteTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newNoteDetail();
            }
        });
    }

    public void newNoteDetail(){
        Intent intent = NoteDetailActivity.newIntent(getBaseContext());
        startActivity(intent);
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
        clearClickStatus();
        switch (viewId) {
            case R.id.txtView_main_note:
                if (noteBookFragment == null) {
                    noteBookFragment = NoteBookFragment.newInstance();
                }
                transaction.replace(R.id.layFrame, noteBookFragment);
                noteFragmentTxtView.setCompoundDrawablesWithIntrinsicBounds(null,getResources().getDrawable(R.drawable.icon_note_fill),null,null);
                break;
            case R.id.txtView_main_search:
                if (mSearchNoteDetailFragment == null) {
                    mSearchNoteDetailFragment = SearchNoteDetailFragment.newInstance();
                }
                transaction.replace(R.id.layFrame, mSearchNoteDetailFragment);
                searchFragmentTxtView.setCompoundDrawablesWithIntrinsicBounds(null,getResources().getDrawable(R.drawable.icon_note_fill),null,null);
                break;
            case R.id.txtView_main_news:
                if (questionFragment == null) {
                    questionFragment = QuestionFragment.newInstance();
                }
                transaction.replace(R.id.layFrame, questionFragment);
                newsFragmentTxtView.setCompoundDrawablesWithIntrinsicBounds(null,getResources().getDrawable(R.drawable.icon_note_fill),null,null);
                break;
            case R.id.txtView_main_control:
                if (questionFragment == null) {
                    questionFragment = QuestionFragment.newInstance();
                }
                personalFragmentTxtView.setCompoundDrawablesWithIntrinsicBounds(null,getResources().getDrawable(R.drawable.icon_note_fill),null,null);
                transaction.replace(R.id.layFrame, questionFragment);
                break;
            default:
                break;
        }
        // 事务提交
        transaction.commit();
    }

    public void clearClickStatus(){
        noteFragmentTxtView.setCompoundDrawablesWithIntrinsicBounds(null,getResources().getDrawable(R.drawable.icon_note),null,null);
        searchFragmentTxtView.setCompoundDrawablesWithIntrinsicBounds(null,getResources().getDrawable(R.drawable.icon_note),null,null);
        newsFragmentTxtView.setCompoundDrawablesWithIntrinsicBounds(null,getResources().getDrawable(R.drawable.icon_note),null,null);
        personalFragmentTxtView.setCompoundDrawablesWithIntrinsicBounds(null,getResources().getDrawable(R.drawable.icon_note),null,null);
    }


    public static Intent newIntent(FragmentActivity activity) {
        return new Intent(activity,MainActivity.class);
    }
}
