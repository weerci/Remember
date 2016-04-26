package com.ortosoft.remember.presentations;

import android.database.Cursor;

import com.ortosoft.remember.Prayers.WorshipBase;
import com.ortosoft.remember.db.Connect;
import com.ortosoft.remember.db.Tables;
import com.ortosoft.remember.members.IsBaptized;
import com.ortosoft.remember.members.IsDead;

import java.util.HashMap;

/**
 * Created by dima on 26.04.2016.
 * Класс предназначен для извлечения из базы и кеширования языков молитвословий
 */
public class WorshipLangs {

    // region Fields
    private WorshipBase _worship;
    private String _nameCES, _nameRUS, _nameENG, _nameGRC, _nameLAT;

    private HashMap _prayerCES = new HashMap();
    private HashMap _prayerRUS = new HashMap();
    private HashMap _prayerENG = new HashMap();
    private HashMap _prayerGRC = new HashMap();
    private HashMap _prayerLAT = new HashMap();
    // endregion

    public WorshipLangs(long id)
    {

    }

    // Возвращает наименование молитвенного правила на выбранном языке
    public String get_name(Lang lang)
    {
        switch (lang)
        {
            case CES:
                if (_nameCES != null)
                    return _nameCES;
                return _nameCES = selectNameFromBase(lang);
            case RUS:
                if (_nameRUS != null)
                    return _nameRUS;
                return _nameRUS = selectNameFromBase(lang);
            case ENG:
                if (_nameENG != null)
                    return _nameENG;
                return _nameENG = selectNameFromBase(lang);
            case GRC:
                if (_nameGRC != null)
                    return _nameGRC;
                return _nameGRC = selectNameFromBase(lang);
            case LAT:
                if (_nameLAT != null)
                    return _nameLAT;
                return _nameLAT = selectNameFromBase(lang);
        }
        return null;
    }

    // region Helper

    private String selectNameFromBase(Lang lang)
    {
        Connect connect = Connect.Item();
        Cursor mCursor = connect.getDb().query(Tables.Worship.TABLE_NAME+"_"+lang, null, Tables.Worship.COLUMN_ID + " = ?",
                new String[]{String.valueOf(_worship.get_Id())}, null, null, null);
        try {
            mCursor.moveToFirst();
            if (!mCursor.isAfterLast())
                return mCursor.getString(Tables.Worship.NUM_COLUMN_NAME);
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
        finally {
            mCursor.close();
        }
        return null;
    }

    // endregion


    public enum  Lang
    {
        CES, // Церковнославянский
        RUS, // Русский
        ENG, // Английский
        GRC, // Греческий
        LAT // Латинский
    }


}
