package com.esh_tech.aviram.barbershop.data;

import android.app.Application;

/**
 * Created by AVIRAM on 20/04/2017.
 */



import android.app.Application;
//
//import com.mobilit.wastetrack.utils.PreferencesUtils;


/*
public class MyGlobalUser extends Application {

    private String role;
    private String password;
    private String username;
    private String email;
    private String siteName;



    private static WasteTrackApp appInstance;

    public static WasteTrackApp getInstance()
    {
        return appInstance;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void loadPreferences()
    {
        username = PreferencesUtils.getPreferenceUserName(this);
        password = PreferencesUtils.getPreferencePassword(this);
        role = PreferencesUtils.getPreferenceRole(this);
        email = PreferencesUtils.getPreferenceEmail(this);
    }
    @Override
    public void onCreate() {
        super.onCreate();
    }
}
*/

//  TODO Marge data
public class MyGlobalUser extends Application {
    private String name;
    private String lastName;
    private String phone;
    private String password;

    private String businessName;
    private String businessPhone;
    private String businessAddress;

    private boolean register;




    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public String getBusinessPhone() {
        return businessPhone;
    }

    public void setBusinessPhone(String businessPhone) {
        this.businessPhone = businessPhone;
    }

    public String getBusinessAddress() {
        return businessAddress;
    }

    public void setBusinessAddress(String businessAddress) {
        this.businessAddress = businessAddress;
    }

    public boolean isRegister() {
        return register;
    }

    public void setRegister(boolean register) {
        this.register = register;
    }
}
