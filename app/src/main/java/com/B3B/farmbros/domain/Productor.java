package com.B3B.farmbros.domain;

import java.time.LocalDateTime;

public class Productor {

    private String nombre;
    private String apellido;
    private String email;

    /*
    Las consultas no se si esta tan bueno tenerlas en memoria o es mejor pedirlas
     */

    public Productor(){}

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String name) {
        this.nombre = name;
    }

    public String getApellido() { return apellido;}

    public void setApellido(String app) { this.apellido = apellido;}

    public void setEmail(String email) { this.email = email;}

    public String getEmail() {
        return email;
    }

}
