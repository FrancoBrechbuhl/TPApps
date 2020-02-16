package com.B3B.farmbros.domain;


public class Ingeniero extends Usuario{

    private Integer calificacion; // que vaya de 0 a 50 y despues la mostramos tipo 4,5

    public Ingeniero(){
        calificacion = 0;
    }

    public Ingeniero (String nombre, String email, Integer calificacion){
        super(nombre, email);
        this.calificacion = calificacion;
    }

    public Integer getCalificacion() {
        return calificacion;
    }

    public void setCalificacion(Integer calificacion) {
        this.calificacion = calificacion;
    }
}
