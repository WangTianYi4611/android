package com.example.tianyi.sensenote.presenter.impl;

import android.content.Context;
import android.os.HandlerThread;
import android.util.Log;

import com.example.tianyi.sensenote.application.SenseNoteApplication;
import com.example.tianyi.sensenote.bean.NoteBookDetailBean;
import com.example.tianyi.sensenote.dao.DaoSession;
import com.example.tianyi.sensenote.dao.NoteBookDetailEntityDao;
import com.example.tianyi.sensenote.dao.NoteBookEntityDao;
import com.example.tianyi.sensenote.dao.entity.NoteBookDetailEntity;
import com.example.tianyi.sensenote.dao.entity.NoteBookEntity;
import com.example.tianyi.sensenote.presenter.interfaces.INoteBookDetailPresenter;
import com.example.tianyi.sensenote.presenter.interfaces.INoteBookPresenter;
import com.example.tianyi.sensenote.util.StringUtil;
import com.sendtion.xrichtext.RichTextEditor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.sendtion.xrichtext.RichTextEditor.*;

public class NoteBookDetailPresenter implements INoteBookDetailPresenter{

    private Context mContext;

    private NoteBookDetailEntityDao noteBookDetailEntityDao;

    private NoteBookEntityDao noteBookEntityDao;

    private DaoSession mDaoSession;

    private INoteBookPresenter noteBookPresenter;


    public NoteBookDetailPresenter(Context context){
        this.mContext = context;
        this.mDaoSession = ((SenseNoteApplication) context.getApplicationContext()).getDaoSession();
        this.noteBookDetailEntityDao = mDaoSession.getNoteBookDetailEntityDao();
        this.noteBookEntityDao = mDaoSession.getNoteBookEntityDao();
        this.noteBookPresenter = NoteBookPresenter.getInstance();
    }




    @Override
    public boolean addNoteBookDetail(List<RichTextEditor.EditData> editData, String title, final Long noteBookId) {

        final NoteBookDetailEntity noteBookDetailEntity = new NoteBookDetailEntity();
        Date date = new Date();
        noteBookDetailEntity.setContent(serilizeContent(editData));
        noteBookDetailEntity.setNoteBookId(noteBookId);
        noteBookDetailEntity.setNoteBookDetailTitle(title);
        noteBookDetailEntity.setCreateTime(date);
        noteBookDetailEntity.setUpdateTime(date);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                noteBookDetailEntityDao.insert(noteBookDetailEntity);
                NoteBookEntity noteBook  = noteBookEntityDao.loadByRowId(noteBookId);
                noteBook.setCount(noteBook.getCount() + 1);
                noteBookEntityDao.update(noteBook);
            }
        };
        try {
            mDaoSession.runInTx(runnable);
        } catch (Exception e) {
            Log.i("sensenote",e.toString());
            return false;
        }

        Log.d("sensenote", noteBookDetailEntityDao.loadAll().toString());

        return true;
    }

    @Override
    public List<NoteBookDetailBean> getAllNoteBookDetails() {
        List<NoteBookDetailBean> result = new ArrayList<>();
        List<NoteBookDetailEntity> noteBookDetailEntities = noteBookDetailEntityDao.loadAll();
        Collections.sort(noteBookDetailEntities, new Comparator<NoteBookDetailEntity>() {
            @Override
            public int compare(NoteBookDetailEntity o1, NoteBookDetailEntity o2) {
                return (int) (o2.getUpdateTime().getTime() - o1.getUpdateTime().getTime());
            }
        });
        for(NoteBookDetailEntity noteBookEntity : noteBookDetailEntities){
            NoteBookDetailBean noteBookDetailBean = new NoteBookDetailBean();
            noteBookDetailBean.setNoteBookDetailTitle(noteBookEntity.getNoteBookDetailTitle());
            noteBookDetailBean.setId(noteBookEntity.getId());
            noteBookDetailBean.setCreateTime(noteBookEntity.getCreateTime());
            noteBookDetailBean.setNoteBookId(noteBookEntity.getNoteBookId());
            noteBookDetailBean.setContent(deseriliaze(noteBookEntity.getContent()));
            result.add(noteBookDetailBean);
        }
        return result;
    }

    public List<NoteBookDetailBean> getNoteBookDetailsById(Long noteBookId){
        List<NoteBookDetailEntity> noteBookDetailEntities = noteBookDetailEntityDao.queryBuilder().
                where(NoteBookDetailEntityDao.Properties.NoteBookId.eq(noteBookId)).build().list();
        Collections.sort(noteBookDetailEntities, new Comparator<NoteBookDetailEntity>() {
            @Override
            public int compare(NoteBookDetailEntity o1, NoteBookDetailEntity o2) {
                return (int) (o2.getUpdateTime().getTime() - o1.getUpdateTime().getTime());
            }
        });
        List<NoteBookDetailBean> result = new ArrayList<>();
        for(NoteBookDetailEntity noteBookDetailEntity : noteBookDetailEntities){
            result.add(convertNoteBookDetailEntityToBean(noteBookDetailEntity));
        }
        return result;
    }

    @Override
    public NoteBookDetailBean getNoteBookDetailByDetailId(Long noteBookDetailId) {
        NoteBookDetailEntity noteBookDetailEntity = noteBookDetailEntityDao.load(noteBookDetailId);
        return convertNoteBookDetailEntityToBean(noteBookDetailEntity);
    }

    @Override
    public boolean updateNoteBookDetail(List<RichTextEditor.EditData> editData, String title, Long noteBookId, Long noteBookDetailId) {
        try {
            NoteBookDetailEntity originDetail = noteBookDetailEntityDao.load(noteBookDetailId);
            NoteBookDetailEntity noteBookDetailEntity = new NoteBookDetailEntity();
            Date date = new Date();
            noteBookDetailEntity.setId(noteBookDetailId);
            noteBookDetailEntity.setContent(serilizeContent(editData));
            noteBookDetailEntity.setNoteBookId(noteBookId);
            noteBookDetailEntity.setNoteBookDetailTitle(title);
            noteBookDetailEntity.setCreateTime(originDetail.getCreateTime());
            noteBookDetailEntity.setUpdateTime(date);
            noteBookDetailEntityDao.update(noteBookDetailEntity);
        }catch (Exception e){
            return false;
        }
        return true;
    }

    @Override
    public Integer getNoteBookDetailsCount() {
        return (int) noteBookDetailEntityDao.count();
    }

    @Override
    public List<NoteBookDetailBean> getNoteBookDetailsBySearchString(String searchString, int from, int size) {
        List<NoteBookDetailEntity> entities = noteBookDetailEntityDao.queryBuilder().offset(from).limit(size).list();
        List<NoteBookDetailBean> result = new ArrayList<>();
        for(NoteBookDetailEntity entity:entities){
            if(entity.getContent().contains(searchString)){
                result.add(convertNoteBookDetailEntityToBean(entity));
            }
        }
        return result;
    }


    public NoteBookDetailBean convertNoteBookDetailEntityToBean(NoteBookDetailEntity noteBookDetailEntity){
        NoteBookDetailBean noteBookDetailBean = new NoteBookDetailBean();
        noteBookDetailBean.setId(noteBookDetailEntity.getId());
        noteBookDetailBean.setCreateTime(noteBookDetailEntity.getCreateTime());
        if(noteBookDetailBean.getCreateTime() == null) noteBookDetailBean.setCreateTime(new Date());
        noteBookDetailBean.setNoteBookId(noteBookDetailEntity.getNoteBookId());
        noteBookDetailBean.setNoteBookDetailTitle(noteBookDetailEntity.getNoteBookDetailTitle());
        noteBookDetailBean.setContent(deseriliaze(noteBookDetailEntity.getContent()));
        noteBookDetailBean.setNoteBookBean(noteBookPresenter.getNoteBookById(noteBookDetailEntity.getNoteBookId()));

        return noteBookDetailBean;
    }


    private List<String> deseriliaze(String content) {
        List<String> editDatas = curWordsByImgTag(content);
        List<String> result = new ArrayList<>();
        for(String editData:editDatas){
//               if(editData.startsWith("<img")){
//                   result.add(editData);
//               }else{
                   result.add(editData);
   //            }
        }
        return result;
    }

    public static String getImg(String content) {
        String str_src = null;
        //目前img标签标示有3种表达式
        //<img alt="" src="1.jpg"/>   <img alt="" src="1.jpg"></img>     <img alt="" src="1.jpg">
        //开始匹配content中的<img />标签
        Pattern p_img = Pattern.compile("<(img|IMG)(.*?)(/>|></img>|>)");
        Matcher m_img = p_img.matcher(content);
        boolean result_img = m_img.find();
        if (result_img) {
            while (result_img) {
                //获取到匹配的<img />标签中的内容
                String str_img = m_img.group(2);

                //开始匹配<img />标签中的src
                Pattern p_src = Pattern.compile("(src|SRC)=(\"|\')(.*?)(\"|\')");
                Matcher m_src = p_src.matcher(str_img);
                if (m_src.find()) {
                    str_src = m_src.group(3);
                }
                //结束匹配<img />标签中的src

                //匹配content中是否存在下一个<img />标签，有则继续以上步骤匹配<img />标签中的src
                result_img = m_img.find();
            }
        }
        return str_src;
    }

    private List<String> curWordsByImgTag(String targetStr) {
        List<String> splitTextList = new ArrayList<String>();
        Pattern pattern = Pattern.compile("<img.*?src=\\\"(.*?)\\\".*?>");
        Matcher matcher = pattern.matcher(targetStr);
        int lastIndex = 0;
        while (matcher.find()) {
            if (matcher.start() > lastIndex) {
                splitTextList.add(targetStr.substring(lastIndex, matcher.start()));
            }
            splitTextList.add(targetStr.substring(matcher.start(), matcher.end()));
            lastIndex = matcher.end();
        }
        if (lastIndex != targetStr.length()) {
            splitTextList.add(targetStr.substring(lastIndex, targetStr.length()));
        }
        return splitTextList;
    }

    private String serilizeContent(List<RichTextEditor.EditData> editData) {
        StringBuffer content = new StringBuffer();
        for (RichTextEditor.EditData itemData : editData) {
            if (itemData.inputStr != null) {
                content.append(itemData.inputStr);
            } else if (itemData.imagePath != null) {
                content.append("<img src=\"").append(itemData.imagePath).append("\"/>");
            }
        }
        return content.toString();
    }
}
