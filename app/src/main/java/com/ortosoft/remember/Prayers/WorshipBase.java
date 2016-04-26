package com.ortosoft.remember.Prayers;

import android.database.Cursor;
import android.provider.MediaStore;

import com.ortosoft.remember.db.Connect;
import com.ortosoft.remember.db.SqlQeuries;
import com.ortosoft.remember.db.Tables;
import com.ortosoft.remember.members.IsBaptized;
import com.ortosoft.remember.members.IsDead;
import com.ortosoft.remember.members.Member;

import java.util.ArrayList;

/**
 * Created by dima on 26.04.2016.
 * Базовый класс реализующий функционал молитвенной службы. Служба состоит из молитв и людей
 * Функционал получения набора молитв переложен на дочерние классы
 * Функционал получения людей реализуется в этом классе
 */
public abstract class WorshipBase {

    protected ArrayList<Long> _prayers;
    private ArrayList<Member> _members;

    protected String _name;
    protected long _id;

    // Возвращает наименование молитвословия
    public String get_name() {
        return _name;
    }

    // Возвращает идентификатор молитвословия
    public long get_id() {
        return _id;
    }

    // Возвращает список молитв
    public ArrayList<Long> get_prayers() {
        return _prayers;
    }

    // Возвращает список людей
    public ArrayList<Member> get_members() {
        return _members;
    }

    // Загружает данные по людям
    public void Load()
    {
        Cursor mCursor = Connect.Item().getDb().rawQuery(SqlQeuries.SelectMembersForWorship, new String [] {String.valueOf(_id)});
        try {
            mCursor.moveToFirst();
            if (!mCursor.isAfterLast()) {
                _members.clear();
                do {
                    Long id = mCursor.getLong(Tables.Member.NUM_COLUMN_ID);
                    String name = mCursor.getString(Tables.Member.NUM_COLUMN_NAME);
                    String comment = mCursor.getString(Tables.Member.NUM_COLUMN_COMMENT);
                    IsDead isDead = Tables.IntToIsDead(mCursor.getInt(Tables.Member.NUM_COLUMN_DEAD));
                    IsBaptized baptized = Tables.IntToBaptized(mCursor.getInt(Tables.Member.NUM_COLUMN_BAPTIZED));
                    _members.add(new Member(id, name, comment, isDead, baptized));
                } while (mCursor.moveToNext());
            }
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
        finally {
            mCursor.close();
        }
    }


}
