package com.ortosoft.remember.db.members;

import android.content.ContentValues;
import android.content.Context;

import com.ortosoft.remember.db.Connect;

import java.util.ArrayList;

/**
 * Created by dima on 29.02.2016.
 */
public class Group {

    // region Fields

    private int id = -1;
    private  String name;

    // endregion
    public Group(String name) {
        this.name = name;
    }

    // region Properties

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // endregion

    // Сохраняет новый или измененный объект в базе данных
    public long Save(Context context){
        return (id == -1) ? insert(getName(), context) : update(getName(), context);
    }

    // region Insert, Update, Delete

    private long insert(String name, Context context)
    {
        ContentValues cv = new ContentValues();
        cv.put(Tables.TableGroup.COLUMN_NAME, name);

        Connect connect = Connect.Item(context);
        connect.getDb().beginTransaction();

        long result = 0;
        try {
            result = connect.getDb().insert(Tables.TableGroup.TABLE_NAME, null, cv);
            connect.getDb().setTransactionSuccessful();
        } finally {
            connect.getDb().endTransaction();
        }

        return result;
    }
    private long update(String name, Context context)
    {
        return 1;
    }

    // Удаление группы по ее id
    public static void Delete(int id, Context context)
    {
        return;
    }

    // endregion

    // region Selectors

    // Возвращает все группы из базы данных
    public static ArrayList<Group> FindAll()
    {
        return null;
    }

    // Поиск группы по ее уникльаному идентификатору
    public static Group FindById(int id)
    {
        return null;
    }

    // Поиск групп по их названию
    public static ArrayList<Group> FindByName(String name, boolean asLike )
    {
        return null;
    }


    // endregion
}
