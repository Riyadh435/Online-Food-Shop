package com.example.e_shop.MainDashBoard;

import java.sql.Date;

public class productData {

    private Integer id;
    private String productId;
    private String productName;
    private String type;
    private Integer stock;
    private Double price;
    private String status;
    private String image;
    private Date date;
    private Integer quantity;
    private String productDetails;
    private String productParent;
    private String discountStatus;
    private Integer discountPercent;

    private String productUploader;

    // this is for merge data.......
    public productData(Integer id, String productId,
                       String productName, String type, Integer stock,
                       Double price, String status, String image, Date date, String productDetails,String discountStatus,Integer discountPercent) {
        this.id = id;
        this.productId = productId;
        this.productName = productName;
        this.type = type;
        this.stock = stock;
        this.price = price;
        this.status = status;
        this.image = image;
        this.date = date;
        this.productDetails = productDetails;
        this.discountStatus=discountStatus;
        this.discountPercent=discountPercent;
    }
    ///

    public productData(Integer id, String productId, String productName,
                       String type, Integer quantity, Double price, String image, Date date, String productDetails,
                       String productUploader,String productParent,String discountStatus,Integer discountPercent) {
        this.id = id;
        this.productId = productId;
        this.productName = productName;
        this.type = type;
        this.price = price;
        this.image = image;
        this.date = date;
        this.quantity = quantity;
        this.productDetails = productDetails;
        this.productUploader = productUploader;
        this.productParent=productParent;
        this.discountStatus=discountStatus;
        this.discountPercent=discountPercent;
    }


    public productData(Integer id, String productId, String productName,
                       String type, Integer quantity, Double price, Date date, String productUploader,String image) {
        this.id = id;
        this.productId = productId;
        this.productName = productName;
        this.type = type;
        this.price = price;
        this.date = date;
        this.quantity = quantity;
        this.productUploader = productUploader;
        this.image=image;
    }

    public Integer getId() {
        return id;
    }

    public String getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public String getType() {
        return type;
    }

    public Integer getStock() {
        return stock;
    }

    public Double getPrice() {
        return price;
    }

    public String getStatus() {
        return status;
    }

    public String getImage() {
        return image;
    }

    public Date getDate() {
        return date;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public String getProductDetails() {
        return productDetails;
    }

    public String getProductUploader() {
        return productUploader;
    }

    public String getProductParent() {
        return productParent;
    }

    public String getDiscountStatus() {
        return discountStatus;
    }

    public Integer getDiscountPercent() {
        return discountPercent;
    }

    @Override
    public String toString() {
        return "productData{" +
                "id=" + id +
                ", productId='" + productId + '\'' +
                ", productName='" + productName + '\'' +
                ", type='" + type + '\'' +
                ", stock=" + stock +
                ", price=" + price +
                ", status='" + status + '\'' +
                ", image='" + image + '\'' +
                ", date=" + date +
                ", quantity=" + quantity +
                ", productDetails='" + productDetails + '\'' +
                ", productUploader='" + productUploader + '\'' +
                '}';
    }
}



