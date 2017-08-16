package com.esh_tech.aviram.barbershop.Utils;

import android.content.Context;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by AVIRAM on 24/06/2017.
 */

public class DateUtils {

//    public static final String dateFormat = "dd/MM/yyyy HH:mm";
    public static final String dateDayNameFormat ="dd/MM/yyyy \n EEEE";
    public static final String dateFormat = "dd/MM/yyyy";
    public static final String timeFormat = "HH:mm";
    public static final String timeDateFormat = "HH:mm dd/MM/yyyy";
    public static final String reportDateFormat = "HH:mm dd/MM/yyyy";


    public String getImageName(){


        return getFullSDF(new Date())+".jpg";
    }
//        0 = start == end
//        1 = start < end
//        2 = start > end

//   Compare Dates
    public int compareDates(String d1, String d2) {
        try {
            // If you already have date objects then skip 1

            //1
            // Create 2 dates starts
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
            Date date1 = sdf.parse(d1);
            Date date2 = sdf.parse(d2);

//        1 = start is before end
//        2 = start is after end
//        0 = start is same as end

            //1

            // Date object is having 3 methods namely after,before and equals for comparing
            // after() will return true if and only if date1 is after date 2
            if (date1.after(date2)) {
                return 2;
            }
            // before() will return true if and only if date1 is before date2
            if (date1.before(date2)) {
                return 1;
//                System.out.println("Date1 is before Date2");
            }

            //equals() returns true if both the dates are equal
            if (date1.equals(date2)) {
                return 0;
//                System.out.println("Date1 is equal Date2");
            }

        } catch (ParseException ex) {
            ex.printStackTrace();
            return -1;
        }
        return -1;
    }
    public static int compareDates(Date date1, Date date2) {
        // if you already have date objects then skip 1
        //1

        //1

        //date object is having 3 methods namely after,before and equals for comparing
        //after() will return true if and only if date1 is after date 2
//        1 = start is before end
//        2 = start is after end
//        0 = start is same as end

        //before() will return true if and only if date1 is before date2
        if (date1.before(date2)) {
            return 1;
//            System.out.println("Date1 is before Date2");
        }

        //equals() returns true if both the dates are equal
        if (date1.equals(date2) ||
                getFullSDF(date1).equals(getFullSDF(date2))) {
            return 0;
//            System.out.println("Date1 is equal Date2");
        }
        if (date1.after(date2)) {
            return 2;
//            System.out.println("Date1 is after Date2");
        }

        return -1;
    }
    public static String compareDatesAppointments(Context context ,Date date1, Date date2) {
        // if you already have date objects then skip 1
        //1

        //1

        //date object is having 3 methods namely after,before and equals for comparing
        //after() will return true if and only if date1 is after date 2
//        1 = start is before end
//        2 = start is after end
//        0 = start is same as end

        //before() will return true if and only if date1 is before date2
        if (date1.before(date2)) {
            return "1";
//            System.out.println("Date1 is before Date2");
        }

        //equals() returns true if both the dates are equal
        if (date1.equals(date2)) {
            return "0";
//            System.out.println("Date1 is equal Date2");
        }
        if (date1.after(date2)) {
            return "0";
//            System.out.println("Date1 is after Date2");
        }

        return "failed";
    }
    public static int compareDates(Calendar date1, Calendar date2) {
        return compareDates(date1.getTime(),date2.getTime());
    }



//    Dates format.
    public Date getDateObjectByString(String setDate){

        SimpleDateFormat sdf = new SimpleDateFormat(
                "dd/MM/yyyy HH:mm", Locale.getDefault());
        Date testDate = null;
        try {
            testDate = sdf.parse(setDate);
        }catch(Exception ex){
            ex.printStackTrace();
        }
//        SimpleDateFormat formatter = new SimpleDateFormat("dd/mm/yyyy HH:mm:");
//        String newFormat = formatter.format(testDate);
        return testDate;
    }


    public static String getFullSDF(Date dateAndTime) {

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm",Locale.getDefault());
        return formatter.format(dateAndTime.getTime());
    }
    public static String getFullSDF(Calendar dateAndTime) {
        return  getFullSDF(dateAndTime.getTime());
    }

    public String getOnlyDateSDF(Date dateAndTime) {

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        return sdf.format(dateAndTime.getTime());
    }
    public String getOnlyDateSDF(Calendar dateAndTime) {
        return  getOnlyDateSDF(dateAndTime.getTime());
    }

    public static String getTimeSDF(Date myDate){
        SimpleDateFormat formatter = new SimpleDateFormat(timeFormat ,Locale.getDefault());
        return formatter.format(myDate);
    }
    public String getTimeSDF(Calendar myDate){
        return getTimeSDF(myDate.getTime());
    }


    public static Calendar toCalendar(Date date){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal;
    }

    public static Date getDateFromString(String stringDate)
    {
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.getDefault());
        try {
            return sdf.parse(stringDate);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Date getTimeDateByString(String stringDate)
    {
        SimpleDateFormat sdf = new SimpleDateFormat(timeFormat, Locale.getDefault());
        Date d1 = Calendar.getInstance().getTime();
        try {
            d1.setTime(sdf.parse(stringDate).getTime());
            return d1;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getStringFromDate(Date date)
    {
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat,Locale.getDefault());
        return sdf.format(date);
    }

    public static String convertDateForReport(String stringDate)
    {
        Date date = getDateFromString(stringDate);
        if(date == null)
            return "";
        SimpleDateFormat sdf = new SimpleDateFormat(reportDateFormat , Locale.getDefault());
        return sdf.format(date);
    }


}