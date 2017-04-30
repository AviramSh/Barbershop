package com.esh_tech.aviram.barbershop.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.esh_tech.aviram.barbershop.Codes.Barbershop;

/**
 * Created by AVIRAM on 22/04/2017.
 */

public class MySQLiteHelper extends SQLiteOpenHelper {


    public MySQLiteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE "+ BarbershopConstants.CUSTOMERS_TABLE_NAME +
                    "("+BarbershopConstants.CUSTOMER_ID +" INTEGER AUTOINCREMENT, "+
                        BarbershopConstants.CUSTOMER_NAME +" TEXT, "+
                        BarbershopConstants.CUSTOMER_PHONE +" TEXT, "+
                        BarbershopConstants.CUSTOMER_DAY_OF_BIRTH +" TEXT, "+
                        BarbershopConstants.CUSTOMER_MONTH_OF_BIRTH +" TEXT, "+
                        BarbershopConstants.CUSTOMER_YEAR_OF_BIRTH +" TEXT) ");


        db.execSQL("CREATE TABLE "+ BarbershopConstants.APPOINTMENTS_TABLE_NAME+
                "("+BarbershopConstants.APPOINTMENT_ID +" INTEGER, "+
                BarbershopConstants.APPOINTMENT_HOUR +" INTEGER, "+
                BarbershopConstants.APPOINTMENT_DAY +" INTEGER, "+
                BarbershopConstants.APPOINTMENT_MONTH +" INTEGER, "+
                BarbershopConstants.APPOINTMENT_YEAR+" INTEGER, "+
                BarbershopConstants.CUSTOMER_ID +" INTEGER, "+
                BarbershopConstants.CUSTOMER_NAME +" TEXT) ");
    }



    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
