package com.example.e_shop.NewsFeed;

public class PostData {
    private int id;
    private String post_uploader;
    private String full_name;
    private String upload_time;
    private String profile_image;
    private String prod_image;
    private String prod_review_text;

    private String post_title;

    public PostData(int id, String post_uploader, String full_name, String upload_time, String profile_image, String prod_image, String prod_review_text,String post_title) {
        this.id = id;
        this.post_uploader = post_uploader;
        this.full_name = full_name;
        this.upload_time = upload_time;
        this.profile_image = profile_image;
        this.prod_image = prod_image;
        this.prod_review_text = prod_review_text;
        this.post_title=post_title;
    }

    public PostData(int id,String upload_time,String prod_image, String prod_review_text,String post_title) {
        this.id = id;
        this.upload_time = upload_time;
        this.prod_image = prod_image;
        this.prod_review_text = prod_review_text;
        this.post_title=post_title;
    }

    public int getId() {
        return id;
    }

    public String getPost_uploader() {
        return post_uploader;
    }

    public String getFull_name() {
        return full_name;
    }

    public String getUpload_time() {
        return upload_time;
    }

    public String getProfile_image() {
        return profile_image;
    }

    public String getProd_image() {
        return prod_image;
    }

    public String getProd_review_text() {
        return prod_review_text;
    }

    public String getPost_title() {
        return post_title;
    }
}
