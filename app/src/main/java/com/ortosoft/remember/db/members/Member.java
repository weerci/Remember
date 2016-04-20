package com.ortosoft.remember.db.members;

import android.content.ContentValues;
import android.database.Cursor;

import com.ortosoft.remember.App;
import com.ortosoft.remember.db.Connect;
import com.ortosoft.remember.db.Tables;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by admin on 14.03.2016.
 * Реализует функционал работы с людьми
 */
public class Member implements Serializable {

    // region Fields and Properties

    private long _id = -1;
    private String _name;
    private String _comment;
    private IsDead _isDead;
    private IsBaptized _baptized;
    private ArrayList<Group> _groups;

    // region Propertie

    // возвращает id записи базы данных
    public long get_id() {
        return _id;
    }

    // Возвращает имя человека
    public String get_name() {
        return _name;
    }

    // Устанавливает имя человека
    public void set_name(String name) {
        _name = name;
    }

    // Возвращает пояснение к имени человека
    public String get_comment() {
        return _comment;
    }

    // Устанавливает пояснение к имени человека
    public void set_comment(String comment) {
        _comment = comment;
    }

    // Возвращает признак того, умер человек или нет
    public IsDead get_isDead() {
        return _isDead;
    }

    // Устанавливает признак того, умер человек или нет
    public void set_isDead(IsDead idDead) {
        _isDead = idDead;
    }

    // Возвращает признак того, крещен человек или нет
    public IsBaptized get_baptized() {
        return _baptized;
    }

    // Устанавливает признак того, крещен человек или нет
    public void set_baptized(IsBaptized baptized) {
        _baptized = baptized;
    }

    // Возвращает список всех групп, в которых состоит человек
    public ArrayList<Group> get_groups() {
        return _groups;
    }


    // endregion Properties

    // endregion Fields

    // region Constructors

    public Member(long id, String name, String comment, IsDead isDead, IsBaptized isBaptized) {
        this(name, comment, isDead, isBaptized);
        _id = id;
    }

    public Member(String name, String comment, IsDead isDead, IsBaptized isBaptized) {
        _name = name;
        _comment = comment;
        _isDead = isDead;
        _baptized = isBaptized;
    }

    // endregion

    // region Methods

    // Сохраняет нового пользователя в базе данных или вносит изменения для существующего
    public long Save() {
        return _id == -1 ? insert() : update();
    }

    // Включает человека в заданную группу
    public void AddGroup(Group group) {
        insertToGroup(group);
        refresh();
    }

    public void RemoveGroup(Group group) {
        removeFromGroup(group);
        refresh();
    }

    // Удаление человека по его идентификатору в базе данных
    public static void Del(long id) {
        Connect connect = Connect.Item();

        connect.getDb().beginTransaction();
        try {
            // Исключение человека из всех категорий, в которые он был включен
            connect.getDb().delete(Tables.MembersGroups.TABLE_NAME,
                    Tables.MembersGroups.COLUMN_ID_MEMBERS + " = ?", new String[]{String.valueOf(id)});
            // Удаление человека
            connect.getDb().delete(Tables.Member.TABLE_NAME, Tables.Group.COLUMN_ID + " = ?", new String[]{String.valueOf(id)});

            connect.getDb().setTransactionSuccessful();
        } finally {
            connect.getDb().endTransaction();
        }

    }

    // endregion

    // region insert, update, delete, refresh

    private void refresh() {
        Member member = Member.FindById(this._id);
        this._name = member.get_name();
        this._comment = member.get_comment();
        this._isDead = member.get_isDead();
        this._baptized = member.get_baptized();
    }

    private long insert() {
        ContentValues cv = new ContentValues();
        cv.put(Tables.Member.COLUMN_NAME, this._name);
        cv.put(Tables.Member.COLUMN_COMMENT, this._comment);
        cv.put(Tables.Member.COLUMN_DEAD, Tables.IsDeadToInt(this._isDead));
        cv.put(Tables.Member.COLUMN_BAPTIZED, Tables.BaptizedToInt(this._baptized));

        Connect connect = Connect.Item();
        connect.getDb().beginTransaction();

        long result = 0;
        try {
            result = connect.getDb().insert(Tables.Member.TABLE_NAME, null, cv);
            connect.getDb().setTransactionSuccessful();
        } finally {
            connect.getDb().endTransaction();
        }
        return result;
    }

    private long update() {
        ContentValues cv = new ContentValues();
        cv.put(Tables.Member.COLUMN_NAME, this._name);
        cv.put(Tables.Member.COLUMN_COMMENT, this._comment);
        cv.put(Tables.Member.COLUMN_DEAD, Tables.IsDeadToInt(this._isDead));
        cv.put(Tables.Member.COLUMN_BAPTIZED, Tables.BaptizedToInt(this._baptized));

        Connect connect = Connect.Item();
        connect.getDb().beginTransaction();
        int result;
        try {
            result = connect.getDb().update(Tables.Member.TABLE_NAME, cv,
                    Tables.Member.COLUMN_ID + " = ?", new String[] { String.valueOf(this._id) });
            connect.getDb().setTransactionSuccessful();
        } finally {
            connect.getDb().endTransaction();
        }
        return result;
    }

    private void insertToGroup(Group group) {
        ContentValues cv = new ContentValues();
        cv.put(Tables.MembersGroups.COLUMN_ID_MEMBERS, this.get_id());
        cv.put(Tables.MembersGroups.COLUMN_ID_GROUP, group.get_id());

        Connect connect = Connect.Item();
        connect.getDb().beginTransaction();

        try {
            connect.getDb().insert(Tables.MembersGroups.TABLE_NAME, null, cv);
            connect.getDb().setTransactionSuccessful();
        } finally {
            connect.getDb().endTransaction();
        }
    }

    private void removeFromGroup(Group group) {
        Connect connect = Connect.Item();
        connect.getDb().beginTransaction();
        try {
            connect.getDb().delete(Tables.MembersGroups.TABLE_NAME,
                    Tables.MembersGroups.COLUMN_ID_GROUP + " = ? AND " + Tables.MembersGroups.COLUMN_ID_MEMBERS + " = ?",
                    new String[]{String.valueOf(group.get_id()), String.valueOf(this.get_id())});
            connect.getDb().setTransactionSuccessful();
        } finally {
            connect.getDb().endTransaction();
        }
    }

    // endregion

    // region Selectors

    // Возвращает всех людей из базы данных
    public static ArrayList<Member> FindAll()
    {
        Connect connect = Connect.Item();
        Cursor mCursor = connect.getDb().query(Tables.Member.TABLE_NAME, null, null, null, null, null, Tables.Member.COLUMN_NAME);
        ArrayList<Member> arr = new ArrayList<>();

        try {
            mCursor.moveToFirst();
            if (!mCursor.isAfterLast()) {
                do {
                    long id = mCursor.getLong(Tables.Member.NUM_COLUMN_ID);
                    String name = mCursor.getString(Tables.Member.NUM_COLUMN_NAME);
                    String comment = mCursor.getString(Tables.Member.NUM_COLUMN_COMMENT);
                    IsDead isDead = Tables.IntToIsDead(mCursor.getInt(Tables.Member.NUM_COLUMN_DEAD));
                    IsBaptized baptized = Tables.IntToBaptized(mCursor.getInt(Tables.Member.NUM_COLUMN_BAPTIZED));
                    arr.add(new Member(id, name, comment, isDead, baptized));
                } while (mCursor.moveToNext());
            }
        } finally {
            mCursor.close();
        }
        return arr;
    }

    public static Member FindById(long id)
    {
        Connect connect = Connect.Item();
        Cursor mCursor = connect.getDb().query(Tables.Member.TABLE_NAME, null, Tables.Member.COLUMN_ID + " = ?",
                new String[]{String.valueOf(id)}, null, null, Tables.Member.COLUMN_NAME);
        try {
            mCursor.moveToFirst();
            if (!mCursor.isAfterLast()) {
                String name = mCursor.getString(Tables.Member.NUM_COLUMN_NAME);
                String comment = mCursor.getString(Tables.Member.NUM_COLUMN_COMMENT);
                IsDead isDead = Tables.IntToIsDead(mCursor.getInt(Tables.Member.NUM_COLUMN_DEAD));
                IsBaptized baptized = Tables.IntToBaptized(mCursor.getInt(Tables.Member.NUM_COLUMN_BAPTIZED));
                return new Member(id, name, comment, isDead, baptized);
            } else {
                return null;
            }
        } finally {
            mCursor.close();
        }
    }

    // endregion

}
