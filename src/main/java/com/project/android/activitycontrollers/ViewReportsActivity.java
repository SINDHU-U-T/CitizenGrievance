package com.project.android.activitycontrollers;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.project.android.R;
import com.project.android.model.User;
import com.project.android.utility.AppInstance;
import com.project.android.utility.Constants;


public class ViewReportsActivity extends AppCompatActivity
{
    private RadioGroup reportTypeRG;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewreports);
        initializeUIComponents();
    }

    public void initializeUIComponents()
    {
        reportTypeRG = (RadioGroup) findViewById(R.id.reportType);
        reportTypeRG.check(R.id.category);
    }

    public void viewReport(View view)
    {
        final User admin = ((AppInstance) getApplicationContext()).getCurrentUser();

        int selectedOption = reportTypeRG.getCheckedRadioButtonId();
        RadioButton reportTypeButton = (RadioButton) findViewById(selectedOption);
        String reportType = reportTypeButton.getText().toString();

        Intent i = new Intent(getApplicationContext(),PieChartActivity.class);
        i.putExtra(Constants.GRIEVANCE_WARD_KEY, admin.getName());
        i.putExtra("Report_Type", reportType);
        startActivity(i);
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
