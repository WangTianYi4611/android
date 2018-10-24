package com.example.tianyi.sensenote.adapter;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tianyi.sensenote.R;
import com.example.tianyi.sensenote.activity.NoteDetailActivity;
import com.example.tianyi.sensenote.application.SenseNoteApplication;
import com.example.tianyi.sensenote.bean.NoteBookBean;
import com.example.tianyi.sensenote.bean.NoteBookDetailBean;
import com.example.tianyi.sensenote.presenter.impl.NoteBookDetailPresenter;
import com.example.tianyi.sensenote.util.DateUtil;
import com.example.tianyi.sensenote.util.DensityUtils;
import com.example.tianyi.sensenote.util.ImageLoader;
import com.example.tianyi.sensenote.util.StringUtil;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NoteBookDetailAdatper extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int EMPTY_VIEW_TYPE = 99;
    //private static final int EMPTY_VIEW_TYPE = 99;
    private List<NoteBookDetailBean> noteBookDetails;
    private Context mContext;
    private Map<Integer,Integer> headerIdMap;
    private ImageLoader mImageLoder;
    private boolean isIdleState;


    public NoteBookDetailAdatper(Context mContext) {
        this.mContext = mContext;
        noteBookDetails = new ArrayList<>();
        headerIdMap = new HashMap<>();
        this.mImageLoder = ((SenseNoteApplication) mContext.getApplicationContext()).getmImageLoader();
        this.isIdleState = true;
    }



    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        RecyclerView.ViewHolder itemHolder = null;
        if(noteBookDetails.size() == 0){
            itemHolder = new NoteBookDetailEmptyViewHolder(LayoutInflater.from(mContext).
                    inflate(R.layout.adapter_emty_textview,viewGroup,false));
        }else {
           itemHolder = new NoteBookDetailViewHolder(LayoutInflater.from(mContext)
                    .inflate(R.layout.adapter_notebookdetail_item, viewGroup, false));
        }
        return itemHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        if(noteBookDetails.size() == 0) return;
        ((NoteBookDetailViewHolder)viewHolder).setNoteBookDetailBean(noteBookDetails.get(position));
    }

    public void setIsIdleState(boolean isIdleState) {
        this.isIdleState = isIdleState;
    }

    public class NoteBookDetailEmptyViewHolder extends RecyclerView.ViewHolder{

        ImageView emptyImageView;
        TextView emptyTextView;

        public NoteBookDetailEmptyViewHolder(@NonNull View itemView) {
            super(itemView);
            emptyImageView = itemView.findViewById(R.id.imgview_empty_view);
            emptyTextView = itemView.findViewById(R.id.textView_empty_text);
            emptyTextView.setText("暂无笔记");
        }
    }

    public NoteBookDetailStickyHeaderHolder createStikcyHeaderHolder(@NonNull ViewGroup viewGroup){
        return new NoteBookDetailStickyHeaderHolder(LayoutInflater.from(mContext)
                .inflate(R.layout.adapter_notebookdetail_sticky_header,viewGroup,false));
    }

    public boolean hasHeader(int position){
        if(noteBookDetails.size() == 0) return false;
        if(position == 0) return true;
        String date = DateUtil.dateFormat(noteBookDetails.get(position).getCreateTime(),DateUtil.DATE_PATTERN);
        String dateBefore = DateUtil.dateFormat(noteBookDetails.get(position - 1).getCreateTime(),DateUtil.DATE_PATTERN);
        if(!date.equals(dateBefore)) return true;
        return false;
    }

    public int getHeaderId(int position){
        return headerIdMap.get(position);
    }

    public void onBindStickyHeaderViewHolder(NoteBookDetailStickyHeaderHolder viewHolder, int position){
        viewHolder.setNoteBookDetailDate(noteBookDetails.get(position).getCreateTime());
    }

    public class NoteBookDetailStickyHeaderHolder extends  RecyclerView.ViewHolder{

        TextView noteBookDetailDateTextView;

        public NoteBookDetailStickyHeaderHolder(@NonNull View itemView) {
            super(itemView);
            noteBookDetailDateTextView = itemView.findViewById(R.id.textView_notebookdetaillist_stickyheader_date);
        }

        public void setNoteBookDetailDate(Date date){
            noteBookDetailDateTextView.setText(DateUtil.dateFormat(date, DateUtil.DATE_PATTERN));
        }
    }


    public class NoteBookDetailViewHolder extends RecyclerView.ViewHolder{

        TextView noteBookDetailTitle;
        TextView noteBookDetailContent;
        ImageView noteBookDetailImg;
        TextView noteBookDetailDate;
        View itemView;
        NoteBookDetailBean noteBookDetailBean;


        public NoteBookDetailViewHolder(@NonNull View itemView) {
            super(itemView);
            initView(itemView);
        }

        private void initView(View itemView) {
            noteBookDetailTitle = itemView.findViewById(R.id.textView_notebookdetaillist_title);
            noteBookDetailContent = itemView.findViewById(R.id.textView_notebookdetaillist_content);
            noteBookDetailDate = itemView.findViewById(R.id.textView_notebookdetaillist_date);
            noteBookDetailImg = itemView.findViewById(R.id.imgView_notebookdetaillist_pic);
            this.itemView = itemView;
            this.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = NoteDetailActivity.newIntent(mContext);
                    intent.putExtra(NoteDetailActivity.ARG_INTENT_NOTEBOOK,noteBookDetailBean.getNoteBookBean());
                    intent.putExtra(NoteDetailActivity.ARG_INTENT_NOTEBOOK_DETAIL_ID,noteBookDetailBean.getId());
                    mContext.startActivity(intent);
                }
            });
        }


        public void setNoteBookDetailBean(NoteBookDetailBean noteBookDetailBean){
            this.noteBookDetailBean = noteBookDetailBean;
            noteBookDetailTitle.setText(noteBookDetailBean.getNoteBookDetailTitle());
            noteBookDetailContent.setText(getSummary(noteBookDetailBean.getContent()));
            //Log.i("sensenote",));
            noteBookDetailDate.setText(DateUtil.dateFormat(noteBookDetailBean.getCreateTime(),DateUtil.DATE_PATTERN));
            String imagePath = getImage(noteBookDetailBean.getContent());
            if(!StringUtil.isEmpty(imagePath)){
                //noteBookDetailImg.setVisibility(View.VISIBLE);
                Log.i("sensenote","image loader loading");
                mImageLoder.bindBitmap(getImage(noteBookDetailBean.getContent()),noteBookDetailImg,noteBookDetailContent.getWidth(),
                        DensityUtils.dip2px(mContext,200));
            }else if(StringUtil.isEmpty(imagePath)){
                noteBookDetailImg.setVisibility(View.GONE);
            }

        }

        public String getSummary(List<String> contents){
            for(String content:contents){
                if(!content.startsWith("<img")){
                    return content;
                }
            }
            return "";
        }
        public String getImage(List<String> contents){
            for(String content:contents){
                if(content.startsWith("<img")){
                    return NoteBookDetailPresenter.getImg(content);
                }
            }
            return "";
        }

    }





    @Override
    public int getItemViewType(int position) {
        if(noteBookDetails.size() <= 0){
            return EMPTY_VIEW_TYPE;
        }
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        return noteBookDetails.size() == 0 ? 1 : noteBookDetails.size();
    }

    public void setNoteBookDetails(List<NoteBookDetailBean> noteBookDetails) {
        this.noteBookDetails = noteBookDetails;
        constructHeaderIdMap(noteBookDetails);
        notifyDataSetChanged();
    }

    public void addNoteBookDetailsToTail(List<NoteBookDetailBean> noteBookDetails){
        int oldCount = this.noteBookDetails.size();
        this.noteBookDetails.addAll(noteBookDetails);
        notifyDataSetChanged();
        //notifyItemRangeInserted(oldCount,noteBookDetails.size());
    }


    private void constructHeaderIdMap(List<NoteBookDetailBean> noteBookDetails) {
        if(noteBookDetails.size() == 0) return;
        headerIdMap.clear();
        int id = 1;
        String preDateStr= DateUtil.dateFormat(noteBookDetails.get(0).getCreateTime(),DateUtil.DATE_PATTERN);
        headerIdMap.put(0,id);
        for(int i = 1; i < noteBookDetails.size();i++){
            String dateStr = DateUtil.dateFormat(noteBookDetails.get(i).getCreateTime(),DateUtil.DATE_PATTERN);
            if(dateStr.equals(preDateStr)){
                headerIdMap.put(i,id);
            }else{
                preDateStr = dateStr;
                headerIdMap.put(i,++id);
            }
        }
        Log.i("sensenote",headerIdMap.toString());
    }
}
