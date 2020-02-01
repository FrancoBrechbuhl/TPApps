package com.B3B.farmbros.domain;


public class Ingeniero {

    private String nombre;
    private String email;
    private Integer califiacion; // que valla de 0 a 50 y despues la mostramos tipo 4,5

    public Ingeniero(){
        califiacion = 0;
    }

    public Ingeniero (String nombre, String email, Integer califiacion){
        this.nombre = nombre;
        this.email = email;
        this.califiacion = califiacion;
    }
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String name) {
        this.nombre = name;
    }

    public void setEmail(String email) { this.email = email;}

    public String getEmail() {
        return email;
    }

}
