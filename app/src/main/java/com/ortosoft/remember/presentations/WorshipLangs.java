package com.ortosoft.remember.presentations;

import android.database.Cursor;

import com.ortosoft.remember.Prayers.Prayer;
import com.ortosoft.remember.Prayers.WorshipBase;
import com.ortosoft.remember.db.Connect;
import com.ortosoft.remember.db.SqlQeuries;
import com.ortosoft.remember.db.Tables;
import com.ortosoft.remember.members.IsBaptized;
import com.ortosoft.remember.members.IsDead;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by dima on 26.04.2016.
 * Класс предназначен для извлечения из базы и кеширования языков молитвословий
 */
public class WorshipLangs {

    // region Fields
    private WorshipBase _worship;
    private String _nameCES, _nameRUS, _nameENG, _nameGRC, _nameLAT;

    private HashMap<Long, Prayer> _prayerCES = new HashMap();
    private HashMap<Long, Prayer> _prayerRUS = new HashMap();
    private HashMap<Long, Prayer> _prayerENG = new HashMap();
    private HashMap<Long, Prayer> _prayerGRC = new HashMap();
    private HashMap<Long, Prayer> _prayerLAT = new HashMap();
    // endregion

    public WorshipLangs(WorshipBase worship)
    {
        _worship = worship;
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

    // Возвращает молитвы для молитвенного правила на выбранном языке
    public HashMap<Long, Prayer> get_prayers(Lang lang)
    {
        switch (lang)
        {
            case CES:
                if (_nameCES != null)
                    return _prayerCES;
                return _prayerCES = selectPrayers(lang);
            case RUS:
                if (_nameRUS != null)
                    return _prayerRUS;
                return _prayerRUS = selectPrayers(lang);
            case ENG:
                if (_nameENG != null)
                    return _prayerENG;
                return _prayerENG = selectPrayers(lang);
            case GRC:
                if (_nameGRC != null)
                    return _prayerGRC;
                return _prayerGRC = selectPrayers(lang);
            case LAT:
                if (_nameLAT != null)
                    return _prayerLAT;
                return _prayerLAT = selectPrayers(lang);
        }
        return null;
    }

    // region Helper

    private String selectName(Lang lang)
    {
        Cursor mCursor = Connect.Item().getDb().query(Tables.Worship.TABLE_NAME+"_"+lang, null, Tables.Worship.COLUMN_ID + " = ?",
                new String[]{String.valueOf(_worship.get_id())}, null, null, null);
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

    private HashMap<Long, Prayer> selectPrayers(Lang lang)
    {
        String queryText = String.format(SqlQeuries.SelectPrayersByLanguage, lang);

        Cursor mCursor = Connect.Item().getDb().rawQuery(queryText, new String[]{String.valueOf(_worship.get_id())});
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
