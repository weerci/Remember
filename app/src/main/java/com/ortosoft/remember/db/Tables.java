package com.ortosoft.remember.db;

import com.ortosoft.remember.db.members.IsBaptized;
import com.ortosoft.remember.db.members.IsDead;

/**
 * Created by dima on 01.03.2016.
 * Описание таблиц базы данных
 */
public class Tables {

    // region Создание таблиц

    public static final String DB_NAME = "remember.db";
    public static final String CREATE_GROUP = "CREATE TABLE groups (id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, name TEXT NOT NULL); ";
    public static final String CREATE_MEMBERS = "CREATE TABLE members (id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, name TEXT NOT NULL, comment TEXT, is_dead INTEGER, baptized INTEGER); ";
    public static final String CREATE_MEMBERS_GROUP = "CREATE TABLE members_groups (id_members INTEGER REFERENCES members (id), id_group INTEGER REFERENCES groups (id), PRIMARY KEY (id_members ASC, id_group ASC)); ";

    public static int VERSION = 1;

    // endregion

    // region Описание таблицы GROUPS
    public static class Group {
        // Название таблицы
        public static final String TABLE_NAME = "groups";

        // Название столбцов
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_NAME = "name";

        // Номера столбцов
        public static final int NUM_COLUMN_ID = 0;
        public static final int NUM_COLUMN_NAME = 1;

    }
    // endregion

    // region Описание таблицы MEMBERS
    public static class Member {
        // Название таблицы
        public static final String TABLE_NAME = "members";

        // Название столбцов
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_COMMENT = "comment";
        public static final String COLUMN_DEAD = "is_dead";
        public static final String COLUMN_BAPTIZED = "baptized";

        // Номера столбцов
        public static final int NUM_COLUMN_ID = 0;
        public static final int NUM_COLUMN_NAME = 1;
        public static final int NUM_COLUMN_COMMENT = 2;
        public static final int NUM_COLUMN_DEAD = 3;
        public static final int NUM_COLUMN_BAPTIZED = 4;

    }
    // endregion

    // region Описание таблицы MEMBER_GROUPS

    public static class MembersGroups {
        // Название таблицы
        public static final String TABLE_NAME = "members_groups";

        // Название столбцов
        public static final String COLUMN_ID_MEMBERS = "id_members";
        public static final String COLUMN_ID_GROUP = "id_group";

        // Номера столбцов
        public static final int NUM_COLUMN_ID_MEMBERS = 0;
        public static final int NUM_COLUMN_ID_GROUP = 1;

    }

    // endregion

    // region Преобразование перечислений IsDead и Baptized

    public static int BaptizedToInt(IsBaptized isBaptized)
    {
        switch (isBaptized)
        {
            case yes:   return 1;
            case no: return 0;
            default: return -1;
        }
    }

    public static IsBaptized IntToBaptized(int isBaptized)
    {
        switch (isBaptized)
        {
            case 0: return IsBaptized.no;
            case 1: return IsBaptized.yes;
            default: return IsBaptized.unknoun;
        }
    }

    public static int IsDeadToInt(IsDead isDead)
    {
        switch (isDead) {
            case yes: return 1;
            case no: return 0;
            default: return -1;
        }
    }

    public static IsDead IntToIsDead(int isDead)
    {
        switch (isDead)
        {
            case 0: return IsDead.no;
            case 1: return IsDead.yes;
            default: return IsDead.unknoun;
        }
    }

    // endregion

}
