package com.B3B.farmbros.domain;

public abstract class Usuario {
    private String nombre;
    private String email;

    public Usuario(){

    }

    public Usuario(String nombre, String email){
        this.email = email;
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
