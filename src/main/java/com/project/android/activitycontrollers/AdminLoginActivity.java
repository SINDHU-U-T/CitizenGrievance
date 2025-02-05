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
import com.project.android.model.User;
import com.project.android.utility.AppInstance;
import com.project.android.utility.Constants;

public class AdminLoginActivity extends AppCompatActivity
{
    private EditText nameET, passwordET;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adminlogin);
        initializeUIComponents();
    }

    public void initializeUIComponents()
    {
        nameET = (EditText) findViewById(R.id.userName);
        passwordET = (EditText) findViewById(R.id.password);
    }

    public void login(View view)
    {
        String userName = nameET.getText().toString().trim();
        String password = passwordET.getText().toString().trim();
        if (userName.length() == 0)
        {
            nameET.setError(Constants.MISSING_NAME);
            nameET.requestFocus();
        }
        else if (password.length() == 0)
        {
            passwordET.setError(Constants.MISSING_PASSWORD);
            passwordET.requestFocus();
        }
        // Validate credentials
        else
        {
            AppDatabaseHelper databaseHelper = new AppDatabaseHelper(this);
            User user = databaseHelper.getAdminUser(userName, password);
            if (user == null)
            {
                Toast.makeText(getApplicationContext(), Constants.INVALID_USER,Toast.LENGTH_SHORT).show();
            }
            else
            {
                ((AppInstance)getApplicationContext()).setCurrentUser(user);
                ((AppInstance)getApplicationContext()).setAdminUser(true);
                Intent i = new Intent(this, AdminHomeActivity.class);
                startActivity(i);
                // close this activity
                finish();
            }
        }
    }

    public boolean onCreateOptionsMenu(android.view.Menu menu)
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
