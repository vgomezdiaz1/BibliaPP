package com.example.biblio.clases;

public class UsuarioLibro {

    private Usuario usuario;
    private Libro libro;
    private boolean en_posesion;
    private boolean deseado;
    private boolean leido;
    private boolean favorito;

    public UsuarioLibro() {
    }

    public UsuarioLibro(Usuario usuario, Libro libro, boolean en_posesion, boolean deseado, boolean leido, boolean favorito) {
        this.usuario = usuario;
        this.libro = libro;
        this.en_posesion = en_posesion;
        this.deseado = deseado;
        this.leido = leido;
        this.favorito = favorito;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Libro getLibro() {
        return libro;
    }

    public void setLibro(Libro libro) {
        this.libro = libro;
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
        return "o:" +
                usuario.getId() +
                 " r:" + libro.getId() +
                " n:" + en_posesion +
                " d:" + deseado +
                " i:" + leido +
                " v:" + favorito;
    }
}
