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

    @Override
    protected Fragment createFragment() {
        return NoteDetailFragment.newInstance(null,null);
    }

    public static Intent newIntent(Context context){
        return new Intent(context,NoteDetailActivity.class);
    }

    public void setChooseNoteBookFragement(String noteBookName){
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.fragment_container, NoteBookChooseFragment.newInstance(noteBookName));
        //transaction.addToBackStack(null);
        transaction.commit();
    }

    public void backToNoteDetailFragement(NoteBookBean noteBookBean){
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.fragment_container, NoteDetailFragment.newInstance(noteBookBean,null));
        //transaction.addToBackStack(null);
        transaction.commit();
    }

}
