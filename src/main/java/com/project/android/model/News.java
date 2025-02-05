package com.project.android.model;

public class News {
    private long newsID;
    private String title;
    private String description;
    private String ward;

    public long getNewsID() {
        return newsID;
    }

    public void setNewsID(long newsID) {
        this.newsID = newsID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getWard() {
        return ward;
    }

    public void setWard(String ward) {
        this.ward = ward;
    }

    public News(String title, String description, String ward) {

        this.title = title;
        this.description = description;
        this.ward = ward;
    }

    public News(long newsID, String title, String description, String ward) {

        this.newsID = newsID;
        this.title = title;
        this.description = description;
        this.ward = ward;
    }
}
