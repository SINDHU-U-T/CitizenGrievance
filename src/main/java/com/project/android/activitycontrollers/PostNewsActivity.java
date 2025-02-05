package com.project.android.activitycontrollers;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.project.android.R;
import com.project.android.database.AppDatabaseHelper;
import com.project.android.model.News;
import com.project.android.model.User;
import com.project.android.utility.AppInstance;
import com.project.android.utility.Constants;


public class PostNewsActivity extends AppCompatActivity
{
    private EditText titleET, descriptionET;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_postnews);
        initializeUIComponents();
    }

    public void initializeUIComponents()
    {
        titleET = (EditText) findViewById(R.id.title);
        descriptionET = (EditText) findViewById(R.id.description);
    }

    public void postNews(View view)
    {
        String title = titleET.getText().toString().trim();
        String description = descriptionET.getText().toString().trim();

        if (title.length() == 0)
        {
            titleET.setError(Constants.MISSING_TITLE);
            titleET.requestFocus();
        }
        else if (description.length() == 0)
        {
            descriptionET.setError(Constants.MISSING_NEWS_DESCRIPTION);
            descriptionET.requestFocus();
        }
        else
        {
            User admin = ((AppInstance)getApplicationContext()).getCurrentUser();
            News newNews = new News(title,description,admin.getName());
            AppDatabaseHelper databaseHelper = new AppDatabaseHelper(this);
            long newsID = databaseHelper.addNews(newNews);
            newNews.setNewsID(newsID);
            Toast.makeText(getApplicationContext(), Constants.NEWS_POSTED_SUCCESSFULLY, Toast.LENGTH_SHORT).show();
            clearUI();
        }
    }

    public void clearUI()
    {
        titleET.setText("");
        descriptionET.setText("");
    }

    public boolean onCreateOptionsMenu(android.view.Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.admin_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.about:
                android.app.AlertDialog.Builder dialogBuilder = new android.app.AlertDialog.Builder(this);
                dialogBuilder.setIcon(R.drawable.grievance);
                dialogBuilder.setTitle(R.string.app_name);
                dialogBuilder.setMessage(Constants.APP_DESCRIPTION);
                dialogBuilder.create();
                dialogBuilder.show();
                return true;

            case R.id.logout:
                ((AppInstance)getApplicationContext()).setCurrentUser(null);
                Intent i = new Intent(this, UserSelectionActivity.class);
                startActivity(i);
                finish();
                return true;
        }
        return false;
    }

}

