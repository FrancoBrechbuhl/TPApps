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
            Thread.sleep(10000);
        }
        catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        Intent i1 = new Intent();
//      i1.putExtra("Posicion", intent.getExtras().getInt("Posicion"));
//      i1.putExtra("NombrePlato", intent.getExtras().getString("NombrePlato"));
        i1.setAction(ConsultaBroadcastReceiver.CONSULTA);
        sendBroadcast(i1);
    }


}
