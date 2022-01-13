package com.example.birthdayhelper;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.birthdayhelper.entity.Contacto;
import com.example.birthdayhelper.repository.MisCumplesRepository;

import static android.provider.ContactsContract.CommonDataKinds.Event;
import static android.provider.ContactsContract.Data;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainActivity extends AppCompatActivity {
    Button boton;

    public static List<Contacto> listaContactos;
    // Request code for READ_CONTACTS. It can be any number > 0.
    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;
    private MisCumplesRepository cumplesRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cumplesRepository = new MisCumplesRepository(this);

        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);
            //After this point you wait for callback in onRequestPermissionsResult(int, String[], int[]) overriden method
        } else {
            listaContactos = getContactList();

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
                listaContactos = getContactList();
            } else {
                Toast.makeText(this, "TIENES QUE DAR PERMISO DE LECTURA DE CONTACTOS", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @SuppressLint("Range")
    private List<Contacto> getContactList() {
        Map<String, Contacto> mapContactos = new HashMap();
        setContacts(mapContactos);
        setPhones(mapContactos);
        List<Contacto> listaContactos = new ArrayList(mapContactos.values());

        if (listaContactos != null) {
            for (Contacto c : listaContactos) {
                Contacto contactoBD = cumplesRepository.getContacto(c.getId());
                if (contactoBD != null) {
                    c.setTelefono(contactoBD.getTelefono());
                    c.setTipoNotif(contactoBD.getTipoNotif());
                }
            }
        }

        return listaContactos;
    }

    @SuppressLint("Range")
    private void setContacts(Map<String, Contacto> listaContactos) {

        Cursor c = getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        while (c.moveToNext()) {
            String id = c.getString(c.getColumnIndex(ContactsContract.Contacts._ID));
            String name = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
//            String phone = c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            String photo = c.getString(c.getColumnIndex(ContactsContract.Contacts.LOOKUP_KEY));
            Uri photoUri = ContactsContract.Contacts.getLookupUri(Long.parseLong(id), photo);

            String birthday = getBirthday(id);

            Contacto contacto = listaContactos.containsKey(name) ? listaContactos.get(name) : new Contacto();
            contacto.setId(Integer.parseInt(id));
            contacto.setNombre(name);
            contacto.setFoto(getPhoto(photoUri));
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
            contacto.getTelefonos().add(phone);
            if (contacto.getTelefono() == null || contacto.getTelefono().equals("")) {
                contacto.setTelefono(phone);
            }
        }
        c.close();
    }


    private Bitmap getPhoto(Uri uri) {
        /*
        Foto del contacto y su id
         */
        Bitmap photo = null;
        String id = null;

        /************* CONSULTA ************/
        Cursor contactCursor = getContentResolver().query(
                uri, new String[]{ContactsContract.Contacts._ID}, null, null, null);

        if (contactCursor.moveToFirst()) {
            id = contactCursor.getString(0);
        }
        contactCursor.close();

        /*
        Usar el método de clase openContactPhotoInputStream()
         */
        try {
            InputStream input =
                    ContactsContract.Contacts.openContactPhotoInputStream(
                            getContentResolver(),
                            ContentUris.withAppendedId(
                                    ContactsContract.Contacts.CONTENT_URI,
                                    Long.parseLong(id))
                    );
            if (input != null) {
                /*
                Dar formato tipo Bitmap a los bytes del BLOB
                correspondiente a la foto
                 */
                photo = BitmapFactory.decodeStream(input);
                input.close();
            }

        } catch (IOException iox) { /* Manejo de errores */ }

        return photo;
    }

}