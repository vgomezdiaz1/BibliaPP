package com.example.biblio.clases;

import java.util.ArrayList;
import java.util.Date;

public class Libro {

    private int id;

    private String isbn;

    private String titulo;

    private String sinopsis;

    private int hojas;

    private int id_portada;

    private String url;

    private boolean en_posesion;

    private boolean deseado;

    private boolean leido;

    private boolean favorito;

    private Autor autor;

    private ArrayList<Tematica> tematica = new ArrayList<>();

    public Libro() {
    }

    public Libro(int id, String isbn, String titulo, String sinopsis, int hojas, int id_portada, String url) {
        this.id = id;
        this.isbn = isbn;
        this.titulo = titulo;
        this.sinopsis = sinopsis;
        this.hojas = hojas;
        this.id_portada = id_portada;
        this.url = url;
    }

    public Libro(int id, String isbn, String titulo, String sinopsis, int hojas,int id_portada, String url,
                 boolean en_posesion, boolean deseado, boolean leido, boolean favorito,
                 Autor autor, ArrayList<Tematica> tematica) {
        this.id = id;
        this.isbn = isbn;
        this.titulo = titulo;
        this.sinopsis = sinopsis;
        this.hojas = hojas;
        this.id_portada = id_portada;
        this.url = url;
        this.en_posesion = en_posesion;
        this.deseado = deseado;
        this.leido = leido;
        this.favorito = favorito;
        this.autor = autor;
        this.tematica = tematica;
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

    public boolean isEn_posesion() {
        return en_posesion;
    }

    public void setEn_posesion(boolean en_posesion) {
        this.en_posesion = en_posesion;
    }

    public boolean isDeseado() {
        return deseado;
    }

    public void setDeseado(boolean deseado) {
        this.deseado = deseado;
    }

    public boolean isLeido() {
        return leido;
    }

    public void setLeido(boolean leido) {
        this.leido = leido;
    }

    public boolean isFavorito() {
        return favorito;
    }

    public void setFavorito(boolean favorito) {
        this.favorito = favorito;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Autor getAutor() {
        return autor;
    }

    public void setAutor(Autor autor) {
        this.autor = autor;
    }

    public ArrayList<Tematica> getTematica() {
        return tematica;
    }

    public void setTematica(ArrayList<Tematica> tematica) {
        this.tematica = tematica;
    }

    public int getId_portada() {
        return id_portada;
    }

    public void setId_portada(int id_portada) {
        this.id_portada = id_portada;
    }

    @Override
    public String toString() {
        return "{" +
                "\"id\":" + id +
                ", \"isbn\":\"" + isbn + '\"' +
                ", \"titulo\":\"" + titulo + '\"' +
                ", \"sinopsis\":\"" + sinopsis.replaceAll("\"","") + '\"' +
                ", \"hojas\":" + hojas +
                ", \"id_portada\":" + id_portada +
                '}';
    }
    public String toStringUsuarioLibro() {
        return  ", \"idLibro\":" + id +
                ", \"isbn\":\"" + isbn + '\"' +
                ", \"titulo\":\"" + titulo + '\"' +
                ", \"sinopsis\":\"" + sinopsis.replaceAll("\"","") + '\"' +
                ", \"hojas\":" + hojas +
                ", \"id_portada\":" + id_portada;
    }

    public String toStringCompleto() {
        return "Libro{" +
                "id=" + id +
                ", isbn='" + isbn + '\'' +
                ", titulo='" + titulo + '\'' +
                ", hojas=" + hojas +
                ", url='" + url + '\'' +
                ", en_posesion=" + en_posesion +
                ", deseado=" + deseado +
                ", leido=" + leido +
                ", favorito=" + favorito +
                ", autor=" + autor.getNombre() +
                ", tematica=" + this.toStringTematicas() +
                '}';
    }

    public String toStringTematicas(){
        String n = "";
        for (Tematica t: this.getTematica()) {
            n += t.getNombre() + "\n";
        }
        return n;
    }
}
