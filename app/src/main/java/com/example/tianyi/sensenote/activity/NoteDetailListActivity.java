package com.example.tianyi.sensenote.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.example.tianyi.sensenote.bean.NoteBookBean;
import com.example.tianyi.sensenote.fragment.NoteDetailListFragment;

public class NoteDetailListActivity extends SingleFragmentActivity{


    @Override
    protected Fragment createFragment() {
        Intent intent = getIntent();
        String searchString = intent.getStringExtra(NoteDetailListFragment.ARG_SEARCH_STRING);
        NoteBookBean noteBookBean = (NoteBookBean) intent.getSerializableExtra(NoteDetailListFragment.ARG_NOTE_BOOK);
        return NoteDetailListFragment.newInstance(noteBookBean,searchString);
    }
    public static Intent newIntent(Context context){
        return new Intent(context,NoteDetailListActivity.class);
    }
}
