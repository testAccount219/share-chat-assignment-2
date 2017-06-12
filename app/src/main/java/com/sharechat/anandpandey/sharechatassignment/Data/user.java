package com.sharechat.anandpandey.sharechatassignment.Data;

/**
 * Created by anandpandey on 12/06/17.
 */

public class user {

    private long id;
    private String type;
    private String authorName;
    private String authorDob;
    private String authorGender;
    private String authorStatus;
    private String profileUrl;
    private String authorContact;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getAuthorDob() {
        return authorDob;
    }

    public void setAuthorDob(String authorDob) {
        this.authorDob = authorDob;
    }

    public String getAuthorGender() {
        return authorGender;
    }

    public void setAuthorGender(String authorGender) {
        this.authorGender = authorGender;
    }

    public String getAuthorStatus() {
        return authorStatus;
    }

    public void setAuthorStatus(String authorStatus) {
        this.authorStatus = authorStatus;
    }

    public String getProfileUrl() {
        return profileUrl;
    }

    public void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
    }

    public user(long id, String type, String authorName, String authorDob, String authorGender, String authorStatus, String profileUrl, String authorContact) {
        this.id = id;
        this.type = type;
        this.authorName = authorName;
        this.authorDob = authorDob;
        this.authorGender = authorGender;
        this.authorStatus = authorStatus;
        this.profileUrl = profileUrl;
        this.authorContact = authorContact;
    }

    public String getAuthorContact() {
        return authorContact;
    }

    public void setAuthorContact(String authorContact) {
        this.authorContact = authorContact;
    }

    public user() {
    }
}
