package com.ortosoft.remember;

import android.database.sqlite.SQLiteException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.ortosoft.remember.Prayers.MorningPrayers;
import com.ortosoft.remember.common.ChainedSQLiteException;
import com.ortosoft.remember.db.RememberSQLHelper;
import com.ortosoft.remember.members.Member;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mTextView = (TextView) findViewById(R.id.main_textView);
//        Typeface mCustomFont = Typeface.createFromAsset(getAssets(),"fonts/IrmIEUcs.ttf");
//        mTextView.setTypeface(mCustomFont);

        mTextView.setMovementMethod(new ScrollingMovementMethod());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Привет всем!!!", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        });

        // Инициализация базы данных
        new AppInitializerTask().execute((Void) null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (mTextView == null)
            return false;

        int id = item.getItemId();
        StringBuilder sb = new StringBuilder();

        if (id == R.id.action_morning_prayers)
                MorningPrayers.LoadPrayersToTextView(mTextView);

        else if (id == R.id.action_members) {
            for (Member m : Member.FindAll()) {
                sb.append(m.get_name());
            }
            mTextView.setText(sb.toString());
        }

        return super.onOptionsItemSelected(item);
    }


    private class AppInitializerTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                RememberSQLHelper.Initialize();
                Log.d("It's ok", "really, really");
            } catch (SQLiteException ex) {
                ex.printStackTrace();
            } catch (ChainedSQLiteException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            Log.d("123", result.toString());
        }
    }

}
