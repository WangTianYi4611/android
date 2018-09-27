package com.example.tianyi.sensenote.dao;

import android.database.sqlite.SQLiteDatabase;

public abstract  class AbstractMigratorHelper {
    public abstract void onUpgrade(SQLiteDatabase db);
}
