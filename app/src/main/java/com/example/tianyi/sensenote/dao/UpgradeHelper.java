package com.example.tianyi.sensenote.dao.entity;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.tianyi.sensenote.dao.AbstractMigratorHelper;
import com.example.tianyi.sensenote.dao.DaoMaster;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;

import java.util.Date;
import org.greenrobot.greendao.annotation.Generated;


public class UpgradeHelper extends DaoMaster.OpenHelper {

    public UpgradeHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
        super(context, name, factory);
    }

    /**
     * Here is where the calls to upgrade are executed
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        /* i represent the version where the user is now and the class named with this number implies that is upgrading from i to i++ schema */
        for (int i = oldVersion; i < newVersion; i++) {
            try {
                /* New instance of the class that migrates from i version to i++ version named DBMigratorHelper{version that the db has on this moment} */
                AbstractMigratorHelper migratorHelper = (AbstractMigratorHelper) Class.forName("com.nameofyourpackage.persistence.MigrationHelpers.DBMigrationHelper" + i).newInstance();

                if (migratorHelper != null) {

                    /* Upgrade de db */
                    migratorHelper.onUpgrade(db);
                }

            } catch (ClassNotFoundException | ClassCastException | IllegalAccessException | InstantiationException e) {

                Log.e("database", "Could not migrate from schema from schema: " + i + " to " + i++);
                /* If something fail prevent the DB to be updated to future version if the previous version has not been upgraded successfully */
                break;
            }


        }
    }
}