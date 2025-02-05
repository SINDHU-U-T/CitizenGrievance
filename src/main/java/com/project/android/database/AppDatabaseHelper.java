package com.project.android.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.project.android.model.Grievance;
import com.project.android.model.News;
import com.project.android.model.User;
import com.project.android.utility.Constants;
import com.project.android.utility.Utility;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;


public class AppDatabaseHelper extends SQLiteOpenHelper {

    public AppDatabaseHelper(Context context) {
        super(context, Constants.DATABASE_NAME, null, Constants.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_USER_TABLE = "CREATE TABLE " + Constants.USER_TABLE_NAME + "("
                + Constants.ID_KEY + " INTEGER PRIMARY KEY," + Constants.USER_NAME_KEY + " TEXT,"
                + Constants.USER_PASSWORD_KEY + " TEXT," + Constants.USER_MAIL_KEY + " TEXT," + Constants.USER_MOBILE_KEY + " TEXT," + Constants.USER_AADHARID_KEY + " TEXT," + Constants.USER_VOTERID_KEY + " TEXT"+ ")";

        String CREATE_GRIEVANCE_TABLE = "CREATE TABLE " + Constants.GRIEVANCE_TABLE_NAME + "("
                + Constants.ID_KEY + " INTEGER PRIMARY KEY," + Constants.GRIEVANCE_WARD_KEY + " TEXT,"
                + Constants.GRIEVANCE_CATEGORY_KEY + " TEXT," + Constants.GRIEVANCE_DESCRIPTION_KEY + " TEXT," + Constants.GRIEVANCE_DATE_KEY + " TEXT," + Constants.GRIEVANCE_IMAGE_KEY + " TEXT," + Constants.GRIEVANCE_STATUS_KEY + " INTEGER,"+ Constants.GRIEVANCE_RATING_KEY + " INTEGER" + ")";

        String CREATE_USER_GRIEVANCE_TABLE = "CREATE TABLE " + Constants.USER_GRIEVANCE_TABLE_NAME + "("
                + Constants.ID_KEY + " INTEGER PRIMARY KEY," + Constants.USER_ID_KEY + " INTEGER,"
                + Constants.GRIEVANCE_ID_KEY + " INTEGER" +  ")";

        String CREATE_NEWS_TABLE = "CREATE TABLE " + Constants.NEWS_TABLE_NAME + "("
                + Constants.ID_KEY + " INTEGER PRIMARY KEY," + Constants.NEWS_TITLE_KEY + " TEXT,"
                + Constants.NEWS_DESCRIPTION_KEY + " TEXT," +  Constants.NEWS_WARD_KEY + " TEXT" + ")";

        db.execSQL(CREATE_USER_TABLE);
        db.execSQL(CREATE_GRIEVANCE_TABLE);
        db.execSQL(CREATE_USER_GRIEVANCE_TABLE);
        db.execSQL(CREATE_NEWS_TABLE);

        // Insert predefined user name and password to User table for Admin
        ContentValues values = new ContentValues();
        // Inserting Row to User table
        values.put(Constants.USER_NAME_KEY, "WARD001");
        values.put(Constants.USER_PASSWORD_KEY, "WARD001");
        db.insert(Constants.USER_TABLE_NAME, null, values);
        values.clear();

        values.put(Constants.USER_NAME_KEY, "WARD002");
        values.put(Constants.USER_PASSWORD_KEY, "WARD002");
        db.insert(Constants.USER_TABLE_NAME, null, values);
        values.clear();

        values.put(Constants.USER_NAME_KEY, "WARD003");
        values.put(Constants.USER_PASSWORD_KEY, "WARD003");
        db.insert(Constants.USER_TABLE_NAME, null, values);
        values.clear();

        values.put(Constants.USER_NAME_KEY, "WARD004");
        values.put(Constants.USER_PASSWORD_KEY, "WARD004");
        db.insert(Constants.USER_TABLE_NAME, null, values);
        values.clear();

        values.put(Constants.USER_NAME_KEY, "WARD005");
        values.put(Constants.USER_PASSWORD_KEY, "WARD005");
        db.insert(Constants.USER_TABLE_NAME, null, values);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public long addUser(User user)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        // Inserting Row to User table
        values.put(Constants.USER_NAME_KEY, user.getName());
        values.put(Constants.USER_PASSWORD_KEY, user.getPassword());
        values.put(Constants.USER_MAIL_KEY, user.getMailID());
        values.put(Constants.USER_MOBILE_KEY, user.getMobile());
        values.put(Constants.USER_AADHARID_KEY, user.getAadharID());
        values.put(Constants.USER_VOTERID_KEY, user.getVoterID());

        long user_id = db.insert(Constants.USER_TABLE_NAME, null, values);

        db.close();
        return user_id;
    }

    // Getting admin given username and password. This is useful in login screen
    public User getAdminUser(String userName, String password)
    {
        SQLiteDatabase db = this.getReadableDatabase();

        String table = Constants.USER_TABLE_NAME;
        String[] columns = new String[]{Constants.ID_KEY};
        String where = Constants.USER_NAME_KEY + " =?" + " AND " + Constants.USER_PASSWORD_KEY + " =?";

        Cursor cursor = db.query(table, columns, where,
                new String[]{userName, password}, null, null, null, null);
        User user = null;
        if (cursor.getCount() > 0)
        {
            cursor.moveToFirst();
            long userID = cursor.getLong(cursor.getColumnIndex(Constants.ID_KEY));

            user = new User(userID, userName, password);
        }

        db.close();
        return user;
    }

    public User getCitizenUser(String aadharID, String password)
    {
        SQLiteDatabase db = this.getReadableDatabase();

        String table = Constants.USER_TABLE_NAME;
        String[] columns = new String[]{Constants.ID_KEY, Constants.USER_NAME_KEY, Constants.USER_MAIL_KEY, Constants.USER_MOBILE_KEY,
                Constants.USER_AADHARID_KEY, Constants.USER_VOTERID_KEY};
        String where = Constants.USER_AADHARID_KEY + " =?" + " AND " + Constants.USER_PASSWORD_KEY + " =?";

        Cursor cursor = db.query(table, columns, where,
                new String[]{aadharID, password}, null, null, null, null);
        User user = null;
        if (cursor.getCount() > 0)
        {
            cursor.moveToFirst();
            long userID = cursor.getLong(cursor.getColumnIndex(Constants.ID_KEY));
            String userName = cursor.getString(cursor.getColumnIndex(Constants.USER_NAME_KEY));
            String userMail = cursor.getString(cursor.getColumnIndex(Constants.USER_MAIL_KEY));
            String userMobile = cursor.getString(cursor.getColumnIndex(Constants.USER_MOBILE_KEY));
            String userAadharID = cursor.getString(cursor.getColumnIndex(Constants.USER_AADHARID_KEY));
            String userVoterID = cursor.getString(cursor.getColumnIndex(Constants.USER_VOTERID_KEY));
            user = new User(userID, userName, password, userMail, userMobile, userAadharID, userVoterID);
        }

        db.close();
        return user;
    }

    public User getCitizenUserWithAadharID(String aadharID)
    {
        SQLiteDatabase db = this.getReadableDatabase();

        String table = Constants.USER_TABLE_NAME;
        String[] columns = new String[]{Constants.ID_KEY, Constants.USER_NAME_KEY, Constants.USER_PASSWORD_KEY, Constants.USER_MAIL_KEY, Constants.USER_MOBILE_KEY,
                Constants.USER_AADHARID_KEY, Constants.USER_VOTERID_KEY};
        String where = Constants.USER_AADHARID_KEY + " =?" ;

        Cursor cursor = db.query(table, columns, where,
                new String[]{aadharID}, null, null, null, null);
        User user = null;
        if (cursor.getCount() > 0)
        {
            cursor.moveToFirst();
            long userID = cursor.getLong(cursor.getColumnIndex(Constants.ID_KEY));
            String userName = cursor.getString(cursor.getColumnIndex(Constants.USER_NAME_KEY));
            String userPassword = cursor.getString(cursor.getColumnIndex(Constants.USER_PASSWORD_KEY));
            String userMail = cursor.getString(cursor.getColumnIndex(Constants.USER_MAIL_KEY));
            String userMobile = cursor.getString(cursor.getColumnIndex(Constants.USER_MOBILE_KEY));
            String userAadharID = cursor.getString(cursor.getColumnIndex(Constants.USER_AADHARID_KEY));
            String userVoterID = cursor.getString(cursor.getColumnIndex(Constants.USER_VOTERID_KEY));
            user = new User(userID, userName, userPassword, userMail, userMobile, userAadharID, userVoterID);
        }

        db.close();
        return user;
    }

    // This method returns the list of all the usernames in the database. It is used to check if a newly entered user name
    // already exists in the database.
    public ArrayList<String> getAllAadharIDs()
    {
        SQLiteDatabase db = this.getReadableDatabase();

        String table = Constants.USER_TABLE_NAME;
        String[] columns = new String[]{Constants.USER_AADHARID_KEY};

        Cursor cursor = db.query(table, columns, null,
                null, null, null, null, null);

        ArrayList<String> aadharIDList = new ArrayList();
        if (cursor.moveToFirst()) {
            do {
                String aadhaarID = cursor.getString(cursor.getColumnIndex(Constants.USER_AADHARID_KEY));
                aadharIDList.add(aadhaarID);
            } while (cursor.moveToNext());
        }

        db.close();
        return aadharIDList;
    }

    public long saveGrievanceForUserWithID(Grievance grievance, long userID)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        // Inserting Row to User table
        values.put(Constants.GRIEVANCE_WARD_KEY, grievance.getWard());
        values.put(Constants.GRIEVANCE_CATEGORY_KEY, grievance.getCategory());
        values.put(Constants.GRIEVANCE_DESCRIPTION_KEY, grievance.getDescription());
        values.put(Constants.GRIEVANCE_STATUS_KEY, grievance.getStatus());
        values.put(Constants.GRIEVANCE_RATING_KEY, grievance.getRating());

        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        String grievanceDate = dateFormatter.format(grievance.getGrievanceDate().getTime());

        values.put(Constants.GRIEVANCE_DATE_KEY, grievanceDate);

        values.put(Constants.GRIEVANCE_IMAGE_KEY, grievance.getImagePath());

        long grievanceID = db.insert(Constants.GRIEVANCE_TABLE_NAME, null, values);

        // Inserting Row to UserGrievances table
        values.clear();
        values.put(Constants.USER_ID_KEY, userID);
        values.put(Constants.GRIEVANCE_ID_KEY, grievanceID);
        db.insert(Constants.USER_GRIEVANCE_TABLE_NAME, null, values);

        db.close();
        return grievanceID;
    }

    public Grievance getGrievanceWithID(long grievanceID)
    {
        SQLiteDatabase db = this.getReadableDatabase();


        String grievanceTable = Constants.GRIEVANCE_TABLE_NAME;
        String[] grievanceTableColumns = new String[]{Constants.GRIEVANCE_WARD_KEY, Constants.GRIEVANCE_CATEGORY_KEY, Constants.GRIEVANCE_DESCRIPTION_KEY, Constants.GRIEVANCE_DATE_KEY, Constants.GRIEVANCE_STATUS_KEY, Constants.GRIEVANCE_RATING_KEY, Constants.GRIEVANCE_IMAGE_KEY};
        String where_clause = Constants.ID_KEY + " =?";

        Cursor grievanceCursor = db.query(grievanceTable, grievanceTableColumns, where_clause,
                new String[]{String.valueOf(grievanceID)}, null, null, null, null);

        Grievance newGrievance = null;
        if (grievanceCursor.getCount() > 0)
        {
            grievanceCursor.moveToFirst();
            String ward = grievanceCursor.getString(grievanceCursor.getColumnIndex(Constants.GRIEVANCE_WARD_KEY));
            String category = grievanceCursor.getString(grievanceCursor.getColumnIndex(Constants.GRIEVANCE_CATEGORY_KEY));
            String description = grievanceCursor.getString(grievanceCursor.getColumnIndex(Constants.GRIEVANCE_DESCRIPTION_KEY));
            int status = grievanceCursor.getInt(grievanceCursor.getColumnIndex(Constants.GRIEVANCE_STATUS_KEY));
            int rating = grievanceCursor.getInt(grievanceCursor.getColumnIndex(Constants.GRIEVANCE_RATING_KEY));

            String dateString = grievanceCursor.getString(grievanceCursor.getColumnIndex(Constants.GRIEVANCE_DATE_KEY));

            Calendar newDate = Utility.stringToDate(dateString);

            String imagePath = grievanceCursor.getString(grievanceCursor.getColumnIndex(Constants.GRIEVANCE_IMAGE_KEY));
            newGrievance = new Grievance(grievanceID, ward, category, description, newDate, imagePath, status, rating);
        }
        db.close();
        return newGrievance;
    }

    public ArrayList<Grievance> getGrievancesOfUserWithID(long userID)
    {
        SQLiteDatabase db = this.getReadableDatabase();

        String table = Constants.USER_GRIEVANCE_TABLE_NAME;
        String[] columns = new String[]{Constants.GRIEVANCE_ID_KEY};
        String where = Constants.USER_ID_KEY + " =?";

        Cursor cursor = db.query(table, columns, where,
                new String[]{String.valueOf(userID)}, null, null, null, null);

        ArrayList<Grievance> grievanceList = new ArrayList<Grievance>();
        if (cursor.moveToFirst()) {
            do {
                int grievance_id = cursor.getInt(cursor.getColumnIndex(Constants.GRIEVANCE_ID_KEY));

                String grievanceTable = Constants.GRIEVANCE_TABLE_NAME;
                String[] grievanceTableColumns = new String[]{Constants.GRIEVANCE_WARD_KEY, Constants.GRIEVANCE_CATEGORY_KEY, Constants.GRIEVANCE_DESCRIPTION_KEY, Constants.GRIEVANCE_DATE_KEY, Constants.GRIEVANCE_STATUS_KEY, Constants.GRIEVANCE_RATING_KEY, Constants.GRIEVANCE_IMAGE_KEY};
                String where_clause = Constants.ID_KEY + " =?";

                Cursor grievanceCursor = db.query(grievanceTable, grievanceTableColumns, where_clause,
                        new String[]{String.valueOf(grievance_id)}, null, null, null, null);

                if (grievanceCursor.getCount() > 0)
                {
                    grievanceCursor.moveToFirst();
                    String ward = grievanceCursor.getString(grievanceCursor.getColumnIndex(Constants.GRIEVANCE_WARD_KEY));
                    String category = grievanceCursor.getString(grievanceCursor.getColumnIndex(Constants.GRIEVANCE_CATEGORY_KEY));
                    String description = grievanceCursor.getString(grievanceCursor.getColumnIndex(Constants.GRIEVANCE_DESCRIPTION_KEY));
                    int status = grievanceCursor.getInt(grievanceCursor.getColumnIndex(Constants.GRIEVANCE_STATUS_KEY));
                    int rating = grievanceCursor.getInt(grievanceCursor.getColumnIndex(Constants.GRIEVANCE_RATING_KEY));

                    String dateString = grievanceCursor.getString(grievanceCursor.getColumnIndex(Constants.GRIEVANCE_DATE_KEY));

                    Calendar newDate = Utility.stringToDate(dateString);

                    String imagePath = grievanceCursor.getString(grievanceCursor.getColumnIndex(Constants.GRIEVANCE_IMAGE_KEY));

                    Grievance newGrievance = new Grievance(grievance_id, ward,category,description, newDate, imagePath, status, rating);
                    grievanceList.add(newGrievance);
                }
            } while (cursor.moveToNext());
        }
        db.close();
        return grievanceList;
    }



    public ArrayList<Grievance> getAllOpenGrievancesForWard(String ward)
    {
        SQLiteDatabase db = this.getReadableDatabase();

        ArrayList<Grievance> grievanceList = new ArrayList<Grievance>();

        String grievanceTable = Constants.GRIEVANCE_TABLE_NAME;
        String[] grievanceTableColumns = new String[]{Constants.ID_KEY, Constants.GRIEVANCE_CATEGORY_KEY, Constants.GRIEVANCE_DATE_KEY, Constants.GRIEVANCE_DESCRIPTION_KEY, Constants.GRIEVANCE_IMAGE_KEY, Constants.GRIEVANCE_RATING_KEY};
        String where_clause = Constants.GRIEVANCE_WARD_KEY + " =?" + " AND " + Constants.GRIEVANCE_STATUS_KEY + " =?";

        Cursor grievanceCursor = db.query(grievanceTable, grievanceTableColumns, where_clause,
                new String[]{ward, String.valueOf(Constants.OPEN_GRIEVANCE)}, null, null, null, null);

        if (grievanceCursor.moveToFirst())
        {
            do
            {
                long grievanceID = Long.parseLong(grievanceCursor.getString(grievanceCursor.getColumnIndex(Constants.ID_KEY)));
                String description = grievanceCursor.getString(grievanceCursor.getColumnIndex(Constants.GRIEVANCE_DESCRIPTION_KEY));
                String category = grievanceCursor.getString(grievanceCursor.getColumnIndex(Constants.GRIEVANCE_CATEGORY_KEY));

                String dateString = grievanceCursor.getString(grievanceCursor.getColumnIndex(Constants.GRIEVANCE_DATE_KEY));
                int status = Constants.OPEN_GRIEVANCE;
                int rating = grievanceCursor.getInt(grievanceCursor.getColumnIndex(Constants.GRIEVANCE_RATING_KEY));

                Calendar newDate = Utility.stringToDate(dateString);

                String imagePath = grievanceCursor.getString(grievanceCursor.getColumnIndex(Constants.GRIEVANCE_IMAGE_KEY));
                Grievance newGrievance = new Grievance(grievanceID, ward,category,description, newDate, imagePath, status, rating);

                grievanceList.add(newGrievance);
            } while (grievanceCursor.moveToNext());
        }
        db.close();
        return grievanceList;
    }

    public HashMap<String,String> getCountOfGrievancesForWardStatusWise(String ward)
    {
        SQLiteDatabase db = this.getReadableDatabase();

        String[] columns = new String[]{ward};

        Cursor cursor = db.rawQuery("select count(_id), status from Grievance where ward" + " =?"

                + " group by status", columns);
        HashMap<String,String> results = new HashMap<>();
        if (cursor.moveToFirst()) {
            do {
                int count = Integer.parseInt(cursor.getString(0));
                String status = cursor.getString(cursor.getColumnIndex(Constants.GRIEVANCE_STATUS_KEY));
                results.put(status,String.valueOf(count));
            } while (cursor.moveToNext());
        }
        db.close();
        return results;
    }

    public HashMap<String,String> getCountOfGrievancesForWardCategoryWise(String ward)
    {
        SQLiteDatabase db = this.getReadableDatabase();

        String[] columns = new String[]{ward};

        Cursor cursor = db.rawQuery("select count(_id), category from Grievance where ward" + " =?"

                + " group by category", columns);
        HashMap<String,String> results = new HashMap<>();
        if (cursor.moveToFirst()) {
            do {
                int count = Integer.parseInt(cursor.getString(0));
                String category = cursor.getString(cursor.getColumnIndex(Constants.GRIEVANCE_CATEGORY_KEY));
                results.put(category,String.valueOf(count));
            } while (cursor.moveToNext());
        }
        db.close();
        return results;
    }

    public HashMap<String,String> getCountOfGrievancesForWardRatingWise(String ward)
    {
        SQLiteDatabase db = this.getReadableDatabase();

        String[] columns = new String[]{ward};

        Cursor cursor = db.rawQuery("select count(_id), rating from Grievance where ward" + " =?"

                + " group by rating", columns);
        HashMap<String,String> results = new HashMap<>();
        if (cursor.moveToFirst()) {
            do {
                int count = Integer.parseInt(cursor.getString(0));
                String rating = cursor.getString(cursor.getColumnIndex(Constants.GRIEVANCE_RATING_KEY));
                results.put(rating,String.valueOf(count));
            } while (cursor.moveToNext());
        }
        db.close();
        return results;
    }

    public ArrayList<Grievance> getAllGrievancesForWardAndCategory(String ward, String category)
    {
        SQLiteDatabase db = this.getReadableDatabase();

        ArrayList<Grievance> grievanceList = new ArrayList<Grievance>();

        String grievanceTable = Constants.GRIEVANCE_TABLE_NAME;
                String[] grievanceTableColumns = new String[]{Constants.ID_KEY, Constants.GRIEVANCE_DATE_KEY, Constants.GRIEVANCE_DESCRIPTION_KEY, Constants.GRIEVANCE_IMAGE_KEY, Constants.GRIEVANCE_STATUS_KEY, Constants.GRIEVANCE_RATING_KEY};
                String where_clause = Constants.GRIEVANCE_WARD_KEY + " =?" + " AND " + Constants.GRIEVANCE_CATEGORY_KEY + " =?";

        Cursor grievanceCursor = db.query(grievanceTable, grievanceTableColumns, where_clause,
                        new String[]{ward, category}, null, null, null, null);

                if (grievanceCursor.moveToFirst())
                {
                    do
                    {
                        long grievanceID = Long.parseLong(grievanceCursor.getString(grievanceCursor.getColumnIndex(Constants.ID_KEY)));
                        String description = grievanceCursor.getString(grievanceCursor.getColumnIndex(Constants.GRIEVANCE_DESCRIPTION_KEY));

                        String dateString = grievanceCursor.getString(grievanceCursor.getColumnIndex(Constants.GRIEVANCE_DATE_KEY));
                        int status = grievanceCursor.getInt(grievanceCursor.getColumnIndex(Constants.GRIEVANCE_STATUS_KEY));
                        int rating = grievanceCursor.getInt(grievanceCursor.getColumnIndex(Constants.GRIEVANCE_RATING_KEY));

                        Calendar newDate = Utility.stringToDate(dateString);

                        String imagePath = grievanceCursor.getString(grievanceCursor.getColumnIndex(Constants.GRIEVANCE_IMAGE_KEY));
                        Grievance newGrievance = new Grievance(grievanceID, ward,category,description, newDate, imagePath, status, rating);

                        grievanceList.add(newGrievance);
                    } while (grievanceCursor.moveToNext());
                }
        db.close();
        return grievanceList;
    }

    public ArrayList<Grievance> getGrievancesOtherThanThatOfUserWithID(long userID)
    {
        SQLiteDatabase db = this.getReadableDatabase();

        String table = Constants.USER_GRIEVANCE_TABLE_NAME;
        String[] columns = new String[]{Constants.GRIEVANCE_ID_KEY};
        String where = Constants.USER_ID_KEY + " !=?";

        Cursor cursor = db.query(table, columns, where,
                new String[]{String.valueOf(userID)}, null, null, null, null);

        ArrayList<Grievance> grievanceList = new ArrayList<Grievance>();
        if (cursor.moveToFirst()) {
            do {
                int grievance_id = cursor.getInt(cursor.getColumnIndex(Constants.GRIEVANCE_ID_KEY));

                String grievanceTable = Constants.GRIEVANCE_TABLE_NAME;
                String[] grievanceTableColumns = new String[]{Constants.GRIEVANCE_WARD_KEY, Constants.GRIEVANCE_CATEGORY_KEY, Constants.GRIEVANCE_DESCRIPTION_KEY, Constants.GRIEVANCE_STATUS_KEY, Constants.GRIEVANCE_RATING_KEY, Constants.GRIEVANCE_DATE_KEY, Constants.GRIEVANCE_IMAGE_KEY};
                String where_clause = Constants.ID_KEY + " =?";

                Cursor grievanceCursor = db.query(grievanceTable, grievanceTableColumns, where_clause,
                        new String[]{String.valueOf(grievance_id)}, null, null, null, null);

                if (grievanceCursor.getCount() > 0)
                {
                    grievanceCursor.moveToFirst();
                    String ward = grievanceCursor.getString(grievanceCursor.getColumnIndex(Constants.GRIEVANCE_WARD_KEY));
                    String category = grievanceCursor.getString(grievanceCursor.getColumnIndex(Constants.GRIEVANCE_CATEGORY_KEY));
                    String description = grievanceCursor.getString(grievanceCursor.getColumnIndex(Constants.GRIEVANCE_DESCRIPTION_KEY));
                    int status = grievanceCursor.getInt(grievanceCursor.getColumnIndex(Constants.GRIEVANCE_STATUS_KEY));
                    int rating = grievanceCursor.getInt(grievanceCursor.getColumnIndex(Constants.GRIEVANCE_RATING_KEY));

                    String dateString = grievanceCursor.getString(grievanceCursor.getColumnIndex(Constants.GRIEVANCE_DATE_KEY));

                    Calendar newDate = Utility.stringToDate(dateString);

                    String imagePath = grievanceCursor.getString(grievanceCursor.getColumnIndex(Constants.GRIEVANCE_IMAGE_KEY));
                    Grievance newGrievance = new Grievance(grievance_id, ward,category,description, newDate,imagePath, status, rating);
                    grievanceList.add(newGrievance);
                }
            } while (cursor.moveToNext());
        }
        db.close();
        return grievanceList;
    }

    public User getUserWithGreivanceID(long grievanceID)
    {
        SQLiteDatabase db = this.getReadableDatabase();

        String table = Constants.USER_GRIEVANCE_TABLE_NAME;
        String[] columns = new String[]{Constants.USER_ID_KEY};
        String where = Constants.GRIEVANCE_ID_KEY + " =?";

        Cursor cursor = db.query(table, columns, where,
                new String[]{String.valueOf(grievanceID)}, null, null, null, null);

        String name = null;
        String aadharID = null;
        String voterID = null;
        String mail = null;
        String mobile = null;

        User user = null;
        if (cursor.moveToFirst())
        {

            int userID = cursor.getInt(cursor.getColumnIndex(Constants.USER_ID_KEY));

            String userTableName = Constants.USER_TABLE_NAME;
            String[] userTableColumns = new String[]{Constants.USER_NAME_KEY, Constants.USER_AADHARID_KEY, Constants.USER_VOTERID_KEY, Constants.USER_MOBILE_KEY, Constants.USER_MAIL_KEY};
            String where_clause = Constants.ID_KEY + " =?";

            Cursor userCursor = db.query(userTableName, userTableColumns, where_clause,
                    new String[]{String.valueOf(userID)}, null, null, null, null);

            if (userCursor.getCount() > 0)
            {
                userCursor.moveToFirst();
                name = userCursor.getString(userCursor.getColumnIndex(Constants.USER_NAME_KEY));
                aadharID = userCursor.getString(userCursor.getColumnIndex(Constants.USER_AADHARID_KEY));
                voterID = userCursor.getString(userCursor.getColumnIndex(Constants.USER_VOTERID_KEY));
                mail = userCursor.getString(userCursor.getColumnIndex(Constants.USER_MAIL_KEY));
                mobile = userCursor.getString(userCursor.getColumnIndex(Constants.USER_MOBILE_KEY));
                user = new User(name, mail, mobile, aadharID, voterID);
            }
        }
        db.close();
        return user;
    }

    public int updateGrievanceStatus(Grievance grievance){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Constants.GRIEVANCE_STATUS_KEY , grievance.getStatus());

        // Updating row
        return db.update(Constants.GRIEVANCE_TABLE_NAME, values, Constants.ID_KEY + "=?",
                new String[]{String.valueOf(grievance.getGrievanceID())});
    }

    public int updateGrievanceRating(Grievance grievance){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Constants.GRIEVANCE_RATING_KEY , grievance.getRating());

        // Updating row
        return db.update(Constants.GRIEVANCE_TABLE_NAME, values, Constants.ID_KEY + "=?",
                new String[]{String.valueOf(grievance.getGrievanceID())});
    }

    public long addNews(News news)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        // Inserting Row to User table
        values.put(Constants.NEWS_TITLE_KEY, news.getTitle());
        values.put(Constants.NEWS_DESCRIPTION_KEY, news.getDescription());
        values.put(Constants.NEWS_WARD_KEY, news.getWard());

        long newsID = db.insert(Constants.NEWS_TABLE_NAME, null, values);

        db.close();
        return newsID;
    }

    public ArrayList<News> getNewsList()
    {
        SQLiteDatabase db = this.getReadableDatabase();

        String table = Constants.NEWS_TABLE_NAME;
        String[] columns = new String[]{Constants.ID_KEY, Constants.NEWS_TITLE_KEY, Constants.NEWS_DESCRIPTION_KEY, Constants.NEWS_WARD_KEY};

        Cursor cursor = db.query(table, columns, null,
                null, null, null, null, null);

        ArrayList<News> newsList = new ArrayList<News>();
        if (cursor.moveToFirst()) {
            do {
                long newsID = cursor.getLong(cursor.getColumnIndex(Constants.ID_KEY));
                String title = cursor.getString(cursor.getColumnIndex(Constants.NEWS_TITLE_KEY));
                String description = cursor.getString(cursor.getColumnIndex(Constants.NEWS_DESCRIPTION_KEY));
                String ward = cursor.getString(cursor.getColumnIndex(Constants.NEWS_WARD_KEY));

                News item = new News(newsID, title, description, ward);
                newsList.add(item);
            } while (cursor.moveToNext());
        }

        db.close();
        return newsList;
    }
}