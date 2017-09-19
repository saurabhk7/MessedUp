package com.messedup.messedup.mess_menu_descriptor;

/**
 * Created by tanmaysinghal98 on 14/09/17.
 */

public class MessInfo {

    private String Name;
    private String GuestCharge;
    private String LunchOpen;
    private String LunchClose;
    private String DinnerOpen;
    private String DinnerClose;
    private String MonthlyCharge;
    private String Contact;
    private String Address;
    private String Location;
    private String NBCollege;
    private String Owner;

    public MessInfo(String name, String guestCharge, String lunchOpen, String lunchClose, String dinnerOpen, String dinnerClose, String monthlyCharge, String contact, String address, String location, String NBCollege, String owner) {
        Name = name;
        GuestCharge = guestCharge;
        LunchOpen = lunchOpen;
        LunchClose = lunchClose;
        DinnerOpen = dinnerOpen;
        DinnerClose = dinnerClose;
        MonthlyCharge = monthlyCharge;
        Contact = contact;
        Address = address;
        Location = location;
        this.NBCollege = NBCollege;
        Owner = owner;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getGuestCharge() {
        return GuestCharge;
    }

    public void setGuestCharge(String guestCharge) {
        GuestCharge = guestCharge;
    }

    public String getLunchOpen() {
        return LunchOpen;
    }

    public void setLunchOpen(String lunchOpen) {
        LunchOpen = lunchOpen;
    }

    public String getLunchClose() {
        return LunchClose;
    }

    public void setLunchClose(String lunchClose) {
        LunchClose = lunchClose;
    }

    public String getDinnerOpen() {
        return DinnerOpen;
    }

    public void setDinnerOpen(String dinnerOpen) {
        DinnerOpen = dinnerOpen;
    }

    public String getDinnerClose() {
        return DinnerClose;
    }

    public void setDinnerClose(String dinnerClose) {
        DinnerClose = dinnerClose;
    }

    public String getMonthlyCharge() {
        return MonthlyCharge;
    }

    public void setMonthlyCharge(String monthlyCharge) {
        MonthlyCharge = monthlyCharge;
    }

    public String getContact() {
        return Contact;
    }

    public void setContact(String contact) {
        Contact = contact;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public String getNBCollege() {
        return NBCollege;
    }

    public void setNBCollege(String NBCollege) {
        this.NBCollege = NBCollege;
    }

    public String getOwner() {
        return Owner;
    }

    public void setOwner(String owner) {
        Owner = owner;
    }
}
