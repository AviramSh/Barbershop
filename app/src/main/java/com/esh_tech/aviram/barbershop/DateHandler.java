package com.esh_tech.aviram.barbershop;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by AVIRAM on 24/06/2017.
 */

public class DateHandler {


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
    public int compareDates(Date date1, Date date2) {
        // if you already have date objects then skip 1
        //1

        //1

        //date object is having 3 methods namely after,before and equals for comparing
        //after() will return true if and only if date1 is after date 2
//        1 = start is before end
//        2 = start is after end
//        0 = start is same as end
        if (date1.after(date2)) {
            return 2;
//            System.out.println("Date1 is after Date2");
        }

        //before() will return true if and only if date1 is before date2
        if (date1.before(date2)) {
            return 1;
//            System.out.println("Date1 is before Date2");
        }

        //equals() returns true if both the dates are equal
        if (date1.equals(date2)) {
            return 0;
//            System.out.println("Date1 is equal Date2");
        }

        return -1;
    }
    public int compareDates(Calendar date1, Calendar date2) {
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


    public String getFullSDF(Date dateAndTime) {

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm",Locale.getDefault());
        return formatter.format(dateAndTime.getTime());
    }
    public String getFullSDF(Calendar dateAndTime) {
        return  getFullSDF(dateAndTime.getTime());
    }

    public String getOnlyDateSDF(Date dateAndTime) {

        SimpleDateFormat sdf = new SimpleDateFormat(
                "dd/MM/yyyy", Locale.getDefault());
        return sdf.format(dateAndTime.getTime());
    }
    public String getOnlyDateSDF(Calendar dateAndTime) {
        return  getOnlyDateSDF(dateAndTime.getTime());
    }

    public String getTimeSDF(Date myDate){
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm" ,Locale.getDefault());
        return formatter.format(myDate);
    }
    public String getTimeSDF(Calendar myDate){
        return getTimeSDF(myDate.getTime());
    }
}