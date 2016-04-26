package com.ortosoft.remember.Prayers;

import android.provider.MediaStore;

import com.ortosoft.remember.db.Tables;
import com.ortosoft.remember.members.Member;

import java.util.ArrayList;

/**
 * Created by dima on 26.04.2016.
 */
public abstract class WorshipBase {

    protected ArrayList<Long> _prayers;
    protected ArrayList<Long> _members;

    protected String _name;
    protected long _id;

    // Возвращает наименование молитвословия
    public String get_name() {
        return _name;
    }

    // Возвращает идентификатор молитвословия
    public long get_Id() {
        return _id;
    }

    public abstract void Load();

}
