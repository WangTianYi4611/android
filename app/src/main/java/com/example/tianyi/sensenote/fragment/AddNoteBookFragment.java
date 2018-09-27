package com.example.tianyi.sensenote.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.tianyi.sensenote.R;
import com.example.tianyi.sensenote.exception.SenseNoteException;
import com.example.tianyi.sensenote.presenter.impl.NoteBookPresenter;
import com.example.tianyi.sensenote.presenter.interfaces.INoteBookPresenter;
import com.example.tianyi.sensenote.util.StringUtil;
import com.example.tianyi.sensenote.util.ToastUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class AddNoteBookFragment extends BaseFragment{
    @BindView(R.id.edtTxt_addNoteBook_name)
    public EditText noteBookNameEdtView;
    public TextView cancelTextView;
    public TextView createTextView;
    private Unbinder unbinder;
    private Context mContext;
    private INoteBookPresenter iNoteBookPresenter;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setHasOptionsMenu(true);
    }
//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        super.onCreateOptionsMenu(menu, inflater);
//        /setCustomActionBar();
//    }


    public static AddNoteBookFragment newInstance(){
        return new AddNoteBookFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mView == null) {
            // 需要inflate一个布局文件 填充Fragment
            mView = inflater.inflate(R.layout.fragment_add_note, container, false);
            init();
        }
        //缓存的mView需要判断是否已经被加过parent， 如果有parent需要从parent删除，要不然会发生这个mView已经有parent的错误。
        ViewGroup parent = (ViewGroup) mView.getParent();
        if (parent != null) {
            parent.removeView(mView);
        }
        unbinder = ButterKnife.bind(this, mView);

        return mView;
    }

    private void init() {
        mContext = getActivity();
        cancelTextView = getActivity().findViewById(R.id.txtView_addNoteMenu_cancel);
        createTextView = getActivity().findViewById(R.id.txtView_addNoteMenu_create);
        iNoteBookPresenter = NoteBookPresenter.getInstance();
        cancelTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
        createTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNoteBook();
            }
        });
    }

    private void createNoteBook() {
        String noteBookName = noteBookNameEdtView.getText().toString();
        if(!ToastUtil.checkNullThenToast(mContext,noteBookName,"笔记本名称不能为空")) return;
        try{
            iNoteBookPresenter.addNoteBook(noteBookName);
            getActivity().finish();
        }catch (SenseNoteException e){
            ToastUtil.toastMsgShort(mContext,e.getMessage());
        }
    }


    @Override
    public void lazyLoad() {

    }
}
