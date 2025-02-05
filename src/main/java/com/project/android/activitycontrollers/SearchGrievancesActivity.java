package com.project.android.activitycontrollers;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.project.android.R;
import com.project.android.database.AppDatabaseHelper;
import com.project.android.model.Grievance;
import com.project.android.model.User;
import com.project.android.utility.AppInstance;
import com.project.android.utility.Constants;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class SearchGrievancesActivity extends AppCompatActivity {
    private TextView wardTV, categoryTV, descriptionTV, dateTV, userNameTV, aadharTV, statusTV;
    private RatingBar ratingBar;
    Grievance grievance = null;
    EditText idET;
    TableLayout detailsLayout;

    AppDatabaseHelper databaseHelper = new AppDatabaseHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchgrievance);
        initializeUIComponents();

    }

    public void initializeUIComponents()
    {
        wardTV = (TextView) findViewById(R.id.ward);
        categoryTV = (TextView) findViewById(R.id.category);
        descriptionTV = (TextView) findViewById(R.id.description);
        dateTV = (TextView) findViewById(R.id.date);
        userNameTV = (TextView) findViewById(R.id.userName);
        aadharTV = (TextView) findViewById(R.id.aadharID);
        statusTV = (TextView) findViewById(R.id.status);
        idET = (EditText) findViewById(R.id.id);
        ratingBar = (RatingBar) findViewById(R.id.rating);
        detailsLayout = (TableLayout) findViewById(R.id.details_layout);
    }

    public void search(View view)
    {
        String id = idET.getText().toString().trim();

        if (id.length() == 0) {
            idET.setError(Constants.MISSING_GRIEVANCEID);
            idET.requestFocus();
        }
        else {
            long grievanceID = Long.parseLong(idET.getText().toString());
            grievance = databaseHelper.getGrievanceWithID(grievanceID);

            if (grievance == null)
            {
                detailsLayout.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), Constants.GRIEVANCE_NOT_FOUND, Toast.LENGTH_LONG).show();
            }
            else {
                detailsLayout.setVisibility(View.VISIBLE);


                wardTV.setText(grievance.getWard());
                categoryTV.setText(grievance.getCategory());
                descriptionTV.setText(grievance.getDescription());
                statusTV.setText(Constants.status[grievance.getStatus()]);
                SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                String grievanceDate = dateFormatter.format(grievance.getGrievanceDate().getTime());

                dateTV.setText(grievanceDate);

                User user = ((AppInstance) getApplicationContext()).getCurrentUser();

                userNameTV.setText(user.getName());
                aadharTV.setText(user.getAadharID());
                ratingBar.setProgress(grievance.getRating());
            }
        }
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
