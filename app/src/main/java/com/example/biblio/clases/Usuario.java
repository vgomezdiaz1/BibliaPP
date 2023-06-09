package com.example.biblio.clases;

import java.util.Date;

public class Usuario {
    private int id;

    private String username;

    private String mail;

    private String nombre;

    private String apellido;

    private String contrasenya;

    public Usuario() {
    }
    public Usuario(String username, String contrasenya) {
        this.username = username;
        this.contrasenya = contrasenya;
    }

    public Usuario(String mail, String nombre, String apellido, String contrasenya) {
        this.id = 0;
        this.username = nombre;
        this.mail = mail;
        this.nombre = nombre;
        this.apellido = apellido;
        this.contrasenya = contrasenya;
    }

    public Usuario(int id, String username, String mail, String nombre, String apellido, String contrasenya) {
        this.id = id;
        this.username = username;
        this.mail = mail;
        this.nombre = nombre;
        this.apellido = apellido;
        this.contrasenya = contrasenya;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getContrasenya() {
        return contrasenya;
    }

    public void setContrasenya(String contrasenya) {
        this.contrasenya = contrasenya;
    }

    @Override
    public String toString() {
        return "{" +
                "\"id\":" + id +
                ", \"username\":\"" + username + '\"' +
                ", \"mail\":\"" + mail + '\"' +
                ", \"nombre\":\"" + nombre + '\"' +
                ", \"apellido\":\"" + apellido + '\"' +
                ", \"contrasenya\":\"" + contrasenya + '\"' +
                '}';
    }

    public String toStringInicioSesion() {
        return  " \"username\":" + username +
                " \"contrasenya\":" + contrasenya +
                '}';
    }
    public String toStringUsuarioLibro() {
        return  "\"id\":" + id +
                ", \"username\":\"" + username + '\"' +
                ", \"mail\":\"" + mail + '\"' +
                ", \"nombre\":\"" + nombre + '\"' +
                ", \"apellido\":\"" + apellido + '\"' +
                ", \"contrasenya\":\"" + contrasenya + '\"';
    }
}
