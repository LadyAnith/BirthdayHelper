package com.example.birthdayhelperapp;

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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.birthdayhelperapp.entity.Contacto;
import com.example.birthdayhelperapp.repository.MisCumplesRepository;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    Button boton;

    public static Map<String, Contacto> listaContactos;
    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;
    private MisCumplesRepository cumplesRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cumplesRepository = new MisCumplesRepository(this);

        setContentView(R.layout.activity_main);

        //If que da permiso a mi aplicación para leer los contactos de mi movil
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);
        } else {
            try {
                //Si son aceptados los permisos, guardo mis contactos en un listado
                listaContactos = getContactList();
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }

        //If que sirve para permitir que mi aplicación mande un sms a un contacto
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_DENIED) {
                Log.d("permission", "permission denied to SEND_SMS - requesting it");
                String[] permissions = {Manifest.permission.SEND_SMS};
                requestPermissions(permissions, PERMISSIONS_REQUEST_READ_CONTACTS);
            }
        }

        //Botón que nos llevará a la aplicación principal
        boton = (Button) findViewById(R.id.button);
        boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, ContactsActivity.class);
                startActivity(i);
            }
        });


    }

    //Método que da permisos a la aplicación para leer los contactos
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Si el permiso ha sido concedido
                try {
                    listaContactos = getContactList();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(this, "Si a Birthday Helper no les das los permisos para acceder a los contactos, no va a funcionar, por favor, acepta los permisos", Toast.LENGTH_SHORT).show();
            }
        }
    }


    //Método que guarda en un listado, todos los contactos de la agenda de telefonos
    @SuppressLint("Range")
    private Map<String, Contacto>  getContactList() throws ParseException {
        //Aquí se guardarán todos los contactos almacenados en el teléfono, usando el nombre de contacto como key
        //queremos aprovechar esta key para encontrar los contactos a la hora de guardar los teléfonos que tiene cada uno
        Map<String, Contacto> mapContactos = new HashMap();
        //En este método se alnacenan en el hashmap todos los contactos del teléfono
        setContacts(mapContactos);
        //y aquí se guardan todos los teléfonos para cada contacto en su objeto correspondiente
        setPhones(mapContactos);

        //Aquí es donde se guardará el listado definitivo de contactos tanto los contactos modificados como los no modificados
        //así que volcamos todos los valores del hashmap en este array

        if (!mapContactos.isEmpty()) {
            //Recorremos cada contacto del listado
            for (String key : mapContactos.keySet()) {
                Contacto c = mapContactos.get(key);
                //Y buscamos si hay registro en nuestra base de datos de sqlite
                Contacto contactoBD = cumplesRepository.getContacto(c.getId());
                //Si existe contacto es que ha sido modificado previamente así que almacenamos los cambios de nuestra
                // bbdd en el contacto actual (el que recuperamos previamente del teléfono)
                if (contactoBD != null) {
                    //Establecemos el teléfono de notificación
                    c.setTelefono(contactoBD.getTelefono());
                    //El tipo de notificación
                    c.setTipoNotif(contactoBD.getTipoNotif());
                    //y un atributo extra que nos dirá que este contacto fue modificado previamente para ahorrarnos consultas posteriores
                    c.setUpdated(true);
                }
            }
        }

        return mapContactos;
    }

    //Método para setear los contactos del hashmap
    @SuppressLint("Range")
    private void setContacts(Map<String, Contacto> listaContactos) throws ParseException {

        Cursor c = getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        //Con el cursor, me muevo a través de los distintos elementos de la base de datos, y guardo su valor en variables
        while (c.moveToNext()) {
            String id = c.getString(c.getColumnIndex(ContactsContract.Contacts._ID));
            String name = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
            String photo = c.getString(c.getColumnIndex(ContactsContract.Contacts.LOOKUP_KEY));
            Uri photoUri = ContactsContract.Contacts.getLookupUri(Long.parseLong(id), photo);

            String birthday = getBirthday(id);

            //Setteo el hasmap del listado ded contacto y guardo los nuevos valores.
            Contacto contacto = listaContactos.containsKey(name) ? listaContactos.get(name) : new Contacto();
            contacto.setId(Integer.parseInt(id));
            contacto.setNombre(name);
            contacto.setFoto(getPhoto(photoUri));
            contacto.setFechaNacimiento(birthday);

            //Añado a
            listaContactos.put(contacto.getNombre(), contacto);

        }
        c.close();
    }

    private String getBirthday(String contactId) throws ParseException {
        String birthday = "";
        String birthdayPosition = "";
        ContentResolver bd = getContentResolver();
        Cursor bdc = bd.query(android.provider.ContactsContract.Data.CONTENT_URI, new String[]{ContactsContract.CommonDataKinds.Event.DATA}, android.provider.ContactsContract.Data.CONTACT_ID + " = " + contactId + " AND " + ContactsContract.Data.MIMETYPE + " = '" + ContactsContract.CommonDataKinds.Event.CONTENT_ITEM_TYPE + "' AND " + ContactsContract.CommonDataKinds.Event.TYPE + " = " + ContactsContract.CommonDataKinds.Event.TYPE_BIRTHDAY, null, android.provider.ContactsContract.Data.DISPLAY_NAME);
        if (bdc.getCount() > 0) {
            while (bdc.moveToNext()) {
                birthday = bdc.getString(0);
                birthdayPosition = colocarfecha(birthday);
            }
        }
        bdc.close();
        return birthdayPosition;
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

        //Foto del contacto y su id
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
    public String colocarfecha(String fecha) throws ParseException {
        Date fechaD = new SimpleDateFormat("yyyy-MM-dd").parse(fecha);
        String fechaFinal = new SimpleDateFormat("dd-MM-yyyy").format(fechaD);
        return fechaFinal;
    }

}