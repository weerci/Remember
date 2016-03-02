package com.ortosoft.remember.db.members;

import android.content.ContentValues;
import android.content.Context;

import com.ortosoft.remember.db.Connect;
import com.ortosoft.remember.db.Tables;

import java.util.ArrayList;

/**
 * Created by dima on 29.02.2016.
 * Класс реализует функционал групп объединяющих людей (семья, работа и пр.)
 */
public class Group {

    // region Fields

    private int _id = -1;
    private  String _name;
    private ArrayList<Member> _members;

    // endregion

    public Group(String _name) {
        this._name = _name;
    }

    // region Properties

    // Возвращает _id записи таблицы базы данных
    public int get_id() {
        return _id;
    }

    // Возвращает наименование группы
    public String get_name() {
        return _name;
    }

    // Устанавливает наименование группы
    public void set_name(String _name) {
        this._name = _name;
    }

    // Возвращает список людей входящих в группу
    public ArrayList<Member> get_members() {
        return _members;
    }

    // endregion

    // Сохраняет новую группу в базе или изменяет название новой
    public long Save(Context context){
        return (_id == -1) ? insert(context) : update(context);
    }

    // Добавляет человека в группу
    public void AddMember(Member member, Context context)
    {
        insertMember(member, context);
        refresh();
    }

    // Удаляет человека из группы
    public void DelMember(Member member)
    {
        _members.remove(member);
    }

    // region Insert, Update, Delete, Refresh

    // Обновляет данные о группе
    private void refresh()
    {
        Group group = FindById(this._id);
        this._id = group.get_id();
        this._name = group.get_name();
        this._members = group.get_members();
    }

    private long insertMember(Member member, Context context)
    {
//        ContentValues cv = new ContentValues();
//        cv.put(Tables.GroupMember.COLUMN_NAME, );
//
//        Connect connect = Connect.Item(context);
//        connect.getDb().beginTransaction();
//
//        long result = 0;
//        try {
//            result = connect.getDb().insert(Tables.GroupMember.TABLE_NAME, null, cv);
//            connect.getDb().setTransactionSuccessful();
//        } finally {
//            connect.getDb().endTransaction();
//        }

//        return result;
        return 0;
    }

    private long insert(Context context)
    {
        ContentValues cv = new ContentValues();
        cv.put(Tables.TableGroup.COLUMN_NAME, this._name);

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
    private long update(Context context)
    {
        ContentValues cv=new ContentValues();
        cv.put(Tables.TableGroup.COLUMN_NAME, this._name);

        Connect connect = Connect.Item(context);
        connect.getDb().beginTransaction();
        int result;
        try {
            result = connect.getDb().update(Tables.TableGroup.TABLE_NAME, cv, Tables.TableGroup.COLUMN_ID + " = ?", new String[] { String.valueOf(this._id) });
            connect.getDb().setTransactionSuccessful();
        } finally {
            connect.getDb().endTransaction();
        }
        return result;
    }

    // Удаление группы по ее _id
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
