//package com.esh_tech.aviram.barbershop;
//
//import android.graphics.Bitmap;
//
///**
// * Created by AVIRAM on 25/04/2017.
// */
//
//public class Appointment {
//
//    private int appointmentID;
//
//    private int year;
//    private int month;
//    private int  day;
//
//    private int  hour;
//    private int minutes;
//
//    private int haircutTime;
//
//    private String customerName;
//    private int customerID;
//
////    Constructor
//    public Appointment(int appointmentID, int year, int month, int day, int hour, int minutes, int haircutTime, String customerName, int customerID) {
//        this.appointmentID = appointmentID;
//        this.year = year;
//        this.month = month;
//        this.day = day;
//        this.hour = hour;
//        this.minutes = minutes;
//        this.haircutTime = haircutTime;
//        this.customerName = customerName;
//        this.customerID = customerID;
//    }
//
//    public Appointment() {
//
//    }
//
//    public Appointment(int minutes, int hour, int day,int month, int year, String customerName, int customerID) {
//        this.minutes = minutes;
//        this.hour = hour;
//        this.day = day;
//        this.month = month;
//        this.year = year;
//        this.customerName = customerName;
//        this.customerID = customerID;
//    }
//
//
//    //    Getter and Setter
//    public int getAppointmentID() {
//        return appointmentID;
//    }
//
//    public void setAppointmentID(int appointmentID) {
//        this.appointmentID = appointmentID;
//    }
//
//    public int getYear() {
//        return year;
//    }
//
//    public void setYear(int year) {
//        this.year = year;
//    }
//
//    public int getMonth() {
//        return month;
//    }
//
//    public void setMonth(int month) {
//        this.month = month;
//    }
//
//    public int getDay() {
//        return day;
//    }
//
//    public void setDay(int day) {
//        this.day = day;
//    }
//
//    public int getHour() {
//        return hour;
//    }
//
//    public void setHour(int hour) {
//        this.hour = hour;
//    }
//
//    public int getMinutes() {
//        return minutes;
//    }
//
//    public void setMinutes(int minutes) {
//        this.minutes = minutes;
//    }
//
//    public int getHaircutTime() {
//        return haircutTime;
//    }
//
//    public void setHaircutTime(int haircutTime) {
//        this.haircutTime = haircutTime;
//    }
//
//    public String getCustomerName() {
//        return customerName;
//    }
//
//    public void setCustomerName(String customerName) {
//        this.customerName = customerName;
//    }
//
//    public int getCustomerID() {
//        return customerID;
//    }
//
//    public void setCustomerID(int customerID) {
//        this.customerID = customerID;
//    }
//
//
//    /**
//     * Created by AVIRAM on 22/03/2017.
//     */
//
//    public static class Customer {
//
//
//        private int id;
//        private String name;
//        private String phone;
//        private String secondPhone;
//        private String email;
//        private int bill;
//        private boolean gender =true;
//        private boolean remainder;
//        private Bitmap customerPhoto;
//
//
//        //pic , date , last time
//
//
//        //Constructors
//
//
//
//        public Customer(String name, String phone, String secondPhone, String email, int bill, boolean gender, boolean remainder, Bitmap customerPhoto) {
//            this.name = name;
//            this.phone = phone;
//            this.secondPhone = secondPhone;
//            this.email = email;
//            this.bill = bill;
//            this.gender = gender;
//            this.remainder = remainder;
//            this.customerPhoto = customerPhoto;
//        }
//
//        public Customer(String name, String phone) {
//
//            this(name,phone,true);
//        }
//
//        public Customer(String name, String phone, boolean gender) {
//            this.name = name;
//            this.phone = phone;
//            this.gender = gender;
//        }
//
//
//        public Customer() {
//            this("", "", "", "", 0, true, false, null);
//        }
//
//
//        //Getter and Setter
//
//
//        public int getId() {
//            return id;
//        }
//
//        public int getBill() {
//            return bill;
//        }
//
//        public void setBill(int bill) {
//            this.bill = bill;
//        }
//
//        public boolean isRemainder() {
//            return remainder;
//        }
//
//        public void setRemainder(boolean remainder) {
//            this.remainder = remainder;
//        }
//
//        public String getName() {
//            return name;
//        }
//
//        public void setName(String name) {
//            this.name = name;
//        }
//
//        public String getEmail() {
//            return email;
//        }
//
//        public void setEmail(String email) {
//            this.email = email;
//        }
//
//        public Bitmap getCustomerPhoto() {
//            return customerPhoto;
//        }
//
//        public void setCustomerPhoto(Bitmap customerPhoto) {
//            this.customerPhoto = customerPhoto;
//        }
//
//        public String getPhone() {
//            return phone;
//        }
//
//        public void setPhone(String phone) {
//            this.phone = phone;
//        }
//
//        public boolean isGender() {
//            return gender;
//        }
//
//        public void setGender(boolean gender) {
//            this.gender = gender;
//        }
//    }
//
//    /**
//     * Created by AVIRAM on 30/04/2017.
//     */
//
//    public static class Product {
//
//        private int productID;
//
//        private String name;
//        private int quantity;
//        private float price;
//
//
//        public Product(int productID, String name, int quantity, float price) {
//            this.productID = productID;
//            this.name = name;
//            this.quantity = quantity;
//            this.price = price;
//        }
//
//
//
//
//
//        public int getProductID() {
//            return productID;
//        }
//
//        public void setProductID(int productID) {
//            this.productID = productID;
//        }
//
//        public String getName() {
//            return name;
//        }
//
//        public void setName(String name) {
//            this.name = name;
//        }
//
//        public int getQuantity() {
//            return quantity;
//        }
//
//        public void setQuantity(int quantity) {
//            this.quantity = quantity;
//        }
//
//        public float getPrice() {
//            return price;
//        }
//
//        public void setPrice(float price) {
//            this.price = price;
//        }
//    }
//}

package com.esh_tech.aviram.barbershop.data;



import java.sql.Time;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by AVIRAM on 27/05/2017.
 */

public class Appointment {

    private int _id;

    private Date dateAndTime;

    private Time theTime;
    private int haircutTime;
    private int customerID;
    private int tackAnHaircut;


    //      Constructors.

    public Appointment(Date dateAndTime, int customerID) {
        this.dateAndTime = dateAndTime;
        this.customerID = customerID;
    }
    public Appointment(Calendar dateAndTime, int customerID) {
        setDateAndTime(dateAndTime);
        this.customerID = customerID;
    }

    public Appointment(int _id, String dateAndTime, int customerID,int tackAnHaircut) {
        this._id = _id;
        setDateAndTime(dateAndTime);
        this.customerID = customerID;
        this.tackAnHaircut =tackAnHaircut;
    }

    public Appointment() {
        this(-1,"",-1,0);
    }





//    Getters and Setters

    public int getTackAnHaircut() {
        return tackAnHaircut;
    }

    public void setTackAnHaircut(int tackAnHaircut) {
        this.tackAnHaircut = tackAnHaircut;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    //Getter And Setter.
    public void setDateAndTime(Date dateAndTime) {
        this.dateAndTime = dateAndTime;
    }

    public void setDateAndTime(Calendar dateAndTime) {
        this.dateAndTime = dateAndTime.getTime();
    }

//    new handler in DateHandler
    public void setDateAndTime(String setDate){
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

        this.dateAndTime = new DateHandler().getDateObjectByString(setDate);
    }

    public Date getDateAndTime() {
        return dateAndTime;
    }

    public String getTime(){
//        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
//        String newFormat = formatter.format(dateAndTime.getTimeSDF());
//        return newFormat;
        return new DateHandler().getTimeSDF(dateAndTime);
    }
    public String getDateAndTimeToDisplay() {
        return new DateHandler().getFullSDF(dateAndTime);
//        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm",Locale.getDefault());
//        String newFormat = formatter.format(dateAndTime.getTimeSDF());
//        return newFormat;
    }

    //    public void setTheDate(Calendar c1) {
//        Date date = c1.getTimeSDF();
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm");
//
//        String myDate= null;
//
//        myDate = sdf.format(date);
//
//        this.theDate = myDate;
//    }

    public int getCustomerID() {
        return customerID;
    }

    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

}
