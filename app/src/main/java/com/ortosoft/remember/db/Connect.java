package com.ortosoft.remember.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.ortosoft.remember.db.members.Tables;

import java.util.ArrayList;

/**
 * Created by dima on 29.02.2016.
 * Устанавливает соединение с базой данных
 */
public class Connect {

    // region Fields
    private static SQLiteDatabase mDataBase;
    private static Connect mConnect;
    private Context mContext;

    private Connect(Context context )
    {
        OpenHelper mOpenHelper = new OpenHelper(context);
        mDataBase = mOpenHelper.getWritableDatabase();
        mContext = context;
    }

    private class OpenHelper extends SQLiteOpenHelper {
        // Данные базы данных и таблиц
        private ArrayList<String> mQueries = new ArrayList<>();

        private OpenHelper(Context context) {
            super(context, Tables.DB_NAME, null, Tables.VERSION);

            mQueries.add(Tables.CREATE_GROUP);
            mQueries.add(Tables.CREATE_MEMBERS);
            mQueries.add(Tables.CREATE_MEMBERS_GROUP);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.beginTransaction();
            try {
                for (String s : mQueries) {
                    db.execSQL(s);
                }
                db.setTransactionSuccessful();
            } finally {
                db.endTransaction();
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        }
    }


    // endregion

    public static Connect Item(Context context){
        if (mConnect == null) {
            mConnect = new Connect(context);
        }
        return  mConnect;
    }

    public static SQLiteDatabase getDb() {
        return mDataBase;
    }

    public Context getContext() {
        return mContext;
    }


}
