package com.B3B.farmbros.domain;

public class Mensaje {
    private String datos;
    private String emailRemitente;
    private String emailReceptor;
    private Long horaCreacion;

    public Mensaje(){

    }

    public Mensaje(String datos, String remitente, String receptor, Long horaCreacion){
        this.datos = datos;
        this.emailRemitente = remitente;
        this.emailReceptor = receptor;
        this.horaCreacion = horaCreacion;
    }

    public String getDatos() {
        return datos;
    }

    public void setDatos(String datos) {
        this.datos = datos;
    }

    public String getRemitente() {
        return emailRemitente;
    }

    public void setRemitente(String remitente) {
        this.emailRemitente = remitente;
    }

    public String getReceptor() {
        return emailReceptor;
    }

    public void setReceptor(String receptor) {
        this.emailReceptor = receptor;
    }

    public Long getHoraCreacion() {
        return horaCreacion;
    }

    public void setHoraCreacion(Long horaCreacion) {
        this.horaCreacion = horaCreacion;
    }
}
