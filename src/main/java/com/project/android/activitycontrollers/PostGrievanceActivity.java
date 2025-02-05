package com.project.android.activitycontrollers;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
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
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.project.android.R;
import com.project.android.database.AppDatabaseHelper;
import com.project.android.model.Grievance;
import com.project.android.model.User;
import com.project.android.sendmail.GMailSender;
import com.project.android.utility.AppInstance;
import com.project.android.utility.Constants;
import com.project.android.utility.ImageFilePath;
import com.project.android.utility.Utility;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

public class PostGrievanceActivity extends AppCompatActivity
{
    private TextView wardTV, categoryTV;
    private EditText descriptionET;
    private ImageView iconIV;
    private String realPath = null;
    private RadioGroup ratingRG;

    Grievance grievance = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_postgrievance);
        initializeUIComponents();
    }

    public void initializeUIComponents()
    {
        wardTV = (TextView) findViewById(R.id.ward);
        categoryTV = (TextView) findViewById(R.id.category);
        descriptionET = (EditText) findViewById(R.id.description);
        iconIV = (ImageView) findViewById(R.id.icon);
        ratingRG = (RadioGroup) findViewById(R.id.rating);
    }

    public void selectWard(View view)
    {
        wardTV.setError(null);
        final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.wards, android.R.layout.simple_spinner_dropdown_item);

        new AlertDialog.Builder(this)
                .setAdapter(adapter, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        wardTV.setText(adapter.getItem(which));
                        dialog.dismiss();
                        categoryTV.requestFocus();
                    }
                }).create().show();
    }

    public void selectCategory(View view)
    {
        categoryTV.setError(null);
        final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.grievance_category, android.R.layout.simple_spinner_dropdown_item);

        new AlertDialog.Builder(this)
                .setAdapter(adapter, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        categoryTV.setText(adapter.getItem(which));
                        dialog.dismiss();
                        descriptionET.requestFocus();
                    }
                }).create().show();
    }

    public void selectPhoto(View view)
    {
        final CharSequence[] items = { "Take Photo", "Choose from Library",
                "Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Photo");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                String userChoosenTask;

                boolean result= Utility.checkPermission(PostGrievanceActivity.this);
                if (items[item].equals("Take Photo")) {
                    userChoosenTask="Take Photo";
                    if(result)
                        cameraIntent();
                } else if (items[item].equals("Choose from Library")) {
                    userChoosenTask="Choose from Library";
                    if(result)
                        galleryIntent();
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    public void postGrievance(View view)
    {
        final String ward = wardTV.getText().toString().trim();
        final String category = categoryTV.getText().toString().trim();
        final String description = descriptionET.getText().toString().trim();

        if (ward.length() == 0) {
            wardTV.requestFocus();
            wardTV.setError(Constants.MISSING_WARD);
        }
        else if (category.length() == 0)
        {
            categoryTV.setError(Constants.MISSING_CATEGORY);
            categoryTV.requestFocus();
        }else if (description.length() == 0)
        {
            descriptionET.setError(Constants.MISSING_DESCRIPTION);
            descriptionET.requestFocus();
        }else
        {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setMessage(Constants.GRIEVANCEPOSTING_PROGRESS); // Setting Message
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
            progressDialog.show(); // Display Progress Dialog
            progressDialog.setCancelable(false);

            new Handler().postDelayed(new Runnable() {
            /*
             * Showing progress dialog with a timer.
             */

                @Override
                public void run() {
                    // This method will be executed once the timer is over
                    // Start your app main activity

                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), Constants.GRIEVANCE_POSTED_SUCCESSFULLY, Toast.LENGTH_SHORT).show();
                }
            }, 10000);

            int selectedOption = ratingRG.getCheckedRadioButtonId();
            int rating = 0;

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
            grievance = new Grievance(ward, category, description, Calendar.getInstance(), realPath, Constants.NEW_GRIEVANCE, rating);
            AppDatabaseHelper databaseHelper = new AppDatabaseHelper(getApplicationContext());
            User user = ((AppInstance) getApplicationContext()).getCurrentUser();
            long grievanceID = databaseHelper.saveGrievanceForUserWithID(grievance, user.getUserID());
            grievance.setGrievanceID(grievanceID);
            clearUI();

            if (ContextCompat.checkSelfPermission(getApplicationContext(),
                    Manifest.permission.SEND_SMS)
                    != PackageManager.PERMISSION_GRANTED)
            {

                // Permission is not granted
                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.SEND_SMS))
                {

                    // Show an explanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.

                }
                else
                {

                    // No explanation needed; request the permission
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.SEND_SMS},
                            Constants.MY_PERMISSIONS_REQUEST_SEND_SMS);

                    // MY_PERMISSIONS_REQUEST_SEND_SMS is an
                    // app-defined int constant. The callback method gets the
                    // result of the request.
                }
            }
            else
            {
                sendSms(user.getMobile(),  "Thank you for using Citizen Grievance Lodging and Redressal App. Your complaint is registered successfully with complaint number " + grievanceID + ". Here are the Complaint details - WARD: " + grievance.getWard() + " CATEGORY: " + grievance.getCategory() + " DESCRIPTION: " + grievance.getDescription());
                // Permission has already been granted
            }

            final String recipientMailID = user.getMailID();
            final String mailBody = "Dear Citizen, \n\nThank you for using Citizen Grievance Lodging and Redressal App. Your complaint is registered successfully with complaint number " + grievanceID + ". Here are the Complaint details: \n\nWARD: " + grievance.getWard() + "\nCATEGORY: " + grievance.getCategory() + "\nDESCRIPTION: " + grievance.getDescription() + "\n\nRegards, \n Team Citizen Grievance Lodging and Redressal App";
            new Thread(new Runnable() {
                public void run() {
                    try {

                        GMailSender sender = new GMailSender(
                                "civicissuesredressal@gmail.com",
                                "brunda123");
                        sender.sendMail("Complaint is Registered", mailBody,
                                "civicissuesredressal@gmail.com",
                                recipientMailID);
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_LONG).show();
                    }
                }
            }).start();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case Constants.MY_PERMISSIONS_REQUEST_SEND_SMS:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    User user = ((AppInstance) getApplicationContext()).getCurrentUser();
                    sendSms(user.getMobile(), "Thank you for using Citizen Grievance Lodging and Redressal App. Your complaint is registered successfully with complaint number " + grievance.getGrievanceID() + ". Here are the Complaint details - WARD: " + grievance.getWard() + " CATEGORY: " + grievance.getCategory() + " DESCRIPTION: " + grievance.getDescription());

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;

        }
    }

        public void clearUI()
    {
        wardTV.setText("");
        categoryTV.setText("");
        descriptionET.setText("");
        wardTV.requestFocus();
        iconIV.setVisibility(View.GONE);
        realPath = null;
    }

    private void cameraIntent()
    {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, Constants.REQUEST_CAMERA);
    }

    private void galleryIntent()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"),Constants.SELECT_FILE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == Constants.SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == Constants.REQUEST_CAMERA)
                onCaptureImageResult(data);
        }
    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {

        Uri uri = data.getData();


        realPath = ImageFilePath.getPath(this, data.getData());

        try
        {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);

            iconIV.setImageBitmap(bitmap);
            iconIV.setVisibility(View.VISIBLE);

        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }


    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");

            com.google.android.gms.vision.text.TextRecognizer textRecognizer = new com.google.android.gms.vision.text.TextRecognizer.Builder(getApplicationContext()).build();

com.google.android.gms.vision.Frame imageFrame = new com.google.android.gms.vision.Frame.Builder()

        .setBitmap(thumbnail)                 // your image bitmap
        .build();

String imageText = "";


android.util.SparseArray<com.google.android.gms.vision.text.TextBlock> textBlocks = textRecognizer.detect(imageFrame);

for (int i = 0; i < textBlocks.size(); i++) {
    com.google.android.gms.vision.text.TextBlock textBlock = textBlocks.get(textBlocks.keyAt(i));
    imageText = textBlock.getValue();                   // return string
}

        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");
        FileOutputStream fo;
        try {
            destination.createNewFile();
            Uri fileUri = Uri.fromFile(destination);
            realPath = ImageFilePath.getPath(this, fileUri);
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        iconIV.setImageBitmap(thumbnail);
        iconIV.setVisibility(View.VISIBLE);
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

        }
        return false;
    }


    private void sendSms(String phonenumber, String message)
    {
        try {

            SmsManager smsManager = SmsManager.getDefault();
            ArrayList<String> msgArray = smsManager.divideMessage(message);

            smsManager.sendMultipartTextMessage(phonenumber, null,msgArray, null, null);
            //Toast.makeText(getApplicationContext(), "Message Sent",Toast.LENGTH_LONG).show();
        } catch (Exception ex) {
            Toast.makeText(getApplicationContext(), ex.getMessage().toString(), Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }
}
