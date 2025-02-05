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
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.project.android.R;
import com.project.android.database.AppDatabaseHelper;
import com.project.android.model.Grievance;
import com.project.android.model.User;
import com.project.android.sendmail.GMailSender;
import com.project.android.utility.AppInstance;
import com.project.android.utility.Constants;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;


public class GrievanceDetailsActivity extends AppCompatActivity
{
    private TextView wardTV, categoryTV, descriptionTV, dateTV, userNameTV, aadharTV, statusTV;
    private ImageView iconIV;
    private RatingBar ratingBar;
    int currentStatusOfGrievance;
    Grievance grievance = null;

    AppDatabaseHelper databaseHelper = new AppDatabaseHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grievancedetails);
        initializeUIComponents();

        Bundle bundle = this.getIntent().getExtras();

        long grievanceID = bundle.getLong(Constants.GRIEVANCE_ID_KEY);
        grievance = databaseHelper.getGrievanceWithID(grievanceID);

        wardTV.setText(grievance.getWard());
        categoryTV.setText(grievance.getCategory());
        descriptionTV.setText(grievance.getDescription());

        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        String grievanceDate = dateFormatter.format(grievance.getGrievanceDate().getTime());

        dateTV.setText(grievanceDate);

        User user = databaseHelper.getUserWithGreivanceID(grievanceID);
        userNameTV.setText(user.getName());
        aadharTV.setText(user.getAadharID());
        currentStatusOfGrievance = grievance.getStatus();
        statusTV.setText(Constants.status[currentStatusOfGrievance]);
        ratingBar.setProgress(grievance.getRating());

        String imagePath = grievance.getImagePath();
        Bitmap bitmap = BitmapFactory.decodeFile(imagePath);

        if(null != bitmap)
        {
            iconIV.setImageBitmap(bitmap);
        }
        else
        {
            int resID = this.getResources().getIdentifier("noimage", Constants.DRAWABLE_RESOURCE, this.getPackageName());
            iconIV.setImageResource(resID);
        }
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
        iconIV = (ImageView) findViewById(R.id.icon);
        ratingBar = (RatingBar) findViewById(R.id.rating);
    }

    public void updateStatus(View view)
    {
        wardTV.setError(null);
        final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.grievance_status, android.R.layout.simple_spinner_dropdown_item);
        final User user = databaseHelper.getUserWithGreivanceID(grievance.getGrievanceID());

        new AlertDialog.Builder(this)
                .setAdapter(adapter, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if ( which < currentStatusOfGrievance )
                        {
                            Toast.makeText(getApplicationContext(), Constants.INVALID_STATUS,Toast.LENGTH_LONG).show();
                        }
                        else
                        {
                            String status = adapter.getItem(which).toString();
                            statusTV.setText(status);
                            grievance.setStatus(which);
                            databaseHelper.updateGrievanceStatus(grievance);
                            currentStatusOfGrievance = which;
                            Toast.makeText(getApplicationContext(), Constants.STATUS_UPDATED_SUCCESSFULY,Toast.LENGTH_LONG).show();

                            if (ContextCompat.checkSelfPermission(getApplicationContext(),
                                    Manifest.permission.SEND_SMS)
                                    != PackageManager.PERMISSION_GRANTED)
                            {

                                // Permission is not granted
                                // Should we show an explanation?
                                if (ActivityCompat.shouldShowRequestPermissionRationale(GrievanceDetailsActivity.this,
                                        Manifest.permission.SEND_SMS))
                                {

                                    // Show an explanation to the user *asynchronously* -- don't block
                                    // this thread waiting for the user's response! After the user
                                    // sees the explanation, try again to request the permission.

                                }
                                else
                                {

                                    // No explanation needed; request the permission
                                    ActivityCompat.requestPermissions(GrievanceDetailsActivity.this,
                                            new String[]{Manifest.permission.SEND_SMS},
                                            Constants.MY_PERMISSIONS_REQUEST_SEND_SMS);

                                    // MY_PERMISSIONS_REQUEST_SEND_SMS is an
                                    // app-defined int constant. The callback method gets the
                                    // result of the request.
                                }
                            }
                            else
                            {
                                sendSms(user.getMobile(),  "Thank you for using Complaint Receiver App. The status of your complaint with complaint number " + grievance.getGrievanceID() + " is updated to \"" + status + "\". Here are the complaint details - WARD: " + grievance.getWard() + " CATEGORY: " + grievance.getCategory() + " DESCRIPTION: " + grievance.getDescription());
                                // Permission has already been granted
                            }

                            final String recipientMailID = user.getMailID();
                            final String mailBody = "Dear Citizen, \n\nThank you for using Citizen Grievance Lodging and Redressal App. The status of your complaint with complaint number " + grievance.getGrievanceID() + " is updated to \"" + status + "\". Here are the complaint details: \n\nWARD: " + grievance.getWard() + "\nCATEGORY: " + grievance.getCategory() + "\nDESCRIPTION: " + grievance.getDescription() + "\n\nRegards, \n Team Citizen Grievance Lodging and Redressal App";
                            new Thread(new Runnable() {
                                public void run() {
                                    try {

                                        GMailSender sender = new GMailSender(
                                                "civicissuesredressal@gmail.com",
                                                "brunda123");
                                        sender.sendMail("Complaint is Updated", mailBody,
                                                "civicissuesredressal@gmail.com",
                                                recipientMailID);
                                    } catch (Exception e) {
                                        Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_LONG).show();
                                    }
                                }
                            }).start();

                        }
                        dialog.dismiss();
                    }
                }).create().show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case Constants.MY_PERMISSIONS_REQUEST_SEND_SMS:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    User user = databaseHelper.getUserWithGreivanceID(grievance.getGrievanceID());

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    sendSms(user.getMobile(),  "Thank you for using Citizen Grievance Lodging and Redressal app. The status of your complaint with complaint number " + grievance.getGrievanceID() + " is updated to \"" + currentStatusOfGrievance + "\". Here are the complaint details - WARD: " + grievance.getWard() + " CATEGORY: " + grievance.getCategory() + " DESCRIPTION: " + grievance.getDescription());

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
        }
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

        }
        return false;
    }


    private void sendSms(String phonenumber, String message)
    {
        try {

            SmsManager smsManager = SmsManager.getDefault();
            ArrayList<String> msgArray = smsManager.divideMessage(message);

            smsManager.sendMultipartTextMessage(phonenumber, null,msgArray, null, null);
        } catch (Exception ex) {
            Toast.makeText(getApplicationContext(), ex.getMessage().toString(), Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }


}
