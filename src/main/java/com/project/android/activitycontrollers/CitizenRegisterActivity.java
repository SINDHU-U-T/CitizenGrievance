package com.project.android.activitycontrollers;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.project.android.R;
import com.project.android.database.AppDatabaseHelper;
import com.project.android.model.User;
import com.project.android.utility.AppInstance;
import com.project.android.utility.Constants;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class CitizenRegisterActivity  extends AppCompatActivity
{
    private EditText aadharET, voterET, nameET, passwordET, confirmPasswordET, mailET, mobileET;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registercitizen);
        initializeUIComponents();
    }

    public void initializeUIComponents()
    {
        aadharET = (EditText) findViewById(R.id.aadharID);
        voterET = (EditText) findViewById(R.id.voterID);
        nameET = (EditText) findViewById(R.id.userName);
        passwordET = (EditText) findViewById(R.id.password);
        confirmPasswordET = (EditText) findViewById(R.id.confirmPassword);
        mailET = (EditText) findViewById(R.id.mail);
        mobileET = (EditText) findViewById(R.id.mobile);
    }

    public static boolean isValidPassword(final String password) {
        Pattern pattern;
        Matcher matcher;
        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{4,}$";
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);
        return matcher.matches();
    }

    public void registerCitizen(View view)
    {
        String aadhar = aadharET.getText().toString().trim();
        String voter = voterET.getText().toString().trim();
        String userName = nameET.getText()
                .toString().trim();
        String password = passwordET.getText().toString().trim();
        String confirmPassword = confirmPasswordET.getText().toString().trim();
        String mail = mailET.getText().toString().trim();
        String mobile = mobileET.getText().toString().trim();


        AppDatabaseHelper databaseHelper = new AppDatabaseHelper(this);
        ArrayList<String> savedAadharIDList = databaseHelper.getAllAadharIDs();

        if (aadhar.length() == 0) {
            aadharET.setError(Constants.MISSING_AADHARID);
            aadharET.requestFocus();
        }
        else if (aadhar.length() != 12) {
            aadharET.setError(Constants.INVALID_AADHARID);
            aadharET.requestFocus();
        }
        else if (savedAadharIDList.size()>0 && savedAadharIDList.contains(aadhar))
        {
            nameET.setError(Constants.DUPLICATE_AADHAR_ID);
            nameET.requestFocus();
        }
        else if (voter.length() == 0) {
            voterET.setError(Constants.MISSING_VOTERID);
            voterET.requestFocus();
        }
        else if (voter.length() != 10) {
            voterET.setError(Constants.INVALID_VOTERID);
            voterET.requestFocus();
        }
        else if (userName.length() == 0) {
            nameET.setError(Constants.MISSING_NAME);
            nameET.requestFocus();
        }
        else if (password.length() == 0) {
            passwordET.setError(Constants.MISSING_PASSWORD);
            passwordET.requestFocus();
        }
        else if(password.length()<Constants.MINIMUM_PASSWORD_LENGTH &&!isValidPassword(password))
        {
            passwordET.setError(Constants.INVALID_PASSWORD);
            passwordET.requestFocus();
        }
        else if (confirmPassword.length() == 0) {
            confirmPasswordET.setError(Constants.MISSING_PASSWORD_CONFIRMATION);
            confirmPasswordET.requestFocus();
        }
        else if (!password.equals(confirmPassword)) {
            confirmPasswordET.setError(Constants.PASSWORD_MISMATCH);
            confirmPasswordET.requestFocus();
        }
        else if (TextUtils.isEmpty(mail))  {
            mailET.setError(Constants.MISSING_MAIL);
            mailET.requestFocus();
        }
        else if(false == android.util.Patterns.EMAIL_ADDRESS.matcher(mail).matches()){
            mailET.setError(Constants.INVALID_MAIL);
            mailET.requestFocus();
        }
        else if (mobile.length() == 0) {
            mobileET.setError(Constants.MISSING_MOBILE);
            mobileET.requestFocus();
        }
        else if (mobile.length() != 10) {
            mobileET.setError(Constants.INVALID_MOBILE);
            mobileET.requestFocus();
        }
        else
        {
            User user = new User(userName, password, mail, mobile, aadhar, voter);
            long user_id = databaseHelper.addUser(user);
            user.setUserID(user_id);
            ((AppInstance)getApplicationContext()).setCurrentUser(user);
            ((AppInstance)getApplicationContext()).setAdminUser(false);

            Intent i = new Intent(this, CitizenHomeActivity.class);
            startActivity(i);
            // close this activity
            finish();
        }
    }
}
