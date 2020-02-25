package com.B3B.farmbros.firebase;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.B3B.farmbros.Home;
import com.B3B.farmbros.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

public class ConsultaMsgService extends FirebaseMessagingService {


    @Override
    public void onNewToken(String token){
        Log.d("Token nuevo ", token);
        saveTokenToPref(token);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage){
        sendNotification();
    }

    @Override
    public void onMessageSent(String msg){
        Log.d("Firebase", "Mensaje enviado");
    }

    private void saveTokenToPref(String token){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("registration_id", token);
        editor.apply();
    }

    private String getTokenFromPref(){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        return preferences.getString("registration_id", null);
    }

    private void sendNotification() {
        Intent intent = new Intent(this, Home.class);
        intent.putExtra("profesion", "productor");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        String channelId = getString(R.string.channel_default);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.drawable.ic_notificacion_firebase)
                        .setContentTitle("FarmBros")
                        .setContentText("Tenes una respuesta en la consulta que realizaste!")
                        .setAutoCancel(true)
                        .setContentIntent(pendingIntent);

        Log.d("Se entra", "Notificacion");

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(getApplicationContext().NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }
}
