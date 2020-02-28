package com.B3B.farmbros;

import android.app.IntentService;
import android.content.Intent;

public class ConsultaIntentService extends IntentService {
    public ConsultaIntentService(){
        super("ConsultaIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        //logica servicio
        try {
            Thread.sleep(2500);
        }
        catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        Intent i1 = new Intent();
        i1.setAction(ConsultaBroadcastReceiver.CONSULTA);
        sendBroadcast(i1);
    }


}
