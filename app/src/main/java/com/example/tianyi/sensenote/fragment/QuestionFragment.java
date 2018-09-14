package com.example.tianyi.sensenote.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.tianyi.sensenote.R;
import com.example.tianyi.sensenote.application.SenseNoteApplication;

public class QuestionFragment extends Fragment {

    private TextView question = null;
    private TextView ansnwer = null;
    private Button showAnswer = null;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_hello,container,false);

        showAnswer = v.findViewById(R.id.ask_question);
        ansnwer = v.findViewById(R.id.question_answer);

        showAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ansnwer.setText("帅！！！！啊啊啊啊！！！帅！！！！");
                volleyStringRequestGet();
            }
        });

        return v;
    }

    public void volleyStringRequestGet() {
        String url = "http://apis.juhe.cn/mobile/get?phone=13160863974&key=335adcc4e891ba4e4be6d7534fd54c5d";
        // 这是StringRequest的请求方式
        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    // 请求成功的回调
                    @Override
                    public void onResponse(String arg0) {
                        Log.e("TAG", arg0);

                    }
                }, new Response.ErrorListener() {
            // 请求失败的回调
            @Override
            public void onErrorResponse(VolleyError arg0) {
                Log.e("TAG", arg0.toString());

            }
        });
        // 将请求添加到请求队列中
        ((SenseNoteApplication) getActivity().getApplicationContext()).getHttpQueues().add(request);
    }
}
