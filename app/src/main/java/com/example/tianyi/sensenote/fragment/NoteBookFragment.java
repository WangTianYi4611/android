package com.example.tianyi.sensenote.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

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
import com.example.tianyi.sensenote.bean.NoteBookBean;
import com.example.tianyi.sensenote.presenter.impl.NoteBookPresenter;
import com.example.tianyi.sensenote.presenter.interfaces.INoteBookPresenter;
import com.example.tianyi.sensenote.util.KeyBoardUtil;
import com.example.tianyi.sensenote.util.StringUtil;

import org.w3c.dom.Text;

public class NoteBookFragment extends BaseFragment{

    public RecyclerView recyclerView;
    private Unbinder unbinder;
    private INoteBookPresenter noteBookPresenter;
    private boolean isPrepared;
    private EditText noteBookSearchEditText;
    private TextView noteBookSearchCancelTextView;
    private View headerView;
    private NoteBookAdapter mAdpter;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCustomActionBar();
    }


    private void setCustomActionBar() {
        ActionBar.LayoutParams lp =new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
        View mActionBarView= LayoutInflater.from(getActivity()).inflate(R.layout.note_book_menu_layout,null);
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionBar.setCustomView(mActionBarView,lp);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
    }

    public static NoteBookFragment newInstance(){
        return new NoteBookFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (mView == null) {
            // 需要inflate一个布局文件 填充Fragment
            mView = inflater.inflate(R.layout.fragment_note, container, false);
            initView();
            initLisenter();
            isPrepared = true;
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

    private void initLisenter() {
        noteBookSearchEditText.setOnFocusChangeListener(new View.OnFocusChangeListener(){
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    noteBookSearchCancelTextView.setVisibility(View.VISIBLE);
                }else{
                    noteBookSearchCancelTextView.setVisibility(View.GONE);
                }
            }
        });
        noteBookSearchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchNoteBook(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

//        noteBookSearchEditText.setOnEditorActionListener(new TextView.OnEditorActionListener(){
//            @Override
//            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//                if(actionId == EditorInfo.IME_ACTION_DONE){
//                    searchNoteBook(v);
//                    return true;
//                }
//                return false;
//            }
//        });
        noteBookSearchCancelTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!StringUtil.isEmpty(noteBookSearchEditText.getText().toString())){
                    mAdpter.setHeaderView(headerView);
                    updateUI(); //刷新所有内容
                }
                noteBookSearchEditText.setText("");
                noteBookSearchEditText.clearFocus();
                KeyBoardUtil.closeKeybord(noteBookSearchEditText,getContext());
            }
        });
    }

    private void searchNoteBook(String notebookName) {
        mAdpter = ((NoteBookAdapter) recyclerView.getAdapter());
        if(StringUtil.isEmpty(notebookName)){ //显示全部内容
            mAdpter.setHeaderView(headerView);
            mAdpter.setNoteBooks(noteBookPresenter.getAllNoteBook());
            return;
        }
        if(mAdpter.getHeaderView() != null) mAdpter.removeHeaderView();
        ((NoteBookAdapter) recyclerView.getAdapter()).setNoteBooks(noteBookPresenter.getSearchNoteBook(notebookName));
    }




    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    private void updateUI() {
        noteBookSearchEditText.setText("");
        noteBookSearchEditText.clearFocus();
        noteBookSearchCancelTextView.setVisibility(View.GONE);
        mAdpter.setNoteBooks(noteBookPresenter.getAllNoteBook());
    }

    private void initView() {
        noteBookSearchEditText = getActivity().findViewById(R.id.edtTxt_noteMenu_search);
        noteBookSearchCancelTextView = getActivity().findViewById(R.id.textView_notebook_search_cancel);
        recyclerView = find(R.id.recyecle_notebook_notebooklist);
        mAdpter = new NoteBookAdapter(getActivity());
        recyclerView.setAdapter(mAdpter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        headerView= LayoutInflater.from(getActivity()).inflate(R.layout.adapter_notebook_header,recyclerView,false);
        mAdpter.setHeaderView(headerView);
        NoteBookItemDecoration itemDecoration = new NoteBookItemDecoration(getActivity(),mAdpter);
        recyclerView.addItemDecoration(itemDecoration);
        recyclerView.addOnItemTouchListener(new StickyRecyclerHeadersTouchListener(recyclerView,itemDecoration));
//        DefaultItemAnimator defaultItemAnimator = new DefaultItemAnimator();
//        defaultItemAnimator.setAddDuration(1000);
//        defaultItemAnimator.setRemoveDuration(1000);
//        recyclerView.setItemAnimator(defaultItemAnimator);
    }

    @Override
    public void lazyLoad() {
        noteBookPresenter = NoteBookPresenter.getInstance();
        mAdpter.setNoteBooks(noteBookPresenter.getAllNoteBook());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //unbinder.unbind(); 如果你用了需要destroy掉
    }
}
