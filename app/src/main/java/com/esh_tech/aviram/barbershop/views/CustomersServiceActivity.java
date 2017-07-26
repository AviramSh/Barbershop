package com.esh_tech.aviram.barbershop.views;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.esh_tech.aviram.barbershop.Database.BarbershopDBHandler;
import com.esh_tech.aviram.barbershop.R;
import com.esh_tech.aviram.barbershop.data.*;
import java.util.ArrayList;

public class CustomersServiceActivity extends AppCompatActivity implements View.OnClickListener{


    //    Database
    BarbershopDBHandler dbHandler;

    Customer customerProfile;
    EditText etMessage;
    String message;

    int MY_PERMISSIONS_REQUEST_SEND_SMS = 1;
    String sent = "SMS_SENT";
    String delivered = "SMS_DELIVERED";
    PendingIntent sentPI,deliveredPI;
    BroadcastReceiver smsSentReceiver,smsDeliveredReceiver;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customers_service);

        init();
    }

    private void init() {
//      TODO Initialized check Boxes  Birthday \ lest seeing messages

        dbHandler = new BarbershopDBHandler(this);
        customerProfile =new Customer();
        etMessage = (EditText)findViewById(R.id.etMessage);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.btSend:
                sandMessageToAllCustomers();
                break;

            case R.id.btClose:
                this.finish();
                break;

//            TODO Create customers Email report

            default:
                Toast.makeText(this, "Not Initialized yet", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void sandMessageToAllCustomers() {
        message = etMessage.getText().toString();
        Toast.makeText(this, message+"", Toast.LENGTH_SHORT).show();

        ArrayList<Customer> allCustomers = new ArrayList<>();
        allCustomers = dbHandler.getAllCustomers();

        for (Customer index :
                allCustomers) {
            customerProfile = index;
            sandSms();
        }

//        sandSms();
    }

    private void sandSms() {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this , new String[]{Manifest.permission.SEND_SMS},
                    MY_PERMISSIONS_REQUEST_SEND_SMS);
        }else {
            SmsManager sms  = SmsManager.getDefault();
            sms.sendTextMessage(customerProfile.getPhone(),null,message,sentPI,deliveredPI);
//            sms.sendTextMessage(etPhone.getText().toString(),null,message,sentPI,deliveredPI);
        }
    }

    @Override
    protected void onResume(){
        super.onResume();

        smsSentReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                switch (getResultCode()){
                    case Activity.RESULT_OK:
                        Toast.makeText(CustomersServiceActivity.this, R.string.smsDelivered, Toast.LENGTH_LONG).show();
                        Intent myIntent1 = new Intent(CustomersServiceActivity.this,MainActivity.class);
                        startActivity(myIntent1);

                        break;

                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        Toast.makeText(CustomersServiceActivity.this, R.string.genericFailure, Toast.LENGTH_LONG).show();
                        break;

                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        Toast.makeText(CustomersServiceActivity.this, R.string.noService, Toast.LENGTH_LONG).show();
                        break;

                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        Toast.makeText(CustomersServiceActivity.this, R.string.nullPDU, Toast.LENGTH_LONG).show();
                        break;

                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        Toast.makeText(CustomersServiceActivity.this, R.string.radioOff, Toast.LENGTH_LONG).show();
                        break;
                }
            }
        };
        smsDeliveredReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        Toast.makeText(CustomersServiceActivity.this, R.string.smsDelivered, Toast.LENGTH_LONG).show();
                        break;

                    case Activity.RESULT_CANCELED:
                        Toast.makeText(CustomersServiceActivity.this, R.string.smsNotDelivered, Toast.LENGTH_LONG).show();
                        break;

                }
            }
        };

        registerReceiver(smsSentReceiver,new IntentFilter(sent));
        registerReceiver(smsDeliveredReceiver,new IntentFilter(delivered));
    }

    @Override
    protected void onPause() {
        super.onPause();

        unregisterReceiver(smsDeliveredReceiver);
        unregisterReceiver(smsSentReceiver);
    }
}
