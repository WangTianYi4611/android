package com.example.tianyi.sensenote.activity;

import android.app.ActionBar;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tianyi.sensenote.R;
import com.example.tianyi.sensenote.adapter.NoteBookAdapter;
import com.example.tianyi.sensenote.adapter.StickyRecyclerHeadersTouchListener;
import com.example.tianyi.sensenote.adapter.decoration.NoteBookItemDecoration;
import com.example.tianyi.sensenote.util.DensityUtils;
import com.example.tianyi.sensenote.viewgroup.SwipeLeftLinearLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TestActivity extends AppCompatActivity{

   @BindView(R.id.testSwipeLayout)
    public SwipeLeftLinearLayout mylayout;

    @BindView(R.id.testTextView)
    public TextView textView;
//    @BindView(R.id.test_recyecle_notebook_notebooklist)
    public RecyclerView recyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        ButterKnife.bind(this);
        test1();
    }

    private void test2(){

        //View header= LayoutInflater.from(this).inflate(R.layout.adapter_notebook_header,recyclerView,false);
        //adapter.setHeaderView(header);
        //NoteBookItemDecoration itemDecoration = new NoteBookItemDecoration(this,adapter);
        //recyclerView.addItemDecoration(itemDecoration);
        //recyclerView.addOnItemTouchListener(new StickyRecyclerHeadersTouchListener(recyclerView,itemDecoration));
    }


    private  void test1(){
        textView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

            }
        });
        TextView deleteView = new TextView(this);
        deleteView.setLayoutParams(new LinearLayout.LayoutParams(120, ActionBar.LayoutParams.MATCH_PARENT));
        deleteView.setText("删除");
        deleteView.setBackgroundColor(getResources().getColor(R.color.green));
        deleteView.setGravity(Gravity.CENTER);
        TextView view2 = new TextView(this);
        view2.setLayoutParams(new LinearLayout.LayoutParams(120, ActionBar.LayoutParams.MATCH_PARENT));
        view2.setText("设置");
        view2.setBackgroundColor(Color.YELLOW);
        view2.setGravity(Gravity.CENTER);

        deleteView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.i("click","删除click");
                return true;
            }
        });
        view2.setClickable(true);
        view2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("click","设置click");
                Toast.makeText(getApplication(),"设置click",Toast.LENGTH_SHORT).show();
            }
        });
        mylayout.addSwipeItem(deleteView,250);
        mylayout.addSwipeItem(view2,250);

    }
}
