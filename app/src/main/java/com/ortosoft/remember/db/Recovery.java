package com.ortosoft.remember.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.ortosoft.remember.App;
import com.ortosoft.remember.RoutineFunction;
import com.ortosoft.remember.db.members.Group;
import com.ortosoft.remember.db.members.Member;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by dima on 12.04.2016.
 * Считывает данные из таблиц members, groups, members_groups, worships_members и сохраняет их в
 * полях класса, предоставляет возможность сохранять данные в файлы и в базу
 */
public class Recovery implements Serializable {

    // region Fields

    private final String SERIALIZE_RECOVERY_FILE = "recovery.out";

    private ArrayList<Member> _members;
    private ArrayList<Group> _groups;
    private ArrayList<Tables.Pair> _members_groups;
    private ArrayList<Tables.Pair> _worships_members;

    public ArrayList<Member> get_members() {
        return _members;
    }
    public ArrayList<Group> get_groups() {
        return _groups;
    }
    public ArrayList<Tables.Pair> get_members_groups() {
        return _members_groups;
    }
    public ArrayList<Tables.Pair> get_worships_members() {
        return _worships_members;
    }

    // endregion

    // Загружает данные из полей в базу
    public void SaveToBase() {

        SQLiteDatabase sd = Connect.Item().getDb();

        sd.beginTransaction();

        sd.delete(Tables.WorshipsMembers.TABLE_NAME, null, null);
        sd.delete(Tables.MembersGroups.TABLE_NAME, null, null);
        sd.delete(Tables.Member.TABLE_NAME, null, null);
        sd.delete(Tables.Group.TABLE_NAME, null, null);

        try {

            String scriptForMembers = scriptForMembers();
            if (!scriptForMembers.isEmpty()) {
                sd.execSQL(scriptForMembers);
            }

            String scriptForGroups = scriptForGroups();
            if (!scriptForGroups.isEmpty()) {
                sd.execSQL(scriptForGroups);
            }

            String scriptForMembersGroups = scriptForMembersGroups();
            if (!scriptForMembersGroups.isEmpty()) {
                sd.execSQL(scriptForMembersGroups);
            }

            String scriptForWorshipsMembers = scriptForWorshipsMembers();
            if (!scriptForWorshipsMembers.isEmpty()) {
                sd.execSQL(scriptForWorshipsMembers);
            }

            sd.setTransactionSuccessful();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sd.endTransaction();
        }
    }

    // Сохраняет данные в файлах
    public void SaveToFile() throws IOException {
//        FileOutputStream fos = new FileOutputStream(SERIALIZE_RECOVERY_FILE);
        OutputStream fos = App.getContext().openFileOutput(SERIALIZE_RECOVERY_FILE, Context.MODE_PRIVATE);

        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(this);
        oos.flush();
        oos.close();
    }

    // Сохраняет данные базы в поля класса
    public void LoadFromBase() {
        _members = Member.FindAll();
        _groups = Group.FindAll();
        _members_groups = FindAll(Tables.MembersGroups.TABLE_NAME);
        _worships_members = FindAll(Tables.WorshipsMembers.TABLE_NAME);
    }

    public  void LoadFromFiles() throws IOException, ClassNotFoundException {
        File file = new File(SERIALIZE_RECOVERY_FILE);
        if (file == null)
            return;
        FileInputStream fis = new FileInputStream(SERIALIZE_RECOVERY_FILE);
        ObjectInputStream oin = new ObjectInputStream(fis);
        Recovery rec = (Recovery) oin.readObject();

        this._members = rec.get_members();
        this._groups = rec.get_groups();
        this._members_groups = rec.get_members_groups();
        this._worships_members = rec.get_worships_members();
    }

    // region Helper

    private String scriptForMembers() {
        if (_members == null || _members.isEmpty())
            return null;

        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO members (_id, name, comment, is_dead, baptized) VALUES ");

        for (Member m : _members)
            sb.append(String.format("(%d, '%s', '%s', %d, %d),", m.get_id(), m.get_name(), m.get_comment(), Tables.IsDeadToInt(m.get_isDead()), Tables.BaptizedToInt(m.get_baptized())));
        sb.deleteCharAt(sb.length() - 1);

        return sb.toString();
    }

    private String scriptForGroups() {
        if (_groups == null || _groups.isEmpty())
            return null;

        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO groups (_id, name) VALUES ");

        for (Group g : _groups)
            sb.append(String.format("(%d, '%s'),", g.get_id(), g.get_name()));

        sb.deleteCharAt(sb.length() - 1);

        return sb.toString();
    }

    private String scriptForMembersGroups() {
        if (_members_groups == null || _members_groups.isEmpty())
            return null;

        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO members_groups (id_members, id_groups) VALUES ");

        for (Tables.Pair p : _members_groups)
            sb.append(String.format("(%d, %d),", p.get_id1(), p.get_id2()));

        sb.deleteCharAt(sb.length() - 1);

        return sb.toString();
    }

    private String scriptForWorshipsMembers() {
        if (_worships_members == null || _worships_members.isEmpty())
            return null;

        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO worships_members (id_worship, id_member) VALUES ");

        for (Tables.Pair p : _worships_members)
            sb.append(String.format("(%d, %d),", p.get_id1(), p.get_id2()));
        sb.deleteCharAt(sb.length() - 1);

        return sb.toString();
    }

    private ArrayList<Tables.Pair> FindAll(String tableName) {
        Cursor mCursor = Connect.Item().getDb().query(tableName, null, null, null, null, null, null);
        ArrayList<Tables.Pair> arr = new ArrayList<>();

        try {
            mCursor.moveToFirst();
            if (!mCursor.isAfterLast()) {
                do {
                    long id1 = mCursor.getLong(0);
                    long id2 = mCursor.getLong(1);
                    arr.add(new Tables.Pair(id1, id2));
                } while (mCursor.moveToNext());
            }
        } finally {
            mCursor.close();
        }
        return arr;
    }

    // endregion

}



