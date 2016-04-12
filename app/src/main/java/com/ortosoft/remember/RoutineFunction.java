package com.ortosoft.remember;

import android.content.Context;
import android.text.Spannable;
import android.text.Spanned;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

/**
 * Created by dima on 12.04.2016.
 * В классе реализованны функции встречающееся повсеместно в коде программы
 */
public class RoutineFunction {

    // Для нормального span в TextView
    public static Spannable revertSpanned(Spanned stext) {
        Object[] spans = stext.getSpans(0, stext.length(), Object.class);
        Spannable ret = Spannable.Factory.getInstance().newSpannable(stext.toString());
        if (spans != null && spans.length > 0) {
            for (int i = spans.length - 1; i >= 0; --i) {
                ret.setSpan(spans[i], stext.getSpanStart(spans[i]), stext.getSpanEnd(spans[i]), stext.getSpanFlags(spans[i]));
            }
        }

        return ret;
    }

    public static void SaveToInternalStorage(String filename, String text) {
        try {
            OutputStream outputStream = App.getContext().openFileOutput(filename, Context.MODE_PRIVATE);
            OutputStreamWriter osw = new OutputStreamWriter(outputStream);
            osw.write(text);
            osw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String LoadFromInternalStorage(String fileName) throws FileNotFoundException {
        StringBuilder builder = new StringBuilder();

        File file = new File(fileName);
        if (file == null)
            return null;

        try {

            FileInputStream inputStream = new FileInputStream(file);

            if (inputStream != null) {
                InputStreamReader isr = new InputStreamReader(inputStream);
                BufferedReader reader = new BufferedReader(isr);
                String line;

                while ((line = reader.readLine()) != null) {
                    builder.append(line);
                }
            }
            inputStream.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return builder.toString();
    }
}