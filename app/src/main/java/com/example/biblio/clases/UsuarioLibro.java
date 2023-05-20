package com.example.biblio.clases;

public class UsuarioLibro {

    private int id_usuario;
    private int id_libro;
    private boolean en_posesion;
    private boolean deseado;
    private boolean leido;
    private boolean favorito;

    public UsuarioLibro() {
    }

    public UsuarioLibro(int id_usuario, int id_libro, boolean en_posesion, boolean deseado, boolean leido, boolean favorito) {
        this.id_usuario = id_usuario;
        this.id_libro = id_libro;
        this.en_posesion = en_posesion;
        this.deseado = deseado;
        this.leido = leido;
        this.favorito = favorito;
    }

    public int getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(int id_usuario) {
        this.id_usuario = id_usuario;
    }

    public int getId_libro() {
        return id_libro;
    }

    public void setId_libro(int id_libro) {
        this.id_libro = id_libro;
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

    @Override
    public String toString() {
        return "{" +
                "\"id_usuario\":" + id_usuario + '\"' +
                ",\"id_libro\":" + id_libro + '\"' +
                ",\"en_posesion\":" + en_posesion + '\"' +
                ",\"deseado\":" + deseado + '\"' +
                ",\"leido\":" + leido + '\"' +
                ",\"favorito\":" + favorito + '\"' +
                '}';
    }
}
