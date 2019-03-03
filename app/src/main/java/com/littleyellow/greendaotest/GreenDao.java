package com.littleyellow.greendaotest;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.littleyellow.greendaotest.db.DaoMaster;
import com.littleyellow.greendaotest.db.DaoSession;

class GreenDao {

    private DaoSession mSession;
    private static Context context;

    public static void init(Context ct){
        context = ct;
    }

    static DaoSession getSession() {
        return SingletonHolder.INSTANCE.mSession;
    }

    private GreenDao() {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(context,"test.db");
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        mSession = daoMaster.newSession();
    }

    private static class SingletonHolder {
        private static final GreenDao INSTANCE = new GreenDao();  //创建实例的地方
    }




}
