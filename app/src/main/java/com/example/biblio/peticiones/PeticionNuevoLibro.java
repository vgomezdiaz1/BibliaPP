package com.example.biblio.peticiones;

import android.content.Context;
import android.util.JsonReader;
import android.util.Log;
import android.widget.Toast;

import com.example.biblio.R;
import com.example.biblio.clases.Autor;
import com.example.biblio.clases.Libro;
import com.example.biblio.clases.Mensaje;
import com.example.biblio.clases.Tematica;
import com.example.biblio.clases.Usuario;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;

public class PeticionNuevoLibro  extends Thread{

    Mensaje men;
    Usuario usuario;
    String isbn;
    ArrayList<Libro> libro;
    public PeticionNuevoLibro(Mensaje men, Usuario usuario, String isbn, ArrayList<Libro> libro){
        this.men = men;
        this.usuario = usuario;
        this.isbn = isbn;
        this.libro = libro;
    }
    @Override
    public void run() {
        super.run();
        URL url = null;
        String envio = "{id_usuario:"+ usuario.getId() + ",isbn:" + this.isbn + " }";
        try {
            url = new URL("http://192.168.1.148:8080/BibliotecaAPI/resources/app/nuevoLibro");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Content-Type", "text/plain");
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestMethod("POST");
            conn.setConnectTimeout(8000);
            try(OutputStream os = conn.getOutputStream()) {
                byte[] input = men.codificarMensaje(envio).getBytes("utf-8");
                os.write(input, 0, input.length);
            }catch(Exception e){
                e.printStackTrace();
            }
            if(conn.getResponseCode()==200){
                try {
                    InputStream is = conn.getInputStream();
                    InputStreamReader isr = new InputStreamReader(is, "UTF-8");
                    JsonReader jr = new JsonReader(isr);
                    jr.beginArray();
                    while (jr.hasNext()) {
                        Libro l = new Libro();
                        jr.beginArray();
                        l.setId(Integer.parseInt(jr.nextString()));
                        l.setHojas(Integer.parseInt(jr.nextString()));
                        l.setIsbn(jr.nextString());
                        l.setSinopsis(jr.nextString());
                        l.setTitulo(jr.nextString());
                        l.setId_portada(jr.nextInt());
                        l.setUrl(jr.nextString());
                        int idAutor = jr.nextInt();
                        String nombreAutor = jr.nextString();
                        l.setEn_posesion(jr.nextBoolean());
                        l.setDeseado(jr.nextBoolean());
                        l.setLeido(jr.nextBoolean());
                        l.setFavorito(jr.nextBoolean());
                        int idTematica = jr.nextInt();
                        String nombreTematica = jr.nextString();
                        boolean posibilidad = true;
                        try {
                            for (Libro li : libro) {
                                if (li.getId() == l.getId()) {
                                    li.getTematica().add(new Tematica(idTematica, nombreTematica));
                                    posibilidad = false;
                                    break;
                                }
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        if (posibilidad) {
                            l.setAutor(new Autor(idAutor, nombreAutor));
                            l.getTematica().add(new Tematica(idTematica, nombreTematica));
                            this.libro.add(l);
                        }
                        jr.endArray();
                    }
                    jr.endArray();
                }catch (Exception e){
                    usuario.setApellido("0");
                }
            }else{
                if(conn.getResponseCode() != 0){
                    usuario.setApellido("404");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
