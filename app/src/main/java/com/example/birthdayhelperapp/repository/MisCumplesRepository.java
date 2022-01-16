package com.example.birthdayhelperapp.repository;

import android.content.ContentValues;
import android.content.Context;
import android.content.ContextWrapper;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


import com.example.birthdayhelperapp.entity.Contacto;

import java.util.List;

public class MisCumplesRepository {
    private SQLiteDatabase db;
    private ContextWrapper context;
    private String dataBaseName = "BirthdayHelper.db";

    public MisCumplesRepository(ContextWrapper context) {
        this.context = context;
    }

    public void openOrCreateDatabase(){
        db = context.openOrCreateDatabase(dataBaseName, Context.MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS miscumples(ID integer, TipoNotif char(1), Mensaje VARCHAR(160), Telefono VARCHAR(15), FechaNacimiento VARCHAR(15), Nombre VARCHAR(128));" );
    }

    //Método encargado de añadir contactos a la base de datos
    public void addContactoSQLite(Contacto c) {

        openOrCreateDatabase();

        ContentValues cv = new ContentValues();
        //Con el put, indico el nombre de la tabla donde va a ir cada valor
        cv.put("ID", c.getId());
        cv.put("TipoNotif", c.getTipoNotif());
        cv.put("Mensaje", c.getMensaje());
        cv.put("Telefono", c.getTelefono());
        cv.put("FechaNacimiento", c.getFechaNacimiento());
        cv.put("Nombre", c.getNombre());

        //Con un insert, inserto los datos recogidos anteriormente con el ContentValues en la base de datos
        db.insert("miscumples", null, cv);
        db.close();
    }

    //Método encargado de actualizar contactos de la base de datos
    public void updateContactoSQLite(Contacto c) {

        openOrCreateDatabase();
        ContentValues cv = new ContentValues();

        //Al igual que en el método anterior, con el .put indico en que columna se va a hacer el update y le asigno un nuevo valor
        cv.put("TipoNotif", c.getTipoNotif());
        cv.put("Mensaje", c.getMensaje());
        cv.put("Telefono", c.getTelefonos().get(0));
        cv.put("FechaNacimiento", c.getFechaNacimiento());
        cv.put("Nombre", c.getNombre());

        //Aquí se crea la query que va a hacer el udate, extrayendo los datos aanteriores, comprueba con el ID de contacto, que contacto ha de actualizar
        db.update("miscumples", cv, "ID = ?", new String[]{String.valueOf(c.getId())});
        db.close();
    }

    //Método encargado de añadir un listado de contactos a la base de datos
    public void addContactosSQLite(List<Contacto> contactos) {
        if (contactos != null) {
            for (Contacto contacto : contactos) {
                addContactoSQLite(contacto);
            }
        }
    }

    //Método que se encarga de conseguir un contacto de la base de datos
    public Contacto getContacto(int id) {
        openOrCreateDatabase();

        //Mediante el objeto Cursor, nos movemos por las tablas de la BBDD buscando el contatco con esa Id
        Cursor c = db.rawQuery("SELECT * FROM miscumples WHERE ID = ?", new String[]{String.valueOf(id)});
        Contacto contacto = null;
        //Una vez encontrado el contacto, nos movemos por las columnas extrayendo los datos y guardándolos en variables, para finalmente retornar un Contacto
        if (c.moveToFirst()) {

            int id2 = c.getInt(0);
            String tipoNotif = c.getString(1);
            String mensaje = c.getString(2);
            String telefono = c.getString(3);
            String fechaNacimiento = c.getString(4);
            String nombre = c.getString(5);

            //Guardo los datos de la BBDD en un objeto Contacto
            contacto = new Contacto(id2, tipoNotif, mensaje, telefono, fechaNacimiento, nombre);
        }
        c.close();
        db.close();
        return contacto;
    }

    //Método que inserta o actualiza un contacto en la base de datos
    public void insertOrUpdate(Contacto c){
        if(getContacto(c.getId())!=null){
            updateContactoSQLite(c);
        } else{
            addContactoSQLite(c);
        }
    }
}
