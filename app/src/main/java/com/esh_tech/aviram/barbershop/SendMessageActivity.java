package com.esh_tech.aviram.barbershop;

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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.esh_tech.aviram.barbershop.Constants.SharedPreferencesConstants;
import com.esh_tech.aviram.barbershop.Database.BarbershopDBHandler;

public class SendMessageActivity extends AppCompatActivity implements View.OnClickListener{

    BarbershopDBHandler dbHandler;
    Customer customerProfile;

    EditText etPhone;
    EditText etEmail;
    EditText etMessage;

    CheckBox cbPhone;
    CheckBox cbEmail;

    String emailTo;
    String message;

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
        init();
    }

    private void init() {
        Intent myIntent =getIntent();
//        Toast.makeText(this, myIntent.getStringExtra("userPhone"), Toast.LENGTH_SHORT).show();
        dbHandler = new BarbershopDBHandler(this);
        customerProfile = new Customer();

        emailTo = "";
        message = "";

        etMessage = (EditText) findViewById(R.id.etMessage);
        etPhone = (EditText) findViewById(R.id.etPhone);
        etEmail = (EditText) findViewById(R.id.etEmail);

        cbPhone = (CheckBox) findViewById(R.id.cbPhone);
        cbEmail = (CheckBox) findViewById(R.id.cbEmail);

//        get user data

        customerProfile = dbHandler.getCustomerByID(myIntent.getIntExtra(SharedPreferencesConstants.CUSTOMER_ID_SMS,-1));
//        etPhone.setText(myIntent.getStringExtra("userPhone"));
        if(customerProfile.get_id() == -1)
            etPhone.setText(customerProfile.getPhone());
        else etPhone.setText(customerProfile.getName());

//        SMS
        sentPI = PendingIntent.getBroadcast(this,0,new Intent(sent),0);
        deliveredPI = PendingIntent.getBroadcast(this,0,new Intent(delivered),0);

    }

    public void sendTheMessage() {

        message = etMessage.getText().toString();

        if(cbEmail.isChecked())sandEmail();
        if(cbPhone.isChecked())sandSms();

//        /*Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.fromParts("sms","050500000",null));
//        myIntent.putExtra("SMS_content","Hello its working.");
//        startActivity(myIntent);*/

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

    private void sandEmail() {
//        TODO send email with JavaMail API
//        emailTo = etEmail.getText().toString();
        emailTo = customerProfile.getEmail();

        Intent emailIntent = new Intent(Intent.ACTION_SEND);

        emailIntent.putExtra(Intent.EXTRA_EMAIL,new String[]{emailTo});
        emailIntent.putExtra(Intent.EXTRA_SUBJECT,getResources().getText(R.string.haircutAppointment));
        emailIntent.putExtra(Intent.EXTRA_TEXT,message);


        emailIntent.setType("message/rfc822");

        try {
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
            finish();
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(SendMessageActivity.this,
                    "There is no email client installed.", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(SendMessageActivity.this, R.string.smsDelivered, Toast.LENGTH_LONG).show();
                        Intent myIntent1 = new Intent(SendMessageActivity.this,MainActivity.class);
                        startActivity(myIntent1);

                        break;

                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        Toast.makeText(SendMessageActivity.this, R.string.genericFailure, Toast.LENGTH_LONG).show();
                        break;

                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        Toast.makeText(SendMessageActivity.this, R.string.noService, Toast.LENGTH_LONG).show();
                        break;

                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        Toast.makeText(SendMessageActivity.this, R.string.nullPDU, Toast.LENGTH_LONG).show();
                        break;

                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        Toast.makeText(SendMessageActivity.this, R.string.radioOff, Toast.LENGTH_LONG).show();
                        break;
                }
            }
        };
        smsDeliveredReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        Toast.makeText(SendMessageActivity.this, R.string.smsDelivered, Toast.LENGTH_LONG).show();
                        break;

                    case Activity.RESULT_CANCELED:
                        Toast.makeText(SendMessageActivity.this, R.string.smsNotDelivered, Toast.LENGTH_LONG).show();
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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btSendMassage:
                sendTheMessage();
                break;
            case R.id.btCancel:
                Intent myIntent = new Intent(this,CustomerActivity.class);
                startActivity(myIntent);
                this.finish();
                break;
            default:
                Toast.makeText(this, "Not Initialized yet.", Toast.LENGTH_SHORT).show();
                break;

        }

    }
}
