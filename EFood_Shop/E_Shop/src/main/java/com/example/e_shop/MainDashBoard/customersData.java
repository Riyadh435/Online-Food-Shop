package com.example.e_shop.MainDashBoard;

import java.sql.Date;

public class customersData {


    private String prod_id;
    private String prod_name;
    private String quantity;
    private String price;
    private String date;
    private String buyer;
    private String time;

    public customersData(String prod_id, String prod_name, String quantity, String price, String date, String buyer, String time) {
        this.prod_id = prod_id;
        this.prod_name = prod_name;
        this.quantity = quantity;
        this.price = price;
        this.date = date;
        this.buyer = buyer;
        this.time = time;
    }

    public String getProd_id() {
        return prod_id;
    }

    public String getProd_name() {
        return prod_name;
    }

    public String getQuantity() {
        return quantity;
    }

    public String getPrice() {
        return price;
    }

    public String getDate() {
        return date;
    }

    public String getBuyer() {
        return buyer;
    }

    public String getTime() {
        return time;
    }
}