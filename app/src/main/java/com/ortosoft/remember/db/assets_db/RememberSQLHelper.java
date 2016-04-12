package com.ortosoft.remember.db.assets_db;

import android.app.Application;
import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.ortosoft.remember.App;
import com.ortosoft.remember.db.Recovery;
import com.ortosoft.remember.db.Tables;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by dima on 03.03.2016.
 * Инициализация базы данных, если база данных не существует или версия ее не совпадает с установленной версией, то
 * копируется из assets новая версия
 */
public class RememberSQLHelper extends SQLiteOpenHelper {

    //region static fields
    private static final String LOG_TAG = RememberSQLHelper.class.getName();
    private static final String DB_NAME = "remember.db";
    private static final String DB_FOLDER = "/data/data/"+ App.getInstance().getPackageName() + "/databases/";
    private static final String DB_PATH = DB_FOLDER + DB_NAME;
    private static final String DB_ASSETS_PATH = "db/" + DB_NAME;
    private static final int DB_VERSION = 1;
    private static final int DB_FILES_COPY_BUFFER_SIZE = 8192;
    //endregion

    public static void Initialize() throws ChainedSQLiteException, IOException {
        if (isInitialized() == false) {
            copyInialDBfromAssets();
        }
    }
    private static void copyInialDBfromAssets() throws IOException {

        Context appContext = App.getInstance().getApplicationContext();
        InputStream inStream = null;
        OutputStream outStream = null;

        try {
            inStream = new BufferedInputStream(appContext.getAssets().open(DB_ASSETS_PATH), DB_FILES_COPY_BUFFER_SIZE);
            File dbDir = new File(DB_FOLDER);
            if (dbDir.exists() == false)
                dbDir.mkdir();
            outStream = new BufferedOutputStream(new FileOutputStream(DB_PATH),
                    DB_FILES_COPY_BUFFER_SIZE);

            byte[] buffer = new byte[DB_FILES_COPY_BUFFER_SIZE];
            int length;
            while ((length = inStream.read(buffer)) > 0) {
                outStream.write(buffer, 0, length);
            }
        }
        catch (Exception e)
        {
            Log.d("copy", "что то пошло не так");
        }
        finally {
            outStream.flush();
            outStream.close();
            inStream.close();
        }
    }
    private static boolean isInitialized() {

        SQLiteDatabase checkDB = null;
        Boolean correctVersion = false;

        try {
            checkDB = SQLiteDatabase.openDatabase(DB_PATH, null, SQLiteDatabase.OPEN_READONLY);
            correctVersion = checkDB.getVersion() == DB_VERSION;
        }
        catch (Exception e)
        {
            Log.d(e.toString(), "Что то не то");
        }
        finally {
            if (checkDB != null)
                checkDB.close();
        }

        return checkDB != null && correctVersion;
    }

    public RememberSQLHelper() {
        super(App.getContext(), DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        throw new SQLiteException(
                "Метод onCreate для базы. Этот метод не должен никогда вызываться!!!");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        throw new SQLiteException(
                "Метод onUpgrade для базы. Этот метод не должен никогда вызываться!!!");
    }

}
