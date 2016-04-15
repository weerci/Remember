package com.ortosoft.remember.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.ortosoft.remember.App;
import com.ortosoft.remember.db.assets_db.RememberSQLHelper;

import java.util.ArrayList;

/**
 * Created by dima on 29.02.2016.
 * Устанавливает соединение с базой данных
 */
public class Connect {

    // region Fields

    private static SQLiteDatabase mDataBase;
    private static Connect mConnect;

    // endregion

    private Connect()
    {
        mDataBase = SQLiteDatabase.openDatabase(RememberSQLHelper.DB_PATH, null, SQLiteDatabase.OPEN_READWRITE);
//        RememberSQLHelper mOpenHelper = new RememberSQLHelper();
//        mDataBase = mOpenHelper.getWritableDatabase();
    }

    // Статический конструктор для класса
    public static Connect Item(){
        if (mConnect == null) {
            mConnect = new Connect();
        }
        return  mConnect;
    }

    public SQLiteDatabase getDb() {
        return mDataBase;
    }

}
