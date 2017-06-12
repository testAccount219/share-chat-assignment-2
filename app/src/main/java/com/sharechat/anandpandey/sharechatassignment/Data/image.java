package com.sharechat.anandpandey.sharechatassignment.Data;

/**
 * Created by anandpandey on 12/06/17.
 */

public class image {
    private long id;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    private String type;

    public image(long id, String authorName, String url, long postedOn, String type) {
        this.id = id;
        this.authorName = authorName;
        this.url = url;
        this.postedOn = postedOn;
        this.type = type;
    }
    public image() {
    }

    private String authorName;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public long getPostedOn() {
        return postedOn;
    }

    public void setPostedOn(long postedOn) {
        this.postedOn = postedOn;
    }

    private String url;
    private long postedOn;

}
