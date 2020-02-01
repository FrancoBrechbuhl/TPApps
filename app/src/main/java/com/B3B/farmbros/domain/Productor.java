package com.B3B.farmbros.domain;

import java.time.LocalDateTime;

public class Productor {

    private String nombre;
    private String email;

    /*
    Las consultas no se si esta tan bueno tenerlas en memoria o es mejor pedirlas
     */

    public Productor(){}

    public Productor(String nombre,String email){
        this.nombre = nombre;
        this.email = email;
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
