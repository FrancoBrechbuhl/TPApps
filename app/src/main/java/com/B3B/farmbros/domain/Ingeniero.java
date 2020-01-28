package com.B3B.farmbros.domain;


public class Ingeniero {

    private String nombre;
    //private String apellido;
    private String email;
    private Integer califiacion; // que valla de 0 a 50 y despues la mostramos tipo 4,5

    public Ingeniero(){
        califiacion = 0;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String name) {
        this.nombre = name;
    }

    /*public String getApellido() { return apellido;}

    public void setApellido(String app) { this.apellido = apellido;}
*/
    public void setEmail(String email) { this.email = email;}

    public String getEmail() {
        return email;
    }

}
