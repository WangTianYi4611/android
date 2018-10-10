package com.example.tianyi.sensenote.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.tianyi.sensenote.R;
import com.example.tianyi.sensenote.activity.NoteDetailActivity;
import com.example.tianyi.sensenote.adapter.ChooseNoteBookTouchListener;
import com.example.tianyi.sensenote.adapter.NoteBookAdapter;
import com.example.tianyi.sensenote.adapter.StickyRecyclerHeadersTouchListener;
import com.example.tianyi.sensenote.adapter.decoration.NoteBookItemDecoration;
import com.example.tianyi.sensenote.bean.NoteBookBean;
import com.example.tianyi.sensenote.presenter.impl.NoteBookPresenter;
import com.example.tianyi.sensenote.util.ToastUtil;

import java.util.List;

public class NoteBookChooseFragment extends BaseFragment {

    private static final String ARG_NOTE_BOOK_NAME = "noteBookName";
    private static final String ARG_NOTE_BOOK = "noteBook";
    private RecyclerView mRecycleView;
    private NoteBookAdapter mAdpter;
    private NoteBookPresenter noteBookPresenter;


    public static NoteBookChooseFragment newInstance(String noteBookName){
        Bundle bundle = new Bundle();
        bundle.putString(ARG_NOTE_BOOK_NAME,noteBookName);
        NoteBookChooseFragment fragment = new NoteBookChooseFragment();
        fragment.setArguments(bundle);
        return fragment;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setActionBar();
        setHasOptionsMenu(true);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:   //返回键的id
                backToDetail();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void backToDetail() {
        ((NoteDetailActivity) getActivity()).backToNoteDetailFragement(null);
    }

    private void setActionBar() {
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        // 显示返回按钮
        actionBar.setDisplayHomeAsUpEnabled(true);
        // 去掉logo图标
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setTitle("移动笔记");
        //actionBar.setIcon(R.drawable.ic_note_back);
        actionBar.show();
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mView == null) {
            // 需要inflate一个布局文件 填充Fragment
            mView = inflater.inflate(R.layout.fragment_note, container, false);
            initPresenter();
            initView();
//        实现懒加载
            lazyLoad();
        }
        //缓存的mView需要判断是否已经被加过parent， 如果有parent需要从parent删除，要不然会发生这个mView已经有parent的错误。
        ViewGroup parent = (ViewGroup) mView.getParent();
        if (parent != null) {
            parent.removeView(mView);
        }

        return mView;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    public void updateUI(){
        List<NoteBookBean> noteBooks = noteBookPresenter.getChooseNoteBook();
        mAdpter.setNoteBooks(noteBooks);
    }

    private void initPresenter() {
        noteBookPresenter = NoteBookPresenter.getInstance();
    }

    private void initView() {
        String noteBookName = getArguments().getString(ARG_NOTE_BOOK_NAME);
        mRecycleView = find(R.id.recyecle_notebook_notebooklist);
        mAdpter = new NoteBookAdapter(getActivity(),noteBookPresenter.getSearchNoteBook(noteBookName).get(0),true);
        mAdpter.setmOnItemClickListener(new NoteBookAdapter.OnItemClickListener() {
            @Override
            public void onClick(View v, int position, NoteBookBean noteBookBean) {
                ((NoteDetailActivity) getActivity()).backToNoteDetailFragement(noteBookBean);
            }
        });
        mRecycleView.setAdapter(mAdpter);
        mRecycleView.setLayoutManager(new LinearLayoutManager(getActivity()));
        NoteBookItemDecoration itemDecoration = new NoteBookItemDecoration(getActivity(),mAdpter);
        mRecycleView.addItemDecoration(itemDecoration);
        mRecycleView.addOnItemTouchListener(new StickyRecyclerHeadersTouchListener(mRecycleView,itemDecoration));
    }

    @Override
    public void lazyLoad() {
        List<NoteBookBean> noteBooks = noteBookPresenter.getChooseNoteBook();
        mAdpter.setNoteBooks(noteBooks);
    }
}
