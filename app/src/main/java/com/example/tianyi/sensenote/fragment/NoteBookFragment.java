package com.example.tianyi.sensenote.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tianyi.sensenote.R;
import com.example.tianyi.sensenote.adapter.NoteBookAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import com.example.tianyi.sensenote.adapter.StickyRecyclerHeadersTouchListener;
import com.example.tianyi.sensenote.adapter.decoration.NoteBookItemDecoration;
public class NoteBookFragment extends BaseFragment{

    @BindView(R.id.recyecle_notebook_notebooklist)
    public RecyclerView recyclerView;
    private Unbinder unbinder;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public static NoteBookFragment newInstance(){
        return new NoteBookFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        mView = inflater.inflate(R.layout.fragment_note,container,false);
        unbinder = ButterKnife.bind(this, mView);
        init();
        return mView;

    }



    private void init() {
        List<String> notes = new ArrayList<>();
        for(int i = 0; i < 100;i++){
            notes.add("笔记本"+i);
        }
        NoteBookAdapter adapter = new NoteBookAdapter(notes,getActivity());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        View header= LayoutInflater.from(getActivity()).inflate(R.layout.adapter_notebook_header,recyclerView,false);
        adapter.setHeaderView(header);
        NoteBookItemDecoration itemDecoration = new NoteBookItemDecoration(getActivity(),adapter);
        recyclerView.addItemDecoration(itemDecoration);
        recyclerView.addOnItemTouchListener(new StickyRecyclerHeadersTouchListener(recyclerView,itemDecoration));

    }

    @Override
    public void lazyLoad() {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
