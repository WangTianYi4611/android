package com.example.tianyi.sensenote.fragment;

import android.app.IntentService;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.tianyi.sensenote.R;
import com.example.tianyi.sensenote.adapter.NoteBookDetailAdatper;
import com.example.tianyi.sensenote.adapter.decoration.NoteBookDetailDecoration;
import com.example.tianyi.sensenote.bean.NoteBookBean;
import com.example.tianyi.sensenote.bean.NoteBookDetailBean;
import com.example.tianyi.sensenote.presenter.impl.NoteBookDetailPresenter;
import com.example.tianyi.sensenote.presenter.interfaces.INoteBookDetailPresenter;
import com.example.tianyi.sensenote.util.CollectionUtils;
import com.example.tianyi.sensenote.util.ToastUtil;

import java.util.Collections;
import java.util.List;

public class NoteDetailListFragment extends BaseFragment{

    public static final String ARG_NOTE_BOOK = "noteBook";

    public static final String ARG_SEARCH_STRING = "searchString";

    private NoteBookBean mNoteBook;

    private String mSearchString;

    private RecyclerView mRecyclerView;

    private NoteBookDetailAdatper mAdapter;

    private INoteBookDetailPresenter noteBookDetailPresenter;

    private int type;


    public static NoteDetailListFragment newInstance(NoteBookBean noteBookBean, String searchString){
        Bundle bundle = new Bundle();
        bundle.putSerializable(ARG_NOTE_BOOK,noteBookBean);
        bundle.putString(ARG_SEARCH_STRING,searchString);
        NoteDetailListFragment fragment = new NoteDetailListFragment();
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mNoteBook = (NoteBookBean)getArguments().getSerializable(ARG_NOTE_BOOK);
        mSearchString = getArguments().getString(ARG_SEARCH_STRING);
        setActionBar();
        setHasOptionsMenu(true);
    }


    private void setActionBar() {
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        // 显示返回按钮
        actionBar.setDisplayHomeAsUpEnabled(true);
        // 去掉logo图标
        actionBar.setDisplayShowHomeEnabled(false);
        if(mNoteBook != null) {
            actionBar.setTitle(mNoteBook.getNoteBookName());
        }
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
        if(mSearchString == null) mRecyclerView.addItemDecoration(new NoteBookDetailDecoration(getContext(),mAdapter));
//        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
//                if(newState == RecyclerView.SCROLL_STATE_IDLE ){
//                    mAdapter.setIsIdleState(true);
//                    mAdapter.notifyDataSetChanged();
//                }else{
//                    mAdapter.setIsIdleState(false);
//                }
//            }
//        });
    }

    private void initPresenter(){
        noteBookDetailPresenter = new NoteBookDetailPresenter(getContext());
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    private void updateUI() {
        if(mNoteBook != null) {
            mAdapter.setNoteBookDetails(noteBookDetailPresenter.getNoteBookDetailsById(mNoteBook.getId()));
        }else{
            //new SearchAsyncTask().execute(mSearchString);
        }
    }

    @Override
    public void lazyLoad() {
        if(mNoteBook != null) {
            mAdapter.setNoteBookDetails(noteBookDetailPresenter.getNoteBookDetailsById(mNoteBook.getId()));
        }else{
            new SearchAsyncTask().execute(mSearchString);
        }
    }

    private class SearchAsyncTask extends AsyncTask<String,List<NoteBookDetailBean>,Void>{

        @Override
        protected Void doInBackground(String... strings) {
            String searchString = strings[0];
            Integer count = noteBookDetailPresenter.getNoteBookDetailsCount();
            Log.i("sensenote","note book details count:"+count);
            int index = 0;
            int size = 10;
            while(index < count){
                List<NoteBookDetailBean> searchResult = noteBookDetailPresenter.getNoteBookDetailsBySearchString(searchString,index,size);
                publishProgress(searchResult);
                index += size;
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(List<NoteBookDetailBean>... values) {
            Log.i("sensenote","cur batch search result :" + values[0].toString());
            if(!CollectionUtils.isEmpty(values[0])) {
                mAdapter.addNoteBookDetailsToTail(values[0]);
            }
            //super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            ToastUtil.toastMsgShort(getContext(),"搜索完成");
        }
    }

}
