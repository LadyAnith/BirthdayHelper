package com.example.birthdayhelper.entity;

import android.graphics.Bitmap;
import android.net.Uri;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Contacto implements Serializable{

    private int id;
    private String tipoNotif;
    private String mensaje;
    private List<String> telefonos = new ArrayList<>();
    private String telefono;
    private String fechaNacimiento;
    private String nombre;
    private Bitmap foto;

    public Contacto() {
        this.tipoNotif = "N";
        this.mensaje = "Felicidades!!";
    }



    public Contacto(int id, String tipoNotif, String mensaje, String telefono, String fechaNacimiento, String nombre) {
        this.id = id;
        this.tipoNotif = tipoNotif;
        this.mensaje = mensaje;
        this.telefono = telefono;
        telefonos.add(telefono);
        this.fechaNacimiento = fechaNacimiento;
        this.nombre = nombre;

    }

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
}
