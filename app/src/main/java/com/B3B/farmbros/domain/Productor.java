package com.B3B.farmbros.domain;

public class Productor{

    private Integer id;
    private String nombre;
    private String email;
    private String token;
    /*
    Las consultas no se si esta tan bueno tenerlas en memoria o es mejor pedirlas
     */

    public Productor(){}

    public Productor(String nombre,String email){
        this.nombre = nombre;
        this.email = email;
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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
