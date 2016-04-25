package com.ortosoft.remember.Prayers;

import android.database.Cursor;
import android.text.Html;
import android.text.Spanned;
import android.widget.TextView;

import com.ortosoft.remember.common.RoutineFunction;
import com.ortosoft.remember.db.Connect;
import com.ortosoft.remember.db.Views;

/**
 * Created by dima on 25.04.2016.
 */
public class MorningPrayers {
    public static void LoadPrayersToTextView(TextView textView)
    {
        if (textView == null) {
            return;
        }

        Connect connect = Connect.Item();
        Cursor mCursor = connect.getDb().query(Views.ViewMorningPrayers.VIEW_NAME, null, null, null, null, null, null);
        StringBuilder sb = new StringBuilder();

        try {
            mCursor.moveToFirst();
            if (!mCursor.isAfterLast()) {
                do {
                    sb.append(mCursor.getString(Views.ViewMorningPrayers.NUM_COLUMN_BODY));
                } while (mCursor.moveToNext());

                Spanned spannedText = Html.fromHtml(sb.toString());
                textView.setText(RoutineFunction.revertSpanned(spannedText));
            }
        } finally {
            mCursor.close();
        }

    }

}
