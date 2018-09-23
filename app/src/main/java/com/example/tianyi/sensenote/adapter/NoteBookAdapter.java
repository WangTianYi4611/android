package com.example.tianyi.sensenote.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tianyi.sensenote.R;
import com.example.tianyi.sensenote.activity.AddNoteActivity;
import com.example.tianyi.sensenote.fragment.AddNoteBookFragment;
import com.example.tianyi.sensenote.fragment.LoginFragment;

import org.w3c.dom.Text;

import java.util.List;

public class NoteBookAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_FOOTER = 1;
    private static final int TYPE_NORMAL = 2;
    private List<String> items;
    private Context context;
    private View headerView;



    public NoteBookAdapter(List<String> items,Context context) {
        this.items = items;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (headerView != null && viewType == TYPE_HEADER) {
            return new NoteBookHeadViewHolder(headerView);
        }
        NoteBookItemViewHolder holder = new NoteBookItemViewHolder(LayoutInflater.from(context).
                inflate(R.layout.adapter_notebook_item, parent, false));
        return holder;
    }
    //获取每条数据属于哪一分组
    public int getHeaderId(int position) {
        return 1;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        if(getItemViewType(position)  == TYPE_NORMAL) {
            ((NoteBookItemViewHolder) viewHolder).setNoteBookTitile(items.get(position - 1));
        }
    }

    public int getRealPosition(int position){
        return headerView != null ? position - 1: position;
    }


    public boolean hasHeader(int pos) {
        if (pos == 1) {
            return true;
        } else {
            return false;
        }
    }


    @Override
    public int getItemViewType(int position) {
        if (headerView == null ) {
            return TYPE_NORMAL;
        }
        if (position == 0) {
            //第一个item应该加载Header
            return TYPE_HEADER;
        }
        return TYPE_NORMAL;
    }

    public NoteBookStrickyHeadViewHolder createStrickyHeader(ViewGroup parent){
        return new NoteBookStrickyHeadViewHolder(LayoutInflater.from(context).inflate(R.layout.adapter_notebook_stricky_header, parent, false));
    }


    @Override
    public int getItemCount() {
        return (headerView == null) ? items.size() : items.size() + 1;
    }

    public class NoteBookItemViewHolder extends RecyclerView.ViewHolder{

        private TextView noteBookTextView;

        public NoteBookItemViewHolder(@NonNull View itemView) {
            super(itemView);
            noteBookTextView = itemView.findViewById(R.id.textView_notebook_item);
            noteBookTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, "note item click", Toast.LENGTH_SHORT).show();
                }
            });
        }

        public void setNoteBookTitile(String title){
            noteBookTextView.setText(title);
        }
    }


    public class NoteBookHeadViewHolder extends  RecyclerView.ViewHolder{

        private TextView allNoteTextView;

        public NoteBookHeadViewHolder(@NonNull View itemView) {
            super(itemView);
            allNoteTextView = itemView.findViewById(R.id.textView_notebook_allnote);
            allNoteTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, "all note click", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public class NoteBookStrickyHeadViewHolder extends RecyclerView.ViewHolder{
        private ImageView addNoteImgView;
        public NoteBookStrickyHeadViewHolder(@NonNull View itemView) {
            super(itemView);
            addNoteImgView = itemView.findViewById(R.id.imgView_notebook_addnotebook);
            initView();

        }
        private void initView() {
            addNoteImgView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    jumpToAddNoteActivity();
                }
            });
        }

        public void jumpToAddNoteActivity(){
            Intent intent = AddNoteActivity.newIntent(context);
            context.startActivity(intent);
        }
    }

    public View getHeaderView() {
        return headerView;
    }

    public void setHeaderView(View headerView) {
        this.headerView=headerView;
        notifyItemInserted(0);

    }




}
