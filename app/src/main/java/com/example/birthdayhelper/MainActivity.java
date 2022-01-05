package com.example.birthdayhelper;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.birthdayhelper.entity.Contacto;

import static android.provider.ContactsContract.CommonDataKinds.Event;
import static android.provider.ContactsContract.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainActivity extends AppCompatActivity {
    Button boton;
    SQLiteDatabase db;
    // Request code for READ_CONTACTS. It can be any number > 0.
    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);
            //After this point you wait for callback in onRequestPermissionsResult(int, String[], int[]) overriden method
        } else {
            List<Map<String, String>> listaContactos = getContactList();

            db = openOrCreateDatabase("MisCumples", Context.MODE_PRIVATE, null);
            db.execSQL("CREATE TABLE IF NOT EXISTS miscumples(ID integer,TipoNotif char(1),Mensaje VARCHAR(160),Telefono VARCHAR(15),FechaNacimiento VARCHAR(15),Nombre VARCHAR(128)); ");

        }

        boton = (Button) findViewById(R.id.button);
        boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, MainActivity2.class);
                startActivity(i);
            }
        });


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted
                getContactList();
            } else {
                Toast.makeText(this, "TIENES QUE DAR PERMISO DE LECTURA DE CONTACTOS", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @SuppressLint("Range")
    private List<Map<String, String>> getContactList() {
        Map<String, Contacto> listaContactos = new HashMap();
        setContacts(listaContactos);
        setPhones(listaContactos);

        return new ArrayList(listaContactos.values());
    }

    @SuppressLint("Range")
    private void setContacts(Map<String, Contacto> listaContactos) {

        Cursor c = getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        while (c.moveToNext()) {
            String id = c.getString(c.getColumnIndex(ContactsContract.Contacts._ID));
            String name = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
//            String phone = c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            String birthday = getBirthday(id);

            Contacto contacto = listaContactos.containsKey(name) ? listaContactos.get(name) : new Contacto();
            contacto.setId(Integer.parseInt(id));
            contacto.setNombre(name);
//            contacto.getTelefono().add(phone);
            contacto.setFechaNacimiento(birthday);

            listaContactos.put(contacto.getNombre(), contacto);

        }
        c.close();
    }

    private String getBirthday(String contactId) {
        String birthday = "";
        ContentResolver bd = getContentResolver();
        Cursor bdc = bd.query(android.provider.ContactsContract.Data.CONTENT_URI, new String[]{Event.DATA}, android.provider.ContactsContract.Data.CONTACT_ID + " = " + contactId + " AND " + Data.MIMETYPE + " = '" + Event.CONTENT_ITEM_TYPE + "' AND " + Event.TYPE + " = " + Event.TYPE_BIRTHDAY, null, android.provider.ContactsContract.Data.DISPLAY_NAME);
        if (bdc.getCount() > 0) {
            while (bdc.moveToNext()) {
                birthday = bdc.getString(0);
            }
        }
        bdc.close();
        return birthday;
    }

    @SuppressLint("Range")
    private void setPhones(Map<String, Contacto> listaContactos) {

        Cursor c = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
        while (c.moveToNext()) {
            String name = c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String phone = c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

            if (!listaContactos.containsKey(name))
                continue;

            Contacto contacto = listaContactos.get(name);
            contacto.getTelefono().add(phone);
        }
        c.close();
    }

}