package com.esh_tech.aviram.barbershop.data;

import com.esh_tech.aviram.barbershop.Utils.DateUtils;

import java.util.Calendar;

/**
 * Created by Aviram on 27/11/2017.
 */

public class Message {

    private int id;
    private int customer_id;
    private int appointment_id;
    private int message_execute;
    private Calendar execute_time;


    public Message() {
        this(0,0,0,0, Calendar.getInstance());
    }

    public Message(int id, int customer_id, int appointment_id , int message_execute, Calendar execute_time ) {
        this.id = id;
        this.customer_id = customer_id;
        this.appointment_id = appointment_id;
        this.execute_time = execute_time;
        this.message_execute =message_execute;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(int customer_id) {
        this.customer_id = customer_id;
    }

    public int getAppointment_id() {
        return appointment_id;
    }

    public void setAppointment_id(int appointment_id) {
        this.appointment_id = appointment_id;
    }

    public Calendar getExecute_time() {
        return execute_time;
    }

    public void setExecute_time(Calendar execute_time) {
        this.execute_time = execute_time;
    }

    public int getIsMessageExecute() {
        return message_execute;
    }

    public void setMessageExecute(int message_execute) {
        this.message_execute = message_execute;
    }
}
