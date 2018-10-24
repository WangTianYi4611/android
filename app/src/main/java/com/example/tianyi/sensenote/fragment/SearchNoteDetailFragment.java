package com.example.tianyi.sensenote.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.example.tianyi.sensenote.R;
import com.example.tianyi.sensenote.activity.NoteDetailActivity;
import com.example.tianyi.sensenote.activity.NoteDetailListActivity;
import com.example.tianyi.sensenote.presenter.impl.RecentSearchPresenter;
import com.example.tianyi.sensenote.presenter.interfaces.IRecentSearchPresenter;
import com.example.tianyi.sensenote.util.DensityUtils;
import com.example.tianyi.sensenote.util.KeyBoardUtil;
import com.example.tianyi.sensenote.util.StringUtil;
import com.example.tianyi.sensenote.util.ToastUtil;
import com.example.tianyi.sensenote.viewgroup.TagLayout;

import java.util.Arrays;
import java.util.List;

public class SearchNoteDetailFragment extends BaseFragment{

    private TagLayout recentSearchTagLayout;
    private EditText noteBookDetailSearchEditText;
    private TextView noteBookDetailSearchCancelTextView;
    private IRecentSearchPresenter mRecentSearchPresenter;


    public static SearchNoteDetailFragment newInstance(){
        return new SearchNoteDetailFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Log.i("sensenote","searchnoteDetail oncreate");
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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i("sensenote","searchnoteDetail oncreateView");
        if (mView == null) {
            // 需要inflate一个布局文件 填充Fragment
            mView = inflater.inflate(R.layout.fragment_search_note, container, false);
            initView();
            initPresenter();
//        实现懒加载
            lazyLoad();
        }
        initLisenter();
        //缓存的mView需要判断是否已经被加过parent， 如果有parent需要从parent删除，要不然会发生这个mView已经有parent的错误。
        ViewGroup parent = (ViewGroup) mView.getParent();
        if (parent != null) {
            parent.removeView(mView);
        }
        return mView;
    }

    private void initView() {
        recentSearchTagLayout = find(R.id.tagLayout_search_note);
    }

    @Override
    public void onResume() {
        Log.i("sensenote","searchnoteDetail onResume");
        super.onResume();
        updateUI();
    }
    private void updateUI() {
        testTagLayout(mRecentSearchPresenter.getRecentSearchString(10));
    }

    private void initLisenter() {
        noteBookDetailSearchEditText = getActivity().findViewById(R.id.edtTxt_noteMenu_search);
        noteBookDetailSearchEditText.setHint(R.string.search_notebook_detail_hint);
        noteBookDetailSearchEditText.clearFocus();
        noteBookDetailSearchCancelTextView = getActivity().findViewById(R.id.textView_notebook_search_cancel);
        noteBookDetailSearchEditText.setOnFocusChangeListener(new View.OnFocusChangeListener(){
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                Log.i("sensenote","focus change");
                if(hasFocus){
                    noteBookDetailSearchCancelTextView.setVisibility(View.VISIBLE);
                }else{
                    noteBookDetailSearchCancelTextView.setVisibility(View.GONE);
                }
            }
        });
        noteBookDetailSearchEditText.setOnEditorActionListener(new TextView.OnEditorActionListener(){
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_DONE){
                    searchNoteBookDtail(v);
                    return true;
                }
                return false;
            }
        });
        noteBookDetailSearchCancelTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                noteBookDetailSearchEditText.setText("");
                noteBookDetailSearchEditText.clearFocus();
                KeyBoardUtil.closeKeybord(noteBookDetailSearchEditText,getActivity());
            }
        });
    }

    private void searchNoteBookDtail(TextView v) {
        String searchString = v.getText().toString();
        if(StringUtil.isEmpty(searchString)) return;
        mRecentSearchPresenter.insertRecentSearch(searchString);
        //ToastUtil.toastMsgShort(getContext(),v.getText().toString());
        Intent intent = NoteDetailListActivity.newIntent(getContext());
        intent.putExtra(NoteDetailListFragment.ARG_SEARCH_STRING,searchString);
        startActivity(intent);
    }

    private void testTagLayout(List<String> recentSearch) {
        recentSearchTagLayout.removeAllViews();
        for(String str : recentSearch){
            TextView textView = new TextView(getContext());
            ViewGroup.MarginLayoutParams marginLayoutParams = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
            int margin =  DensityUtils.dip2px(getContext(),8);
            int padding = DensityUtils.dip2px(getContext(),8);
            marginLayoutParams.setMargins(0,0,margin,margin);
            textView.setLayoutParams(marginLayoutParams);
            textView.setText(str);
            textView.setPadding(padding,padding,padding,padding);
            textView.setTextSize(15);
            textView.setBackgroundResource(R.drawable.round_corner_background);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ToastUtil.toastMsgShort(getContext(),((TextView) v).getText().toString());
                }
            });
//            int widthSpec = View.MeasureSpec.makeMeasureSpec(marginLayoutParams.width, View.MeasureSpec.AT_MOST);
//            int heightSpec = View.MeasureSpec.makeMeasureSpec(marginLayoutParams.height, View.MeasureSpec.AT_MOST);
            recentSearchTagLayout.addView(textView);
        }

    }

    private void initPresenter(){
        mRecentSearchPresenter = RecentSearchPresenter.getInstance();
    }

    @Override
    public void lazyLoad() {
        testTagLayout(mRecentSearchPresenter.getRecentSearchString(10));
    }
}
