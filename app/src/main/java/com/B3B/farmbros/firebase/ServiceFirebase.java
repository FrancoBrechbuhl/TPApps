package com.B3B.farmbros.firebase;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.util.Log;

import com.B3B.farmbros.Home;
import com.B3B.farmbros.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class ServiceFirebase extends FirebaseMessagingService {

    public ServiceFirebase() {
    }

    @Override
    public void onNewToken(String token){
        Log.d("Token nuevo ", token);
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("push_notification", "Notificaciones push", importance);
            channel.setDescription("Permite recibir actualizaciones de respuestas a consultas realizadas");
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        createNotificationChannel();

        String title = remoteMessage.getNotification().getTitle();
        String body = remoteMessage.getNotification().getBody();

        Intent destinoNotificacion = new Intent(this, Home.class);
        if(body.equals("Se ha respondido una consulta que realizaste")) {
            destinoNotificacion.putExtra("profesion", "productor");
        }
        else {
            destinoNotificacion.putExtra("profesion", "ingeniero");
        }
        destinoNotificacion.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, destinoNotificacion, 0);

        NotificationManagerCompat manager = NotificationManagerCompat.from(this);
        Notification notification = new NotificationCompat.Builder(this, "push_notification")
                .setContentText(body)
                .setContentTitle(title)
                .setSmallIcon(R.drawable.ic_notificacion_firebase)
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .build();
        manager.notify(0, notification);
    }
}