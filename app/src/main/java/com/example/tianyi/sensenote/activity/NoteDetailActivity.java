package com.example.tianyi.sensenote.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.example.tianyi.sensenote.R;
import com.example.tianyi.sensenote.bean.NoteBookBean;
import com.example.tianyi.sensenote.fragment.NoteBookChooseFragment;
import com.example.tianyi.sensenote.fragment.NoteDetailFragment;
import com.example.tianyi.sensenote.fragment.RegisterFragment;

public class NoteDetailActivity extends SingleFragmentActivity{

    private NoteDetailFragment mNoteDetailFragment;
    private NoteBookChooseFragment mNoteBookChooseFragment;
    public static String ARG_INTENT_NOTEBOOK = "notebook";
    public static String ARG_INTENT_NOTEBOOK_DETAIL_ID ="notebookDetailId";


    @Override
    protected Fragment createFragment() {
        Intent intent = getIntent();
        NoteBookBean noteBookBean = (NoteBookBean) intent.getSerializableExtra(ARG_INTENT_NOTEBOOK);
        Long noteBookDetailId = intent.getLongExtra(ARG_INTENT_NOTEBOOK_DETAIL_ID,-1);
        if(noteBookDetailId == -1) noteBookDetailId = null;
        mNoteDetailFragment = NoteDetailFragment.newInstance(noteBookBean,noteBookDetailId);
        return mNoteDetailFragment;
    }

    public static Intent newIntent(Context context){
        return new Intent(context,NoteDetailActivity.class);
    }

    public void setChooseNoteBookFragement(String noteBookName){
        mNoteBookChooseFragment = NoteBookChooseFragment.newInstance(noteBookName);
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.hide(mNoteDetailFragment);
        transaction.add(R.id.fragment_container,mNoteBookChooseFragment);
        //transaction.replace(R.id.fragment_container, NoteBookChooseFragment.newInstance(noteBookName));
        //transaction.addToBackStack(null);
        transaction.commit();
    }

    public void backToNoteDetailFragement(NoteBookBean noteBookBean){
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.hide(mNoteBookChooseFragment);
        transaction.show(mNoteDetailFragment);
        mNoteDetailFragment.setNoteBookBean(noteBookBean);
        //transaction.add(R.id.fragment_container, NoteDetailFragment.newInstance(noteBookBean,null));
        //transaction.addToBackStack(null);
        transaction.commit();
    }

}
