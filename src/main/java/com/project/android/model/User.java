package com.project.android.model;

public class User
{
    private long userID;
    private String name;
    private String password;
    private String mailID;
    private String mobile;
    private String aadharID;
    private String voterID;

    public User(long userID, String name, String password, String mailID, String mobile, String aadharID, String voterID) {
        this.userID = userID;
        this.name = name;
        this.password = password;
        this.mailID = mailID;
        this.mobile = mobile;
        this.aadharID = aadharID;
        this.voterID = voterID;
    }

    public User(String name, String password, String mailID, String mobile, String aadharID, String voterID) {
        this.name = name;
        this.password = password;
        this.mailID = mailID;
        this.mobile = mobile;
        this.aadharID = aadharID;
        this.voterID = voterID;
    }

    public User(String name, String mailID, String mobile, String aadharID, String voterID) {
        this.name = name;
        this.password = password;
        this.mailID = mailID;
        this.mobile = mobile;
        this.aadharID = aadharID;
        this.voterID = voterID;
    }

    public User(long userID, String name, String password) {
        this.name = name;
        this.password = password;
    }

    public long getUserID() {
        return userID;
    }

    public void setUserID(long userID) {
        this.userID = userID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMailID() {
        return mailID;
    }

    public void setMailID(String mailID) {
        this.mailID = mailID;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
    public String getAadharID() {
        return aadharID;
    }

    public void setAadharID(String aadharID) {
        this.aadharID = aadharID;
    }

    public String getVoterID() {
        return voterID;
    }

    public void setVoterID(String voterID) {
        this.voterID = voterID;
    }
}
