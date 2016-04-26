package com.ortosoft.remember.Prayers;

import android.database.Cursor;

import com.ortosoft.remember.db.Connect;
import com.ortosoft.remember.db.Tables;

/**
 * Created by dima on 26.04.2016.
 * Реализация выгрузки из базы утренних молитв
 */
public class MorningWorship extends WorshipBase {



    @Override
    public void Load() {

        String queryText = String.format("select %1$s from %2$s wm where %3$s  = %4$s", Tables.WorshipsMembers.COLUMN_ID_MEMBER, Tables.WorshipsMembers.TABLE_NAME,
                Tables.WorshipsMembers.COLUMN_ID_GROUP, _id);

        Cursor mCursor = Connect.Item().getDb().rawQuery(queryText, null);
        try {
            mCursor.moveToFirst();
            if (!mCursor.isAfterLast()) {
                do {
                    long id = mCursor.getLong(TableSms.NUM_COLUMN_ID);
                    String titleSms = mCursor.getString(TableSms.NUM_COLUMN_TITLE_SMS);
                    String textSms = mCursor.getString(TableSms.NUM_COLUMN_TEXT_SMS);
                    String phoneNumber = mCursor.getString(TableSms.NUM_COLUMN_PHONE_NUMBER);
                    int priority = mCursor.getInt(TableSms.NUM_COLUMN_PRIORITY);
                    arr.add(new DbSms(id, titleSms, textSms, phoneNumber, priority, category));
                } while (mCursor.moveToNext());
            }
        }
        
        catch (Exception ex){
            ex.printStackTrace();
        }
        finally {
            mCursor.close();
        }
        return null;

    }
}
