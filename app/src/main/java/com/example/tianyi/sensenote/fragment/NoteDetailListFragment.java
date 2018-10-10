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
import com.example.tianyi.sensenote.adapter.NoteBookDetailAdatper;
import com.example.tianyi.sensenote.adapter.decoration.NoteBookDetailDecoration;
import com.example.tianyi.sensenote.bean.NoteBookBean;
import com.example.tianyi.sensenote.presenter.impl.NoteBookDetailPresenter;
import com.example.tianyi.sensenote.presenter.interfaces.INoteBookDetailPresenter;

public class NoteDetailListFragment extends BaseFragment{

    public static final String ARG_NOTE_BOOK = "noteBook";

    private NoteBookBean mNoteBook;

    private RecyclerView mRecyclerView;

    private NoteBookDetailAdatper mAdapter;

    private INoteBookDetailPresenter noteBookDetailPresenter;

    public static NoteDetailListFragment newInstace(NoteBookBean noteBookBean){
        Bundle bundle = new Bundle();
        bundle.putSerializable(ARG_NOTE_BOOK,noteBookBean);
        NoteDetailListFragment fragment = new NoteDetailListFragment();
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setActionBar();
        setHasOptionsMenu(true);
    }


    private void setActionBar() {
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        // 显示返回按钮
        actionBar.setDisplayHomeAsUpEnabled(true);
        // 去掉logo图标
        actionBar.setDisplayShowHomeEnabled(false);
        mNoteBook = (NoteBookBean)getArguments().getSerializable(ARG_NOTE_BOOK);
        actionBar.setTitle(mNoteBook.getNoteBookName());
        //actionBar.setIcon(R.drawable.ic_note_back);
        actionBar.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:   //返回键的id
                getActivity().finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mView == null) {
            // 需要inflate一个布局文件 填充Fragment
            mView = inflater.inflate(R.layout.fragment_note_detail_list, container, false);
            initView();
            initPresenter();
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

    private void initView() {
        mRecyclerView = find(R.id.recyecle_notebook_notebookdetaillist);
        mAdapter = new NoteBookDetailAdatper(getContext());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addItemDecoration(new NoteBookDetailDecoration(getContext(),mAdapter));
    }

    private void initPresenter(){
        noteBookDetailPresenter = new NoteBookDetailPresenter(getContext());

    }

    @Override
    public void lazyLoad() {
        mAdapter.setNoteBookDetails(noteBookDetailPresenter.getNoteBookDetailsById(mNoteBook.getId()));
    }
}
