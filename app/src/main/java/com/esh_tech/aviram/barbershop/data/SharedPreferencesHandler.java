package com.esh_tech.aviram.barbershop.data;

/**
 * Created by Aviram on 29/07/2017.
 */

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.esh_tech.aviram.barbershop.Constants.SharedPreferencesConstants;
public class SharedPreferencesHandler {



    SharedPreferences settings;

    SharedPreferences.Editor editor;
    public SharedPreferencesHandler(Context context) {
        this.settings = PreferenceManager.getDefaultSharedPreferences(context);
    }


    public String getData() {
        return "";
    }

    public void setData(String lable , String data) {

        editor = settings.edit();



        editor.apply();
    }




}
