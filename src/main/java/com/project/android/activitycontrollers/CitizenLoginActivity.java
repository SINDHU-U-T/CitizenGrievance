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
import com.project.android.sendmail.GMailSender;
import com.project.android.utility.AppInstance;
import com.project.android.utility.Constants;


public class CitizenLoginActivity extends AppCompatActivity
{
    private EditText aadharIDET, passwordET;
    int numberOfLoginAttempts = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_citizenlogin);
        initializeUIComponents();
    }

    public void initializeUIComponents()
    {
        aadharIDET = (EditText) findViewById(R.id.aadharID);
        passwordET = (EditText) findViewById(R.id.password);
    }

    public void login(View view)
    {
        String aadharID = aadharIDET.getText().toString().trim();
        String password = passwordET.getText().toString().trim();

        if (aadharID.length() == 0)
        {
            aadharIDET.setError(Constants.MISSING_AADHARID);
            aadharIDET.requestFocus();
        }
        else if (aadharID.length() < 12) {
            aadharIDET.setError(Constants.INVALID_AADHARID);
            aadharIDET.requestFocus();
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
            User user = databaseHelper.getCitizenUserWithAadharID(aadharID);
            if (user == null)
            {
                Toast.makeText(getApplicationContext(), Constants.UNREGISTERED_USER,Toast.LENGTH_SHORT).show();
            }
            else if (false == password.equals(user.getPassword()))
            {
                numberOfLoginAttempts = numberOfLoginAttempts + 1;
                if (numberOfLoginAttempts <= 3)
                {
                    Toast.makeText(getApplicationContext(), Constants.INVALID_USER,Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(getApplicationContext(), Constants.FORGOT_PASSWORD,Toast.LENGTH_LONG).show();

                    // send password to user's mail id

                    final String recipientMailID = user.getMailID();
                    final String passwordString = "Hello " + user.getName() + ". \nYour registered password is: " + user.getPassword() + "\n Team Citizen Grievance Lodging and Redressal";
                    new Thread(new Runnable() {
                        public void run() {
                            try {

                                GMailSender sender = new GMailSender(
                                        "civicissuesredressal@gmail.com",
                                        "brunda123");
                                sender.sendMail("Grievance is Registered", passwordString,
                                        "civicissuesredressal@gmail.com",
                                        recipientMailID);
                            } catch (Exception e) {
                                Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_LONG).show();
                            }
                        }
                    }).start();
                }
            }
            else
            {
                ((AppInstance)getApplicationContext()).setCurrentUser(user);
                ((AppInstance)getApplicationContext()).setAdminUser(false);
                Intent i = new Intent(this, CitizenHomeActivity.class);
                startActivity(i);
                // close this activity
                finish();
            }
        }
    }

    public void registerCitizen(View view)
    {
        Intent i = new Intent(this, CitizenRegisterActivity.class);
        startActivity(i);
        // close this activity
        finish();
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
