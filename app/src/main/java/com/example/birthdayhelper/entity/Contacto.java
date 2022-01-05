package com.example.birthdayhelper.entity;

import java.util.ArrayList;
import java.util.List;

public class Contacto {

    private int id;
    private String tipoNotif;
    private String mensaje;
    private List<String> telefono = new ArrayList<>();
    private String fechaNacimiento;
    private String nombre;

    public Contacto() {
        this.tipoNotif = "N";
        this.mensaje = "Felicidades!!";
    }

    public Contacto(int id, String tipoNotif, String mensaje, List<String> telefono, String fechaNacimiento, String nombre) {
        this.id = id;
        this.tipoNotif = tipoNotif;
        this.mensaje = mensaje;
        this.telefono = telefono;
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

    public List<String> getTelefono() {
        return telefono;
    }

    public void setTelefono(List<String> telefono) {
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
}
