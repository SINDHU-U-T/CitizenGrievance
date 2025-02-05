package com.project.android.activitycontrollers;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.project.android.R;
import com.project.android.adapters.AllGrievancesListItemAdapter;
import com.project.android.database.AppDatabaseHelper;
import com.project.android.model.Grievance;
import com.project.android.model.User;
import com.project.android.utility.AppInstance;
import com.project.android.utility.Constants;

import java.util.ArrayList;


public class ViewOpenGrievancesActivity extends AppCompatActivity
{
        private ListView listView = null;

        AllGrievancesListItemAdapter customAdapter;
        ArrayList<Grievance> grievances;
        private TextView mNoGrievancesView;
        String category = null;

        @Override
        protected void onCreate(Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_opengrievances);
            populateListView();
        }

    public void populateListView()
    {
        final User admin = ((AppInstance) getApplicationContext()).getCurrentUser();

        mNoGrievancesView = (TextView) findViewById(R.id.no_grievances_text);

        final AppDatabaseHelper databaseHelper = new AppDatabaseHelper(this);

        grievances = databaseHelper.getAllOpenGrievancesForWard(admin.getName());
        if (grievances.size() > 0)
        {
            mNoGrievancesView.setVisibility(View.GONE);
            customAdapter = new AllGrievancesListItemAdapter(this, R.layout.allgrievanceslist_item);
            customAdapter.setGrievances(grievances);
            listView = (ListView) findViewById(R.id.listView);
            listView.setAdapter(customAdapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
                {
                    Grievance grievance = grievances.get(i);
                    Intent intent = new Intent(getApplicationContext(), GrievanceDetailsActivity.class);

                    intent.putExtra(Constants.GRIEVANCE_ID_KEY, grievance.getGrievanceID());
                    startActivity(intent);
                }
            });

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
