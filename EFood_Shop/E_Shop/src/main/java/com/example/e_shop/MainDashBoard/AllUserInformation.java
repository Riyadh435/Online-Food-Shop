package com.example.e_shop.MainDashBoard;

public class AllUserInformation {

    private String Name;
    private String Email;
    private String blockStatus;

    public AllUserInformation(String Name, String Email,String blockStatus) {
        this.Name= Name;
        this.Email=Email;
        this.blockStatus = blockStatus;
    }

    public AllUserInformation(String Name, String Email) {
        this.Name= Name;
        this.Email=Email;
    }

    public String getBlockStatus() {
        return blockStatus;
    }

    public String getEmail() {
        return Email;
    }

    public String getName() {
        return Name;
    }
}
