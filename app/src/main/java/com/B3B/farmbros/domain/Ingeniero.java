package com.B3B.farmbros.domain;


public class Ingeniero{

    private Integer id;
    private String nombre;
    private String email;
    private Integer calificacion; // que vaya de 0 a 50 y despues la mostramos tipo 4,5

    public Ingeniero(){
        calificacion = 0;
    }

    public Ingeniero (String nombre, String email, Integer calificacion){
        this.nombre = nombre;
        this.email = email;
        this.calificacion = calificacion;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public Integer getCalificacion() {
        return calificacion;
    }

    public void setCalificacion(Integer calificacion) {
        this.calificacion = calificacion;
    }
}
