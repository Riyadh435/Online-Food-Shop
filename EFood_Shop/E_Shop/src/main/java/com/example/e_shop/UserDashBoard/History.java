package com.example.e_shop.UserDashBoard;

import java.sql.Date;

public class History {


    private Double total;
    private Date date;
    private String productDetails;

    private String purchaseTime;

    private String orderType;

    private Double Amount;
    private String Time;


    public History(Double total, Date date, String productDetails, String purchaseTime) {
        this.total = total;
        this.date = date;
        this.productDetails = productDetails;
        this.purchaseTime = purchaseTime;
    }


    public History(String Time, Date date,Double Amount){
        this.Time=Time;
        this.date=date;
        this.Amount=Amount;
    }

    public Double getTotal() {
        return total;
    }

    public Date getDate() {
        return date;
    }

    public String getProductDetails() {
        return productDetails;
    }

    public String getPurchaseTime() {
        return purchaseTime;
    }

    public String getOrderType() {
        return orderType;
    }

    public Double getAmount() {
        return Amount;
    }

    public String getTime() {
        return Time;
    }

}
