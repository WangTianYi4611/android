package com.example.tianyi.sensenote.application;

import android.app.Application;
import android.app.IntentService;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;


import com.example.tianyi.sensenote.bean.UserBean;
import com.example.tianyi.sensenote.dao.DaoMaster;
import com.example.tianyi.sensenote.dao.DaoSession;
import com.example.tianyi.sensenote.dao.MySQLiteOpenHelper;
import com.example.tianyi.sensenote.dao.NoteBookEntityDao;
import com.example.tianyi.sensenote.httpservice.RetrofitClient;
import com.example.tianyi.sensenote.service.InitializeService;
import com.example.tianyi.sensenote.util.ImageLoader;
import com.example.tianyi.sensenote.util.SaveToLocalUtil;

public class SenseNoteApplication extends Application {

    private static SenseNoteApplication instance;

    private DaoSession mDaoSession;

    private UserBean userBean;

    private ImageLoader mImageLoader;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        initGreenDao();
        InitializeService.start(this);
        mImageLoader = ImageLoader.build(this);
    }

    //这块的东西其实可以搞一个单例模式
    private void initGreenDao() {
        MySQLiteOpenHelper helper = new MySQLiteOpenHelper(this, "sensenote-db", null);
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        mDaoSession = daoMaster.newSession();
    }

    public DaoSession getDaoSession(){
        return mDaoSession;
    }

    public UserBean getUserBean() {
        if(userBean == null){
            userBean = (UserBean) SaveToLocalUtil.restoreObject(this.getFilesDir().getPath().toString()+SaveToLocalUtil.USER_TOKEN_FILE);
        }
        return userBean;
    }

    public void setUserBean(UserBean userBean){
        this.userBean = userBean;
        RetrofitClient.rebuild();
    }

    public static SenseNoteApplication getInstance(){
        return instance;
    }


    public ImageLoader getmImageLoader() {
        return mImageLoader;
    }
}
