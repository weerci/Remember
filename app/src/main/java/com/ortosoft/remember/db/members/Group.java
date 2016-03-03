package com.ortosoft.remember.db.members;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.support.design.widget.TabLayout;

import com.ortosoft.remember.db.Connect;
import com.ortosoft.remember.db.Tables;

import java.util.ArrayList;

/**
 * Created by dima on 29.02.2016.
 * Класс реализует функционал групп объединяющих людей (семья, работа и пр.)
 */
public class Group {

    // region Fields

    private long _id = -1;
    private  String _name;
    private ArrayList<Member> _members;
    private Context _context;
    // endregion


    public Group(long id, String name, Context context) {
        this(name, context);
        _id = id;
    }

    public Group(String name, Context context) {
        _name = name;
        _context = context;
    }


    // region Properties

    // Возвращает _id записи таблицы базы данных
    public long get_id() {
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

    // region Methods

    // Создает новую группу в базе, или сохраняет внесенные изменения в существую группу
    public long Save(){
        return (_id == -1) ? insert() : update();
    }

    // Добавляет человека в группу
    public void AddMember(Member member)
    {
        insertMember(member);
        refresh();
    }

    // Удаляет человека из группы
    public void DelMember(Member member)
    {
        DeleteMember(member);
        refresh();
    }

    // Удаление группы по ее id
    public static void Delete(int id, Context context)
    {
        Connect connect = Connect.Item(context);

        connect.getDb().beginTransaction();
        try {
            // Удаление из категории всех людей
            connect.getDb().delete(Tables.MembersGroups.TABLE_NAME, Tables.MembersGroups.COLUMN_ID_GROUP + " = ?", new String[]{String.valueOf(id)});

            // Удаление категории
            connect.getDb().delete(Tables.Group.TABLE_NAME, Tables.Group.COLUMN_ID + " = ?", new String[]{String.valueOf(id)});

            connect.getDb().setTransactionSuccessful();
        } finally {
            connect.getDb().endTransaction();
        }
    }


    // endregion

    // region Insert, Update, Delete, Refresh

    // Обновляет данные о группе
    private void refresh()
    {
        Group group = FindById(this._id, _context);
        this._id = group.get_id();
        this._name = group.get_name();
        this._members = group.get_members();
    }

    private long insertMember(Member member)
    {
        ContentValues cv = new ContentValues();
        cv.put(Tables.MembersGroups.COLUMN_ID_GROUP, this.get_id());
        cv.put(Tables.MembersGroups.COLUMN_ID_MEMBERS, member.get_id());

        Connect connect = Connect.Item(_context);
        connect.getDb().beginTransaction();

        long result = 0;
        try {
            result = connect.getDb().insert(Tables.MembersGroups.TABLE_NAME, null, cv);
            connect.getDb().setTransactionSuccessful();
        } finally {
            connect.getDb().endTransaction();
        }

        return result;
    }

    private void DeleteMember(Member member)
    {
        Connect connect = Connect.Item(_context);
        connect.getDb().beginTransaction();

        connect.getDb().beginTransaction();
        try {
            connect.getDb().delete(Tables.MembersGroups.TABLE_NAME,
                    Tables.MembersGroups.COLUMN_ID_GROUP + " = ? AND " + Tables.MembersGroups.COLUMN_ID_MEMBERS + " = ?",
                    new String[]{String.valueOf(this.get_id()), String.valueOf(member.get_id())});
            connect.getDb().setTransactionSuccessful();
        } finally {
            connect.getDb().endTransaction();
        }
    }


    private long insert()
    {
        ContentValues cv = new ContentValues();
        cv.put(Tables.Group.COLUMN_NAME, this._name);

        Connect connect = Connect.Item(_context);
        connect.getDb().beginTransaction();

        long result = 0;
        try {
            result = connect.getDb().insert(Tables.Group.TABLE_NAME, null, cv);
            connect.getDb().setTransactionSuccessful();
        } finally {
            connect.getDb().endTransaction();
        }

        return result;
    }
    private long update()
    {
        ContentValues cv=new ContentValues();
        cv.put(Tables.Group.COLUMN_NAME, this._name);

        Connect connect = Connect.Item(_context);
        connect.getDb().beginTransaction();
        int result;
        try {
            result = connect.getDb().update(Tables.Group.TABLE_NAME, cv, Tables.Group.COLUMN_ID + " = ?", new String[] { String.valueOf(this._id) });
            connect.getDb().setTransactionSuccessful();
        } finally {
            connect.getDb().endTransaction();
        }
        return result;
    }

    // endregion

    // region Selectors

    // Возвращает все группы из базы данных
    public static ArrayList<Group> FindAll(Context context)
    {
        Connect connect = Connect.Item(context);
        Cursor mCursor = connect.getDb().query(Tables.Group.TABLE_NAME, null, null, null, null, null, Tables.Group.COLUMN_NAME);
        ArrayList<Group> arr = new ArrayList<>();

        try {
            mCursor.moveToFirst();
            if (!mCursor.isAfterLast()) {
                do {
                    long id = mCursor.getLong(Tables.Group.NUM_COLUMN_ID);
                    String name = mCursor.getString(Tables.Group.NUM_COLUMN_NAME);
                    arr.add(new Group(id, name, context));
                } while (mCursor.moveToNext());
            }
        } finally {
            mCursor.close();
        }
        return arr;
    }

    // Поиск группы по ее уникльаному идентификатору
    public static Group FindById(long id, Context context)
    {
        Connect connect = Connect.Item(context);
        Cursor mCursor = connect.getDb().query(Tables.Group.TABLE_NAME, null, Tables.Group.COLUMN_ID + " = ?", new String[]{String.valueOf(id)}, null, null, Tables.Group.COLUMN_NAME);
        try {
            mCursor.moveToFirst();
            if (!mCursor.isAfterLast()) {
                String name = mCursor.getString(Tables.Group.NUM_COLUMN_NAME);
                return new Group(id, name, context);
            } else {
                return null;
            }
        } finally {
            mCursor.close();
        }
    }

    // Поиск групп по их названию
    public static ArrayList<Group> FindByName(String name, boolean asLike, Context context )
    {
        String likeQuery = Tables.Group.COLUMN_NAME + " like %'" + name + "'%";
        String equalQuery = Tables.Group.COLUMN_NAME + " = " + name + "'";
        Connect connect = Connect.Item(context);
        ArrayList<Group> arr = new ArrayList<>();

        Cursor cursor = asLike ? connect.getDb().query(Tables.Group.TABLE_NAME, null, likeQuery, null, null, null, Tables.Group.COLUMN_NAME):
            connect.getDb().query(Tables.Group.TABLE_NAME, null, equalQuery, null, null, null, Tables.Group.COLUMN_NAME);

        try {
            cursor.moveToFirst();
            if (!cursor.isAfterLast()) {
                do {
                    long id = cursor.getLong(Tables.Group.NUM_COLUMN_ID);
                    String nameReturned = cursor.getString(Tables.Group.NUM_COLUMN_NAME);
                    arr.add(new Group(id, nameReturned, context));
                } while (cursor.moveToNext());
                return arr;
            } else {
                return null;
            }
        } finally {
            cursor.close();
        }
    }

    // endregion
}
