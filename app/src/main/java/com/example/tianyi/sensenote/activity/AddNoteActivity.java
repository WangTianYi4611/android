package com.example.tianyi.sensenote.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toolbar;

import com.example.tianyi.sensenote.R;
import com.example.tianyi.sensenote.fragment.AddNoteBookFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddNoteActivity extends SingleFragmentActivity {

    @BindView(R.id.txtView_addNoteMenu_cancel)
    public TextView cancelTextView;
    @BindView(R.id.txtView_addNoteMenu_create)
    public TextView createTextView;


    @Override
    protected Fragment createFragment() {
        return AddNoteBookFragment.newInstance();
    }

    public static Intent newIntent(Context context){
        return new Intent(context,AddNoteActivity.class);
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCustomActionBar();
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        cancelTextView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    private void setCustomActionBar() {
        ActionBar.LayoutParams lp =new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
        View mActionBarView= LayoutInflater.from(this).inflate(R.layout.add_note_menu_layout,null);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setCustomView(mActionBarView,lp);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
    }
}
