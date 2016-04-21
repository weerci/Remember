package com.ortosoft.remember;

import android.database.sqlite.SQLiteException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.ortosoft.remember.db.assets_db.ChainedSQLiteException;
import com.ortosoft.remember.db.assets_db.RememberSQLHelper;
import com.ortosoft.remember.db.members.Group;
import com.ortosoft.remember.db.members.Member;
import com.ortosoft.remember.db.members.Prayer;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mTextView = (TextView) findViewById(R.id.main_textView);
        mTextView.setMovementMethod(new ScrollingMovementMethod());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mTextView != null) {
                    StringBuilder sb = new StringBuilder();
                    ArrayList<Member> mList = Member.FindAll();
                    for (Member m : mList)
                        sb.append(String.format("%d, %s, %s\n", m.get_id(), m.get_name(), m.get_comment()));

                    sb.append("\n\n");

                    ArrayList<Group> gList = Group.FindAll();
                    for (Group g : gList)
                        sb.append(String.format("%d, %s\n", g.get_id(), g.get_name()));

                    sb.append(Prayer.FindById(1).get_bodySpannable());

                    mTextView.setText(sb.toString());
                }
                //mTextView.setText(Member.FindById(1).get_name());
                //Snackbar.make(view, "Привет всем!!!", Snackbar.LENGTH_LONG).setAction("Action", null).show();
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
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
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
