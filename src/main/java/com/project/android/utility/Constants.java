package com.project.android.utility;

public class Constants
{

    public static final int REQUEST_CAMERA = 0;
    public static final int SELECT_FILE = 1;

    // Error Messages
    public static final String MISSING_NAME = "Please enter your name";
    public static final String MISSING_PASSWORD = "Please enter your password";
    public static final String MISSING_PASSWORD_CONFIRMATION = "Please confirm your password";
    public static final String MISSING_MAIL = "Please enter your email address";
    public static final String INVALID_MAIL = "Invalid email address";
    public static final String MISSING_MOBILE = "Please enter your mobile number";
    public static final String INVALID_MOBILE = "Mobile number should be 10 digit number";
    public static final String MISSING_AADHARID = "Please enter your Aadhar ID";
    public static final String MISSING_VOTERID = "Please enter your Voter ID";
    public static final String INVALID_AADHARID = "Invalid Aadhar ID";
    public static final String INVALID_VOTERID = "Invalid Voter ID";
    public static final String INVALID_USER = "Wrong Credentials";
    public static final String UNREGISTERED_USER = "You are not registered with the app. Please register to continue";
    public static final String DUPLICATE_AADHAR_ID = "Aadhar ID already exists. Please enter a different Aadhar ID";
    public static final String PASSWORD_MISMATCH = "Password does not match";
    public static final String INVALID_PASSWORD = "Password must contain minimum 8 characters including a number and special character";
    public static final String FORGOT_PASSWORD = "You seem to have forgotten your password. The registered password has been sent to your registered mail id";
    public static final String MISSING_WARD = "Please select a ward";
    public static final String MISSING_CATEGORY = "Please select a complaint category";
    public static final String MISSING_DESCRIPTION = "Please add a description of your complaint";

    public static final String INVALID_STATUS = "You cannot select a status lower than the previous one";

    public static final String MISSING_TITLE = "Please add a title to the news";
    public static final String MISSING_NEWS_DESCRIPTION = "Please add a description to the news";

    public static final String MISSING_GRIEVANCEID = "Please enter a Complaint ID";

    public static final int MINIMUM_PASSWORD_LENGTH = 8;
    // App Description
    public static final String APP_DESCRIPTION = "Citizen Grievance Lodging and Redressal is a Citizen friendly complaints registration and tracking app that enables citizens and other city stakeholders to understand the city’s problems better and constructively participate in the governance of the city.";

    public static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 1;

    public static final String GRIEVANCE_NOT_FOUND = "There is no complaint with that ID. Please enter a valid Complaint ID";
    // Database version
    public static final int DATABASE_VERSION = 1;

    // Database name
    public static final String DATABASE_NAME = "CitizenGrievance";

    // Database tables
    public static final String USER_TABLE_NAME = "User";
    public static final String GRIEVANCE_TABLE_NAME = "Grievance";
    public static final String USER_GRIEVANCE_TABLE_NAME = "UserGrievances";
    public static final String NEWS_TABLE_NAME = "News";

    // Common column names
    public static final String ID_KEY = "_id";

    // User Table
    public static final String USER_NAME_KEY = "name";
    public static final String USER_PASSWORD_KEY = "password";
    public static final String USER_MAIL_KEY = "mail";
    public static final String USER_MOBILE_KEY = "mobile";
    public static final String USER_AADHARID_KEY = "aadharid";
    public static final String USER_VOTERID_KEY = "voterid";

    // Grievance Table
    public static final String GRIEVANCE_WARD_KEY = "ward";
    public static final String GRIEVANCE_CATEGORY_KEY = "category";
    public static final String GRIEVANCE_DESCRIPTION_KEY = "description";
    public static final String GRIEVANCE_DATE_KEY = "date";
    public static final String GRIEVANCE_IMAGE_KEY = "image";
    public static final String GRIEVANCE_STATUS_KEY = "status";
    public static final String GRIEVANCE_RATING_KEY = "rating";

    // UserGrievance Table
    public static final String USER_ID_KEY = "user_id";
    public static final String GRIEVANCE_ID_KEY = "grievance_id";

    // News Table
    public static final String NEWS_TITLE_KEY = "title";
    public static final String NEWS_DESCRIPTION_KEY = "description";
    public static final String NEWS_WARD_KEY = "ward";

    public static final String GRIEVANCE_POSTED_SUCCESSFULLY = "Complaint is posted successfully";
    public static final String NEWS_POSTED_SUCCESSFULLY = "News is posted successfully";
    public static final String STATUS_UPDATED_SUCCESSFULY = "Status of the complaint is updated successfully";
    public static final String DRAWABLE_RESOURCE = "drawable";

    public static final String NOGRIEVANCES_DESCRIPTION = "There are no complaints for this category";
    public static final String NOGRIEVANCES_BYOTHERS_DESCRIPTION = "There are no complaints posted by other users";
    public static final String NOGRIEVANCES_FOR_USER = "There are no complaints for user ";
    public static final String NONEWS_DESCRIPTION = "There is no news in the Notice Board";
    public static final String GRIEVANCEPOSTING_PROGRESS = "Posting Complaint...";

    public static final int NEW_GRIEVANCE = 0;
    public static final int OPEN_GRIEVANCE = 1;
    public static final int PENDING_GRIEVANCE = 2;
    public static final int  RESOLVED_GRIEVANCE = 3;
    public static final int CLOSED_GRIEVANCE = 4;

    public static final String[] status = {"NEW", "OPEN", "PENDING", "RESOLVED", "CLOSED"};
    public static final String[] rating = {"AVERAGE", "POOR", "BAD", "WORSE", "IMMEDIATE ACTION"};

}
