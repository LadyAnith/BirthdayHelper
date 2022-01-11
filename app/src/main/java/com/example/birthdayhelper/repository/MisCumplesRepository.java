package com.example.birthdayhelper.repository;

import android.content.ContentValues;
import android.content.Context;
import android.content.ContextWrapper;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.birthdayhelper.entity.Contacto;

import java.util.List;

public class MisCumplesRepository {
    SQLiteDatabase db;
    ContextWrapper context;
    String dataBaseName = "BirthdayHelper.db";

    public MisCumplesRepository(ContextWrapper context) {
        this.context = context;
    }

    public void openOrCreateDatabase(){
        db = context.openOrCreateDatabase(dataBaseName, Context.MODE_PRIVATE, null);
    }

    public void addContactoSQLite(Contacto c) {

        openOrCreateDatabase();

        ContentValues cv = new ContentValues();
        cv.put("ID", c.getId());
        cv.put("TipoNotif", c.getTipoNotif());
        cv.put("Mensaje", c.getMensaje());
        cv.put("Telefono", c.getTelefonos().get(0));
        cv.put("FechaNacimiento", c.getFechaNacimiento());
        cv.put("Nombre", c.getNombre());

        db.insert("miscumples", null, cv);
        db.close();
    }

    public void updateContactoSQLite(Contacto c) {

        openOrCreateDatabase();
        ContentValues cv = new ContentValues();

        cv.put("TipoNotif", c.getTipoNotif());
        cv.put("Mensaje", c.getMensaje());
        cv.put("Telefono", c.getTelefonos().get(0));
        cv.put("FechaNacimiento", c.getFechaNacimiento());
        cv.put("Nombre", c.getNombre());

        db.update("miscumples", cv, "ID = ?", new String[]{String.valueOf(c.getId())});
        db.close();
    }

    public void addContactosSQLite(List<Contacto> contactos) {
        if (contactos != null) {
            for (Contacto contacto : contactos) {
                addContactoSQLite(contacto);
            }
        }
    }

    public Contacto getContacto(int id) {
        openOrCreateDatabase();

        Cursor c = db.rawQuery("SELECT * FROM miscumples WHERE ID = ?", new String[]{String.valueOf(id)});
        Contacto contacto = null;
        if (c.moveToFirst()) {

            int id2 = c.getInt(0);
            String tipoNotif = c.getString(1);
            String mensaje = c.getString(2);
            String telefono = c.getString(3);
            String fechaNacimiento = c.getString(4);
            String nombre = c.getString(5);

            contacto = new Contacto(id2, tipoNotif, mensaje, telefono, fechaNacimiento, nombre);
        }
        c.close();
        db.close();
        return contacto;
    }

    public void insertOrUpdate(Contacto c){
        if(getContacto(c.getId())!=null){
            updateContactoSQLite(c);
        } else{
            addContactoSQLite(c);
        }
    }
}
