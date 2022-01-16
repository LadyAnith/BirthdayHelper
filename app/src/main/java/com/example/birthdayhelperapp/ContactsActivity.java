package com.example.birthdayhelperapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.birthdayhelperapp.entity.Contacto;
import com.example.birthdayhelperapp.repository.MisCumplesRepository;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ContactsActivity extends AppCompatActivity {

    RecyclerView recycler;

    Calendar fechaActual = Calendar.getInstance();
    Calendar fechaCumple;
    public static List<String> cumpleDeContactos = new ArrayList();
    public static List<Integer> idCumpleDeContactos = new ArrayList();
    int hour;
    int minutos;
    Calendar horaNotificacionCumple;
    AlarmManager alarmManager;
    private MaterialTimePicker picker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        recycler = (RecyclerView) findViewById(R.id.recyclerId);
        recycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false));

        AdapterContacts adaptador = new AdapterContacts(new ArrayList<>(MainActivity.listaContactos.values()));
        recycler.setAdapter(adaptador);

        crearCanalDeNotificacion();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        /**
        int id = item.getItemId();
        if(id == R.id.itemMenu){
            TimePickerDialog timePickerDialog = new TimePickerDialog(
                    this,
                    android.R.style.Theme_Holo_Dialog_MinWidth,
                    new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                            String time = hourOfDay + ":" + minute;
                            //Formato 24 horas
                            SimpleDateFormat f24Horas = new SimpleDateFormat(
                                    "HH:mm"
                            );
                                hour = hourOfDay;
                                minutos = minute;

                        }
                    }, 24,0, true
            );
            //Cambiar el fondo
            timePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            //timePickerDialog.updateTime(tHour, tMinute);
            timePickerDialog.setTitle("Hora de Notificación");
            //Mostrar dialogo de la hora
            timePickerDialog.show();

            Button buttonPicker = timePickerDialog.getButton(timePickerDialog.BUTTON_POSITIVE);
            buttonPicker.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    System.out.println(hour+ ":" + minutos);
                    horaNotificacionCumple = Calendar.getInstance();
                    horaNotificacionCumple.set(Calendar.HOUR_OF_DAY, hour);
                    horaNotificacionCumple.set(Calendar.MINUTE, minutos);
                    horaNotificacionCumple.set(Calendar.SECOND, 0);
                    if(cumpleDeContactos != null && ! cumpleDeContactos.isEmpty()){
                        startAlarma(horaNotificacionCumple);
                    }
                }
            });
        }
*/
        mostrarTimePicker();

        return super.onOptionsItemSelected(item);

    }

    private void mostrarTimePicker() {
        picker = new MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .setHour(23)
                .setMinute(0)
                .setTitleText("Hora Notificación")
                .build();
        picker.show(getSupportFragmentManager(), "notificacionAlarma");
        picker.addOnPositiveButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                horaNotificacionCumple = Calendar.getInstance();
                horaNotificacionCumple.set(Calendar.HOUR_OF_DAY, picker.getHour());
                horaNotificacionCumple.set(Calendar.MINUTE, picker.getMinute());
                horaNotificacionCumple.set(Calendar.SECOND, 0);
                try {
                    cumpleDeContactos = comprobarCumples();
                    idCumpleDeContactos = guardarIdContactosCumple();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if(cumpleDeContactos != null && ! cumpleDeContactos.isEmpty()){
                    startAlarma(horaNotificacionCumple);
                }



            }
        });
    }

    public Calendar convertirStringACalendar(String fecha){
        System.out.println(fecha);
        Date fechaD = null;
        try {
            fechaD = new SimpleDateFormat("dd-MM-yyyy").parse(fecha);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar calendario = Calendar.getInstance();
        calendario.setTime(fechaD);
        return calendario;
    }


    public List<String> comprobarCumples() throws ParseException {
        List<String> contactosCumple = new ArrayList();
        for(Contacto contacto : MainActivity.listaContactos.values()){
            fechaCumple = convertirStringACalendar(contacto.getFechaNacimiento());
            if(fechaCumple.get(Calendar.DAY_OF_MONTH) == fechaActual.get(Calendar.DAY_OF_MONTH) && fechaCumple.get(Calendar.MONTH) == fechaActual.get(Calendar.MONTH)){
                contactosCumple.add(contacto.getNombre());
                System.out.println("Hoy hace los años: " + contacto.getNombre());
            }
        }
        return contactosCumple;
    }

    public List<Integer> guardarIdContactosCumple() throws ParseException {
        List<Integer> contactosIdCumple = new ArrayList();
        for(Contacto contacto : MainActivity.listaContactos.values()){
            fechaCumple = convertirStringACalendar(contacto.getFechaNacimiento());
            if(fechaCumple.get(Calendar.DAY_OF_MONTH) == fechaActual.get(Calendar.DAY_OF_MONTH) && fechaCumple.get(Calendar.MONTH) == fechaActual.get(Calendar.MONTH)){
                contactosIdCumple.add(contacto.getId());
            }
        }
        return contactosIdCumple;
    }

    public void crearCanalDeNotificacion(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Notificacion";
            String description ="Canal para Alarma Manager";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel notificacionChannel = new NotificationChannel("notificacionAlarma", name, importance);
            notificacionChannel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(notificacionChannel);

        }

    }

    public void startAlarma(Calendar calendar){
        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, Alarma.class );
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, horaNotificacionCumple.getTimeInMillis(),pendingIntent);

        List<Contacto> contactosNotificacionSMS = contactosALosQueMandarSMS();
        for(Contacto resultado : contactosNotificacionSMS){
            enviarSMS(resultado.getTelefono(), resultado.getMensaje(), resultado.getNombre());
        }
    }

    public List<Contacto> contactosALosQueMandarSMS(){
        List<Contacto> listadoContactosSMS = new ArrayList();
        for(Contacto c: MainActivity.listaContactos.values()){
            if ("S".equals(c.getTipoNotif()) && c.isUpdated()){
                listadoContactosSMS.add(c);
            }
        }

        return listadoContactosSMS;
    }

    public void enviarSMS(String telefono, String mensaje, String nombre){
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(telefono, null, mensaje, null, null);
            Toast.makeText(getApplicationContext(), "Enviado SMS para Felicitar a " + nombre ,Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(),
                    "SMS no enviado, por favor, inténtalo otra vez.",
                    Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }
}