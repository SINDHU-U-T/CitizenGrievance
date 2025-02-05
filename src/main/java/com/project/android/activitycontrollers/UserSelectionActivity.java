package com.project.android.activitycontrollers;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioGroup;

import com.project.android.R;
import com.project.android.utility.Constants;


public class UserSelectionActivity extends AppCompatActivity
{
    private RadioGroup userTypeRG;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userselection);
        initializeUIComponents();
    }

    public void initializeUIComponents()
    {
        userTypeRG = (RadioGroup) findViewById(R.id.userType);
        userTypeRG.check(R.id.citizen);
    }

    public void selectUser(View view)
    {
        int selectedOption = userTypeRG.getCheckedRadioButtonId();
        Intent i;
        if (selectedOption == R.id.admin)
        {
            i = new Intent(getApplicationContext(),AdminLoginActivity.class);
        }
        else
        {
            i = new Intent(getApplicationContext(),CitizenLoginActivity.class);
        }
        startActivity(i);
        finish();
    }

    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.about_menu, menu);
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
        }
        return false;
    }

    public void onBackPressed()
    {
        moveTaskToBack(true);
    }
}
