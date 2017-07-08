package com.esh_tech.aviram.barbershop.data;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.telephony.SmsManager;


public class AlarmService extends Service {

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent i, int flags, int startId) {
        String SPhone = i.getStringExtra("exPhone");
        String SSms = i.getStringExtra("exSmS");

        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(SPhone, null, SSms, null, null);

        return START_STICKY;
    }

}
