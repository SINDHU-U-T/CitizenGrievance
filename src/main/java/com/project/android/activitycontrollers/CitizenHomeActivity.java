package com.project.android.activitycontrollers;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.project.android.R;
import com.project.android.utility.AppInstance;
import com.project.android.utility.Constants;

public class CitizenHomeActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_citizenhome);
    }

    public void postGrievance(View view)
    {
        Intent i = new Intent(this, PostGrievanceActivity.class);
        startActivity(i);
    }

    public void viewGrievances(View view)
    {
        Intent i = new Intent(this, ViewOthersGrievancesActivity.class);
        startActivity(i);
    }

    public void searchGrievances(View view)
    {
        Intent i = new Intent(this, SearchGrievancesActivity.class);
        startActivity(i);
    }



    public void onBackPressed()
    {
        moveTaskToBack(true);
    }

    public boolean onCreateOptionsMenu(android.view.Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.citizen_menu, menu);
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

            case R.id.my_grievances:
                Intent intent = new Intent(this, MyGrievancesActivity.class);
                startActivity(intent);
                return true;

            case R.id.noticeboard:
                Intent newsIntent = new Intent(this, NoticeBoardActivity.class);
                startActivity(newsIntent);
                return true;
        }
        return false;
    }
}
