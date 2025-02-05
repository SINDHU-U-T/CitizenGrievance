package com.project.android.activitycontrollers;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.project.android.R;
import com.project.android.adapters.OtherGrievancesListItemAdapter;
import com.project.android.database.AppDatabaseHelper;
import com.project.android.model.Grievance;
import com.project.android.model.User;
import com.project.android.utility.AppInstance;
import com.project.android.utility.Constants;

import java.util.ArrayList;

public class ViewOthersGrievancesActivity extends AppCompatActivity
{
        private ListView listView = null;

        OtherGrievancesListItemAdapter customAdapter;
        ArrayList<Grievance> grievances;
        private TextView mNoGrievancesView;
        String category = null;

        @Override
        protected void onCreate(Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_othergrievances);
            populateListView();
        }

    public void populateListView()
    {
        final User citizen = ((AppInstance) getApplicationContext()).getCurrentUser();

        mNoGrievancesView = (TextView) findViewById(R.id.no_grievances_text);
        mNoGrievancesView.setText(Constants.NOGRIEVANCES_BYOTHERS_DESCRIPTION );

        final AppDatabaseHelper databaseHelper = new AppDatabaseHelper(this);

        grievances = databaseHelper.getGrievancesOtherThanThatOfUserWithID(citizen.getUserID());
        if (grievances.size() > 0)
        {
            mNoGrievancesView.setVisibility(View.GONE);
            customAdapter = new OtherGrievancesListItemAdapter(this, R.layout.othergrievanceslist_item);
            customAdapter.setGrievances(grievances);
            listView = (ListView) findViewById(R.id.listView);
            listView.setAdapter(customAdapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ViewOthersGrievancesActivity.this);
                    builder.setCancelable(false);
                    final Grievance selectedGrievance = (Grievance) adapterView.getItemAtPosition(i);

                    // Get the layout inflater
                    LayoutInflater inflater = ViewOthersGrievancesActivity.this.getLayoutInflater();

                    // Inflate and set the layout for the dialog
                    // Pass null as the parent view because its going in the dialog layout
                    final View post = inflater.inflate(R.layout.rating_dialog, null);
                    builder.setView(post);
                    final AlertDialog dialog = builder.create();
                    dialog.show();

                    Button okButton = (Button) post.findViewById(R.id.select_rating);
                    okButton.setOnClickListener(new Button.OnClickListener() {
                    public void onClick(View v)
                    {
                        RadioGroup ratingGroup = (RadioGroup) post.findViewById(R.id.rating);
                        int selectedOption = ratingGroup.getCheckedRadioButtonId();
                        int rating =  0;
                        if (selectedOption == R.id.one)
                        {
                            rating = 1;
                        }
                        else if (selectedOption == R.id.two)
                        {
                            rating = 2;
                        }
                        else if (selectedOption == R.id.three)
                        {
                            rating = 3;
                        }
                        else if (selectedOption == R.id.four)
                        {
                            rating = 4;
                        }
                        else if (selectedOption == R.id.five)
                        {
                            rating = 5;
                        }
                        selectedGrievance.setRating(rating);
                        AppDatabaseHelper databaseHelper = new AppDatabaseHelper(getApplicationContext());
                        databaseHelper.updateGrievanceRating(selectedGrievance);
                        dialog.cancel();

                        refreshListView();
                    }
                });
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

    public void refreshListView()
    {
        grievances.clear();
        AppDatabaseHelper databaseHelper = new AppDatabaseHelper(this);
        User citizen = ((AppInstance) getApplicationContext()).getCurrentUser();
        grievances = databaseHelper.getGrievancesOtherThanThatOfUserWithID(citizen.getUserID());
        customAdapter.setGrievances(grievances);
        customAdapter.notifyDataSetChanged();
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
        inflater.inflate(R.menu.citizen_menu, menu);
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
