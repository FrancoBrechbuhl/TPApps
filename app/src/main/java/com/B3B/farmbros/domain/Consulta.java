package com.B3B.farmbros.domain;

import java.time.LocalDateTime;

public class Consulta {
    private int idConsulta;
    private String textoConsulta;
    private LocalDateTime fechaConsulta;
    private String fotoConsultaBase64;
    private double LatConsulta;
    private double LngConsulta;
    private Productor remitenteConsulta;

    public Consulta(){

    }

    public int getIdConsulta() {
        return idConsulta;
    }

    public void setIdConsulta(int idConsulta) {
        this.idConsulta = idConsulta;
    }

    public String getTextoConsulta() {
        return textoConsulta;
    }

    public void setTextoConsulta(String textoConsulta) {
        this.textoConsulta = textoConsulta;
    }

    public LocalDateTime getFechaConsulta() {
        return fechaConsulta;
    }

    public void setFechaConsulta(LocalDateTime fechaConsulta) {
        this.fechaConsulta = fechaConsulta;
    }

    public String getFotoConsultaBase64() {
        return fotoConsultaBase64;
    }

    public void setFotoConsultaBase64(String fotoConsultaBase64) {
        this.fotoConsultaBase64 = fotoConsultaBase64;
    }

    public double getLatConsulta() {
        return LatConsulta;
    }

    public void setLatConsulta(double latConsulta) {
        LatConsulta = latConsulta;
    }

    public double getLngConsulta() {
        return LngConsulta;
    }

    public void setLngConsulta(double lngConsulta) {
        LngConsulta = lngConsulta;
    }

    public Productor getRemitenteConsulta() {
        return remitenteConsulta;
    }

    public void setRemitenteConsulta(Productor remitenteConsulta) {
        this.remitenteConsulta = remitenteConsulta;
    }
}
