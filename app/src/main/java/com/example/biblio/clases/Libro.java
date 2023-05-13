package com.example.biblio.clases;

import java.util.Date;

public class Libro {

    private int id;

    private String isbn;

    private String titulo;

    private String sinopsis;

    private int hojas;

    private Date creado;

    public Libro() {
    }

    public Libro(int id, String isbn, String titulo, String sinopsis, int hojas, Date creado) {
        this.id = id;
        this.isbn = isbn;
        this.titulo = titulo;
        this.sinopsis = sinopsis;
        this.hojas = hojas;
        this.creado = creado;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getSinopsis() {
        return sinopsis;
    }

    public void setSinopsis(String sinopsis) {
        this.sinopsis = sinopsis;
    }

    public int getHojas() {
        return hojas;
    }

    public void setHojas(int hojas) {
        this.hojas = hojas;
    }

    public Date getCreado() {
        return creado;
    }

    public void setCreado(Date creado) {
        this.creado = creado;
    }

    @Override
    public String toString() {
        return "Libro{" +
                "id=" + id +
                ", isbn='" + isbn + '\'' +
                ", titulo='" + titulo + '\'' +
                ", sinopsis='" + sinopsis + '\'' +
                ", hojas=" + hojas +
                ", creado=" + creado +
                '}';
    }
}
