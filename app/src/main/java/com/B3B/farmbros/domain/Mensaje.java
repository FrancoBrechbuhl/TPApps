package com.B3B.farmbros.domain;

public class Mensaje {
    private String datos;
    private Usuario remitente;
    private Long horaCreacion;

    public Mensaje(){

    }

    public Mensaje(String datos, Usuario remitente, Long horaCreacion){
        this.datos = datos;
        this.remitente = remitente;
        this.horaCreacion = horaCreacion;
    }

    public String getDatos() {
        return datos;
    }

    public void setDatos(String datos) {
        this.datos = datos;
    }

    public Usuario getRemitente() {
        return remitente;
    }

    public void setRemitente(Usuario remitente) {
        this.remitente = remitente;
    }

    public Long getHoraCreacion() {
        return horaCreacion;
    }

    public void setHoraCreacion(Long horaCreacion) {
        this.horaCreacion = horaCreacion;
    }
}
