package com.ortosoft.remember.db;

/**
 * Created by dima on 25.04.2016.
 */
public class Views {

    public static class ViewMorningPrayers {
        // Название таблицы
        public static final String VIEW_NAME = "ViewMorningPrayers";

        // Название столбцов
        public static final String COLUMN_ID = "id_worship";
        public static final String COLUMN_BODY = "body";
        public static final String COLUMN_BODY_RUS = "body_rus";

        // Номера столбцов
        public static final int NUM_COLUMN_ID = 0;
        public static final int NUM_COLUMN_BODY = 1;
        public static final int NUM_COLUMN_BODY_RUS = 2;
    }}
