package com.example.tianyi.sensenote.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.tianyi.sensenote.dao.AbstractMigratorHelper;
import com.example.tianyi.sensenote.dao.DaoMaster;
import com.example.tianyi.sensenote.dao.DetailRecentSearchEntityDao;
import com.example.tianyi.sensenote.dao.MigrationHelper;
import com.example.tianyi.sensenote.dao.NoteBookDetailEntityDao;
import com.example.tianyi.sensenote.dao.NoteBookEntityDao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;

import java.util.Date;
import org.greenrobot.greendao.annotation.Generated;


public class MySQLiteOpenHelper  extends DaoMaster.OpenHelper {

    public MySQLiteOpenHelper (Context context, String name, SQLiteDatabase.CursorFactory factory) {
        super(context, name, factory);
    }

    /**
     * Here is where the calls to upgrade are executed
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        MigrationHelper.migrate(db, DetailRecentSearchEntityDao.class,NoteBookDetailEntityDao.class, NoteBookEntityDao.class);
    }
}