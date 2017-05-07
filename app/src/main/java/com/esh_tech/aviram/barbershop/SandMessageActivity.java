package com.esh_tech.aviram.barbershop;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class SandMessageActivity extends AppCompatActivity {

    EditText etPhone;
    EditText etMessage;


//    SMS
    int MY_PERMISSIONS_REQUEST_SEND_SMS = 1;
    String sent = "SMS_SENT";
    String delivered = "SMS_DELIVERED";
    PendingIntent sentPI,deliveredPI;
    BroadcastReceiver smsSentReceiver,smsDeliveredReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sand_message);

        Intent myIntent =getIntent();
//        Toast.makeText(this, myIntent.getStringExtra("userPhone"), Toast.LENGTH_SHORT).show();

        etMessage = (EditText)findViewById(R.id.etMessageContent);
        etPhone = (EditText) findViewById(R.id.etMessageToPhone);

        etPhone.setText(myIntent.getStringExtra("userPhone"));

//        SMS
        sentPI = PendingIntent.getBroadcast(this,0,new Intent(sent),0);
        deliveredPI = PendingIntent.getBroadcast(this,0,new Intent(delivered),0);


    }

    public void sendTheMessage(View view) {

        String message = etMessage.getText().toString();
//        Toast.makeText(this, "Message:"+message +" was sanded", Toast.LENGTH_SHORT).show();

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this , new String[]{Manifest.permission.SEND_SMS},
                    MY_PERMISSIONS_REQUEST_SEND_SMS);
        }else {
            SmsManager sms  =SmsManager.getDefault();
            sms.sendTextMessage(etPhone.getText().toString(),null,message,sentPI,deliveredPI);
        }

        /*Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.fromParts("sms","050500000",null));
        myIntent.putExtra("SMS_content","Hello its working.");
        startActivity(myIntent);*/

    }


    public void backToCustomer(View view) {
        Intent myIntent = new Intent(this,CustomerActivity.class);
        startActivity(myIntent);
    }

    @Override
    protected void onResume(){
        super.onResume();

        smsSentReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                switch (getResultCode()){
                    case Activity.RESULT_OK:
                        Toast.makeText(SandMessageActivity.this, "SMS sent", Toast.LENGTH_LONG).show();
                        Intent myIntent1 = new Intent(SandMessageActivity.this,MainActivity.class);
                        startActivity(myIntent1);

                        break;

                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        Toast.makeText(SandMessageActivity.this, "Generic failure!", Toast.LENGTH_LONG).show();
                        break;

                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        Toast.makeText(SandMessageActivity.this, "No service!", Toast.LENGTH_LONG).show();
                        break;

                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        Toast.makeText(SandMessageActivity.this, "Null PDU!", Toast.LENGTH_LONG).show();
                        break;

                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        Toast.makeText(SandMessageActivity.this, "Radio off!", Toast.LENGTH_LONG).show();
                        break;
                }
            }
        };
        smsDeliveredReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        Toast.makeText(SandMessageActivity.this, "SMS delivered!", Toast.LENGTH_LONG).show();
                        break;

                    case Activity.RESULT_CANCELED:
                        Toast.makeText(SandMessageActivity.this, "SMS not delivered!", Toast.LENGTH_LONG).show();
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
