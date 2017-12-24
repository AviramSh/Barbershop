package com.esh_tech.aviram.barbershop.JobsHandler;


import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

import com.esh_tech.aviram.barbershop.Constants.SharedPreferencesConstants;
import com.esh_tech.aviram.barbershop.Constants.UserDBConstants;
import com.esh_tech.aviram.barbershop.Database.BarbershopDBHandler;
import com.esh_tech.aviram.barbershop.R;
import com.esh_tech.aviram.barbershop.Utils.DateUtils;
import com.esh_tech.aviram.barbershop.data.Appointment;
import com.esh_tech.aviram.barbershop.data.Customer;
import com.esh_tech.aviram.barbershop.data.Message;
import com.esh_tech.aviram.barbershop.data.SendMail;
import com.esh_tech.aviram.barbershop.views.SplashScreenActivity;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Aviram on 19/11/2017.
 */

public class MJobExecuter extends AsyncTask <Void,Void,String>{

    private static final String TAG = "Programmer";

    @SuppressLint("StaticFieldLeak")
    private Context context;
    private BarbershopDBHandler dbHandler;
    SharedPreferences settings;
    SQLiteDatabase db;

//    SMS
    int MY_PERMISSIONS_REQUEST_SEND_SMS = 28;
    PendingIntent sentPI,deliveredPI;
    BroadcastReceiver smsSentReceiver,smsDeliveredReceiver;


    public MJobExecuter(Context applicationContext) {
        this.context = applicationContext;
    }

    @Override
    protected String doInBackground(Void... voids) {
//        TODO Create an SMS Handler Scheduler for this service.
        String logMessage = "Message Background Task : \n";

        Calendar cSendTime = Calendar.getInstance();
        settings = PreferenceManager.getDefaultSharedPreferences(context);
        dbHandler =  new BarbershopDBHandler(context);
        ArrayList<Message> messages = dbHandler.getAllMessagesRemainder();

        switch (settings.getInt(UserDBConstants.USER_SMS_TIME,2)){
            case 0:
                cSendTime.add(Calendar.HOUR_OF_DAY,-1);
                break;
            case 1:
                cSendTime.add(Calendar.HOUR_OF_DAY,-2);
                break;
            case 2:
                cSendTime.add(Calendar.HOUR_OF_DAY,-4);
                break;
            case 3:
                cSendTime.add(Calendar.HOUR_OF_DAY,-24);
                break;
            default:
                cSendTime.add(Calendar.HOUR_OF_DAY,-2);
                break;
        }




        for (Message index:
             messages) {


            if(index.getExecute_time().after(cSendTime)&&index.getIsMessageExecute()==0){


                Customer customerProfile = dbHandler.getCustomerByID(index.getCustomer_id());
//                Appointment newAppointment =dbHandler.getAppointmentById(index.getAppointment_id());
                logMessage = "Customer Name: "+customerProfile.getName();

                String sendMessage = "";

                if (settings.getBoolean(SharedPreferencesConstants.SYSTEM_DEFAULT_SMS_IS_CHECKED, true)) {

                    logMessage += " Default message ," ;

                    sendMessage += context.getResources().getString(R.string.system_sms_1_add_name) +" "+
                            customerProfile.getName() +" "+
                            context.getResources().getString(R.string.system_sms_2_add_time) +": "+
                            DateUtils.getDateAndTime(dbHandler.getAppointmentById(index.getAppointment_id()).getcDateAndTime()) +" "+
                            context.getResources().getString(R.string.system_sms_3add_business) +" "+
                            settings.getString(UserDBConstants.USER_BUSINESS_NAME, ".");
                } else {
                    logMessage += " custom message ," ;
                    sendMessage += settings.getString(UserDBConstants.USER_DEFAULT_SMS, "Haircut appointment.");
                }

                SmsManager sms = SmsManager.getDefault();
                if(!customerProfile.getPhone().equals("")&&customerProfile.getPhone()!=null)
                    sms.sendTextMessage(customerProfile.getPhone(), null, sendMessage, sentPI, deliveredPI);

//                SendMail sm = new SendMail(
//                        context, customerProfile.getEmail(), context.getResources().getString(R.string.emailSubject), sendMessage);

                //Executing send mail to send etEmail
//                sm.execute();


               if(dbHandler.deleteRemainderById(index.getId())){
                   Log.d(TAG,"Appointment remainder have been deleted.");
               } else{
                   Log.d(TAG,"failed to delete remainder .");
               }

            }else{
//                Toast.makeText(context, "Failed to send customer message remainder", Toast.LENGTH_SHORT).show();
                Log.d(TAG,"failed to send remainder .");
            }


        }

        Log.d(TAG,"Background Long Running Task Finished...");

        return logMessage;
    }


}
