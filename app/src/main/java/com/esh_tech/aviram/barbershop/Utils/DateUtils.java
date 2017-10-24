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

//    public static final String sdf_d = "dd/MM/yyyy HH:mm";


    public static final String sdf_d = "dd/MM/yyyy";
    public static final String sdf_t = "HH:mm";
    public static final String sdf_n = "EEEE";

    public static final String sdf_d_t = "dd/MM/yyyy HH:mm";
    public static final String sdf_d_t_s = "dd/MM/yyyy HH:mm:ss";

    public static final String sdf_d_n ="dd/MM/yyyy \n EEEE";


    public static final String reportDateFormat = "HH:mm dd/MM/yyyy";


//
////    public String getImageName(){
////
////
////        return getFullSDF(new Date())+".jpg";
////    }
////        0 = start == end
////        1 = start < end
////        2 = start > end
//
////   Compare Dates
public int compareDates(Calendar c1, Calendar c2) {

    // If you already have date objects then skip 1

    //1
    // Create 2 dates starts
//            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());

    Calendar calendar_1 = Calendar.getInstance();
    Calendar calendar_2 = Calendar.getInstance();

    calendar_1.setTime(c1.getTime());
    calendar_2.setTime(c2.getTime());

//        1 = start is before end
//        2 = start is after end
//        0 = start is same as end

    //1

    // Date object is having 3 methods namely after,before and equals for comparing
    // after() will return true if and only if date1 is after date 2
    if (calendar_1.after(calendar_2)) {
        return 2;
    }
    // before() will return true if and only if date1 is before date2
    if (calendar_1.before(calendar_2)) {
        return 1;
//                System.out.println("Date1 is before Date2");
    }

    //equals() returns true if both the dates are equal
    if (calendar_1.equals(calendar_2)) {
        return 0;
//                System.out.println("Date1 is equal Date2");
    }


    return -1;
}
////    public static int compareDates(Date date1, Date date2) {
////        // if you already have date objects then skip 1
////        //1
////
////        //1
////
////        //date object is having 3 methods namely after,before and equals for comparing
////        //after() will return true if and only if date1 is after date 2
//////        1 = start is before end
//////        2 = start is after end
//////        0 = start is same as end
////
////        //before() will return true if and only if date1 is before date2
////        if (date1.before(date2)) {
////            return 1;
//////            System.out.println("Date1 is before Date2");
////        }
////
////        //equals() returns true if both the dates are equal
////        if (date1.equals(date2) ||
////                getFullSDF(date1).equals(getFullSDF(date2))) {
////            return 0;
//////            System.out.println("Date1 is equal Date2");
////        }
////        if (date1.after(date2)) {
////            return 2;
//////            System.out.println("Date1 is after Date2");
////        }
////
////        return -1;
////    }
////    public static String compareDatesAppointments(Context context ,Date date1, Date date2) {
////        // if you already have date objects then skip 1
////        //1
////
////        //1
////
////        //date object is having 3 methods namely after,before and equals for comparing
////        //after() will return true if and only if date1 is after date 2
//////        1 = start is before end
//////        2 = start is after end
//////        0 = start is same as end
////
////        //before() will return true if and only if date1 is before date2
////        if (date1.before(date2)) {
////            return "1";
//////            System.out.println("Date1 is before Date2");
////        }
////
////        //equals() returns true if both the dates are equal
////        if (date1.equals(date2)) {
////            return "0";
//////            System.out.println("Date1 is equal Date2");
////        }
////        if (date1.after(date2)) {
////            return "0";
//////            System.out.println("Date1 is after Date2");
////        }
////
////        return "failed";
////    }
////    public static int compareDates(Calendar date1, Calendar date2) {
////        return compareDates(date1.getTime(),date2.getTime());
////    }
//
//
//
////    Dates format.
//    public Date getDateObjectByString(String setDate){
//
//        SimpleDateFormat sdf = new SimpleDateFormat(
//                "dd/MM/yyyy HH:mm", Locale.getDefault());
//        Date testDate = null;
//        try {
//            testDate = sdf.parse(setDate);
//        }catch(Exception ex){
//            ex.printStackTrace();
//        }
////        SimpleDateFormat formatter = new SimpleDateFormat("dd/mm/yyyy HH:mm:");
////        String newFormat = formatter.format(testDate);
//        return testDate;
//    }
//
//
//    public static String getFullSDF(Date dateAndTime) {
//
//        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm",Locale.getDefault());
//        return formatter.format(dateAndTime.getTime());
//    }
//    public static String getFullSDF(Calendar dateAndTime) {
//        return  getFullSDF(dateAndTime.getTime());
//    }
//
//    public String getOnlyDateSDF(Date dateAndTime) {
//
//        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
//        return sdf.format(dateAndTime.getTime());
//    }
//    public String getOnlyDateSDF(Calendar dateAndTime) {
//        return  getOnlyDateSDF(dateAndTime.getTime());
//    }
//
//    public static String getTimeSDF(Date myDate){
//        SimpleDateFormat formatter = new SimpleDateFormat(sdf_t,Locale.getDefault());
//        return formatter.format(myDate);
//    }
////    public String getTimeSDF(Calendar myDate){
////        return getTimeSDF(myDate.getTime());
////    }
//
//    public static String getDateDaySDF(Calendar convertTime){
//        SimpleDateFormat formatter = new SimpleDateFormat(sdf_d_n,Locale.getDefault());
//        return formatter.format(convertTime.getTime());
//    }
//
//
////    public static Calendar toCalendar(Date date){
////        Calendar cal = Calendar.getInstance();
////        cal.setTime(date);
////        return cal;
////    }
//
//    public static Date getDateFromString(String stringDate)
//    {
//        SimpleDateFormat sdf = new SimpleDateFormat(sdf_d, Locale.getDefault());
//        try {
//            return sdf.parse(stringDate);
//        } catch (ParseException e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
//
////    public static Date getTimeDateByString(String stringDate)
//    {
//        SimpleDateFormat sdf = new SimpleDateFormat(sdf_t, Locale.getDefault());
//        Date d1 = Calendar.getInstance().getTime();
//        try {
//            d1.setTime(sdf.parse(stringDate).getTime());
//            return d1;
//        } catch (ParseException e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
//
//    public static String getStringFromDate(Date date)
//    {
//        SimpleDateFormat sdf = new SimpleDateFormat(sdf_d,Locale.getDefault());
//        return sdf.format(date);
//    }
//
//    public static String getTheDayName(Date date)
//    {
//        SimpleDateFormat sdf = new SimpleDateFormat(sdf_n,Locale.getDefault());
//        return sdf.format(date);
//    }

//    public static String convertDateForReport(String stringDate){
//        Date date = getDateFromString(stringDate);
//        if(date == null)
//            return "";
//        SimpleDateFormat sdf = new SimpleDateFormat(reportDateFormat , Locale.getDefault());
//        return sdf.format(date);
//    }






//    New


    public static String getDateAndTime(Calendar calendar){
        SimpleDateFormat formatter = new SimpleDateFormat(sdf_d_t, Locale.getDefault());
        return formatter.format(calendar.getTime());
    }



    //  Get Or Date Or Time Or Day
    public static String getDateAndName(Calendar calendar) {
        SimpleDateFormat formatter = new SimpleDateFormat(sdf_d_n, Locale.getDefault());
        return formatter.format(calendar.getTime());
    }
    public static String getOnlyDate(Calendar calendar){
        SimpleDateFormat formatter = new SimpleDateFormat(sdf_d, Locale.getDefault());
        return formatter.format(calendar.getTime());
    }
    public static String getOnlyTime(Calendar calendar){
        SimpleDateFormat formatter = new SimpleDateFormat(sdf_t, Locale.getDefault());
        return formatter.format(calendar.getTime());
    }
    public static String getOnlyDay(Calendar calendar) {
        SimpleDateFormat formatter = new SimpleDateFormat(sdf_d, Locale.getDefault());
        return formatter.format(calendar.getTime());
    }


    public static Calendar getCalendar(String sdfToFormat){

        try {
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat formatter = new SimpleDateFormat(sdf_d_t, Locale.getDefault());
            cal.setTime(formatter.parse(sdfToFormat));
            return cal;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }

    }


    public static Calendar getCalendarFromDB(String sdfToFormat){

        try {
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat formatter = new SimpleDateFormat(sdf_d_t, Locale.getDefault());
            cal.setTime(formatter.parse(sdfToFormat));
            return cal;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }

    }
    public static String setCalendarToDB(Calendar calendar){
        SimpleDateFormat formatter = new SimpleDateFormat(sdf_d_t, Locale.getDefault());
        return formatter.format(calendar.getTime());
    }


    public static Calendar getPicCalendar(String sdfToFormat){

        try {
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat formatter = new SimpleDateFormat(sdf_d_t_s, Locale.getDefault());
            cal.setTime(formatter.parse(sdfToFormat));
            return cal;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }

    }
    public static String getPicName(Calendar calendar){
        SimpleDateFormat formatter = new SimpleDateFormat(sdf_d_t, Locale.getDefault());
        return formatter.format(calendar.getTime());
    }

}