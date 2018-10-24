package com.example.tianyi.sensenote.presenter.impl;

import android.content.Context;
import android.util.Log;

import com.example.tianyi.sensenote.application.SenseNoteApplication;
import com.example.tianyi.sensenote.dao.DetailRecentSearchEntityDao;
import com.example.tianyi.sensenote.dao.entity.DetailRecentSearchEntity;
import com.example.tianyi.sensenote.presenter.interfaces.IRecentSearchPresenter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RecentSearchPresenter implements IRecentSearchPresenter{

    private static RecentSearchPresenter instance = new RecentSearchPresenter();

    private Context mContext;
    private DetailRecentSearchEntityDao recentSearchDao;

    private RecentSearchPresenter(){
        this.mContext = SenseNoteApplication.getInstance().getApplicationContext();
        this.recentSearchDao = ((SenseNoteApplication) mContext).getDaoSession().getDetailRecentSearchEntityDao();
    }

    public static RecentSearchPresenter getInstance(){
        return instance;
    }

    @Override
    public boolean insertRecentSearch(String searchString) {
        DetailRecentSearchEntity entity = new DetailRecentSearchEntity();

        Date date = new Date();
        entity.setSearchString(searchString);
        entity.setCreateTime(date);
        entity.setUpdateTime(date);
        entity.setIsDeleted(0);
        recentSearchDao.insert(entity);
        Log.i("sensenote","insert success");
        return true;
    }

    @Override
    public List<String> getRecentSearchString(Integer num) {
        List<DetailRecentSearchEntity> searchEntities = recentSearchDao.loadAll();
        List<String> result = new ArrayList<>();
        for(DetailRecentSearchEntity entity : searchEntities){
            result.add(entity.getSearchString());
        }
        if(result.size() > num){
            result = result.subList(0,num);
        }
        Log.i("sensenote","search " + result.toString());
        return result;
    }
}
