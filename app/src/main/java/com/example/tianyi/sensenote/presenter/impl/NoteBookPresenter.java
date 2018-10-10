package com.example.tianyi.sensenote.presenter.impl;

import android.content.Context;
import android.provider.ContactsContract;
import android.util.Log;

import com.example.tianyi.sensenote.application.SenseNoteApplication;
import com.example.tianyi.sensenote.bean.NoteBookBean;
import com.example.tianyi.sensenote.dao.NoteBookEntityDao;
import com.example.tianyi.sensenote.dao.entity.NoteBookEntity;
import com.example.tianyi.sensenote.exception.SenseNoteException;
import com.example.tianyi.sensenote.presenter.interfaces.INoteBookPresenter;
import com.example.tianyi.sensenote.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;


public class NoteBookPresenter implements INoteBookPresenter {

    private static NoteBookPresenter instance = new NoteBookPresenter();

    private NoteBookEntityDao noteBookEntityDao;

    private Context mContext;

    private List<NoteBookBean> allNoteBooks;


    public NoteBookPresenter() {
        this.mContext = SenseNoteApplication.getInstance().getApplicationContext();
        this.noteBookEntityDao = ((SenseNoteApplication) mContext).getDaoSession().getNoteBookEntityDao();

    }

    public static NoteBookPresenter getInstance(){
        return instance;
    }

    @Override
    public boolean addNoteBook(String noteBookName) {

        if(queryNoteBookByName(noteBookName) != 0){
            throw new SenseNoteException("笔记本名称已存在");
        }

        Date createTime = new Date();
        NoteBookEntity noteBookEntity = new NoteBookEntity();
        noteBookEntity.setCreateTime(createTime);
        noteBookEntity.setUpdateTime(createTime);
        noteBookEntity.setNoteBookName(noteBookName);
        noteBookEntityDao.insert(noteBookEntity);
        Log.i("database","insert success");
        if(allNoteBooks == null){
            allNoteBooks = getAllNoteBook(true);
        }
        allNoteBooks.add(allNoteBooks.size() -1 ,convertEntityToBean(noteBookEntity));
        //Log.i("database","cur note book :" + noteBookEntityDao.queryBuilder().list().toString());
        return true;
    }

    @Override
    public void deleteNoteBook(Long id) {
        noteBookEntityDao.deleteByKey(id);
        Iterator<NoteBookBean> iter = allNoteBooks.iterator();
        while(iter.hasNext()){
            if(iter.next().getId() == id){
                iter.remove();
                break;
            }
        }
    }

    @Override
    public List<NoteBookBean> getAllNoteBook(boolean refresh) {
        if(!refresh && !CollectionUtils.isEmpty(allNoteBooks)) return allNoteBooks;
        allNoteBooks = new ArrayList<>();
        List<NoteBookEntity> allNoteBookEntites = noteBookEntityDao.loadAll();
        for(NoteBookEntity noteBookEntity:allNoteBookEntites){
            allNoteBooks.add(convertEntityToBean(noteBookEntity));
        }
        NoteBookBean noteBookBean = new NoteBookBean();
        noteBookBean.setNoteBookName("废纸篓");
        noteBookBean.setCount(0);
        allNoteBooks.add(noteBookBean);
        return allNoteBooks;
    }

    @Override
    public List<NoteBookBean> getChooseNoteBook() {
        return new ArrayList<>(allNoteBooks.subList(0,allNoteBooks.size() - 1));
    }

    public void refreshAllNoteBook(){

    }

    @Override
    public List<NoteBookBean> getSearchNoteBook(final String noteBookName) {
        List<NoteBookBean> searchNoteBooks = new ArrayList<>();
        for(NoteBookBean noteBook:allNoteBooks){
            if(noteBook.getNoteBookName().contains(noteBookName)){
                searchNoteBooks.add(noteBook);
            }
        }
        return searchNoteBooks;
    }

    @Override
    public NoteBookBean getDefaultNoteBook() {
        return allNoteBooks.get(0);
    }

    private NoteBookBean convertEntityToBean(NoteBookEntity noteBookEntity) {
        return new NoteBookBean(noteBookEntity.getId(),noteBookEntity.getNoteBookName(),noteBookEntity.getCount());
    }

    private long queryNoteBookByName(String noteBookName){

        long count = noteBookEntityDao.queryBuilder().
                where(NoteBookEntityDao.Properties.NoteBookName.eq(noteBookName)).count();

        return count;
    }

}
