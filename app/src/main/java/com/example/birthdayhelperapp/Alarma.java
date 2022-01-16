package com.example.birthdayhelperapp;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.util.List;

public class Alarma extends BroadcastReceiver {
    private String mensaje = "Hoy es el Cumpleaños de: ";
    private String persona = "";
    @Override
    public void onReceive(Context context, Intent intent) {

        Intent i = new Intent(context, ContactsActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, i,0);

        List <String> cumplesHoy = getCumpleDeContactos();
        if(cumplesHoy != null && ! cumplesHoy.isEmpty()){
            for(String cumple : cumplesHoy){
                persona =persona + cumple + ", ";
            }
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "notificacionAlarma")
        .setSmallIcon(R.drawable.ic_baseline_cake_24)
        .setContentTitle("Aviso de cumpleaños")
        .setContentText(mensaje + persona)
        .setAutoCancel(true)
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .setDefaults(Notification.DEFAULT_ALL)
        .setContentIntent(pendingIntent);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        notificationManagerCompat.notify(123,builder.build());
    }

    private List<String> getCumpleDeContactos() {
        return ContactsActivity.cumpleDeContactos;
    }
}
