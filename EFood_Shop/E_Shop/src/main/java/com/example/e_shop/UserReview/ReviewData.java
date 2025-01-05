package com.example.e_shop.UserReview;

public class ReviewData {
    private Integer id;
    private String reviewer_username;
    private String reviewer_fullname;
    private String reviewer_profilePic;
    private String prod_id;
    private String prod_name;
    private String prod_review;
    private String prod_image;
    private String rv_title;
    private String rc;

    public ReviewData(Integer id, String reviewer_username, String reviewer_fullname,
                      String reviewer_profilePic, String prod_id, String prod_name,
                      String prod_review, String prod_image,String rv_title,String rc) {
        this.id = id;
        this.reviewer_username = reviewer_username;
        this.reviewer_fullname = reviewer_fullname;
        this.reviewer_profilePic = reviewer_profilePic;
        this.prod_id = prod_id;
        this.prod_name = prod_name;
        this.prod_review = prod_review;
        this.prod_image=prod_image;
        this.rv_title=rv_title;
        this.rc=rc;
    }

    public Integer getId() {
        return id;
    }

    public String getReviewer_username() {
        return reviewer_username;
    }

    public String getReviewer_fullname() {
        return reviewer_fullname;
    }

    public String getReviewer_profilePic() {
        return reviewer_profilePic;
    }

    public String getProd_id() {
        return prod_id;
    }

    public String getProd_name() {
        return prod_name;
    }

    public String getProd_review() {
        return prod_review;
    }

    public String getProd_image() {
        return prod_image;
    }

    public String getRv_title() {
        return rv_title;
    }

    public String getRc() {
        return rc;
    }
}
