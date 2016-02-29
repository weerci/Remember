package com.ortosoft.remember.db.members;

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
    public static class TableGroup {
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
}
