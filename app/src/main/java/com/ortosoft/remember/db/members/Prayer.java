package com.ortosoft.remember.db.members;

import android.content.ContentValues;
import android.database.Cursor;
import android.text.Html;
import android.text.Spannable;
import android.text.Spanned;

import com.ortosoft.remember.RoutineFunction;
import com.ortosoft.remember.db.Connect;
import com.ortosoft.remember.db.Tables;

import java.util.ArrayList;

/**
 * Created by dima on 11.04.2016.
 * Реализует функционал работы с молитвами
 */
public class Prayer {

        // region Fields and Properties

        private long _id = -1;
        private String _name;
        private String _body;
        private String _comment;

        // region Properties

        // возвращает id записи базы данных
        public long get_id() {
            return _id;
        }

        // Возвращает наименование молитвы
        public String get_name() {
            return _name;
        }

        // Возвращает текст молитвы в том виде как он хранится в базе данных
        public String get_body() {
            return _body;
        }

        // Возвращает текст в виде объекта класса Spannable
        public Spannable get_bodySpannable()
        {
            Spanned spannedText = Html.fromHtml(_body);
            return RoutineFunction.revertSpanned(spannedText);
        }


        // Возвращает пояснение к молитве
        public String get_comment() {
            return _comment;
        }

        // endregion Properties

        // endregion Fields

        // region Constructors

        public Prayer(long id, String name, String body, String comment) {
            _id = id;
            _name = name;
            _body = body;
            _comment = comment;
        }

        // endregion

        // region Selectors

        public static Prayer FindById(long id)
        {
            Connect connect = Connect.Item();
            Cursor mCursor = connect.getDb().query(Tables.Prayer.TABLE_NAME, null, Tables.Prayer.COLUMN_ID + " = ?",
                    new String[]{String.valueOf(id)}, null, null, Tables.Prayer.COLUMN_NAME);
            try {
                mCursor.moveToFirst();
                if (!mCursor.isAfterLast()) {
                    String name = mCursor.getString(Tables.Prayer.NUM_COLUMN_NAME);
                    String body = mCursor.getString(Tables.Prayer.NUM_COLUMN_BODY);
                    String comment = mCursor.getString(Tables.Prayer.NUM_COLUMN_COMMENT);
                    return new Prayer(id, name, body, comment);
                } else {
                    return null;
                }
            } finally {
                mCursor.close();
            }
        }

        // endregion


}
