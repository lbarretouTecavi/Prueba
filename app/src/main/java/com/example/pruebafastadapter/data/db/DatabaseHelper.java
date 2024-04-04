package com.example.pruebafastadapter.data.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.pruebafastadapter.data.db.dao.DaoMaster;
import com.example.pruebafastadapter.data.db.dao.DaoSession;
import com.example.pruebafastadapter.data.db.dao.TaskDao;

import org.greenrobot.greendao.database.Database;

public class DatabaseHelper extends DaoMaster.OpenHelper {
    private final static String DATABASE_NAME = "task-db";
    private DaoSession daoSession;

    public DatabaseHelper(Context context ) {
        super(context, DATABASE_NAME);
    }

    @Override
    public void onCreate(Database db) {
        super.onCreate(db);
    }

    @Override
    public void onUpgrade(Database db, int oldVersion, int newVersion) {
        super.onUpgrade(db, oldVersion, newVersion);
    }

    public DaoSession getDaoSession() {
        if (daoSession == null){
            SQLiteDatabase db = getWritableDatabase();
            DaoMaster daoMaster = new DaoMaster(db);
            daoSession = daoMaster.newSession();
        }
        return daoSession;
    }
}
