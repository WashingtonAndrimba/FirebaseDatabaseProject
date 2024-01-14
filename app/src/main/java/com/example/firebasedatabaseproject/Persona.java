package com.example.firebasedatabaseproject;

import androidx.appcompat.app.AppCompatActivity;

public class Persona {
    private String cedula;
    private String nombre;
    private String genero;
    private String provincia;
    private String email;

    public Persona() {
    }

    public Persona(String cedula, String nombre, String genero, String provincia, String email) {
        this.cedula = cedula;
        this.nombre = nombre;
        this.genero = genero;
        this.provincia = provincia;
        this.email = email;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
