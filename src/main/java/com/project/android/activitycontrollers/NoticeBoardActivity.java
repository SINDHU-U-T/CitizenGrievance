package com.project.android.activitycontrollers;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.project.android.R;
import com.project.android.adapters.NewsListItemAdapter;
import com.project.android.database.AppDatabaseHelper;
import com.project.android.model.News;
import com.project.android.utility.AppInstance;
import com.project.android.utility.Constants;

import java.util.ArrayList;


public class NoticeBoardActivity extends AppCompatActivity
{
        private ListView listView = null;

        NewsListItemAdapter customAdapter;
        ArrayList<News> newsList;
        private TextView mNoNewsView;

        @Override
        protected void onCreate(Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_noticeboard);
            populateListView();
        }

    public void populateListView()
    {
        mNoNewsView = (TextView) findViewById(R.id.no_news_text);
        mNoNewsView.setText(Constants.NONEWS_DESCRIPTION );

        final AppDatabaseHelper databaseHelper = new AppDatabaseHelper(this);
        newsList = databaseHelper.getNewsList();

        if (newsList.size() > 0)
        {
            mNoNewsView.setVisibility(View.GONE);
            customAdapter = new NewsListItemAdapter(this, R.layout.news_item);
            customAdapter.setNewsList(newsList);
            listView = (ListView) findViewById(R.id.listView);
            listView.setAdapter(customAdapter);

        } else
        {
            mNoNewsView.setVisibility(View.VISIBLE);
            if (customAdapter != null)
            {
                reloadData();
            }
        }
    }

    public void reloadData()
    {
        newsList.clear();
        customAdapter.setNewsList(newsList);
        customAdapter.notifyDataSetChanged();
    }

    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.admin_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.logout:
                ((AppInstance)getApplicationContext()).setCurrentUser(null);
                Intent i = new Intent(this, UserSelectionActivity.class);
                startActivity(i);
                finish();
                return true;

            case R.id.about:
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
                dialogBuilder.setIcon(R.drawable.grievance);
                dialogBuilder.setTitle(R.string.app_name);
                dialogBuilder.setMessage(Constants.APP_DESCRIPTION);
                dialogBuilder.create();
                dialogBuilder.show();
                return true;
        }
        return false;
    }
}
