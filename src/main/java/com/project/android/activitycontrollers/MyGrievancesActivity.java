
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
import com.project.android.adapters.MyGrievancesListItemAdapter;
import com.project.android.database.AppDatabaseHelper;
import com.project.android.model.Grievance;
import com.project.android.model.User;
import com.project.android.utility.AppInstance;
import com.project.android.utility.Constants;

import java.util.ArrayList;


public class MyGrievancesActivity extends AppCompatActivity
{
        private ListView listView = null;

        MyGrievancesListItemAdapter customAdapter;
        ArrayList<Grievance> grievances;
        private TextView mNoGrievancesView;

        @Override
        protected void onCreate(Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_mygrievances);
            populateListView();
        }

    public void populateListView()
    {
        final User user = ((AppInstance) getApplicationContext()).getCurrentUser();

        mNoGrievancesView = (TextView) findViewById(R.id.no_grievances_text);
        mNoGrievancesView.setText(Constants.NOGRIEVANCES_FOR_USER + user.getName() );

        final AppDatabaseHelper databaseHelper = new AppDatabaseHelper(this);

        grievances = databaseHelper.getGrievancesOfUserWithID(user.getUserID());
        if (grievances.size() > 0)
        {
            mNoGrievancesView.setVisibility(View.GONE);
            customAdapter = new MyGrievancesListItemAdapter(this, R.layout.mygrievanceslist_item);
            customAdapter.setGrievances(grievances);
            listView = (ListView) findViewById(R.id.listView);
            listView.setAdapter(customAdapter);
        } else
        {
            mNoGrievancesView.setVisibility(View.VISIBLE);
            if (customAdapter != null)
            {
                reloadData();
            }
        }
    }

    public void reloadData()
    {
        grievances.clear();
        customAdapter.setGrievances(grievances);
        customAdapter.notifyDataSetChanged();
    }

    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.citizen_submenu, menu);
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
