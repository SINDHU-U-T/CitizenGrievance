package com.project.android.model;


import java.util.Calendar;

public class Grievance
{
    private long grievanceID;
    private String ward;
    private String category;
    private String description;
    private Calendar grievanceDate;
    private String imagePath;
    private int status;
    private int rating;

    public Grievance(long grievanceID, String ward, String category, String description, Calendar grievanceDate, String imagePath, int status, int rating)
    {
        this.grievanceID = grievanceID;
        this.ward = ward;
        this.category = category;
        this.description = description;
        this.grievanceDate = grievanceDate;
        this.imagePath = imagePath;
        this.status = status;
        this.rating = rating;
    }

    public Grievance(String ward, String category, String description, Calendar grievanceDate, String imagePath, int status, int rating)
    {
        this.ward = ward;
        this.category = category;
        this.description = description;
        this.grievanceDate = grievanceDate;
        this.imagePath = imagePath;
        this.status = status;
        this.rating = rating;

    }

    public long getGrievanceID()
    {
        return grievanceID;
    }

    public void setGrievanceID(long grievanceID)
    {
        this.grievanceID = grievanceID;
    }

    public String getWard()
    {
        return ward;
    }

    public void setWard(String ward)
    {
        this.ward = ward;
    }

    public String getCategory()
    {
        return category;
    }

    public void setCategory(String category)
    {
        this.category = category;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public Calendar getGrievanceDate() {
        return grievanceDate;
    }

    public void setGrievanceDate(Calendar grievanceDate) {
        this.grievanceDate = grievanceDate;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = (this.rating + rating)/2;
    }
}
