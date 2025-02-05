package com.project.android.activitycontrollers;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.project.android.R;
import com.project.android.utility.AppInstance;
import com.project.android.utility.Constants;

public class AdminHomeActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adminhome);
    }

    public void postNews(View view)
    {
        Intent i = new Intent(this, PostNewsActivity.class);
        startActivity(i);
    }

    public void viewGrievances(View view)
    {
        Intent i = new Intent(this, ViewGrievancesActivity.class);
        startActivity(i);
    }

    public void viewOpenGrievances(View view)
    {
        Intent i = new Intent(this, ViewOpenGrievancesActivity.class);
        startActivity(i);
    }


    public void viewReports(View view)
    {
        Intent i = new Intent(this, ViewReportsActivity.class);
        startActivity(i);
    }

    public void onBackPressed()
    {
        moveTaskToBack(true);
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
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
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
