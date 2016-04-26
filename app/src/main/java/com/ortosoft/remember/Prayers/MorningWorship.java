package com.ortosoft.remember.Prayers;

import android.database.Cursor;

import com.ortosoft.remember.db.Connect;
import com.ortosoft.remember.db.SqlQeuries;
import com.ortosoft.remember.db.Tables;

/**
 * Created by dima on 26.04.2016.
 * Реализация выгрузки из базы утренних молитв
 */
public class MorningWorship extends WorshipBase {

    private MorningWorship(long id, String name)
    {
        super._id = id;
        super._name = name;
    }

    public static MorningWorship Item(String name)
    {
        Cursor mCursor = Connect.Item().getDb().query(Tables.Worship.TABLE_NAME, null, Tables.Prayer.COLUMN_NAME+ " = ?", new String[]{name}, null, null, null);
        try {
            mCursor.moveToFirst();
            if (!mCursor.isAfterLast()) {
                return new MorningWorship(mCursor.getLong(Tables.Worship.NUM_COLUMN_ID), mCursor.getString(Tables.Worship.NUM_COLUMN_NAME));
            }
        } finally {
            mCursor.close();
        }
        return null;
    }

    // Заполнение полей класса данными о молитвах и людях
    @Override
    public void Load() {
        super.Load();

        Cursor mCursor = Connect.Item().getDb().rawQuery(SqlQeuries.SelectPrayersForWorship, new String[] {String.valueOf(_id)});
        try {
            mCursor.moveToFirst();
            if (!mCursor.isAfterLast()) {
                _prayers.clear();
                do {
                    _prayers.add(mCursor.getLong(Tables.PrayersWorships.NUM_COLUMN_ID_PRAYER));
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
