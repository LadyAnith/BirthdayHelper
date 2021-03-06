package com.example.birthdayhelperapp.entity;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Contacto implements Serializable, Parcelable{

    //Atributos
    private int id;
    private String tipoNotif;
    private String mensaje;
    private List<String> telefonos = new ArrayList<>();
    private String telefono;
    private String fechaNacimiento;
    private String nombre;
    private Bitmap foto;
    private boolean updated = false;

    //Método Constructor que pone por defecto el tipo de notificación y un mensaje
    public Contacto() {
        this.tipoNotif = "N";
        this.mensaje = "Felicidades!!";
    }


    //Método constructor con todos los atributos de la clase
    public Contacto(int id, String tipoNotif, String mensaje, String telefono, String fechaNacimiento, String nombre) {
        this.id = id;
        this.tipoNotif = tipoNotif;
        this.mensaje = mensaje;
        this.telefono = telefono;
        telefonos.add(telefono);
        this.fechaNacimiento = fechaNacimiento;
        this.nombre = nombre;

    }

    protected Contacto(Parcel in) {
        id = in.readInt();
        tipoNotif = in.readString();
        mensaje = in.readString();
        telefonos = in.createStringArrayList();
        telefono = in.readString();
        fechaNacimiento = in.readString();
        nombre = in.readString();
        foto = in.readParcelable(Bitmap.class.getClassLoader());
    }


    public static final Creator<Contacto> CREATOR = new Creator<Contacto>() {
        @Override
        public Contacto createFromParcel(Parcel in) {
            return new Contacto(in);
        }

        @Override
        public Contacto[] newArray(int size) {
            return new Contacto[size];
        }
    };


    //Getter & Setter
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTipoNotif() {
        return tipoNotif;
    }

    public void setTipoNotif(String tipoNotif) {
        this.tipoNotif = tipoNotif;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public List<String> getTelefonos() {
        return telefonos;
    }

    public void setTelefonos(List<String> telefonos) {
        this.telefonos = telefonos;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(String fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Bitmap getFoto() {
        return foto;
    }

    public void setFoto(Bitmap foto) {
        this.foto = foto;
    }

    public boolean isUpdated() {
        return updated;
    }

    public void setUpdated(boolean updated) {
        this.updated = updated;
    }

    //Método to String
    @Override
    public String toString() {
        return "Contacto{" +
                "id=" + id +
                ", tipoNotif='" + tipoNotif + '\'' +
                ", mensaje='" + mensaje + '\'' +
                ", telefonos=" + telefonos +
                ", telefono='" + telefono + '\'' +
                ", fechaNacimiento='" + fechaNacimiento + '\'' +
                ", nombre='" + nombre + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(tipoNotif);
        dest.writeString(mensaje);
        dest.writeStringList(telefonos);
        dest.writeString(telefono);
        dest.writeString(fechaNacimiento);
        dest.writeString(nombre);
        dest.writeParcelable(foto, flags);
    }
}
