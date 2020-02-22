package com.B3B.farmbros.domain;

public class Consulta {

    private int id;
    private String textoConsulta;
    private String asuntoConsulta;
    private long fechaConsulta;
    private String fotoConsultaBase64;
    private double LatConsulta;
    private double LngConsulta;
    private Productor remitenteConsulta;
    private Ingeniero encargadoConsulta;
    private String localidad;
    private EstadoConsulta estado;

    public Consulta(){
        fotoConsultaBase64="";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTextoConsulta() {
        return textoConsulta;
    }

    public void setTextoConsulta(String textoConsulta) {
        this.textoConsulta = textoConsulta;
    }

    public long getFechaConsulta() {
        return fechaConsulta;
    }

    public void setFechaConsulta(long fechaConsulta) {
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

    public Ingeniero getEncargadoConsulta(){return encargadoConsulta;}

    public void setEncargadoConsulta(Ingeniero ingeniero) {encargadoConsulta = ingeniero;}

    public void setAsuntoConsulta(String asuntoConsulta) {
        this.asuntoConsulta = asuntoConsulta;
    }

    public String getAsuntoConsulta() {
        return asuntoConsulta;
    }

    public void setLocalidad(String localidad) {
        this.localidad = localidad;
    }

    public String getLocalidad() {
        return localidad;
    }

    public EstadoConsulta getEstado() {
        return estado;
    }

    public void setEstado(EstadoConsulta estado) {
        this.estado = estado;
    }
}
