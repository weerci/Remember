package com.ortosoft.remember.db;

import android.database.Cursor;

import com.ortosoft.remember.RoutineFunction;
import com.ortosoft.remember.db.members.Group;
import com.ortosoft.remember.db.members.Member;

import java.io.FileNotFoundException;
import java.util.ArrayList;

/**
 * Created by dima on 12.04.2016.
 * При изменении версии базы данных, старая база перезаписывается новой.
 * Класс реализует сохранение данных о людях, их группах, привязки молитв к молитвословию и пр.
 */
public class Recovery {

    private final static String FILE_MEMBERS = "fileMembers.txt";
    private final static String FILE_GROUPS = "fileGroups.txt";
    private final static String FILE_MEMBERS_GROUPS = "fileMembersGroups.txt";
    private final static String FILE_WORSHIPS_MEMBERS = "fileWorshipMember.txt";

    // region Fields

    private ArrayList<Member> _members;
    private ArrayList<Group> _groups;
    private ArrayList<Tables.Pair> _members_groups;
    private ArrayList<Tables.Pair> _worships_members;

    // endregion

    // Сохраняет данные из полей класса в базе данных
    public void LoadFilesToBase() {

        Connect.Item().getDb().beginTransaction();

        try {

            String scriptForMembers = RoutineFunction.LoadFromInternalStorage(FILE_MEMBERS);
            if (!scriptForMembers.isEmpty()) {
                Connect.Item().getDb().execSQL(scriptForMembers);
            }

            String scriptForGroups = RoutineFunction.LoadFromInternalStorage(FILE_GROUPS);
            if (!scriptForGroups.isEmpty()) {
                Connect.Item().getDb().execSQL(scriptForGroups);
            }

            String scriptForMembersGroups = RoutineFunction.LoadFromInternalStorage(FILE_MEMBERS_GROUPS);
            if (!scriptForMembersGroups.isEmpty()) {
                Connect.Item().getDb().execSQL(scriptForMembersGroups);
            }

            String scriptForWorshipsMembers = RoutineFunction.LoadFromInternalStorage(FILE_WORSHIPS_MEMBERS);
            if (!scriptForWorshipsMembers.isEmpty()) {
                Connect.Item().getDb().execSQL(scriptForWorshipsMembers);
            }

            Connect.Item().getDb().setTransactionSuccessful();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }finally {
            Connect.Item().getDb().endTransaction();
        }
    }

    // Сохраняет во внутреннем хранилище скрипты таблиц members, groups, members_groups, worship_members
    public void UploadDataToFiles() {

        fillArrays();

        String insertMembers = scriptForMembers();
        if (insertMembers != null) {
            RoutineFunction.SaveToInternalStorage(FILE_MEMBERS, insertMembers);
        }

        String insertGroup = scriptForGroups();
        if (insertGroup != null) {
            RoutineFunction.SaveToInternalStorage(FILE_GROUPS, insertGroup);
        }

        String insertMemberGroup = scriptForMembersGroups();
        if (insertMemberGroup != null) {
            RoutineFunction.SaveToInternalStorage(FILE_MEMBERS_GROUPS, insertMemberGroup);
        }

        String insertWorshipMember = scriptForWorshipsMembers();
        if (insertWorshipMember != null) {
            RoutineFunction.SaveToInternalStorage(FILE_WORSHIPS_MEMBERS, insertWorshipMember);
        }
    }

    // region Helper

    private void fillArrays() {
        _members = Member.FindAll();
        _groups = Group.FindAll();
        _members_groups = FindAll(Tables.MembersGroups.TABLE_NAME);
        _worships_members = FindAll(Tables.WorshipsMembers.TABLE_NAME);
    }


    private String scriptForMembers() {
        if (_members == null ||  _members.isEmpty())
            return null;

        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO members (_id, name, comment, is_dead, baptized) VALUES ");

        String prefix = "";
        for (Member m : _members) {
            sb.append(prefix);
            prefix = ",";
            sb.append(String.format("(?, '?', '?', ?, ?), ", m.get_id(), m.get_name(), m.get_comment(), m.get_isDead(), m.get_baptized()));
        }

        return sb.toString();
    }

    private String scriptForGroups() {
        if (_groups == null || _groups.isEmpty())
            return null;

        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO groups (_id, name) VALUES ");

        String prefix = "";
        for (Group g : _groups) {
            sb.append(prefix);
            prefix = ",";
            sb.append(String.format("(?, '?'),", g.get_id(), g.get_name()));
        }

        return sb.toString();
    }

    private String scriptForMembersGroups() {
        if (_members_groups == null || _members_groups.isEmpty())
            return null;

        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO members_groups (id_members, id_groups) VALUES ");

        String prefix = "";
        for (Tables.Pair p : _members_groups) {
            sb.append(prefix);
            prefix = ",";
            sb.append(String.format("(?, ?),", p.get_id1(), p.get_id2()));
        }

        return sb.toString();
    }

    private String scriptForWorshipsMembers() {
        if (_worships_members == null || _worships_members.isEmpty())
            return null;

        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO worships_members (id_worship, id_member) VALUES ");

        String prefix = "";
        for (Tables.Pair p : _worships_members) {
            sb.append(prefix);
            prefix = ",";
            sb.append(String.format("(?, ?),", p.get_id1(), p.get_id2()));
        }

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

